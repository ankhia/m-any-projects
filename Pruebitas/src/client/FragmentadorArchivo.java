package client;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;


public class FragmentadorArchivo {

	public static int MAXIMO_TAMANIO_BYTES = 65;
	
	private P2P principal;
	
	public FragmentadorArchivo(P2P principal) {this.principal = principal;}

	public void procesarArchivo(File archivo) 
	{
		try 
		{
			BufferedReader bf = new BufferedReader(new InputStreamReader(new FileInputStream(archivo)));
			byte[] datos = new byte[MAXIMO_TAMANIO_BYTES];
			int tama = 0;
			int offset = 1;
			ArrayList<Data>  fragmentacionArchivo = new ArrayList<Data>();
			byte b = (byte) bf.read();
			while(b!=-1){
				if(tama < MAXIMO_TAMANIO_BYTES){
					datos[tama]= b;
					tama++;
				}else{
					Data data = new Data();
					data.setDatos(datos);
					data.setOffset(offset);
					data.setNombreArchivo(archivo.getName());
					data.setCodigoArchivo(archivo.getName()+"_"+offset);
					data.setTarea(P2P.EVIAR_PARTE_ARCHIVO);
					data.setIpOrigen(principal.getHost());
					data.setPuertoOrigen(principal.getPuerto());
					fragmentacionArchivo.add(data);
					tama = 0;
					datos = new byte[MAXIMO_TAMANIO_BYTES];
					offset++;
				}
				b = (byte) bf.read();
			}
			
			for(int i=0;i<fragmentacionArchivo.size();++i){
				Data data = fragmentacionArchivo.get(i);
				data.setFragmentos(fragmentacionArchivo.size()-1);
				principal.enviarPaqueteAHostAlAzar(data);
			}
			
			bf.close();
//			
//			int fragmentos = fis.available() /MAXIMO_TAMANIO_BYTES+ 1;
//			int offset = 1;
//			while( fis.available() > 0 ){
//				int tamanio = fis.available() > MAXIMO_TAMANIO_BYTES ? MAXIMO_TAMANIO_BYTES: fis.available();
//				byte[] datos = new byte[tamanio];
//				for (int i=0; i<datos.length; i++) {
//					datos[i] = (byte)fis.read();
//				}
//				
//				//Paquete paquete = new Paquete(archivo.getName(), fragmentos, offset++, datos);
//				String host = "localhost";
//				int puertoAEnviar = darPuerto();
//
//				//new Cliente(host, puertoAEnviar, P2P.INSERTAR_ARCHIVO, paquete);
//			}
//			fis.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
