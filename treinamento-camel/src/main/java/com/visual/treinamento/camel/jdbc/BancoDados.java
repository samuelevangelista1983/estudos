package com.visual.treinamento.camel.jdbc;

import java.util.ResourceBundle;

import javax.sql.DataSource;

import org.apache.camel.CamelContext;
import org.apache.camel.impl.DefaultCamelContext;
import org.apache.camel.impl.SimpleRegistry;
import org.apache.camel.main.Main;
import org.apache.commons.dbcp2.BasicDataSource;

public class BancoDados {
	
	private static DataSource getDataSource(ResourceBundle bundle) {
		BasicDataSource dataSource = new BasicDataSource();
		dataSource.setDriverClassName(bundle.getString("database.driver.class.name"));
		dataSource.setUsername(bundle.getString("database.username"));
		dataSource.setPassword(bundle.getString("database.password"));
		dataSource.setUrl(bundle.getString("database.connection.url"));
		return dataSource;
	}
	
	public static void main(String[] args) throws Exception {
		ResourceBundle bundle = ResourceBundle.getBundle("application");
		DataSource dataSource = getDataSource(bundle);
		SimpleRegistry registry = new SimpleRegistry();
		registry.put("dataSource", dataSource);
		CamelContext context = new DefaultCamelContext(registry);
		context.addRoutes(new JdbcRouteBuilder());
		Main main = new Main();
		main.getCamelContexts().add(context);
		main.run();
	}

}
