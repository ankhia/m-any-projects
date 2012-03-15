package client;

import java.io.IOException;
import java.net.UnknownHostException;
import java.util.Vector;

public class P2P 
{
	public static final String TAREA_CONECTARSE="CONECTARSE";

	private Vector<ThreadConexionOtros> manejadorClientes;
	
	private EscuchaInfo escharInfo;
	
	private EscuchaBroadCast escucharBroadCast;
	
	public P2P() throws IOException
	{
		manejadorClientes = new Vector<ThreadConexionOtros>();
		
		escharInfo = new EscuchaInfo(this);
		new Thread(escharInfo).start();
		
		escucharBroadCast = new EscuchaBroadCast(escharInfo.PUERTO_ESCUCHA, this);		
		new Thread(escucharBroadCast).start();
	}
	
	public synchronized void agregarConexionP2P(String ipDestino, int puertoDestino) throws UnknownHostException, IOException
	{
		System.out.println("Agrego el Host: " + ipDestino + " puerto " + puertoDestino );
		boolean existe = false;
		for(int i=0;i<manejadorClientes.size() && !existe;++i){
			ThreadConexionOtros  t = manejadorClientes.get(i);
			if(t.getPuertoDestino() == puertoDestino && ipDestino.equals(t.getHostDestino()))
				existe=true;
		}
		if(!existe){
			ThreadConexionOtros t = new ThreadConexionOtros(ipDestino, puertoDestino, this, escucharBroadCast.getIpOrigen(), escucharBroadCast.getPuertoOrigen());
			t.start();
			manejadorClientes.add(t);
		}else {
			System.out.println("Ya tengo una conexion con "+ ipDestino + " por el puerto "+ puertoDestino);
		}
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
