package br.com.capela.web.controller.movimentacao;

import java.util.Comparator;
import java.util.Date;
import java.util.List;

import javax.annotation.PostConstruct;

import org.joda.time.LocalDate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.web.context.WebApplicationContext;

import br.com.capela.infra.web.controller.MsevController;
import br.com.capela.model.Dinheiro;
import br.com.capela.model.financeiro.movimentacao.AnaliseDaMovimentacaoFinanceira;
import br.com.capela.model.financeiro.movimentacao.Conta;
import br.com.capela.model.financeiro.movimentacao.Movimento;
import br.com.capela.model.financeiro.movimentacao.RelatorioDeMovimentacao;
import br.com.capela.model.financeiro.movimentacao.TotalizacaoPorConta;

import com.google.common.base.Predicate;
import com.google.common.collect.Collections2;
import com.google.common.collect.Lists;
import com.google.common.collect.Multimap;
import com.google.common.collect.Ordering;

@Controller
@Scope(WebApplicationContext.SCOPE_SESSION)
public class MovimentacaoController extends MsevController {

	private static final long serialVersionUID = 1L;

	@Autowired
	private AnaliseDaMovimentacaoFinanceira movimentacaoFinanceiraService;

	private RelatorioDeMovimentacao relatorioDeMovimentacao;

	private Date dataInicial;
	private Date dataFinal;

	private boolean exibeEntradas;
	private boolean exibeSaidas;

	private List<TotalizacaoPorConta> totalizacaoPorContas;
	private Multimap<Conta, Movimento> movimentacaoPorConta;
	private Multimap<Conta, Movimento> movimentacaoPorContaEProduto;

	private Ordering<Movimento> ordenacaoPorValorDeEntrada = Ordering.from(new Comparator<Movimento>() {
		@Override
		public int compare(final Movimento o1, final Movimento o2) {
			final Dinheiro entrada1 = o1.getValorDeEntrada();
			final Dinheiro entrada2 = o2.getValorDeEntrada();
			return entrada1.compareTo(entrada2);
		}
	});

	private Ordering<Movimento> ordenacaoPorValorDeSaida = Ordering.from(new Comparator<Movimento>() {
		@Override
		public int compare(final Movimento o1, final Movimento o2) {
			final Dinheiro saida1 = o1.getValorDeSaida();
			final Dinheiro saida2 = o2.getValorDeSaida();
			return saida1.compareTo(saida2);
		}
	});

	public MovimentacaoController() {
		exibeEntradas = true;
		exibeSaidas = true;
	}

	@PostConstruct
	public void carregarDadosDoMesAtual() {
		dataInicial = new LocalDate().minusMonths(1).toDateMidnight().toDate();
		dataFinal = new LocalDate().toDateMidnight().toDate();
		relatorioDeMovimentacao = new RelatorioDeMovimentacao(movimentacaoFinanceiraService);
	}

	public void consulta() {
		relatorioDeMovimentacao.consulta(dataInicial, dataFinal);
		movimentacaoPorConta = relatorioDeMovimentacao.obtemMovimentacaoPorConta();
		totalizacaoPorContas = relatorioDeMovimentacao.obtemTotalizacaoPorContas();
		movimentacaoPorContaEProduto = relatorioDeMovimentacao.obtemMovimentacaoPorContaEProduto();
	}

	public List<Movimento> getMovimentacaoPorConta(final Conta conta) {
		List<Movimento> movimentacao = Lists.newArrayList(movimentacaoPorContaEProduto.get(conta));

		if (!(exibeEntradas && exibeSaidas)) {
			movimentacao = Lists.newArrayList(Collections2.filter(movimentacao, new Predicate<Movimento>() {
				@Override
				public boolean apply(final Movimento movimento) {
					if (exibeEntradas)
						return movimento.getValorDeEntrada().isNaoEhZero();
					else if (exibeSaidas)
						return movimento.getValorDeSaida().isNaoEhZero();
					else
						return false;
				};
			}));
			if (exibeEntradas) {
				movimentacao = ordenacaoPorValorDeEntrada.sortedCopy(movimentacao);
			}
			else {
				movimentacao = ordenacaoPorValorDeSaida.sortedCopy(movimentacao);
			}
		} else {
			// movimentacao =
			// ordenacaoPorValorDeSaida.reverse().sortedCopy(movimentacao);
		}
		return movimentacao;
	}

	public Date getDataInicial() {
		return dataInicial;
	}

	public void setDataInicial(final Date dataInicial) {
		this.dataInicial = dataInicial;
	}

	public Date getDataFinal() {
		return dataFinal;
	}

	public void setDataFinal(final Date dataFinal) {
		this.dataFinal = dataFinal;
	}

	public List<TotalizacaoPorConta> getTotalizacaoPorContas() {
		if (movimentacaoPorConta == null) {
			return Lists.newArrayList();
		}
		else
			return totalizacaoPorContas;
	}

	public Dinheiro getTotalGeralDeEntradas() {
		return relatorioDeMovimentacao.calculaTotalGeralDeEntradas();
	}

	public Dinheiro getTotalGeralDeSaidas() {
		return relatorioDeMovimentacao.calculaTotalGeralDeSaidas();
	}

	public Dinheiro getSaldoTotal() {
		return relatorioDeMovimentacao.calculaSaldoTotal();
	}

	public boolean ehMenorQueZero(final Dinheiro valor) {
		return valor != null && valor.isMenorQueZero();
	}

	public boolean ehMaiorQueZero(final Dinheiro valor) {
		return valor != null && valor.isMaiorQueZero();
	}

	public void setExibeEntradas(final boolean exibeEntradas) {
		this.exibeEntradas = exibeEntradas;
	}

	public void setExibeSaidas(final boolean exibeSaidas) {
		this.exibeSaidas = exibeSaidas;
	}

	public boolean isExibeEntradas() {
		return exibeEntradas;
	}

	public boolean isExibeSaidas() {
		return exibeSaidas;
	}

}
