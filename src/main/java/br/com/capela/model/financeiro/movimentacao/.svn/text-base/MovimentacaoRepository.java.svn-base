package br.com.capela.model.financeiro.movimentacao;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.joda.time.Interval;
import org.springframework.cache.annotation.Cacheable;

import br.com.capela.infra.IbatisDAO;

public class MovimentacaoRepository extends IbatisDAO {

	@SuppressWarnings("unchecked")
	@Cacheable(value = "movimentacao" , key = "#periodo.hashCode()")
	public List<Movimento> consultaMovimentacaoNoIntervalo(final Interval periodo) {
		final Map<String, Object> parametros = new HashMap<String, Object>();
		parametros.put("dataInicial", periodo.getStart().toDate());
		parametros.put("dataFinal", periodo.getEnd().toDate());
		return getSqlMapClientTemplate().queryForList("MovimentoMapper.movimentacaoNoPeriodo", parametros);
	}

}
