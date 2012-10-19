package com.client.bean;

import java.io.Serializable;

public class Usuario  implements Serializable{
	
	private int id;

	private String username;
	
	private String email;
	
	private int failLogin;
	
	private String password;
	
	private boolean isAdmin;
	
	public Usuario(){
		
	}

	public Usuario(int id, String username, String email, int failLogin,
			String password, boolean isAdmin) {
		super();
		this.id = id;
		this.username = username;
		this.email = email;
		this.failLogin = failLogin;
		this.password = password;
		this.isAdmin = isAdmin;
	}
	
	public boolean isAdmin() {
		return isAdmin;
	}

	public void setAdmin(boolean isAdmin) {
		this.isAdmin = isAdmin;
	}

	public int getId() {
		return id;
	}


	public void setId(int id) {
		this.id = id;
	}


	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public int getFailLogin() {
		return failLogin;
	}

	public void setFailLogin(int failLogin) {
		this.failLogin = failLogin;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}
	
	public String toString(){
		return "Username: " + username + " Password : " + password +" Email; "+ email;
	}
	
}
