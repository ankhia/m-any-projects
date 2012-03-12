import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;
import java.util.Set;


public class Servidor extends Thread{
	
	P2P principal;
	int puerto;
	ServerSocket serverSocket;
	static Object monitor = new Object();
	
	Hashtable<String, Paquete> paquetes;
	
	public Servidor( P2P principal, int puerto ) {
		this.principal = principal;
		this.puerto = puerto;
		paquetes = new Hashtable<String, Paquete>();
		this.start();
	}
	
	public int getPuerto(){
		return puerto;
	}
	
	@Override
	public void run() {
		try {
			serverSocket = new ServerSocket(puerto);
			while(true){
				synchronized (monitor) {
					Socket socket = serverSocket.accept();
					BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
					String tarea = in.readLine() ;
					
					if( tarea.equals(P2P.INSERTAR_ARCHIVO) ){
						Paquete paquete = (Paquete) new ObjectInputStream(socket.getInputStream()).readObject();
						paquetes.put( generarNombrePaquete(paquete), paquete);
					}
					if( tarea.equals(P2P.CONSULTAR_LISTA_ARCHIVOS) ){
						String hostRespuesta = in.readLine();
						int puertoRespuesta = Integer.parseInt(in.readLine());
						consultarListaArchivos( hostRespuesta, puertoRespuesta );
					}
					else if( tarea.equals(P2P.RECIBIR_LISTA_ARCHIVOS) ){
						Set<String> archivos = (Set<String>) new ObjectInputStream(socket.getInputStream()).readObject();
						principal.recibirListaArchivo(archivos);
					}
					monitor.wait(100);
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
	
	private String generarNombrePaquete(Paquete paquete){
		return paquete.getNombreArchivo() + "_" + paquete.getOffset();
	}
	
	private void consultarListaArchivos( String host, int puerto ){
		HashSet<String> archivos = new HashSet<String>();
		for (Iterator iterator = ((Set<String>) paquetes).iterator(); iterator.hasNext();) {
			archivos.add(paquetes.get(iterator.next()).getNombreArchivo());
		}
		new Cliente(host, puerto, P2P.RECIBIR_LISTA_ARCHIVOS, archivos);
	}
	
}
