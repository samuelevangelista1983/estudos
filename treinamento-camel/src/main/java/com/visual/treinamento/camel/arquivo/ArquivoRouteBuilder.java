package com.visual.treinamento.camel.arquivo;

import org.apache.camel.builder.RouteBuilder;

public class ArquivoRouteBuilder extends RouteBuilder {
	
	private String rota;
	
	public ArquivoRouteBuilder(String diretorio) {
		rota = "file:" + diretorio + "?noop=true";
	}
	
	@Override
	public void configure() throws Exception {
		from(rota)
			.log("Lendo o arquivo ${file:name}")
			.choice()
				.when(header("CamelFileName").endsWith(".xml"))
					.log("${body}")
				.otherwise()
					// Transforma a mensagem original em várias mensagens, uma para cada linha do arquivo
					.split(bodyAs(String.class).tokenize("\n"))
						.streaming() // recomendado para arquivos grandes
						.log(body().toString());
			
	}
	
}
