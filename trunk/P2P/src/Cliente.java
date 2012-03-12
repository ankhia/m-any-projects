import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Set;


public class Cliente implements Runnable {

	String host = "localhost";
	int puerto;
	String tarea;
	Paquete paquete;
	Set<String> archivos;
	
	public Cliente( String host, int puerto, String tarea ) {
		this.host = host;
		this.puerto = puerto;
		this.tarea = tarea;
	}
	
	public Cliente( String host, int puerto, String tarea, Paquete paquete ) {
		this.host = host;
		this.puerto = puerto;
		this.tarea = tarea;
		this.paquete = paquete;
		new Thread(this).start();
	}
	
	public Cliente( String host, int puerto, String tarea, Set<String> archivos ) {
		this.host = host;
		this.puerto = puerto;
		this.tarea = tarea;
		this.archivos = archivos;
		new Thread(this).start();
	}

	@Override
	public void run() {
		try {
			Socket socket = new Socket( host, puerto );
			PrintWriter pw = new PrintWriter(socket.getOutputStream());
			pw.println(tarea);
			pw.flush();
			
			if( tarea.equals(P2P.INSERTAR_ARCHIVO)  ){
				ObjectOutputStream oos = new ObjectOutputStream(socket.getOutputStream());
				oos.writeObject(paquete);
				oos.flush();
			}
			else if( tarea.equals(P2P.CONSULTAR_LISTA_ARCHIVOS) ){
				pw = new PrintWriter(socket.getOutputStream());
				pw.println("localhost");
				pw.println(puerto);
				pw.flush();
			}
			else if( tarea.equals(P2P.RECIBIR_LISTA_ARCHIVOS) ){
				ObjectOutputStream oos = new ObjectOutputStream(socket.getOutputStream());
				oos.writeObject(archivos);
				oos.flush();
			}
			pw.close();
			socket.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
}
