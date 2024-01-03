package eventos.RealEstruturas;

import br.com.sankhya.extensions.eventoprogramavel.EventoProgramavelJava;
import br.com.sankhya.jape.dao.JdbcWrapper;
import br.com.sankhya.jape.event.PersistenceEvent;
import br.com.sankhya.jape.event.TransactionContext;
import br.com.sankhya.jape.sql.NativeSql;
import br.com.sankhya.jape.vo.DynamicVO;
import br.com.sankhya.modelcore.util.DynamicEntityNames;
import br.com.sankhya.modelcore.util.EntityFacadeFactory;

import java.math.BigDecimal;
import java.sql.ResultSet;

public class AjustaPisCofins implements EventoProgramavelJava {

    @Override
    public void beforeInsert(PersistenceEvent persistenceEvent) throws Exception {

    }

    @Override
    public void beforeUpdate(PersistenceEvent persistenceEvent) throws Exception {

    }

    @Override
    public void beforeDelete(PersistenceEvent persistenceEvent) throws Exception {

    }

    @Override
    public void afterInsert(PersistenceEvent persistenceEvent) throws Exception {

        DynamicVO tgfDinVO = (DynamicVO) persistenceEvent.getVo();
        BigDecimal nroUnico = tgfDinVO.asBigDecimal("NUNOTA");

        DynamicVO notaOrigVO = (DynamicVO) EntityFacadeFactory.getDWFFacade()
                .findEntityByPrimaryKeyAsVO(DynamicEntityNames.CABECALHO_NOTA, new Object[] { nroUnico });

        DynamicVO topVO = (DynamicVO) notaOrigVO.getProperty("TipoOperacao");

        String validaRegra = topVO.asString("AD_DEDUZISSPISCOFINS");

        if (validaRegra == null)
            validaRegra = "N";

        if (validaRegra.equals("S")) {

            if (tgfDinVO.asBigDecimal("CODIMP").equals(BigDecimal.valueOf(6))
                    || tgfDinVO.asBigDecimal("CODIMP").equals(BigDecimal.valueOf(7))) {

                BigDecimal base = tgfDinVO.asBigDecimal("BASERED");
                BigDecimal aliquota = tgfDinVO.asBigDecimal("ALIQUOTA");
                BigDecimal sequencia = tgfDinVO.asBigDecimal("SEQUENCIA");
                BigDecimal vlrIss = RetornaIssItem(nroUnico, sequencia);
                double baseAjustada = base.doubleValue() - vlrIss.doubleValue();
                double valorAjustado = baseAjustada / 100 * aliquota.doubleValue();

                AtualizaValorImposto(nroUnico, sequencia, tgfDinVO.asBigDecimal("CODIMP"), baseAjustada, valorAjustado);
            }

        }
    }

    @Override
    public void afterUpdate(PersistenceEvent persistenceEvent) throws Exception {

        DynamicVO tgfDinVO = (DynamicVO) persistenceEvent.getVo();
        BigDecimal nroUnico = tgfDinVO.asBigDecimal("NUNOTA");

        DynamicVO notaOrigVO = (DynamicVO) EntityFacadeFactory.getDWFFacade()
                .findEntityByPrimaryKeyAsVO(DynamicEntityNames.CABECALHO_NOTA, new Object[] { nroUnico });

        DynamicVO topVO = (DynamicVO) notaOrigVO.getProperty("TipoOperacao");

        String validaRegra = topVO.asString("AD_DEDUZISSPISCOFINS");

        if (validaRegra == null)
            validaRegra = "N";

        if (validaRegra.equals("S")) {

            if (tgfDinVO.asBigDecimal("CODIMP").equals(BigDecimal.valueOf(6))
                    || tgfDinVO.asBigDecimal("CODIMP").equals(BigDecimal.valueOf(7))) {

                BigDecimal base = tgfDinVO.asBigDecimal("BASERED");
                BigDecimal aliquota = tgfDinVO.asBigDecimal("ALIQUOTA");
                BigDecimal sequencia = tgfDinVO.asBigDecimal("SEQUENCIA");
                BigDecimal vlrIss = RetornaIssItem(nroUnico, sequencia);
                double baseAjustada = base.doubleValue() - vlrIss.doubleValue();
                double valorAjustado = baseAjustada / 100 * aliquota.doubleValue();

                AtualizaValorImposto(nroUnico, sequencia, tgfDinVO.asBigDecimal("CODIMP"), baseAjustada, valorAjustado);
            }

        }
    }

    @Override
    public void afterDelete(PersistenceEvent persistenceEvent) throws Exception {

    }

    @Override
    public void beforeCommit(TransactionContext transactionContext) throws Exception {

    }

    private void AtualizaValorImposto(BigDecimal nunota, BigDecimal sequencia, BigDecimal codImp, double baseReduzida,
            double valor) throws Exception {

        JdbcWrapper jdbc = EntityFacadeFactory.getDWFFacade().getJdbcWrapper();

        NativeSql sql = new NativeSql(jdbc);
        sql.appendSql(
                "UPDATE TGFDIN SET BASERED = :BASE, VALOR = ROUND(:VALOR,2) WHERE NUNOTA = :NUNOTA AND SEQUENCIA = :SEQUENCIA AND CODIMP = :IMPOSTO");
        sql.setNamedParameter("BASE", baseReduzida);
        sql.setNamedParameter("VALOR", valor);
        sql.setNamedParameter("NUNOTA", nunota);
        sql.setNamedParameter("SEQUENCIA", sequencia);
        sql.setNamedParameter("IMPOSTO", codImp);
        sql.executeUpdate();

    }

    private BigDecimal RetornaIssItem(BigDecimal nunota, BigDecimal sequencia) throws Exception {

        JdbcWrapper jdbc = EntityFacadeFactory.getDWFFacade().getJdbcWrapper();

        NativeSql sql = new NativeSql(jdbc);
        sql.appendSql("SELECT VLRISS FROM TGFITE WHERE NUNOTA = :NUNOTA AND SEQUENCIA = :SEQUENCIA");
        sql.setNamedParameter("NUNOTA", nunota);
        sql.setNamedParameter("SEQUENCIA", sequencia);

        ResultSet rs = sql.executeQuery();

        BigDecimal vlrIss = BigDecimal.ZERO;

        while (rs.next()) {

            vlrIss = rs.getBigDecimal("VLRISS");
        }

        rs.close();

        return vlrIss;
    }
}
