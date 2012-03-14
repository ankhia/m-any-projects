package client;

import java.net.ServerSocket;
import java.net.Socket;

public class EscuchaInfo implements Runnable
{

	public static int PUERTO_ESCUCHA = 9990;
	
	public EscuchaInfo()
	{
	}
	
	//Parte que se queda escuchando a los otros 
	public void run() 
	{
		try 
		{
			ServerSocket serverSocket = new ServerSocket(PUERTO_ESCUCHA);
			while(true)
			{
					Socket socket = serverSocket.accept();
					
			}
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		
	}
}
