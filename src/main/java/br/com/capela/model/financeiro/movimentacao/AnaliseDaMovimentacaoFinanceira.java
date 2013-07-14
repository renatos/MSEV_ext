package br.com.capela.model.financeiro.movimentacao;

import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.annotation.Nullable;

import org.joda.time.Interval;
import org.joda.time.LocalDate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import br.com.capela.model.Dinheiro;

import com.google.common.base.Function;
import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Lists;
import com.google.common.collect.Multimap;
import com.google.common.collect.Multimaps;

@Service
public class AnaliseDaMovimentacaoFinanceira {

	@Autowired
	private MovimentacaoRepository repository;

	public List<Movimento> obterMovimentacaoNoPeriodo(final LocalDate dataInicial,
			final LocalDate dataFinal) {
		return repository.consultaMovimentacaoNoIntervalo(new Interval(
				dataInicial.toDateMidnight(), dataFinal.toDateMidnight()));
	}

	public Multimap<Conta, Movimento> agruparMovimentacaoPorConta(final List<Movimento> todosOsLancamentos) {
		return Multimaps.index(todosOsLancamentos, new Function<Movimento, Conta>() {
			@Override
			public Conta apply(final Movimento movimento) {
				return movimento.getConta();
			}
		});
	}

	public Multimap<Conta, Movimento> agruparMovimentacaoPorDescricao(final Multimap<Conta, Movimento> movimentacaoPorConta) {
		final Multimap<Conta, Movimento> movimentacaoPorDescricaoEConta = ArrayListMultimap.create();
		final Set<Conta> contas = movimentacaoPorConta.keySet();
		for (final Conta conta : contas) {
			movimentacaoPorDescricaoEConta
					.putAll(conta, agruparMovimentacaoPorDescricao(movimentacaoPorConta.get(conta)));
		}
		return movimentacaoPorDescricaoEConta;
	}

	public List<Movimento> agruparMovimentacaoPorDescricao(final Collection<Movimento> movimentacaoNaConta) {
		final List<Movimento> movimentosAgrupados = Lists.newArrayList();
		final Multimap<String, Movimento> movimentosAgrupadosPorDescricao = Multimaps.index(movimentacaoNaConta, new Function<Movimento, String>() {
			@Override
			@Nullable
			public String apply(@Nullable final Movimento movimento) {
				final Pattern pattern = Pattern.compile("(- p/|Cli\\.|Cli\\.:)", Pattern.CASE_INSENSITIVE);
				final String descricao = movimento.getDescricao();
				if (!StringUtils.hasText(descricao)) {
					return "DESCONHECIDO";
				}
				final Matcher matcher = pattern.matcher(descricao);
				if (matcher.find()) {
					return descricao.substring(0, matcher.start());
				}
				else
					return descricao;
			}
		});

		final Iterator<String> it = movimentosAgrupadosPorDescricao.keySet().iterator();

		while (it.hasNext()) {
			final String descricao = it.next();
			final List<Movimento> movimentos = Lists.newArrayList(movimentosAgrupadosPorDescricao.get(descricao));
			Dinheiro valor = Dinheiro.zero();
			boolean entrada = true;
			for (final Movimento movimento : movimentos) {
				entrada = movimento.getValorDeEntrada().isNaoEhZero();
				final Dinheiro valorMovimentado =
						entrada ? movimento.getValorDeEntrada() : movimento.getValorDeSaida();
				valor = valor.somar(valorMovimentado);
			}
			final Movimento novoMovimento = new Movimento();
			novoMovimento.setDescricao(descricao);
			if (entrada)
				novoMovimento.setValorDeEntrada(valor);
			else
				novoMovimento.setValorDeSaida(valor);

			movimentosAgrupados.add(novoMovimento);
		}
		return movimentosAgrupados;
	}

	public List<TotalizacaoPorConta> calculaTotalizacao(final Multimap<Conta, Movimento> movimentacaoAgrupadaPorConta) {
		final List<TotalizacaoPorConta> totaisPorConta = Lists.newArrayListWithExpectedSize(movimentacaoAgrupadaPorConta.keySet().size());
		final Iterator<Conta> contas = movimentacaoAgrupadaPorConta.keySet().iterator();
		while (contas.hasNext()) {
			final Conta conta = contas.next();
			final TotalizacaoPorConta totalizacao = new TotalizacaoPorConta(conta);
			final Collection<Movimento> movimentacaoNaConta = movimentacaoAgrupadaPorConta.get(conta);
			for (final Movimento movimentoNaConta : movimentacaoNaConta) {
				totalizacao.incrementar(movimentoNaConta);
			}
			totaisPorConta.add(totalizacao);
		}

		return totaisPorConta;
	}
}
