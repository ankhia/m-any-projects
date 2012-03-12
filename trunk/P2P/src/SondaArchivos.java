

public class SondaArchivos
{
	private  int[] puertos = P2P.puertos;
	
	private String archivos;
	
	private boolean estaSondeando;
	
	private int peticionesPendientes;
	
	private int puerto;
	
	public SondaArchivos(int puerto) {
		this.puerto = puerto;
		archivos= "";
		limpiarSonda();
	}
	
	public void limpiarSonda(){
		estaSondeando = false;
		peticionesPendientes = 0;
	}
	
	public void consultarArchivosSistema(){
		limpiarSonda();
		estaSondeando = true;
		peticionesPendientes = puertos.length;
		for( Integer puerto : puertos ){
			new Cliente("localhost", this.puerto, "localhost", puerto, P2P.CONSULTAR_LISTA_ARCHIVOS );
		}
	}
	
	public void recibirPeticion( String archivos ){
		this.archivos += archivos+";1";
		peticionesPendientes--;
		if( peticionesPendientes == 0 )
			estaSondeando = false;
	}

	/**
	 * @return the estaSondeando
	 */
	public boolean isEstaSondeando() {
		return estaSondeando;
	}
	
	/**
	 * 
	 * @return archivos
	 */
	public String getArchivos(){
		return this.archivos;
	}

}
