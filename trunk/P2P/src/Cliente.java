import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.Iterator;
import java.util.Set;


public class Cliente implements Runnable {

	private String hostOrigen = "localhost";
	
	private int puertoOrigen;
	
	private String hostDestino;
	
	private int puertoDestino;
	
	private String tarea;
	
	private Paquete paquete;
	
	private Set<String> archivos;
	
	public Cliente( String hostOrigen, int puertoOrigen,String hostDestino, int puertoDestino, String tarea ) {
		this.hostOrigen = hostOrigen;
		this.puertoOrigen = puertoOrigen;
		this.hostDestino = hostDestino;
		this.puertoDestino = puertoDestino;
		this.tarea = tarea;
		new Thread(this).start();
	}
	
	public Cliente( String host, int puerto, String tarea, Paquete paquete ) {
		this.hostDestino = host;
		this.puertoDestino = puerto;
		this.tarea = tarea;
		this.paquete = paquete;
		new Thread(this).start();
	}
	
	public Cliente( String host, int puerto, String tarea, Set<String> archivos ) {
		this.hostDestino = host;
		this.puertoDestino = puerto;
		this.tarea = tarea;
		this.archivos = archivos;
		new Thread(this).start();
	}

	public void run() {
		try {
			Socket socket = new Socket( hostDestino, puertoDestino);
			Data d = new Data();
			d.setTarea(tarea);
			
			if( tarea.equals(P2P.INSERTAR_ARCHIVO)  ){
				d.setPaquete(paquete);
			}
			else if( tarea.equals(P2P.CONSULTAR_LISTA_ARCHIVOS) ){
				d.setHostRespuesta(hostOrigen);
				d.setPuertoRespuesta(puertoOrigen);
			}
			else if( tarea.equals(P2P.RECIBIR_LISTA_ARCHIVOS) ){
				StringBuilder sb = new StringBuilder();
				for(Iterator it = archivos.iterator(); it.hasNext();){
					String s = (String) it.next();
					sb.append(s);
					sb.append(";");
				}
				d.setInformacionAdicional(sb.toString());
			}
			System.out.println(puertoDestino +" Envio " + d);
			ObjectOutputStream oos = new ObjectOutputStream(socket.getOutputStream());
			oos.writeObject(d);
			oos.flush();
			
			socket.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
