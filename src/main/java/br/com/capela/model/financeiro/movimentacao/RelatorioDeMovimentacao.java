package br.com.capela.model.financeiro.movimentacao;

import java.util.Collections;
import java.util.Date;
import java.util.List;

import org.joda.time.LocalDate;

import br.com.capela.model.Dinheiro;

import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Lists;
import com.google.common.collect.Multimap;
import com.google.common.collect.Multimaps;

public class RelatorioDeMovimentacao {

	private AnaliseDaMovimentacaoFinanceira movimentacaoFinanceiraService;

	private List<Movimento> movimentacao;
	private List<TotalizacaoPorConta> totalizacaoPorContas;
	private Multimap<Conta, Movimento> movimentacaoPorConta;
	private Multimap<Conta, Movimento> movimentacaoPorContaEProduto;

	public RelatorioDeMovimentacao(final AnaliseDaMovimentacaoFinanceira movimentacaoFinanceiraService) {
		this.movimentacaoFinanceiraService = movimentacaoFinanceiraService;
		this.movimentacao = Lists.newArrayList();
		this.totalizacaoPorContas = Lists.newArrayList();
		this.movimentacaoPorConta = ArrayListMultimap.create();
		this.movimentacaoPorContaEProduto = ArrayListMultimap.create();
	}

	public void consulta(final Date dataInicial, final Date dataFinal) {
		movimentacao = movimentacaoFinanceiraService
				.obterMovimentacaoNoPeriodo(LocalDate.fromDateFields(dataInicial), LocalDate.fromDateFields(dataFinal));
		movimentacaoPorConta = movimentacaoFinanceiraService.agruparMovimentacaoPorConta(movimentacao);
		totalizacaoPorContas = movimentacaoFinanceiraService.calculaTotalizacao(movimentacaoPorConta);
		movimentacaoPorContaEProduto = movimentacaoFinanceiraService.agruparMovimentacaoPorDescricao(movimentacaoPorConta);
	}

	public Dinheiro calculaTotalGeralDeEntradas() {
		Dinheiro totalGeral = Dinheiro.zero();

		for (final TotalizacaoPorConta totalizacaoPorConta : obtemTotalizacaoPorContas()) {
			totalGeral = totalGeral.somar(totalizacaoPorConta.getTotalEmEntradas());
		}

		return totalGeral;
	}

	public Dinheiro calculaTotalGeralDeSaidas() {
		Dinheiro totalGeral = Dinheiro.zero();

		for (final TotalizacaoPorConta totalizacaoPorConta : obtemTotalizacaoPorContas()) {
			totalGeral = totalGeral.somar(totalizacaoPorConta.getTotalEmSaidas());
		}

		return totalGeral;
	}

	public Dinheiro calculaSaldoTotal() {
		Dinheiro totalGeral = Dinheiro.zero();

		for (final TotalizacaoPorConta totalizacaoPorConta : obtemTotalizacaoPorContas()) {
			totalGeral = totalGeral.somar(totalizacaoPorConta.getSaldo());
		}

		return totalGeral;
	}

	public List<Movimento> getMovimentacao() {
		return movimentacao;
	}

	public Multimap<Conta, Movimento> obtemMovimentacaoPorConta() {
		return Multimaps.unmodifiableMultimap(movimentacaoPorConta);
	}

	public Multimap<Conta, Movimento> obtemMovimentacaoPorContaEProduto() {
		return Multimaps.unmodifiableMultimap(movimentacaoPorContaEProduto);
	}

	public List<TotalizacaoPorConta> obtemTotalizacaoPorContas() {
		return Collections.unmodifiableList(totalizacaoPorContas);
	}

}
