import java.util.HashSet;
import java.util.Set;


public class SondaArchivos {
	int[] puertos = P2P.puertos;
	
	
	Set<String> listaArchivos;
	boolean estaSondeando;
	int peticionesPendientes;
	
	public SondaArchivos() {
		limpiarSonda();
	}
	
	public void limpiarSonda(){
		listaArchivos = new HashSet<String>();
		estaSondeando = false;
		peticionesPendientes = 0;
	}
	
	public void consultarArchivosSistema(){
		limpiarSonda();
		estaSondeando = true;
		peticionesPendientes = puertos.length;
		for( Integer puerto : puertos ){
			new Cliente("localhost", puerto, P2P.CONSULTAR_LISTA_ARCHIVOS );
		}
	}
	
	public void recibirPeticion( Set<String> archivos ){
		listaArchivos.addAll(archivos);
		peticionesPendientes--;
		if( peticionesPendientes == 0 ){
			estaSondeando = false;
		}
	}

	/**
	 * @return the listaArchivos
	 */
	public Set<String> getListaArchivos() {
		return listaArchivos;
	}

	/**
	 * @param listaArchivos the listaArchivos to set
	 */
	public void setListaArchivos(Set<String> listaArchivos) {
		this.listaArchivos = listaArchivos;
	}

	/**
	 * @return the estaSondeando
	 */
	public boolean isEstaSondeando() {
		return estaSondeando;
	}

	/**
	 * @param estaSondeando the estaSondeando to set
	 */
	public void setEstaSondeando(boolean estaSondeando) {
		this.estaSondeando = estaSondeando;
	}

	/**
	 * @return the peticionesPendientes
	 */
	public int getPeticionesPendientes() {
		return peticionesPendientes;
	}

	/**
	 * @param peticionesPendientes the peticionesPendientes to set
	 */
	public void setPeticionesPendientes(int peticionesPendientes) {
		this.peticionesPendientes = peticionesPendientes;
	}
}
