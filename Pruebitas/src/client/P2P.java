package client;

import java.io.IOException;

public class P2P {

	
	private EscuchaInfo escharInfo;
	
	private EscuchaBroadCast escucharBroadCast;
	
	public P2P() throws IOException
	{
		escharInfo = new EscuchaInfo();
		new Thread(escharInfo).start();
		
		escucharBroadCast = new EscuchaBroadCast(escharInfo.PUERTO_ESCUCHA, this);		
		new Thread(escucharBroadCast).start();
	}
	
	
	public void agregarConexionP2P(String ipDestino, int puertoDestino) {
		// TODO Auto-generated method stub
		
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
