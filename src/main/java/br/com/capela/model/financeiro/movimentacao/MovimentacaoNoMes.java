package br.com.capela.model.financeiro.movimentacao;

import java.util.Date;
import java.util.List;

import org.joda.time.LocalDate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import br.com.capela.model.Dinheiro;

public class MovimentacaoNoMes {
	
	private int mes;
	private List<Conta> contas;
	private Dinheiro totalDeEntradas;
	private Dinheiro totalDeSaidas;
	private Dinheiro saldo;
	
	@Autowired
	private RelatorioDeMovimentacao relatorioDeMovimentacao;
	
	public MovimentacaoNoMes(int mes){
		LocalDate dataNoMes = new LocalDate().withMonthOfYear(mes);
		Date dataInicial = dataNoMes.withDayOfMonth(1).toDateMidnight().toDate();
		Date dataFinal = dataNoMes.dayOfMonth().withMaximumValue().toDateMidnight().toDate();
		relatorioDeMovimentacao.consulta(dataInicial, dataFinal);
//		totalDeEntradas.put(mes, relatorioDeMovimentacao.calculaTotalGeralDeEntradas());
//		totalDeSaidas.put(mes, relatorioDeMovimentacao.calculaTotalGeralDeSaidas());
//		saldoTotalPorMes.put(mes, relatorioDeMovimentacao.calculaSaldoTotal());
		
	}
	
}
