package com.server;

import java.sql.Connection;
import java.sql.DriverManager;

public class ConnectionDataBase {

	private String puerto;
	
	private String host;
	
	private String usuario;
	
	private String password;
	
	public ConnectionDataBase( String puerto , String host, String usuario , String password){
		this.puerto =  puerto ;
		this.host = host;
		this.usuario = usuario;
		this.password = password;
	}
	
	public Connection getConnection(String database)throws Exception {
		Class.forName("com.mysql.jdbc.Driver").newInstance();
		return DriverManager.getConnection("jdbc:mysql://"+host+":"+puerto+"/"+database, usuario, password); 
	}
	
	
	
}
