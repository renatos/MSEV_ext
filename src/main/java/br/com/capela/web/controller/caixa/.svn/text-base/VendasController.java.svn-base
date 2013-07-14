package br.com.capela.web.controller.caixa;

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
import br.com.capela.model.financeiro.venda.AnaliseDeCaixa;
import br.com.capela.model.financeiro.venda.Venda;

@Controller
@Scope(WebApplicationContext.SCOPE_SESSION)
public class VendasController extends MsevController {

	private static final long serialVersionUID = 1L;

	@Autowired
	private AnaliseDeCaixa analiseDeCaixaService;

	private Date dataInicial;
	private Date dataFinal;

	private List<Venda> vendas;
	private List<Venda> vendasPorDia;
	private Dinheiro valorTotalDasVendas;

	@PostConstruct
	public void carregarDadosDoMesAtual() {
		dataInicial = new LocalDate().minusMonths(1).toDateMidnight().toDate();
		dataFinal = new LocalDate().toDateMidnight().toDate();
	}

	public void consulta() {
		vendas = analiseDeCaixaService
				.obterVendasNoPeriodo(LocalDate.fromDateFields(dataInicial), LocalDate.fromDateFields(dataFinal));
		vendasPorDia = analiseDeCaixaService.agruparPorDia(vendas);
		valorTotalDasVendas = analiseDeCaixaService.obterValorTotalDasVendas(vendasPorDia);
	}

	public List<Venda> getVendas() {
		return vendas;
	}

	public List<Venda> getVendasPorDia() {
		return vendasPorDia;
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
	
	public Dinheiro getValorTotalDasVendas() {
		return valorTotalDasVendas;
	}

	public void setDataFinal(final Date dataFinal) {
		this.dataFinal = dataFinal;
	}

}
