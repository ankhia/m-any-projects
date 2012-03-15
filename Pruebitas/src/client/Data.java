package client;

import java.io.Serializable;
import java.util.HashSet;

public class Data implements Serializable
{
	private String tarea;
	
	private String ipOrigen;
	
	private int puertoOrigen;
	
	private String nombreArchivo;
	
	private String codigoArchivo;
	
	private HashSet<String> hashArchivos;
	
	private int cantidadTotalFragmentos;
	
	private int offset;
	
	private byte[] datos;
	
	public Data(){
	}

	public Data(String tarea, String hostOrigen, int puertoOrigen2){
		this.tarea = tarea;
		this.ipOrigen = hostOrigen;
		this.puertoOrigen = puertoOrigen2;
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
	
	public String getNombreArchivo() {
		return nombreArchivo;
	}

	public void setNombreArchivo(String nombreArchivo) {
		this.nombreArchivo = nombreArchivo;
	}

	public int getFragmentos() {
		return cantidadTotalFragmentos;
	}

	public void setFragmentos(int fragmentos) {
		this.cantidadTotalFragmentos = fragmentos;
	}

	public int getOffset() {
		return offset;
	}

	public void setOffset(int offset) {
		this.offset = offset;
	}

	public byte[] getDatos() {
		return datos;
	}

	public void setDatos(byte[] datos) {
		this.datos = datos;
	}
	
	public String getCodigoArchivo() {
		return codigoArchivo;
	}

	public void setCodigoArchivo(String codigoArchivo) {
		this.codigoArchivo = codigoArchivo;
	}

	public int getCantidadTotalFragmentos() {
		return cantidadTotalFragmentos;
	}

	public void setCantidadTotalFragmentos(int cantidadTotalFragmentos) {
		this.cantidadTotalFragmentos = cantidadTotalFragmentos;
	}

	public String toString(){
		return "Tarea: " + tarea + " IPOrigen " + ipOrigen + " PuertoOrigen " + puertoOrigen;
	}

	/**
	 * @return the hashArchivos
	 */
	public HashSet<String> getHashArchivos() {
		return hashArchivos;
	}

	/**
	 * @param hashArchivos the hashArchivos to set
	 */
	public void setHashArchivos(HashSet<String> hashArchivos) {
		this.hashArchivos = hashArchivos;
	}
}
