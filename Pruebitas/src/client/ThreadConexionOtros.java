package client;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.UnknownHostException;

public class ThreadConexionOtros extends Thread {

	private String hostDestino;
	
	private int puertoDestino;
	
	private String hostOrigen;
	
	private int puertoOrigen;
	
	private Socket socket;
	
	private P2P principal;
	
	private ObjectOutputStream oos;
	
	public ThreadConexionOtros(String host, int puerto, P2P principal, String ipOrigen, int puertoOrigen) throws UnknownHostException, IOException
	{
		this.hostDestino = host;
		this.puertoDestino = puerto;
		this.puertoOrigen = puertoOrigen;
		this.hostOrigen = ipOrigen;
		this.principal = principal;
		this.socket =  new Socket(this.hostDestino , this.puertoDestino);
		oos = new ObjectOutputStream(this.socket.getOutputStream());
		
		enviarData(new Data(P2P.TAREA_CONECTARSE, this.hostOrigen, this.puertoOrigen));
	}
	
	public void run()
	{
		try {
			//Salida
			BufferedReader bf = new BufferedReader(new InputStreamReader(System.in));
			String linea=null;
			while((linea=bf.readLine())!=null){
				oos.writeObject(new Data(linea, hostOrigen, puertoOrigen));
				oos.flush();
			}
		}
		catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public void enviarData(Data data ) throws IOException{
		System.out.println("Envio Data "+ data);
		oos.writeObject(data);
		oos.flush();
	}

	public String getHostDestino() {
		return hostDestino;
	}

	public int getPuertoDestino() {
		return puertoDestino;
	}
	
}
