import java.io.Serializable;


public class Data implements Serializable{
	
	private String tarea;
	
	private Paquete paquete;
	
	private String hostRespuesta;
	
	private int puertoRespuesta;
	
	private String informacionAdicional;
	
	public Data( ){	}
	
	public Data( String tarea, Paquete paquete, String hostRespuesta, int puertoRespuesta, String informacionAdicional )
	{
		this.tarea = tarea;
		this.paquete = paquete;
		this.hostRespuesta = hostRespuesta;
		this.puertoRespuesta = puertoRespuesta;
		this.informacionAdicional = informacionAdicional;
	}

	public String getTarea() {
		return tarea;
	}

	public void setTarea(String tarea) {
		this.tarea = tarea;
	}

	public Paquete getPaquete() {
		return paquete;
	}

	public void setPaquete(Paquete paquete) {
		this.paquete = paquete;
	}

	public String getHostRespuesta() {
		return hostRespuesta;
	}

	public void setHostRespuesta(String hostRespuesta) {
		this.hostRespuesta = hostRespuesta;
	}

	public String getInformacionAdicional() {
		return informacionAdicional;
	}

	public void setInformacionAdicional(String informacionAdicional) {
		this.informacionAdicional = informacionAdicional;
	}

	public int getPuertoRespuesta() {
		return puertoRespuesta;
	}

	public void setPuertoRespuesta(int puertoRespuesta) {
		this.puertoRespuesta = puertoRespuesta;
	}

	public String toString(){
		return "Puerto: "+puertoRespuesta +" Host "+hostRespuesta+" Tarea "+tarea+" Paquete "+paquete+" Informacion Adicional "+informacionAdicional;
	}
}
