DROP TABLE brz_invetariosped
/

CREATE TABLE brz_invetariosped
    (anomes                         NUMBER(6,0),
    codfilial                      VARCHAR2(2 BYTE),
    codprod                        NUMBER(6,0),
    unidade                        VARCHAR2(20 BYTE),
    quantidade                     NUMBER(16,6),
    valorunitario                  NUMBER(16,6),
    valortotal                     NUMBER(16,6))
  NOPARALLEL
  LOGGING
  MONITORING
/

CREATE UNIQUE INDEX idx_0001 ON brz_invetariosped
  (
    codfilial                       ASC,
    anomes                          ASC,
    codprod                         ASC
  )
NOPARALLEL
LOGGING
/


