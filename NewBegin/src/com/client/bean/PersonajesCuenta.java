package com.client.bean;

import java.io.Serializable;
import java.util.Date;

public class PersonajesCuenta implements Serializable {

	private String id_cuenta;
	
	private String cuenta;
	
	private String personaje;
	
	private String ultimaVez;
	
	private String email;

	public PersonajesCuenta(){
		
	}

	public PersonajesCuenta(String id_cuenta, String cuenta, String personaje,
			String ultimaVez, String email) {
		super();
		this.id_cuenta = id_cuenta;
		this.cuenta = cuenta;
		this.personaje = personaje;
		this.ultimaVez = ultimaVez;
		this.email = email;
	}



	public String getId_cuenta() {
		return id_cuenta;
	}

	public void setId_cuenta(String id_cuenta) {
		this.id_cuenta = id_cuenta;
	}

	public String getCuenta() {
		return cuenta;
	}

	public void setCuenta(String cuenta) {
		this.cuenta = cuenta;
	}

	public String getPersonaje() {
		return personaje;
	}

	public void setPersonaje(String personaje) {
		this.personaje = personaje;
	}

	public String getUltimaVez() {
		return ultimaVez;
	}

	public void setUltimaVez(String ultimaVez) {
		this.ultimaVez = ultimaVez;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}
	
	
	
	
	
}
