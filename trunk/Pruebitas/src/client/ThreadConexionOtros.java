package client;

import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;

public class ThreadConexionOtros extends Thread {

	private String host;
	
	private int puerto;
	
	private Socket socket;
	
	private P2P principal;
	
	public ThreadConexionOtros(String host, int puerto, P2P principal) throws UnknownHostException, IOException
	{
		this.host = host;
		this.puerto = puerto;
		this.principal = principal;
		this.socket =  new Socket(this.host , this.puerto);
	}
	
	public void run()
	{
		try {
			
			
				
			
		}
		catch (Exception e) {
			e.printStackTrace();
		}
	}
}
