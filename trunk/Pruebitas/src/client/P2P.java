package client;

import java.io.IOException;
import java.net.UnknownHostException;
import java.util.Vector;

public class P2P {

	private Vector<ThreadConexionOtros> manejadorClientes;
	
	private EscuchaInfo escharInfo;
	
	private EscuchaBroadCast escucharBroadCast;
	
	public P2P() throws IOException
	{
		manejadorClientes = new Vector<ThreadConexionOtros>();
		
		escharInfo = new EscuchaInfo();
		new Thread(escharInfo).start();
		
		escucharBroadCast = new EscuchaBroadCast(escharInfo.PUERTO_ESCUCHA, this);		
		new Thread(escucharBroadCast).start();
	}
	
	public synchronized void agregarConexionP2P(String ipDestino, int puertoDestino) throws UnknownHostException, IOException
	{
		System.out.println("Agrego el Host: " + ipDestino + " puerto " + puertoDestino );
		ThreadConexionOtros t = new ThreadConexionOtros(ipDestino, puertoDestino, this);
		t.start();
		manejadorClientes.add(t);
	}

	public static void main(String[] args) 
	{
		try {
			new P2P();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
