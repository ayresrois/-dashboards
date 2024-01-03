for(var i = 0; i < linhas.length; i++){
    var linha = linhas[i];

var qryFin = getQuery();

qryFin.nativeSelect("SELECT NVL(UF.AD_PARCGNRE,0) AS PARCGNRE, CAB.VLRICMSDIFALDEST AS DIFAL, (SELECT NVL(SUM(VLRFCP),0) FROM TGFDIN WHERE NUNOTA = CAB.NUNOTA) AS FCP FROM TGFCAB CAB INNER JOIN TGFPAR PAR ON CAB.CODPARC = PAR.CODPARC INNER JOIN TSICID CID ON CAB.CODCIDENTREGA = CID.CODCID INNER JOIN TSIUFS UF ON CID.UF = UF.CODUF WHERE CAB.NUNOTA  = "+linha.getCampo("NUNOTA"));
	while(qryFin.next()){

var fin = novaLinha("Financeiro");
  fin.setCampo("CODEMP",1);
  fin.setCampo("DTNEG", linha.getCampo("DTNEG"));
  fin.setCampo("DTVENC", getParam("DTVENC"));
  fin.setCampo("CODPARC", qryFin.getBigDecimal("PARCGNRE"));
  fin.setCampo("CODNAT", getParam("CODNAT"));
  fin.setCampo("CODVEND", 0);
  fin.setCampo("CODTIPTIT", 4);
  fin.setCampo("VLRDESDOB", qryFin.getDouble("DIFAL"));
  fin.setCampo("RECDESP", -1);
  fin.setCampo("CODCTABCOINT", getParam("CODCTABCOINT"));
  fin.setCampo("CODCENCUS", 0);
  fin.setCampo("CODBCO", getParam("CODBCO"));
  fin.setCampo("ORIGEM", 'F');
  fin.setCampo("CODTIPOPER", 51);
  fin.setCampo("DTENTSAI", linha.getCampo("DTNEG"));
  fin.setCampo("NUMNOTA", linha.getCampo("NUMNOTA"));
  fin.setCampo("DESDOBRAMENTO", '1');
  fin.setCampo("HISTORICO", getParam("HISTORICO"));
  

if(qryFin.getDouble("FCP") > 0){
fin.save();


  
var fin = novaLinha("Financeiro");
  fin.setCampo("CODEMP",1);
  fin.setCampo("DTNEG", linha.getCampo("DTNEG"));
  fin.setCampo("DTVENC", linha.getCampo("DTNEG"));
  fin.setCampo("CODPARC", getParam("CODPARC"));
  fin.setCampo("CODNAT", getParam("CODNAT"));
  fin.setCampo("CODVEND", 0);
  fin.setCampo("CODTIPTIT", 4);
  fin.setCampo("VLRDESDOB", qryFin.getDouble("FCP"));
  fin.setCampo("RECDESP", -1);
  fin.setCampo("CODCTABCOINT", getParam("CODCTABCOINT"));
  fin.setCampo("CODCENCUS", 0);
  fin.setCampo("CODBCO", getParam("CODBCO"));
  fin.setCampo("ORIGEM", 'F');
  fin.setCampo("CODTIPOPER", 51);
  fin.setCampo("DTENTSAI", linha.getCampo("DTNEG"));
  fin.setCampo("NUMNOTA", linha.getCampo("NUMNOTA"));
  fin.setCampo("DESDOBRAMENTO", '1');
  fin.setCampo("HISTORICO", getParam("HISTORICO"));
  
fin.save();
}
}
}

mensagem = "Títulos inseridos com sucesso ";