package com.visual.treinamento.camel.jdbc;

import java.util.*;

import org.apache.camel.Message;
import org.apache.camel.builder.RouteBuilder;

public class JdbcRouteBuilder extends RouteBuilder {
	
	@Override
	public void configure() throws Exception {
		from("timer://teste?period=5000&delay=2000")
			.to("sql:select * from tabela")
			.choice()
				.when(body().method("size").isLessThan(2))
					.process()
					.exchange(ex -> {
						Message in = ex.getIn();
						Date date = (Date) in.getHeader("firedTime");
						StringBuilder insert = new StringBuilder("insert into tabela (coluna) values ('");
						insert.append(date.getTime()).append("')");
						in.setBody(insert.toString());
					})
					.to("jdbc:dataSource")
				.otherwise()
					.process()
					.exchange(ex -> {
						List<Map<String, Object>> resultSet = ex.getIn().getBody(List.class);
						resultSet.stream().forEach(r -> System.out.println(r.get("coluna")));
					});
					
	}
	
}
