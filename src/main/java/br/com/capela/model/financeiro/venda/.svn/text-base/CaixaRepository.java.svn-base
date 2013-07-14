package br.com.capela.model.financeiro.venda;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.joda.time.Interval;

import br.com.capela.infra.IbatisDAO;

public class CaixaRepository extends IbatisDAO {

	@SuppressWarnings("unchecked")
	public List<Venda> consultaVendasNoIntervalo(final Interval periodo) {
		final Map<String, Object> parametros = new HashMap<String, Object>();
		parametros.put("dataInicial", periodo.getStart().toDate());
		parametros.put("dataFinal", periodo.getEnd().toDate());
		return getSqlMapClientTemplate().queryForList("CaixaMapper.vendasNoPeriodo", parametros);
	}

}
