package com.github.typesafe_query.dropwizard;

import javax.sql.DataSource;

import io.dropwizard.Configuration;
import io.dropwizard.ConfiguredBundle;
import io.dropwizard.db.DataSourceFactory;
import io.dropwizard.db.DatabaseConfiguration;
import io.dropwizard.setup.Bootstrap;
import io.dropwizard.setup.Environment;

public abstract class TypesafeQueryBundle<T extends Configuration> implements ConfiguredBundle<T>, DatabaseConfiguration<T> {

	@Override
	public void initialize(Bootstrap<?> arg0) {
		//Do nothing
	}

	@Override
	public void run(T configuration, Environment environment) throws Exception {
		final DataSourceFactory dbConfig = getDataSourceFactory(configuration);
		DataSource dataSource = dbConfig.build(environment.metrics(), "typesafe");
		environment.jersey().register(new TransactionalApplicationListener(dataSource));
	}
}
