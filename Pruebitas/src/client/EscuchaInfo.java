package client;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.ServerSocket;
import java.net.Socket;

public class EscuchaInfo implements Runnable
{

	public static int PUERTO_ESCUCHA = 9990;
	
	private ServerSocket serverSocket;

	private P2P principal;
	
	public EscuchaInfo(P2P principal) throws IOException
	{
		this.principal = principal;
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
				final ObjectInputStream ois = new ObjectInputStream(s.getInputStream());
				new Thread(new Runnable() 
				{
					public void run()
					{
						try 
						{
							while(true)
							{
								Data d = (Data)ois.readObject();
								System.out.println("Entrada de informacion => DATA " +d);
								if(d.getTarea().equals(P2P.TAREA_CONECTARSE)){
									principal.agregarConexionP2P(d.getIpOrigen(), d.getPuertoOrigen());
								}
							} 
						}
						catch (Exception e) 
						{
							e.printStackTrace();
						}
					}
				}).start();
			}
		}
		catch (Exception e) {
			e.printStackTrace();
		}
	}
}
