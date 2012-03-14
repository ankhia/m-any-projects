package client;

import java.net.ServerSocket;
import java.net.Socket;
import java.util.Vector;

public class EscuchaInfo implements Runnable
{

	public static int PUERTO_ESCUCHA = 9990;
	
	private Vector<ManejadorCliente> manejardorClientes;
	
	public EscuchaInfo()
	{
		manejardorClientes = new Vector<ManejadorCliente>();
	}
	
	public void run() 
	{
		try 
		{
			ServerSocket serverSocket = new ServerSocket(PUERTO_ESCUCHA);
			while(true)
			{
					Socket socket = serverSocket.accept();
					manejardorClientes.add(new ManejadorCliente(socket));
			}
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		
	}
}
