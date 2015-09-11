package com.github.typesafe_query.dropwizard;


import java.lang.reflect.Method;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

import javax.sql.DataSource;

import org.glassfish.jersey.server.internal.process.MappableException;
import org.glassfish.jersey.server.model.Resource;
import org.glassfish.jersey.server.model.ResourceMethod;
import org.glassfish.jersey.server.monitoring.ApplicationEvent;
import org.glassfish.jersey.server.monitoring.ApplicationEventListener;
import org.glassfish.jersey.server.monitoring.RequestEvent;
import org.glassfish.jersey.server.monitoring.RequestEventListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.github.typesafe_query.ConnectionHolder;


public class TransactionalApplicationListener implements ApplicationEventListener{

	private static Logger logger = LoggerFactory.getLogger(TransactionalApplicationListener.class);
	
	private DataSource dataSource;
	
	public TransactionalApplicationListener(DataSource dataSource) {
		this.dataSource = dataSource;
	}
	
	private static class TransactionalEventListener implements RequestEventListener{

		private final Map<Method,Transactional> methodMap;
		private final DataSource dataSource;
		private Connection con;
		private Transactional transactional;
		
		public TransactionalEventListener(Map<Method,Transactional> methodMap,DataSource dataSource) {
			this.methodMap = methodMap;
			this.dataSource = dataSource;
		}
		
		@Override
		public void onEvent(RequestEvent event) {
			if (event.getType() == RequestEvent.Type.RESOURCE_METHOD_START) {
				transactional = this.methodMap.get(event.getUriInfo()
						.getMatchedResourceMethod().getInvocable().getDefinitionMethod());
				if (transactional != null) {
					try {
						con = dataSource.getConnection();
						logger.debug("Create connection [{}]{}",con,this);
						logger.debug("Product[{}]",con.getMetaData().getDatabaseProductName());
						logger.debug("Version[{}]",con.getMetaData().getDatabaseMajorVersion());
						logger.debug("AutoCommit[{}]",con.getAutoCommit());
						
						configureSession();
						beginTransaction();
						ConnectionHolder.getInstance().set(con);
					} catch (Throwable th) {
						throw new MappableException(th);
					}
				}
			}else if (event.getType() == RequestEvent.Type.RESP_FILTERS_START) {
				try {
					if (this.con != null && !con.isClosed()) {
						try {
							commitTransaction();
						} catch (Exception e) {
							rollbackTransaction();
							throw new MappableException(e);
						} finally {
							ConnectionHolder.getInstance().set(null);
							con.close();
						}
					}
				} catch (SQLException e) {
					throw new MappableException(e);
				}
			}else if (event.getType() == RequestEvent.Type.ON_EXCEPTION) {
				try {
					if (this.con != null && !con.isClosed()) {
						try {
							rollbackTransaction();
						} finally {
							ConnectionHolder.getInstance().set(null);
							con.close();
						}
					}
				} catch (SQLException e) {
					throw new MappableException(e);
				}
			}
		}
		
		private void beginTransaction() throws SQLException {
			if (transactional.readOnly()) {
				logger.debug("This is read only Connection [{}]{}",con,this);
				//エラーになる
				//con.setReadOnly(true);
			}
		}

		private void configureSession() {
			//Do nothing.
		}

		private void rollbackTransaction() throws SQLException {
			if (!transactional.readOnly()) {
				if(!con.isClosed()){
					logger.debug("Rollback Transaction [{}]{}",con,this);
					con.rollback();
				}
			}
		}

		private void commitTransaction() throws SQLException {
			if (!transactional.readOnly()) {
				logger.debug("Commit Transaction [{}]{}",con,this);
				con.commit();
			}
		}
		
	}
	
	private Map<Method,Transactional> methodMap = new HashMap<>();

	@Override
	public void onEvent(ApplicationEvent event) {
		if (event.getType() == ApplicationEvent.Type.INITIALIZATION_APP_FINISHED) {
			for (Resource resource : event.getResourceModel().getResources()) {
				for (ResourceMethod method : resource.getAllMethods()) {
					registerTransactionalAnnotations (method);
				}

				for (Resource childResource : resource.getChildResources()) {
					for (ResourceMethod method : childResource.getAllMethods()) {
						registerTransactionalAnnotations (method);
					}
				}
			}
		}
	}

	@Override
	public RequestEventListener onRequest(RequestEvent arg0) {
		RequestEventListener listener = new TransactionalEventListener(methodMap,dataSource);
		return listener;
	}
	
	private void registerTransactionalAnnotations (ResourceMethod method) {
		Transactional annotation = method.getInvocable().getDefinitionMethod().getAnnotation(Transactional.class);

		if (annotation == null) {
			annotation = method.getInvocable().getHandlingMethod().getAnnotation(Transactional.class);
		}

		if (annotation != null) {
			this.methodMap.put(method.getInvocable().getDefinitionMethod(), annotation);
		}

	}
}
