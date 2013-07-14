package br.com.capela.model.financeiro.venda;

import java.util.Collection;
import java.util.Comparator;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import org.joda.time.Interval;
import org.joda.time.LocalDate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.com.capela.model.Dinheiro;

import com.google.common.base.Function;
import com.google.common.collect.Lists;
import com.google.common.collect.Multimap;
import com.google.common.collect.Multimaps;
import com.google.common.collect.Ordering;

@Service
public class AnaliseDeCaixa {

	@Autowired
	private CaixaRepository caixaRepository;

	public List<Venda> obterVendasNoPeriodo(final LocalDate dataInicial,
			final LocalDate dataFinal) {
		return caixaRepository.consultaVendasNoIntervalo(new Interval(
				dataInicial.toDateMidnight(), dataFinal.toDateMidnight()));
	}

	public List<Venda> agruparPorDia(final List<Venda> todasAsVendas) {

		final Multimap<Date, Venda> vendasPorDia = Multimaps.index(todasAsVendas, new Function<Venda, Date>() {
			@Override
			public Date apply(final Venda venda) {
				return venda.getData();
			}
		});

		// final Group<Venda> vendas = Lambda.group(todasAsVendas, conditions)

		final Iterator<Date> datas = vendasPorDia.asMap().keySet().iterator();
		final List<Venda> vendasAgrupadasPorDia = Lists.newArrayListWithExpectedSize(50);

		while (datas.hasNext()) {
			final Date data = datas.next();
			Venda totalDeVendasPorDia = null;
			Dinheiro valorTotalPorDia = new Dinheiro();
			final Collection<Venda> vendasNaData = vendasPorDia.asMap().get(data);
			for (final Venda vendaNaData : vendasNaData) {
				valorTotalPorDia = valorTotalPorDia.somar(vendaNaData.getValorTotal());
			}
			totalDeVendasPorDia = new Venda(data, valorTotalPorDia);
			vendasAgrupadasPorDia.add(totalDeVendasPorDia);
		}

		final Ordering<Venda> ordenacaoPorData = Ordering.from(new Comparator<Venda>() {
			@Override
			public int compare(final Venda o1, final Venda o2) {
				return o1.getData().compareTo(o2.getData());
			}
		});

		return ordenacaoPorData.sortedCopy(vendasAgrupadasPorDia);
	}

	public Dinheiro obterValorTotalDasVendas(final List<Venda> todasAsVendas) {
		Dinheiro valorTotal = Dinheiro.zero();

		for (final Venda venda : todasAsVendas) {
			valorTotal = valorTotal.somar(venda.getValorTotal());
		}

		return valorTotal;
	}
}
