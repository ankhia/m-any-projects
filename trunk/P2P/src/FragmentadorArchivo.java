import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;


public class FragmentadorArchivo {

	int[] puertos = P2P.puertos;
	int puerto;
	
	public FragmentadorArchivo(int puerto) {
		this.puerto = puerto;
	}

	private int darPuerto() {
		int port = Integer.MIN_VALUE;
		while( port == Integer.MIN_VALUE ){
			int i = (int) (Math.random() * puertos.length);
			if( puertos[i] != puerto )
				port = puertos[i];
		}
		return port;
	}
	
	public void procesarArchivo(File archivo) {
		try {
			FileInputStream fis = new FileInputStream(archivo);
			int fragmentos = fis.available() / Paquete.MAX_TAM + 1;
			int offset = 1;
			while( fis.available() > 0 ){
				int tamanio = fis.available() > Paquete.MAX_TAM ? Paquete.MAX_TAM : fis.available();
				byte[] datos = new byte[tamanio];
				for (int i=0; i<datos.length; i++) {
					datos[i] = (byte)fis.read();
				}
				Paquete paquete = new Paquete(archivo.getName(), fragmentos, offset++, datos);
				String host = "localhost";
				int puertoAEnviar = darPuerto();

				new Cliente(host, puertoAEnviar, P2P.INSERTAR_ARCHIVO, paquete);
			}
			fis.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}
	
}
