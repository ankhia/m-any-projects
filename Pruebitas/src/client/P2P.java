package client;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.UnknownHostException;
import java.util.Random;
import java.util.Vector;

import javax.swing.JFileChooser;
import javax.swing.JFrame;

public class P2P 
{
	public static final String TAREA_CONECTARSE="CONECTARSE";
	
	public static final String EVIAR_PARTE_ARCHIVO="ENVIAR PARTE ARCHIVO";

	private Vector<ThreadConexionOtros> manejadorClientes;
	
	private EscuchaInfo escharInfo;
	
	private EscuchaBroadCast escucharBroadCast;
	
	private FragmentadorArchivo fragmentadorArchivos;
	
	public P2P() throws IOException
	{
		fragmentadorArchivos = new FragmentadorArchivo(this);
		
		manejadorClientes = new Vector<ThreadConexionOtros>();
		
		escharInfo = new EscuchaInfo(this);
		new Thread(escharInfo).start();
		
		escucharBroadCast = new EscuchaBroadCast(escharInfo.PUERTO_ESCUCHA, this);		
		new Thread(escucharBroadCast).start();
		
		enviarPaqueteAHostAlAzar(null);
		menu();
		
	}
	
	private void menu() throws NumberFormatException, IOException
	{
		BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
		int op = 0;
		do{
			System.out.println("1. Insertar Archivo");
			System.out.println("2. Consultar Lista de Archivos");
			System.out.println("3. Eliminar Archivo");
			System.out.println("4. Consultar contenido Archivo");
			System.out.println("5. Consultar Lista de Nodos que tienen Fragmentos de Archivo");

			switch( op ){
				case 1: insertarArchivo();break;
				case 2: break;
				case 3: break;
				case 4: break;
				case 5: break;
			}
			op = Integer.parseInt(in.readLine());
		}while(op!=-1);
	}

	public synchronized void agregarConexionP2P(String ipDestino, int puertoDestino) throws UnknownHostException, IOException
	{
		boolean existe = false;
		for(int i=0;i<manejadorClientes.size() && !existe;++i){
			ThreadConexionOtros  t = manejadorClientes.get(i);
			if(t.getPuertoDestino() == puertoDestino && ipDestino.equals(t.getHostDestino()))
				existe=true;
		}
		if(!existe){
			System.out.println("Agrego el Host: " + ipDestino + " puerto " + puertoDestino );
			ThreadConexionOtros t = new ThreadConexionOtros(ipDestino, puertoDestino, this, escucharBroadCast.getIpOrigen(), escucharBroadCast.getPuertoOrigen());
			t.start();
			manejadorClientes.add(t);
		}else 
			System.out.println("Ya tengo una conexion con "+ ipDestino + " por el puerto "+ puertoDestino);
	}

	private void insertarArchivo(){
		JFileChooser chooser = new JFileChooser();
		chooser.setDialogTitle("choose");
		chooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
		
		JFrame jf = new JFrame();
		jf.setVisible(true);
		
		int returnVal = chooser.showOpenDialog(jf);
		if(returnVal== JFileChooser.APPROVE_OPTION){
			fragmentadorArchivos.procesarArchivo(chooser.getSelectedFile());
		}
		jf.dispose();
	}
	
	public void enviarPaqueteAHostAlAzar(Data data) throws IOException{
		int p = (int)(Math.random()*manejadorClientes.size());
		System.out.println("El elegido es "+ p + " cantadad manejadores "+ manejadorClientes.size());
		if(p < manejadorClientes.size()&& p>=0){
			manejadorClientes.get(p).enviarData(data);
		}else
			System.out.println("No hay personas conectadas a las que se le pueda enviar fragmentos del archivo");
		
	}
	
	public String getHost(){
		return escucharBroadCast.getIpOrigen();
	}
	
	public int getPuerto(){
		return escucharBroadCast.getPuertoOrigen();
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