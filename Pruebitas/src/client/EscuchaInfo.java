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
								}else if(d.getTarea().equals(P2P.ENVIAR_PARTE_ARCHIVO)){
									//Recibiendo parte de archivo
									principal.recibirPaqueteArchivo(d);
								}else if(d.getTarea().equals(P2P.ELIMINAR_ARCHIVO)){
									principal.eliminarArchivo( d.getNombreArchivo() );
								}else if(d.getTarea().equals(P2P.CONSULTAR_ARCHIVOS)){
									principal.consultarArchivos( d.getIpOrigen(), d.getPuertoOrigen() );
								}else if(d.getTarea().equals(P2P.RECIBIR_CONSULTA_ARCHIVOS)){
									principal.recibirConsultaArchivos( d.getHashArchivos() , d.getIpOrigen(), d.getPuertoOrigen());
								}else if(d.getTarea().equals(P2P.CONSULTAR_NODOS_ARCHIVO)){
									principal.consultarFragmentosPorNombreArchivo(d.getNombreArchivo(),d.getIpOrigen(), d.getPuertoOrigen());
								}else if(d.getTarea().equals(P2P.RESPUESTA_ARCHIVOS_NODOS)){
									principal.archivosPorNodo(d.getIpOrigen(), d.getNombreArchivo(), d.getPuertoOrigen());
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
