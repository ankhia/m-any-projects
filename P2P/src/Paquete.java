import java.io.Serializable;


public class Paquete implements Serializable {
	
	public static int MAX_TAM = 1024;
	
	String nombreArchivo;
	int fragmentos;
	int offset;
	byte[] datos;
	
	public Paquete( String nombreArchivo, int fragmentos, int offset, byte[] datos ) {
		this.nombreArchivo = nombreArchivo;
		this.fragmentos = fragmentos;
		this.offset = offset;
		this.datos = datos;
	}

	/**
	 * @return the nombreArchivo
	 */
	public String getNombreArchivo() {
		return nombreArchivo;
	}

	/**
	 * @param nombreArchivo the nombreArchivo to set
	 */
	public void setNombreArchivo(String nombreArchivo) {
		this.nombreArchivo = nombreArchivo;
	}

	/**
	 * @return the fragmentos
	 */
	public int getFragmentos() {
		return fragmentos;
	}

	/**
	 * @param fragmentos the fragmentos to set
	 */
	public void setFragmentos(int fragmentos) {
		this.fragmentos = fragmentos;
	}

	/**
	 * @return the offset
	 */
	public int getOffset() {
		return offset;
	}

	/**
	 * @param offset the offset to set
	 */
	public void setOffset(int offset) {
		this.offset = offset;
	}

	/**
	 * @return the datos
	 */
	public byte[] getDatos() {
		return datos;
	}

	/**
	 * @param datos the datos to set
	 */
	public void setDatos(byte[] datos) {
		this.datos = datos;
	}
	
	
}
