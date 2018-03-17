package com.visual.treinamento.camel.inicializacao;

import org.apache.camel.CamelContext;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.impl.DefaultCamelContext;

public class StartContext {

	public static void main(String[] args) throws Exception {
		CamelContext context = new DefaultCamelContext();
		context.addRoutes(new RouteBuilder() {
			@Override
			public void configure() throws Exception {
				from("file:data/inbox").to("file:data/outbox");
			}
		});
		context.start();
		System.out.println("Iniciou o contexto Camel...");
		context.stop();
		System.out.println("Parou o contexto Camel...");
	}

}
