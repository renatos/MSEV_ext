package br.com.capela.model;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.Currency;
import java.util.Locale;

import org.joda.money.CurrencyUnit;
import org.joda.money.Money;

import com.google.common.base.Optional;

public class Dinheiro implements Comparable<Dinheiro> {

	private static final NumberFormat FORMATACAO_REAL = DecimalFormat.getCurrencyInstance(new Locale("pt", "br"));
	private static final CurrencyUnit REAL = CurrencyUnit.of(Currency.getInstance("BRL"));
	private Money valor;

	public Dinheiro() {
		valor = Money.zero(REAL);
	}

	public static Dinheiro zero() {
		return new Dinheiro();
	}

	public Dinheiro(final BigDecimal valor) {
		this.valor = Money.of(REAL, valor);
	}

	private Dinheiro(final Money valor) {
		this.valor = valor;
	}

	public Dinheiro somar(final Dinheiro outro) {
		final Optional<Dinheiro> optional = Optional.fromNullable(outro);
		return new Dinheiro(valor.plus(optional.or(new Dinheiro()).valor));
	}

	public Dinheiro subtrair(final Dinheiro outro) {
		final Optional<Dinheiro> optional = Optional.fromNullable(outro);
		return new Dinheiro(valor.minus(optional.or(new Dinheiro()).valor));
	}

	public boolean isZero() {
		return valor.equals(Money.zero(REAL));
	}

	public boolean isNaoEhZero() {
		return !isZero();
	}

	public boolean isMaiorQueZero() {
		return getValor().doubleValue() > 0;
	}

	public boolean isMenorQueZero() {
		return !isMaiorQueZero();
	}

	public BigDecimal getValor() {
		return valor.getAmount();
	}

	@Override
	public String toString() {
		return FORMATACAO_REAL.format(getValor());
	}

	@Override
	public boolean equals(final Object obj) {
		return obj instanceof Dinheiro ?
				getValor().equals(obj) : false;
	}

	@Override
	public int hashCode() {
		return getValor().hashCode();
	}

	@Override
	public int compareTo(final Dinheiro outro) {
		return outro != null ? getValor().compareTo(outro.getValor()) : 0;
	}
}
