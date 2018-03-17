package com.visual.treinamento.camel.ws;

import org.apache.camel.Exchange;
import org.apache.camel.Message;
import org.apache.camel.builder.RouteBuilder;

public class RestServiceRouteBuilder extends RouteBuilder {
	
	private String fileRoute;
	
	public RestServiceRouteBuilder(String diretorio) {
		fileRoute = "file:" + diretorio;
	}
	
	@Override
	public void configure() throws Exception {
		from("timer://teste?delay=1000&repeatCount=1")
		.setHeader("CamelHttpMethod", constant("GET"))
			.to("http4://viacep.com.br/ws/30840530/json/")
			.process()
			.exchange(ex -> ex.getIn().setBody(ex.getIn().getBody(String.class)))
			.multicast()
				.parallelProcessing()
				.to("direct:arquivo", "direct:database");
		
		from("direct:arquivo")
			.log("Escrevendo o JSON em arquivo")
			.process()
			.exchange(ex -> {
				String json = ex.getIn().getBody(String.class);
				Message out = ex.getOut();
				out.setBody(json);
				out.setHeader(Exchange.FILE_NAME, "endereco.json");
			})
			.to(fileRoute);
		
		from("direct:database")
			.log("Inserindo o JSON no banco de dados")
			.process()
			.exchange(ex -> {
				Message in = ex.getIn();
				StringBuilder insert = new StringBuilder("insert into tabela (coluna) values ('");
				insert.append(in.getBody(String.class)).append("')");
				in.setBody(insert.toString());
			})
			.to("jdbc:dataSource");
	}

}
