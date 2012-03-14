package client;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.net.ServerSocket;
import java.net.Socket;

public class EscuchaInfo implements Runnable
{

	public static int PUERTO_ESCUCHA = 9990;
	
	private ServerSocket serverSocket;
	
	public EscuchaInfo() throws IOException
	{
		serverSocket = new ServerSocket(PUERTO_ESCUCHA);
	}
	
	//Parte que se queda escuchando a los otros 
	public void run() 
	{
		try 
		{
			while(true)
			{
				Socket s = serverSocket.accept();
				ObjectInputStream ois = new ObjectInputStream(s.getInputStream());
				while(true){
					if(ois.readObject()!=null)
						System.out.println((String)ois.readObject());
				}
			}
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		
	}
}
