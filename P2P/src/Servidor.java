import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.Iterator;


public class Servidor extends Thread
{
	private P2P principal;
	
	private int puerto;
	
	private ServerSocket serverSocket;
	
	static Object monitor = new Object();
	
	private Hashtable<String, Paquete> paquetes;
	
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
					Data data = (Data) new ObjectInputStream(socket.getInputStream()).readObject();
					System.out.println(puerto+" Recibo " + data);
					String tarea = data.getTarea();
					
					if( tarea.equals(P2P.INSERTAR_ARCHIVO) ){
						Paquete p = data.getPaquete();
						paquetes.put( generarNombrePaquete(p), p);
					}
					if( tarea.equals(P2P.CONSULTAR_LISTA_ARCHIVOS) ){
						String hostRespuesta = data.getHostRespuesta();
						int puertoRespuesta = data.getPuertoRespuesta();
						consultarListaArchivos( hostRespuesta, puertoRespuesta );
					}
					else if( tarea.equals(P2P.RECIBIR_LISTA_ARCHIVOS) ){
						String archivos = data.getInformacionAdicional();
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
		for (Iterator iterator = paquetes.keySet().iterator(); iterator.hasNext();) {
			archivos.add(paquetes.get(iterator.next()).getNombreArchivo());
		}
		new Cliente(host, puerto, P2P.RECIBIR_LISTA_ARCHIVOS, archivos);
	}
	
}
