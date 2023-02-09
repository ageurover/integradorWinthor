/* 
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Other/SQLTemplate.sql to edit this template
 */
/**
 * Author:  ageur
 * Created: 8 de fev. de 2023
 */

-- Start of DDL Script for Function BRASILDIS.KT_APURACAO_DRE
-- Generated 8-fev-2023 17:33:14 from BRASILDIS@PROD

CREATE OR REPLACE 
FUNCTION kt_apuracao_dre
   ( pDATAINICIAL IN DATE,
     pDATAFINAL IN DATE,
     pCODFILIAL IN VARCHAR2,
     pDEPTO IN NUMBER,
     pSECAO IN NUMBER,
     pVALORFOLHAPAGTO IN NUMBER DEFAULT 0,
     pVALORFOLHAPAGTOADM IN NUMBER DEFAULT 0
   )
   return VARCHAR2
   IS
   PRAGMA AUTONOMOUS_TRANSACTION;
--
-- Purpose: Briefly explain the functionality of the procedure
-- Calcular o DRE por Departamento / Seção
-- Faturamento / CMV / Resultado
-- Baixa de produtos por percas
-- Inventáros de produtos
-- Verbas aplicadas dentro do periodo
--
-- MODIFICATION HISTORY
-- Person      Date    Comments
-- ---------   ------  -------------------------------------------
-- Ageu Rover  01/2023 create
-- Declare program variables as shown above
  vResultado         varchar2(32672);
  vContaVerbaAvaria  VARCHAR2(2000);
  vSqlInsert         VARCHAR2(2000);

  v_vlfaturamentoDepto  NUMBER ;
  v_vlfaturamento       NUMBER ;
  v_vlcustoreal         NUMBER ;
  v_vllucro             NUMBER ;
  vPercLucro            NUMBER ;
  v_vlPercas            NUMBER ;
  vPercPercas           NUMBER ;
  v_vlInventario        NUMBER ;
  v_vlverbarecebida     NUMBER ;
  v_vlverbaaplicada     NUMBER ;
  v_vlverbasaldo        NUMBER ;
  v_vladicaocusto       NUMBER ;
  vPercAdicaoCusto      NUMBER ;
  vqtdReg               NUMBER ;


  vValorDespEnegiaGeral  NUMBER ;
  vPartDespesaDepto      NUMBER ;
  vValorDespEnegiaDepto  NUMBER ;
  vPartDespesaSecao      NUMBER ;
  vValorDespEnergiaSecao NUMBER ;

  vValorDespConsumoSecao NUMBER ;
  vValorDespManutSecao   NUMBER ;

  vValorDespDeptoFolhaAdm     NUMBER ;
  vValorDespSecaoFolhaAdm     NUMBER ;

BEGIN
  vSqlInsert := '';
  vResultado := '';
  vqtdReg := 0;
  vValorDespSecaoFolhaAdm:=0.00;
  vValorDespDeptoFolhaAdm:=0.00;
  vValorDespConsumoSecao:=0.00;
  vValorDespManutSecao:=0.00;
  vValorDespEnegiaDepto:=0.00;
  vValorDespEnergiaSecao:=0.00;
  vPartDespesaDepto :=0.00;
  vPartDespesaSecao := 0.00;
  v_vlfaturamentoDepto :=0.00;
  v_vlfaturamento :=0.00;
  v_vlcustoreal :=0.00;
  v_vllucro :=0.00;
  vPercLucro:=0.00;
  v_vlPercas :=0.00;
  vPercPercas:=0.00;
  v_vlInventario :=0.00;
  v_vlverbarecebida :=0.00;
  v_vlverbaaplicada :=0.00;
  v_vlverbasaldo :=0.00;
  v_vladicaocusto :=0.00;
  vPercAdicaoCusto:=0.00;

  vResultado := vResultado || 'Periodo: ' ||pDATAINICIAL || ' até ' || pDATAFINAL  ||chr(13) || chr(10) ;
  vResultado := vResultado || 'Filial: ' ||PCODFILIAL || ' Departamento ' || PDEPTO || '  Seção: ' || PSECAO ||chr(13) || chr(10) ;

  DECLARE
      -- BUSCA AS VENDAS DO PERIODO AGRUPANDO POR DEPARTAMENTO
        CURSOR C0 IS
             SELECT CODIGO,DESCRICAO,VLCUSTOREAL, VLLIQUIDO, VLLUCRO, PERCLUCRO, PERCPARTTOTAL
                    FROM table(CAST(FUNC_RESUMOFATURAMENTO(PCODFILIAL,
                                                 pDATAINICIAL,
                                                 pDATAFINAL,
                                                 17,
                                                 0,1,0,0,0,0,0,NULL,0,0,0,0,0,0,NULL,NULL,NULL,
                                                 NULL,NULL,NULL,NULL,NULL,
                                                 NULL,
                                                 NULL,NULL,NULL,NULL,NULL,NULL,0,NULL,0,0,0,
                                                 0,NULL,NULL,0,NULL,NULL,NULL,NULL,NULL,NULL,
                                                 NULL,NULL,NULL,NULL,'N' ) as tabela_faturamento)) vendas;

    -- BUSCA AS VENDAS DO PERIODO AGRUPANDO POR SECAO
         CURSOR C1 IS
                SELECT SUM(NVL(vendas.vlliquido,0)) AS vlliquido
                       ,SUM(NVL(vendas.vlcustoreal,0)) AS vlcustoreal
                       ,SUM(NVL(vendas.vllucro,0)) as vllucro
                       ,AVG(NVL(vendas.perclucro,0)) as percLucro
                       ,SUM(NVL(vendas.PERCPARTTOTAL,0)) as percPartTotal
                    FROM table(CAST(FUNC_RESUMOFATURAMENTO(PCODFILIAL,
                                                 pDATAINICIAL,
                                                 pDATAFINAL,
                                                 31,
                                                 0,1,0,0,0,0,0,NULL,0,0,0,0,0,0,NULL,NULL,NULL,
                                                 NULL,NULL,NULL,NULL,NULL,
                                                 PDEPTO,
                                                 NULL,NULL,NULL,NULL,NULL,NULL,0,NULL,0,0,0,
                                                 0,NULL,NULL,0,NULL,NULL,NULL,NULL,NULL,NULL,
                                                 NULL,NULL,NULL,NULL,'N' ) as tabela_faturamento)) vendas
                    WHERE TO_CHAR(vendas.codigo) IN (SELECT CODIGO FROM KT_TBL_GRUPOCONTA WHERE TIPO = 'FATURAMENTO' AND CODEPTO = pDEPTO AND CODSEC = pSECAO) ;


--- ****** PERCAS
       CURSOR C2 IS
           SELECT TOTAL.CODFILIAL,
                  TOTAL.CODCONTA,
                  TOTAL.CONTA,
                  SUM(TOTAL.VLORCAMES) VLORCAMES,
                  SUM(TOTAL.VPAGO) VPAGO,
                  SUM(TOTAL.VLPREVISTO) VLPREVISTO
           FROM (
                  SELECT PCLANC.CODFILIAL,
                         PCCONTA.CODCONTA,
                         PCCONTA.CONTA,
                         0 VLORCAMES,
                         SUM((DECODE(nvl(PCLANC.VPAGO,0),0,DECODE(PCLANC.DESCONTOFIN, PCLANC.VALOR, PCLANC.VALOR, 0),nvl(PCLANC.VPAGO,0)) * (1)) * (-1)) VPAGO,
                         0 VLPREVISTO,
                         COUNT(1) QTLANC
                 FROM PCLANC, PCNFSAID, PCCONTA, PCCONSUM
                      WHERE   1=1
                      AND PCLANC.CODCONTA = PCCONTA.CODCONTA
                      AND PCLANC.DTPAGTO IS NOT NULL
                      AND  (PCLANC.DTPAGTO IS NOT NULL)
                      AND  (PCLANC.DTPAGTO >= TO_DATE(pDATAINICIAL, 'DD/MM/YYYY'))
                      AND  (PCLANC.DTPAGTO <= TO_DATE(pDATAFINAL, 'DD/MM/YYYY'))
                      AND  NVL(PCLANC.VPAGO,0) <> 0
                      AND (NVL(PCCONTA.INVESTIMENTO,'N') <> 'S')
                      AND PCLANC.NUMTRANSVENDA = PCNFSAID.NUMTRANSVENDA(+)
                      AND NVL(PCLANC.CODROTINABAIXA,0) <> 737
                      AND NVL (PCNFSAID.CONDVENDA, 0) NOT IN (10, 20, 98, 99)
                      AND (    (NVL(PCNFSAID.CODFISCAL,0) NOT IN (522,622,722,532,632,732) )
                       OR ( TO_CHAR(PCLANC.CODCONTA) = (SELECT VALOR FROM PCPARAMFILIAL WHERE NOME = 'CON_CODCONTRECJUR') )
                          )
                     AND (NOT EXISTS (SELECT D.NUMTRANSENT
                                        FROM PCESTCOM D, PCNFSAID N
                                           WHERE D.NUMTRANSENT = PCLANC.NUMTRANSENT
                                           AND D.DTESTORNO = PCLANC.DTLANC
                                           AND D.NUMTRANSVENDA = N.NUMTRANSVENDA
                                           AND N.CONDVENDA IN (20)))
                    AND PCLANC.CODFILIAL IN (PCODFILIAL)
                    AND Pcconta.CODCONTA in (SELECT CODIGO FROM KT_TBL_GRUPOCONTA WHERE TIPO = 'PERDAS' AND CODEPTO=PDEPTO AND CODSEC =pSECAO)
                GROUP BY Pcconta.CODCONTA, Pcconta.CONTA,PCLANC.CODFILIAL, PCLANC.CODCONTA, PCCONSUM.CODCONTAADIANTFOR, PCCONSUM.CODCONTAADIANTFOROUTROS
       UNION ALL
                SELECT PCLANC.CODFILIAL,
                       PCCONTA.CODCONTA,
                       PCCONTA.CONTA,
                       0 VLORCAMES,
                       0 VPAGO,
                       SUM(NVL(PCLANC.VALOR, 0) * (-1)) VLPREVISTO,
                       COUNT(1) QTLANC
                FROM PCLANC, PCNFSAID, PCCONTA, PCCONSUM
                  WHERE   1=1
                  AND PCLANC.CODCONTA = PCCONTA.CODCONTA
                  AND PCLANC.DTPAGTO IS NULL
                  AND (NVL(PCCONTA.INVESTIMENTO,'N') <> 'S')
                  AND PCLANC.NUMTRANSVENDA = PCNFSAID.NUMTRANSVENDA(+)
                  AND NVL(PCLANC.CODROTINABAIXA,0) <> 737
                  AND PCLANC.VALOR <> 0
                  AND NVL (PCNFSAID.CONDVENDA, 0) NOT IN (10, 20, 98, 99)
                  AND NVL(PCNFSAID.CODFISCAL,0) NOT IN (522,622,722,532,632,732)
                  AND (NOT EXISTS (SELECT D.NUMTRANSENT
                                     FROM PCESTCOM D, PCNFSAID N
                                     WHERE D.NUMTRANSENT = PCLANC.NUMTRANSENT
                                     AND D.NUMTRANSVENDA = N.NUMTRANSVENDA
                                     AND D.DTESTORNO = PCLANC.DTLANC
                                     AND N.CONDVENDA IN (20)))
                   AND  (PCLANC.DTPAGTO IS NULL)
                   AND  NVL(PCLANC.VALOR,0) <> 0
                   AND  (PCLANC.DTVENC >= TO_DATE(pDATAINICIAL, 'DD/MM/YYYY'))
                   AND  (PCLANC.DTVENC <= TO_DATE(pDATAFINAL, 'DD/MM/YYYY'))
                   AND  PCLANC.CODFILIAL IN(PCODFILIAL)
                   AND Pcconta.CODCONTA in (SELECT CODIGO FROM KT_TBL_GRUPOCONTA WHERE TIPO = 'PERDAS' AND CODEPTO=PDEPTO AND CODSEC =pSECAO)
             GROUP BY Pcconta.CODCONTA, Pcconta.CONTA,PCLANC.CODFILIAL,PCLANC.CODCONTA, PCCONSUM.CODCONTAADIANTFOR, PCCONSUM.CODCONTAADIANTFOROUTROS
     UNION ALL
             SELECT LA.CODFILIAL,
                    LA.CODCONTA,
                    PCCONTA.CONTA,
                    0 VLORCAMES,
                    SUM(BA.VPAGO + NVL(BA.VLVARIACAOCAMBIAL, 0))  AS VPAGO,
                    0 VLPREVISTO,
                    COUNT(1) QTLANC
             FROM PCLANCADIANTFORNEC A
                  ,PCLANC LA --LANCAMENTO DO ADIANTAMENTO
                  ,PCLANC BA  --BAIXA DO ADIANTAMENTO
                  ,PCCONTA
                  ,PCCONSUM
               WHERE A.RECNUMADIANTAMENTO = LA.RECNUM
               AND   A.RECNUMPAGTO        = BA.RECNUM
               AND   A.DTESTORNO IS NULL
               AND   PCCONTA.CODCONTA = LA.CODCONTA
               AND   Pcconta.CODCONTA in (SELECT CODIGO FROM KT_TBL_GRUPOCONTA WHERE TIPO = 'PERDAS' AND CODEPTO=PDEPTO AND CODSEC =pSECAO)
               AND   A.DTLANC BETWEEN TO_DATE(pDATAINICIAL, 'DD/MM/YYYY') AND TO_DATE(pDATAFINAL, 'DD/MM/YYYY')
               AND   ((LA.CODCONTA = PCCONSUM.CODCONTAADIANTFOR) OR (LA.CODCONTA = PCCONSUM.CODCONTAADIANTFOROUTROS))
               AND   BA.DTPAGTO IS NOT NULL
               AND LA.CODFILIAL IN(PCODFILIAL)
               AND   BA.DTESTORNOBAIXA IS NULL
            GROUP BY  LA.CODFILIAL,  LA.CODCONTA,  PCCONTA.CONTA
     ) TOTAL
     WHERE TOTAL.CODCONTA in (SELECT CODIGO FROM KT_TBL_GRUPOCONTA WHERE TIPO = 'PERDAS' AND CODEPTO=PDEPTO AND CODSEC =pSECAO)
     HAVING  SUM(TOTAL.QTLANC) > 0
     GROUP BY  TOTAL.CODFILIAL, TOTAL.CODCONTA, TOTAL.CONTA
     ORDER BY SUM( TOTAL.VPAGO ) DESC;


---*********** INVENTARIO
    CURSOR C3 IS
           SELECT ROUND(SUM((M.QT * M.PUNIT) ),2) AS INVENTARIO_TOTAL, M.CODEPTO, M.CODSEC
            FROM PCMOV M,
                (SELECT  PCLANC.RECNUM, PCLANC.NUMTRANSENT,
                        PCLANC.DTPAGTO,
                        CONCAT(CONCAT(TRIM(PCLANC.HISTORICO), '. '), TRIM(PCLANC.HISTORICO2)) HISTORICO,
                        (DECODE(PCLANC.DTPAGTO, NULL, PCLANC.VALOR, (DECODE(nvl(PCLANC.VPAGO,0),0,DECODE(PCLANC.DESCONTOFIN, PCLANC.VALOR, PCLANC.VALOR, 0),nvl(PCLANC.VPAGO,0)) * (1)))) * (-1) VPAGO,
                        PCLANC.NUMNOTA,
                        PCLANC.NUMBANCO, PCLANC.NUMCHEQUE2, PCLANC.LOCALIZACAO, PCLANC.NOMEFUNC,
                        DECODE(PCLANC.TIPOPARCEIRO,
                             'F', (SELECT FORNECEDOR FROM PCFORNEC WHERE CODFORNEC = PCLANC.CODFORNEC),
                             'R', (SELECT NOME FROM PCUSUARI WHERE CODUSUR = PCLANC.CODFORNEC),
                             'M', (SELECT NOME FROM PCEMPR WHERE MATRICULA = PCLANC.CODFORNEC),
                             'L', (SELECT NOME FROM PCEMPR WHERE MATRICULA = PCLANC.CODFORNEC),
                             'C', (SELECT CLIENTE FROM PCCLIENT WHERE CODCLI = PCLANC.CODFORNEC),
                             'OUTROS') AS  FORNECEDOR,
                       PCLANC.DTRECLASSIFIC, PCLANC.CODFUNCRECLASSIFIC,
                       PCLANC.CODFILIAL,
                       (SELECT NOME FROM PCEMPR WHERE MATRICULA = PCLANC.CODFUNCBAIXA) NOMEFUNCBAIXA, DECODE(PCLANC.DTPAGTO, NULL, 'P', 'R') TIPOLANC
                FROM PCLANC,PCNFSAID, PCCONTA, PCCONSUM
                  WHERE   (PCLANC.CODCONTA = 402999)
                  AND PCLANC.CODCONTA = PCCONTA.CODCONTA
                  AND (NVL(PCCONTA.INVESTIMENTO,'N') <> 'S')
                  AND NVL(PCLANC.CODROTINABAIXA,0) <> 737
                  AND PCLANC.NUMTRANSVENDA = PCNFSAID.NUMTRANSVENDA(+)
                  AND NVL (PCNFSAID.CONDVENDA, 0) NOT IN (10, 20, 98, 99)
                  AND ((NVL(PCNFSAID.CODFISCAL,0) NOT IN (522,622,722,532,632,732) )
                      OR ( TO_CHAR(PCLANC.CODCONTA) = (SELECT VALOR FROM PCPARAMFILIAL WHERE NOME = 'CON_CODCONTRECJUR') )
                      )
                  AND (NOT EXISTS (SELECT D.NUMTRANSENT
                                     FROM PCESTCOM D, PCNFSAID N
                                     WHERE D.NUMTRANSENT = PCLANC.NUMTRANSENT
                                     AND D.DTESTORNO = PCLANC.DTLANC
                                     AND D.NUMTRANSVENDA = N.NUMTRANSVENDA
                                     AND N.CONDVENDA IN (20)))
                   AND  ( --realizados
                        (  (PCLANC.DTPAGTO IS NOT NULL)
                           AND  (PCLANC.DTPAGTO BETWEEN TO_DATE(pDATAINICIAL, 'DD/MM/YYYY') AND TO_DATE(pDATAFINAL, 'DD/MM/YYYY'))
                           AND  NVL(PCLANC.VPAGO,0) <> 0
                        )
                  OR --previstos
                       (  (PCLANC.DTPAGTO IS NULL)
                          AND  PCLANC.DTVENC BETWEEN TO_DATE(pDATAINICIAL, 'DD/MM/YYYY') AND TO_DATE(pDATAFINAL, 'DD/MM/YYYY')
                          AND  NVL(PCLANC.VALOR,0) <> 0
                        ) )
                  AND PCLANC.CODFILIAL = PCODFILIAL
               UNION ALL
               SELECT BA.RECNUM, BA.NUMTRANSENT
                      ,BA.DTPAGTO
                      ,'BX ADTO FORNEC REF LANC '|| A.RECNUMADIANTAMENTO || ' - PG ' || TO_CHAR(LA.DTPAGTO,'DD/MM/YYYY')  AS  HISTORICO
                      ,BA.VPAGO + NVL(BA.VLVARIACAOCAMBIAL, 0) AS VPAGO
                      ,BA.NUMNOTA
                      ,BA.NUMBANCO
                      ,BA.NUMCHEQUE2
                      ,BA.LOCALIZACAO
                      ,BA.NOMEFUNC
                      ,DECODE(BA.TIPOPARCEIRO
                      ,'F' ,(SELECT FORNECEDOR FROM PCFORNEC WHERE CODFORNEC = BA.CODFORNEC)
                      ,'R'  ,(SELECT NOME FROM PCUSUARI WHERE CODUSUR = BA.CODFORNEC)
                      ,'M'  ,(SELECT NOME FROM PCEMPR WHERE MATRICULA = BA.CODFORNEC)
                      ,'L'  ,(SELECT NOME FROM PCEMPR WHERE MATRICULA = BA.CODFORNEC)
                      ,'C'  ,(SELECT CLIENTE FROM PCCLIENT WHERE CODCLI = BA.CODFORNEC)
                      ,'OUTROS') AS FORNECEDOR
                      ,BA.DTRECLASSIFIC
                      ,BA.CODFUNCRECLASSIFIC
                      ,BA.CODFILIAL
                      ,(SELECT NOME FROM PCEMPR WHERE MATRICULA = BA.CODFUNCBAIXA) NOMEFUNCBAIXA,DECODE(BA.DTPAGTO, NULL, 'P', 'R') TIPOLANC
                FROM PCLANCADIANTFORNEC A
                     ,PCLANC LA --LANCAMENTO DO ADIANTAMENTO
                     ,PCLANC BA --BAIXA DO ADIANTAMENTO
                     ,PCCONTA
                     ,PCCONSUM
               WHERE A.RECNUMADIANTAMENTO = LA.RECNUM
                 AND A.RECNUMPAGTO = BA.RECNUM
                 AND A.DTESTORNO IS NULL
                 AND PCCONTA.CODCONTA = LA.CODCONTA
                 AND A.DTLANC BETWEEN TO_DATE(pDATAINICIAL, 'DD/MM/YYYY') AND TO_DATE(pDATAFINAL, 'DD/MM/YYYY')
                 AND ((LA.CODCONTA = PCCONSUM.CODCONTAADIANTFOR) OR
                      (LA.CODCONTA = PCCONSUM.CODCONTAADIANTFOROUTROS))
                 AND BA.DTPAGTO IS NOT NULL
                 AND LA.CODCONTA = 402999
                 AND LA.CODFILIAL = PCODFILIAL
                 AND BA.DTESTORNOBAIXA IS NULL
             ) L
        WHERE M.NUMTRANSENT = L.NUMTRANSENT
        AND M.CODEPTO = PDEPTO
        AND M.CODSEC in (SELECT CODIGO FROM KT_TBL_GRUPOCONTA WHERE TIPO = 'FATURAMENTO' AND CODEPTO = pDEPTO AND CODSEC = pSECAO)
        GROUP BY M.CODEPTO, M.CODSEC;

   ---************
   -- VERBAS
   CURSOR C4 IS
        SELECT VERBAS.CODCONTA
               ,SUM(VERBAS.VALOR) AS VALOR
               ,SUM(VERBAS.VLAPLIC) AS VLAPLIC
               ,SUM(VERBAS.SALDO) AS SALDO
           FROM
                 (SELECT PCVERBA.NUMVERBA
                             , PCVERBA.CODFILIAL
                             , PCFORNEC.CODFORNEC
                             , PCFORNEC.CODFORNEC CODFOR
                             , PCFORNEC.FORNECEDOR
                             , PCVERBA.DTEMISSAO
                             , PCVERBA.DTVENC
                             , PCVERBA.TIPO
                             , DECODE(PCVERBA.FORMAPGTO, 'M', 'Merc.', 'Din.') FORMAPGTO
                             , PCVERBA.VALOR
                             , SUM(NVL(VLAPLIC, 0)) VLAPLIC
                             , MAX(PCAPLICVERBA.DTAPLIC) DTAPLIC
                             , (PCVERBA.VALOR -(SUM(NVL(PCAPLICVERBA.VLAPLIC, 0)))) SALDO
                             , PCVERBA.CODCONTA
                             , PCVERBA.REFERENCIA
                             , PCVERBA.REFERENCIA1
                             , PCCONTA.CONTA
                          FROM PCVERBA
                             , PCCONTA
                             , PCFORNEC
                             , PCAPLICVERBA
                         WHERE PCVERBA.CODCONTA = PCCONTA.CODCONTA
                           AND PCVERBA.CODFORNEC = PCFORNEC.CODFORNEC
                           AND PCVERBA.CODFILIAL = PCODFILIAL
                           AND PCVERBA.NUMVERBA BETWEEN 0 AND 9999999
                           AND PCAPLICVERBA.NUMVERBA(+) = PCVERBA.NUMVERBA
                           AND PCVERBA.NUMVERBA IN(SELECT A.NUMVERBA
                                                     FROM PCAPLICVERBA A
                                                     WHERE A.DTAPLIC BETWEEN TO_DATE(pDATAINICIAL, 'DD/MM/YYYY') AND TO_DATE(pDATAFINAL, 'DD/MM/YYYY')
                                                    )
                        AND PCVERBA.CODCONTA = PARAMFILIAL.OBTERCOMOVARCHAR2('CODCONTAVERBAAVARIA')
                        GROUP BY PCVERBA.NUMVERBA,
                        PCVERBA.CODFILIAL,
                        PCFORNEC.CODFORNEC,
                        PCFORNEC.FORNECEDOR,
                        PCVERBA.DTEMISSAO,
                        PCVERBA.DTVENC,
                        PCVERBA.TIPO,
                        PCVERBA.FORMAPGTO,
                        PCVERBA.VALOR,
                        PCVERBA.CODCONTA,
                        PCVERBA.REFERENCIA,
                        PCVERBA.REFERENCIA1,
                        PCCONTA.CONTA
                        --HAVING ROUND((PCVERBA.valor - (SUM(NVL(PCAPLICVERBA.VLAPLIC, 0)))),2) <= 0.02 -- SOMENTE AS APLICADAS
                        ORDER BY PCFORNEC.CODFORNEC, PCVERBA.NUMVERBA
                ) VERBAS
        WHERE 1=1
        AND VERBAS.CODFORNEC IN (SELECT DISTINCT CODFORNEC FROM PCPRODUT WHERE CODEPTO = PDEPTO AND CODSEC in (SELECT CODIGO FROM KT_TBL_GRUPOCONTA WHERE TIPO = 'FATURAMENTO' AND CODEPTO = pDEPTO AND CODSEC = pSECAO))
        GROUP BY VERBAS.CODCONTA;

   ---************
   ---************
   -- Adição de custo na entrada 1301
   CURSOR C5 IS
        select ROUND(nvl(sum(decode(nvl(pcmov.VLDESPFORANF,0), 0,((pcmov.punit * (pcmov.percoutroscustos/100))*PCMOV.QT), pcmov.VLDESPFORANF*PCMOV.QT)),0),2) AS TOTAL_VLDESPFORANF
            from pcmov
                where 1 = 1
                AND PCMOV.CODFILIAL = pCODFILIAL
                AND PCMOV.DTMOV BETWEEN TO_DATE(pDATAINICIAL,'DD/MM/YYYY') AND TO_DATE(pDATAFINAL,'DD/MM/YYYY')
                AND PCMOV.codoper IN ('E')
                AND PCMOV.codepto = PDEPTO
                AND PCMOV.CODSEC IN (SELECT CODIGO FROM KT_TBL_GRUPOCONTA WHERE TIPO = 'FATURAMENTO' AND CODEPTO = pDEPTO AND CODSEC = pSECAO);

   ---************
   --- Despesas Energia Eletrica total do periodo
   CURSOR C6 IS
       SELECT   sum(pclanc.valor) totalPagar, sum(pclanc.vpago) totalPago
          FROM   pclanc,
                 pcnfsaid,
                 pcconta,
                 pcconsum
         WHERE       1 = 1
                 AND pclanc.codconta = pcconta.codconta
                 AND pclanc.dtpagto IS NOT NULL
                 AND (pclanc.dtpagto IS NOT NULL)
                 AND (pclanc.dtpagto >= TO_DATE (pdatainicial, 'DD/MM/YYYY'))
                 AND (pclanc.dtpagto <= TO_DATE (pdatafinal, 'DD/MM/YYYY'))
                 AND NVL (pclanc.vpago, 0) <> 0
                 AND (NVL (pcconta.investimento, 'N') <> 'S')
                 AND pclanc.numtransvenda = pcnfsaid.numtransvenda(+)
                 AND NVL (pclanc.codrotinabaixa, 0) <> 737
                 AND NVL (pcnfsaid.condvenda, 0) NOT IN (10, 20, 98, 99)
                 AND ( (NVL (pcnfsaid.codfiscal, 0) NOT IN
                                (522, 622, 722, 532, 632, 732))
                      OR (TO_CHAR (pclanc.codconta) =
                              (SELECT   valor
                                 FROM   pcparamfilial
                                WHERE   nome = 'CON_CODCONTRECJUR')))
                 AND (NOT EXISTS
                          (SELECT   d.numtransent
                             FROM   pcestcom d, pcnfsaid n
                            WHERE       d.numtransent = pclanc.numtransent
                                    AND d.dtestorno = pclanc.dtlanc
                                    AND d.numtransvenda = n.numtransvenda
                                    AND n.condvenda IN (20)))
                 AND pclanc.codfilial IN (pcodfilial)
                 AND Pcconta.CODCONTA in (SELECT CODIGO FROM KT_TBL_GRUPOCONTA WHERE TIPO = 'ENERGIA');


    ---************
    --- DESPESAS INTERNAS DE MATERIAL DE CONSUMO PARA O SETOR
    CURSOR C7 IS
      SELECT   sum(pclanc.valor) as valorPagar, sum(pclanc.vpago) as valorPago
          FROM   pclanc,
                 pcnfsaid,
                 pcconta,
                 pcconsum
         WHERE       1 = 1
                 AND pclanc.codconta = pcconta.codconta
                 AND pclanc.dtpagto IS NOT NULL
                 AND (pclanc.dtpagto IS NOT NULL)
                 AND (pclanc.dtpagto >= TO_DATE (pdatainicial, 'DD/MM/YYYY'))
                 AND (pclanc.dtpagto <= TO_DATE (pdatafinal, 'DD/MM/YYYY'))
                 AND NVL (pclanc.vpago, 0) <> 0
                 AND (NVL (pcconta.investimento, 'N') <> 'S')
                 AND pclanc.numtransvenda = pcnfsaid.numtransvenda(+)
                 AND NVL (pclanc.codrotinabaixa, 0) <> 737
                 AND NVL (pcnfsaid.condvenda, 0) NOT IN (10, 20, 98, 99)
                 AND ( (NVL (pcnfsaid.codfiscal, 0) NOT IN
                                (522, 622, 722, 532, 632, 732))
                      OR (TO_CHAR (pclanc.codconta) =
                              (SELECT   valor
                                 FROM   pcparamfilial
                                WHERE   nome = 'CON_CODCONTRECJUR')))
                 AND (NOT EXISTS
                          (SELECT   d.numtransent
                             FROM   pcestcom d, pcnfsaid n
                            WHERE       d.numtransent = pclanc.numtransent
                                    AND d.dtestorno = pclanc.dtlanc
                                    AND d.numtransvenda = n.numtransvenda
                                    AND n.condvenda IN (20)))
                 AND pclanc.codfilial IN (pcodfilial)
                 AND Pcconta.CODCONTA in (SELECT CODIGO FROM KT_TBL_GRUPOCONTA WHERE TIPO = 'CONSUMO_INTERNO'  AND CODEPTO = pDEPTO AND CODSEC = pSECAO);

    ---************
    --- DESPESAS MANUTENCAO POR SETOR
    CURSOR C8 IS
      SELECT   sum(pclanc.valor) as valorPagar, sum(pclanc.vpago) as valorPago
          FROM   pclanc,
                 pcnfsaid,
                 pcconta,
                 pcconsum
         WHERE       1 = 1
                 AND pclanc.codconta = pcconta.codconta
                 AND pclanc.dtpagto IS NOT NULL
                 AND (pclanc.dtpagto IS NOT NULL)
                 AND (pclanc.dtpagto >= TO_DATE (pdatainicial, 'DD/MM/YYYY'))
                 AND (pclanc.dtpagto <= TO_DATE (pdatafinal, 'DD/MM/YYYY'))
                 AND NVL (pclanc.vpago, 0) <> 0
                 AND (NVL (pcconta.investimento, 'N') <> 'S')
                 AND pclanc.numtransvenda = pcnfsaid.numtransvenda(+)
                 AND NVL (pclanc.codrotinabaixa, 0) <> 737
                 AND NVL (pcnfsaid.condvenda, 0) NOT IN (10, 20, 98, 99)
                 AND ( (NVL (pcnfsaid.codfiscal, 0) NOT IN
                                (522, 622, 722, 532, 632, 732))
                      OR (TO_CHAR (pclanc.codconta) =
                              (SELECT   valor
                                 FROM   pcparamfilial
                                WHERE   nome = 'CON_CODCONTRECJUR')))
                 AND (NOT EXISTS
                          (SELECT   d.numtransent
                             FROM   pcestcom d, pcnfsaid n
                            WHERE       d.numtransent = pclanc.numtransent
                                    AND d.dtestorno = pclanc.dtlanc
                                    AND d.numtransvenda = n.numtransvenda
                                    AND n.condvenda IN (20)))
                 AND pclanc.codfilial IN (pcodfilial)
                 AND Pcconta.CODCONTA in (SELECT CODIGO FROM KT_TBL_GRUPOCONTA WHERE TIPO = 'MANUTENCAO'  AND CODEPTO = pDEPTO AND CODSEC = pSECAO);

    ---************

    R0 C0%ROWTYPE; -- FATURAMENTO E CMV por DEPTO
    R1 C1%ROWTYPE; -- FATURAMENTO E CMV por seção
    R2 C2%ROWTYPE; -- PERCAS
    R3 C3%ROWTYPE; -- INVENTARIO
    R4 C4%ROWTYPE; -- VERBAS AVARIAS
    R5 C5%ROWTYPE; -- ADICAO AO CUSTO DE ENTRADA 1301
    R6 C6%ROWTYPE; -- DESPEAS ENERGIA ELETRICA
    R7 C7%ROWTYPE; -- DESPEAS INTERNA MATERIA DE CONSUMO / INSUMOS
    R8 C8%ROWTYPE; -- DESPEAS MANUTENÇÃO DE EQUIPAMENTOS DEPTO/SECAO

    ---*********
    BEGIN
        -- FATURAMENTO Departamento
         -- PEGA O % DE PARTICIPAÇÃO DO DEPTO PARA AS DEPESAS TOTAIS DE ENERGIA E ADMINISTRATIVAS
          OPEN C0;
              LOOP
                FETCH C0
                INTO R0;

                EXIT WHEN C0%NOTFOUND;
                    IF R0.CODIGO = pDEPTO THEN
                        v_vlfaturamentoDepto := v_vlfaturamentoDepto + R0.vlliquido;
                        vPartDespesaDepto := vPartDespesaDepto + R0.PERCPARTTOTAL;
                    END IF;

             END LOOP;
             vResultado := vResultado || 'Faturamento Depto......: ' || v_vlfaturamentoDepto ||chr(13) || chr(10) ;
             vResultado := vResultado || 'Participa Despesa %....: ' || vPartDespesaDepto ||chr(13) || chr(10) ;

         -- FATURAMENTO por seção
         -- PEGA O % DE PARTICIPAÇÃO DAS VENDAS DO SETOR NO DEPTO PARA AS DEPESAS PARCIAS DE ENERGIA E ADMINISTRATIVAS
          OPEN C1;
              LOOP
                FETCH C1
                INTO R1;

                EXIT WHEN C1%NOTFOUND;

                    v_vlfaturamento := R1.vlliquido;
                    v_vlcustoreal := R1.vlcustoreal;
                    v_vllucro := R1.vllucro;
                    vPercLucro := R1.perclucro;
                    vPartDespesaSecao:= R1.PERCPARTTOTAL;
             END LOOP;
             vResultado := vResultado || 'Participa Despesa seção %: ' || vPartDespesaSecao ||chr(13) || chr(10) ;
             vResultado := vResultado || 'Faturamento......: ' || v_vlfaturamento ||chr(13) || chr(10) ;
             vResultado := vResultado || 'CMV(-)...........: ' || v_vlcustoreal ||chr(13) || chr(10) ;
             vResultado := vResultado || 'Resultado........: ' || v_vllucro || ' %Lucro:' || vPercLucro ||chr(13) || chr(10) ;

        --- PERCAS
          OPEN C2;
              LOOP
                FETCH C2
                INTO R2;

                EXIT WHEN C2%NOTFOUND;
                v_vlPercas := v_vlPercas + R2.VPAGO;
             END LOOP;
             vPercPercas:= Round((v_vlPercas / v_vlfaturamento) * 100,6);
             vResultado := vResultado || 'Perdas(-)........: ' || Round(v_vlPercas,4) || ' % Perdas: ' || vPercPercas ||chr(13) || chr(10) ;

        --- INVENTARIO
         OPEN C3;
              LOOP
                FETCH C3
                INTO R3;
                EXIT WHEN C3%NOTFOUND;
                v_vlInventario := v_vlInventario + R3.INVENTARIO_TOTAL;
             END LOOP;
             vResultado := vResultado || 'Inventario(+/-)..: ' || Round(v_vlInventario,4) ||chr(13) || chr(10) ;

        --- VERBAS
         OPEN C4;
              LOOP
                FETCH C4
                INTO R4;
                EXIT WHEN C4%NOTFOUND;

                v_vlverbarecebida := R4.VALOR;
                v_vlverbaaplicada := R4.VLAPLIC;
                v_vlverbasaldo := R4.SALDO;
             END LOOP;
               vResultado := vResultado || 'Verbas Recebidas .....: ' || round(v_vlverbarecebida,4) ||chr(13) || chr(10) ;
               vResultado := vResultado || 'Verbas Aplicadas (+)..: ' || round(v_vlverbaaplicada,4) ||chr(13) || chr(10) ;
               vResultado := vResultado || 'Verbas Saldo a Aplic..: ' || round(v_vlverbasaldo,4)    ||chr(13) || chr(10) ;

        --- ADICAO AO CUSTO
         OPEN C5;
              LOOP
                FETCH C5
                INTO R5;
                EXIT WHEN C5%NOTFOUND;
                v_vladicaocusto := v_vladicaocusto + R5.TOTAL_VLDESPFORANF;
             END LOOP;
             vPercAdicaoCusto:= round((v_vladicaocusto / v_vlcustoreal)*100,6);

             vResultado := vResultado || 'Valor Add Custo Ent...: ' || round(v_vladicaocusto,4)  || ' % Adicionado: ' || vPercAdicaoCusto ||chr(13) || chr(10) ;

        --- ENERGIA ELETRICA
            -- PEGA O VALOR TOTAL E ENCONTRA O VALOR DO DEPARTAMENTO COM BASE NA PARTICIPA DAS VENDAS
            -- PEGA O VALOR DO DEPTO E ENCOTRA O VALOR DO SETOR DENTRO DAS VENDAS DO DEPTO
         OPEN C6;
              LOOP
                FETCH C6
                INTO R6;
                EXIT WHEN C6%NOTFOUND;
                vValorDespEnegiaGeral:=  R6.totalPago;
                -- valor conforme participacao do depto no tatal da energia eletrica;
                vValorDespEnegiaDepto:= Round(R6.totalPago * (vPartDespesaDepto/100),4);
                -- valor conforme partivipacao da secao no total do depto da energia eletrica;
                vValorDespEnergiaSecao := Round(vValorDespEnegiaDepto * (vPartDespesaSecao/100),4);

             END LOOP;
             vResultado := vResultado || 'Valor Energia Geral...: ' || Round(vValorDespEnegiaGeral,4) ||chr(13) || chr(10) ;
             vResultado := vResultado || 'Valor Energia Depto...: ' || Round(vValorDespEnegiaDepto,4) || ' %Part.:' || vPartDespesaDepto ||chr(13) || chr(10) ;
             vResultado := vResultado || 'Valor Energia Secao...: ' || Round(vValorDespEnergiaSecao,4)  || ' %Part.:' || vPartDespesaSecao ||chr(13) || chr(10) ;

        --- DESPESAS COM CONSUMO INTERNO DO DEPTO/SECAO
         OPEN C7;
              LOOP
                FETCH C7
                INTO R7;
                EXIT WHEN C7%NOTFOUND;
                vValorDespConsumoSecao := vValorDespConsumoSecao + R7.valorPago;
             END LOOP;
             vResultado := vResultado || 'Valor Consumo Interno...: ' || Round(vValorDespConsumoSecao,4)  ||chr(13) || chr(10) ;

        --- DESPESAS COM MANUTENCAO DO DEPTO/SECAO
         OPEN C8;
              LOOP
                FETCH C8
                INTO R8;
                EXIT WHEN C8%NOTFOUND;
                vValorDespManutSecao := vValorDespManutSecao + R8.valorPago;
             END LOOP;
             vResultado := vResultado || 'Valor Manuteção secao...: ' || Round(vValorDespManutSecao,4)  ||chr(13) || chr(10) ;

    END;

-- CALCULAR DEPESAS FOLHA ADM PROPORCIONAL
vValorDespDeptoFolhaAdm := pVALORFOLHAPAGTOADM * (vPartDespesaDepto/100);
vValorDespSecaoFolhaAdm := Round(vValorDespDeptoFolhaAdm * (vPartDespesaSecao/100),4);

-- VALIDA SE JA EXISTE REGISTRO NA TABELA
  SELECT COUNT(1) INTO vqtdReg
       FROM kt_tbl_apuracao_dre tbl
         WHERE 1=1
           AND ( tbl.DATAINICIAL BETWEEN TO_DATE(pDATAINICIAL, 'DD/MM/YYYY') AND TO_DATE(pDATAFINAL, 'DD/MM/YYYY')
               OR tbl.DATAFINAL BETWEEN TO_DATE(pDATAINICIAL, 'DD/MM/YYYY') AND TO_DATE(pDATAFINAL, 'DD/MM/YYYY') )
           AND tbl.CODFILIAL = PCODFILIAL
           AND tbl.CODEPTO = PDEPTO
           AND tbl.CODSEC = PSECAO;

  IF vqtdReg >0 THEN
  -- SE EXISTIR APAGA
    DELETE FROM kt_tbl_apuracao_dre tbl WHERE 1=1
           AND ( tbl.DATAINICIAL BETWEEN TO_DATE(pDATAINICIAL, 'DD/MM/YYYY') AND TO_DATE(pDATAFINAL, 'DD/MM/YYYY')
               OR tbl.DATAFINAL BETWEEN TO_DATE(pDATAINICIAL, 'DD/MM/YYYY') AND TO_DATE(pDATAFINAL, 'DD/MM/YYYY') )
           AND tbl.CODFILIAL = PCODFILIAL
           AND tbl.CODEPTO = PDEPTO
           AND tbl.CODSEC = PSECAO;
  END IF;

  -- INSERE OS NOVOS DADOS NA TABELA
  INSERT INTO kt_tbl_apuracao_dre (
              DATAINICIAL,DATAFINAL,CODFILIAL,CODEPTO,CODSEC,
              VLFATURAMENTO,VLCUSTOREAL,VLLUCRO,PERCLUCRO,
              VLPERCAS,VLINVENTARIO,
              VLVERBARECEBIDA,VLVERBAAPLICADA,VLVERBASALDO,
              VLADICAOCUSTO,PERCADICAOCUSTO,
              VLFOLHAPAGTO,VLFOLHAPAGTOADM,
              VLENERGIA,VLMANUTENCAO,VLCONSUMOINTERNO,
              PERCDESPDEPTO,PERCDESPSECAO
              ) VALUES (
                     TO_DATE(pDATAINICIAL, 'DD/MM/YYYY'),
                     TO_DATE(pDATAFINAL, 'DD/MM/YYYY'),
                     PCODFILIAL,
                     PDEPTO,
                     PSECAO,
                     v_vlfaturamento,
                     v_vlcustoreal,
                     v_vllucro,
                     vPercLucro,
                     v_vlPercas,
                     v_vlInventario,
                     v_vlverbarecebida,
                     v_vlverbaaplicada,
                     v_vlverbasaldo,
                     v_vladicaocusto,
                     vPercAdicaoCusto,
                     (pVALORFOLHAPAGTO*-1),
                     (vValorDespSecaoFolhaAdm*-1),
                     (vValorDespEnergiaSecao*-1),
                     (vValorDespManutSecao*-1),
                     (vValorDespConsumoSecao*-1),
                     vPartDespesaDepto,
                     vPartDespesaSecao
                    );

  commit;
  dbms_output.put_line(vResultado);
  return vResultado;
EXCEPTION
    WHEN OTHERS THEN
        ROLLBACK;
        raise_application_error(-20001,'Erros encontrados Procedure kt_apuracao_dre - '||SQLCODE||' -ERROR- '||SQLERRM);

END; -- Procedure
/



-- End of DDL Script for Function BRASILDIS.KT_APURACAO_DRE
