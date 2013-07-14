package br.com.capela.model.financeiro.venda;

import java.util.Date;

import org.codehaus.jackson.annotate.JsonAutoDetect;

import br.com.capela.model.Dinheiro;
import br.com.capela.model.ObjetoDoDominio;

import com.google.common.base.Objects;

@JsonAutoDetect
public class Venda extends ObjetoDoDominio {

	private static final long serialVersionUID = 1L;

	private Date data;
	private String produto;
	private Dinheiro valorTotal;

	public Venda() {

	}

	public Venda(final Date data, final Dinheiro valor) {
		this.data = data;
		valorTotal = valor;
	}

	public Date getData() {
		return data;
	}

	public void setData(final Date data) {
		this.data = data;
	}

	public String getProduto() {
		return produto;
	}

	public void setProduto(final String produto) {
		this.produto = produto;
	}

	public Dinheiro getValorTotal() {
		return valorTotal;
	}

	public void setValorTotal(final Dinheiro valorTotal) {
		this.valorTotal = valorTotal;
	}

	@Override
	public int hashCode() {
		return Objects.hashCode(data, produto, valorTotal);
	}

	@Override
	public boolean equals(final Object obj) {
		return Objects.equal(this, obj);
	}
}
