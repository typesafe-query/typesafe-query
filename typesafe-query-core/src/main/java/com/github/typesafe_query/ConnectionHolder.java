package com.github.typesafe_query;

import java.io.Closeable;
import java.sql.Connection;

/**
 * FIXME v0.x.x これもっといい方法ないものか
 * Play!Frameworkを参考にした。あれはThreadLocalで管理してたはず。1系だけど。
 * @author Takahiko Sato(MOSA architect Inc.)
 *
 */
public final class ConnectionHolder implements Closeable{
	
	private static final ConnectionHolder INSTANCE = new ConnectionHolder();
	
	private static ThreadLocal<Connection> HOLDER;
	
	private ConnectionHolder(){
		HOLDER = new ThreadLocal<Connection>();
	}
	
	public static ConnectionHolder getInstance(){
		return INSTANCE;
	}
	
	public void set(Connection con){
		HOLDER.set(con);
	}
	
	public Connection get(){
		return HOLDER.get();
	}
	
	@Override
	public void close(){
		HOLDER = null;
	}
}
