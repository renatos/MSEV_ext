package br.com.capela.model.financeiro.movimentacao;

import java.util.Date;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;

import com.google.common.base.Optional;

import br.com.capela.model.Dinheiro;
import br.com.capela.model.ObjetoDoDominio;

public class Movimento extends ObjetoDoDominio {

	private static final long serialVersionUID = 1L;

	private Conta conta;
	private Date lancamento;
	private Dinheiro valorDeEntrada;
	private Dinheiro valorDeSaida;
	private String descricao;

	public Movimento() {
		valorDeEntrada = Dinheiro.zero();
		valorDeSaida = Dinheiro.zero();
	}

	public Conta getConta() {
		return conta;
	}

	public void setConta(final Conta conta) {
		this.conta = conta;
	}

	public Date getLancamento() {
		return lancamento;
	}

	public void setLancamento(final Date lancamento) {
		this.lancamento = lancamento;
	}

	public Dinheiro getValorDeEntrada() {
		Optional<Dinheiro> optional = Optional.fromNullable(valorDeEntrada);
		return optional.or(Dinheiro.zero());
	}

	public void setValorDeEntrada(final Dinheiro valorDeEntrada) {
		this.valorDeEntrada = valorDeEntrada;
	}

	public Dinheiro getValorDeSaida() {
		Optional<Dinheiro> optional = Optional.fromNullable(valorDeSaida);
		return optional.or(Dinheiro.zero());
	}

	public void setValorDeSaida(final Dinheiro valorDeSaida) {
		this.valorDeSaida = valorDeSaida;
	}

	public String getDescricao() {
		return descricao;
	}

	public void setDescricao(final String descicao) {
		descricao = descicao;
	}
	
	@Override
	public boolean equals(Object arg0) {
		return EqualsBuilder.reflectionEquals(this, arg0);
	}
	
	@Override
	public int hashCode() {
		return HashCodeBuilder.reflectionHashCode(this);
	}
}
