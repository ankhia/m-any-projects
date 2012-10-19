package com.client.bean;

import java.io.Serializable;

public class Characters implements Serializable{
	private int id;
	
	private String nombre;
	
	private String raza;
	
	private String genero;
	
	private int nivel;
	
	private long plata;
	
	public Characters(){
		
	}

	
	public Characters(int id, String nombre, String raza, String genero,
			int nivel, long plata) {
		super();
		this.id = id;
		this.nombre = nombre;
		this.raza = raza;
		this.genero = genero;
		this.nivel = nivel;
		this.plata = plata;
	}




	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public String getRaza() {
		return raza;
	}

	public void setRaza(String raza) {
		this.raza = raza;
	}

	public String getGenero() {
		return genero;
	}

	public void setGenero(String genero) {
		this.genero = genero;
	}

	public int getNivel() {
		return nivel;
	}

	public void setNivel(int nivel) {
		this.nivel = nivel;
	}

	public long getPlata() {
		return plata;
	}

	public void setPlata(long plata) {
		this.plata = plata;
	}
	
	
}
