package client;

import java.io.Serializable;

public class Data implements Serializable
{
	private String tarea;
	
	private String ipOrigen;
	
	private int puertoOrigen;
	
	
	public Data(){
	}

	public Data(String tarea){
		this.tarea = tarea;
	}
	
	public String getTarea(){
		return this.tarea;
	}
	
	public void setTarea(String tarea){
		this.tarea = tarea;
	}

	public String getIpOrigen() {
		return ipOrigen;
	}

	public void setIpOrigen(String ipOrigen) {
		this.ipOrigen = ipOrigen;
	}

	public int getPuertoOrigen() {
		return puertoOrigen;
	}

	public void setPuertoOrigen(int puertoOrigen) {
		this.puertoOrigen = puertoOrigen;
	}
	
	public String toString(){
		return "Tarea: " + tarea + " IPOrigen " + ipOrigen + " PuertoOrigen " + puertoOrigen;
	}
}
