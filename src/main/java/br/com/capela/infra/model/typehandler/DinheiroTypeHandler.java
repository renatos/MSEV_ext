package br.com.capela.infra.model.typehandler;

import java.math.BigDecimal;
import java.sql.SQLException;
import java.sql.Types;

import br.com.capela.model.Dinheiro;

import com.ibatis.sqlmap.client.extensions.ParameterSetter;
import com.ibatis.sqlmap.client.extensions.ResultGetter;
import com.ibatis.sqlmap.client.extensions.TypeHandlerCallback;

public class DinheiroTypeHandler implements TypeHandlerCallback {

	@Override
	public void setParameter(final ParameterSetter setter, final Object parameter)
			throws SQLException {
		if (parameter == null) {
			setter.setNull(Types.DECIMAL);
		} else {
			final Dinheiro e = (Dinheiro) parameter;
			setter.setBigDecimal(e.getValor());
		}
	}

	@Override
	public Object getResult(final ResultGetter getter) throws SQLException {
		final BigDecimal valor = getter.getBigDecimal();
		if (getter.wasNull()) {
			return null;
		}
		return new Dinheiro(valor);
	}

	@Override
	public Object valueOf(final String s) {
		return s;
	}

}