for(var i = 0; i < linhas.length; i++){
    var linha = linhas[i];
 

    var ImpostosHelper = newJava("br.com.sankhya.modelcore.comercial.impostos.ImpostosHelpper");
ImpostosHelper.setForcarRecalculo(true);
        ImpostosHelper.calcularImpostos(linha.getCampo("NUNOTA"));


var query = getQuery();

 


}
mensagem = "Impostos Calculados com sucesso ";