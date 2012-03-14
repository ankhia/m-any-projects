package client;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.UnknownHostException;

public class ThreadConexionOtros extends Thread {

	private String host;
	
	private int puerto;
	
	private Socket socket;
	
	private P2P principal;
	
	private ObjectOutputStream oos;
	
	public ThreadConexionOtros(String host, int puerto, P2P principal) throws UnknownHostException, IOException
	{
		this.host = host;
		this.puerto = puerto;
		this.principal = principal;
		this.socket =  new Socket(this.host , this.puerto);
		oos = new ObjectOutputStream(this.socket.getOutputStream());
	}
	
	public void run()
	{
		try {
			//Salida
			BufferedReader bf = new BufferedReader(new InputStreamReader(System.in));
			while(true){
				String linea=null;
				while((linea=bf.readLine())!=null){
					oos.writeObject(bf.readLine());
					oos.flush();
				}
			}
		}
		catch (Exception e) {
			e.printStackTrace();
		}
	}
}
