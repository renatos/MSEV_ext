package br.com.capela.web.controller.movimentacao;

import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;

import org.jfree.data.time.Month;
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

import com.google.common.collect.HashBasedTable;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Maps;
import com.google.common.collect.Multimap;
import com.google.common.collect.Table;

@Controller
@Scope(WebApplicationContext.SCOPE_SESSION)
public class MovimentacaoNoAnoController extends MsevController {

	private static final long serialVersionUID = 1L;

	@Autowired
	private AnaliseDaMovimentacaoFinanceira movimentacaoFinanceiraService;

	private RelatorioDeMovimentacao relatorioDeMovimentacao;

	private Map<Integer, Dinheiro> totalGeralDeEntradasPorMes;
	private Map<Integer, Dinheiro> totalGeralDeSaidasPorMes;
	private Map<Integer, Dinheiro> saldoTotalPorMes;

	public MovimentacaoNoAnoController() {
		totalGeralDeEntradasPorMes = Maps.newHashMap();
		totalGeralDeSaidasPorMes = Maps.newHashMap();
		saldoTotalPorMes = Maps.newHashMap();
	}

	@PostConstruct
	public void carregarDadosDoMesAtual() {
		relatorioDeMovimentacao = new RelatorioDeMovimentacao(movimentacaoFinanceiraService);
		consulta();
	}

	public void consulta() {
		LocalDate dataAtual = new LocalDate();
		for(int mes = Month.JANUARY; mes < dataAtual.getMonthOfYear(); mes++){
			LocalDate dataNoMes = new LocalDate().withMonthOfYear(mes);
			Date dataInicial = dataNoMes.withDayOfMonth(1).toDateMidnight().toDate();
			Date dataFinal = dataNoMes.dayOfMonth().withMaximumValue().toDateMidnight().toDate();
			relatorioDeMovimentacao.consulta(dataInicial, dataFinal);
			totalGeralDeEntradasPorMes.put(mes, relatorioDeMovimentacao.calculaTotalGeralDeEntradas());
			totalGeralDeSaidasPorMes.put(mes, relatorioDeMovimentacao.calculaTotalGeralDeSaidas());
			saldoTotalPorMes.put(mes, relatorioDeMovimentacao.calculaSaldoTotal());
		}
	}
	
	public List<Integer> mesesDoAno(){
		return ImmutableList.of(0,1,2,3,4,5,6,7);
	}
	
	public Dinheiro getTotalGeralDeEntradasNoMes(int mes){
		return totalGeralDeEntradasPorMes.get(mes);
	}
	
	public Dinheiro getTotalGeralDeSaidasNoMes(int mes){
		return totalGeralDeSaidasPorMes.get(mes);
	}
	
	public Dinheiro getSaldoTotalNoMes(int mes){
		return saldoTotalPorMes.get(mes);
	}
}
