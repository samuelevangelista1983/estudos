package com.visual.treinamento.camel.arquivo;

import java.util.ResourceBundle;

import org.apache.camel.CamelContext;
import org.apache.camel.impl.DefaultCamelContext;
import org.apache.camel.main.Main;

public class Arquivo {

	public static void main(String[] args) throws Exception {
		ResourceBundle bundle = ResourceBundle.getBundle("application");
		CamelContext context = new DefaultCamelContext();
		context.addRoutes(new ArquivoRouteBuilder(bundle.getString("diretorio.monitorado")));
		Main main = new Main();
		main.getCamelContexts().add(context);
		main.run();
	}

}
