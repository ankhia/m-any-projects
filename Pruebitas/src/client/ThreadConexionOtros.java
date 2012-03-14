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
		this.principal = principal;
		this.socket =  new Socket(this.hostDestino , this.puertoDestino);
		oos = new ObjectOutputStream(this.socket.getOutputStream());
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
}
