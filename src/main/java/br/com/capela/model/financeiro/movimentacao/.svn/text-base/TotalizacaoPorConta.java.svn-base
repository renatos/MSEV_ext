package br.com.capela.model.financeiro.movimentacao;

import br.com.capela.model.Dinheiro;
import br.com.capela.model.ObjetoDoDominio;

public class TotalizacaoPorConta extends ObjetoDoDominio {

	private static final long serialVersionUID = 1L;

	private Conta conta;
	private Dinheiro totalEmEntradas;
	private Dinheiro totalEmSaidas;

	public TotalizacaoPorConta(final Conta conta) {
		this.conta = conta;
		totalEmEntradas = Dinheiro.zero();
		totalEmSaidas = Dinheiro.zero();
	}

	public void incrementar(final Movimento movimento) {
		if (movimento.getValorDeEntrada().isNaoEhZero()) {
			totalEmEntradas = totalEmEntradas.somar(movimento.getValorDeEntrada());
		}
		else {
			totalEmSaidas = totalEmSaidas.somar(movimento.getValorDeSaida());
		}
	}

	public Conta getConta() {
		return conta;
	}

	public Dinheiro getTotalEmEntradas() {
		return totalEmEntradas;
	}

	public Dinheiro getTotalEmSaidas() {
		return totalEmSaidas;
	}

	public Dinheiro getSaldo() {
		return totalEmEntradas.subtrair(totalEmSaidas);
	}
}
