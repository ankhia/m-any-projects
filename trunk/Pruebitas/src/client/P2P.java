package client;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.Set;
import java.util.Vector;

import javax.swing.JFileChooser;
import javax.swing.JFrame;

public class P2P 
{
	public static final String TAREA_CONECTARSE="CONECTARSE";
	
	public static final String ENVIAR_PARTE_ARCHIVO="ENVIAR PARTE ARCHIVO";
	
	public static final String ELIMINAR_ARCHIVO="ELIMINAR ARCHIVO";
	
	public static final String CONSULTAR_ARCHIVOS = "CONSULTAR ARCHIVOS";
	
	public static final String RECIBIR_CONSULTA_ARCHIVOS = "RECIBIR CONSULTA ARCHIVOS";

	private Vector<ThreadConexionOtros> manejadorClientes;
	
	private EscuchaInfo escharInfo;
	
	private EscuchaBroadCast escucharBroadCast;
	
	private FragmentadorArchivo fragmentadorArchivos;
	
	private Hashtable<String, ArrayList<Data>> paquetesArchivos;

	private HashSet<String> nombresArchivos;
	
	private int contarCantSolic;
	
	public P2P() throws IOException
	{
		nombresArchivos = new HashSet<String>();
		
		paquetesArchivos = new Hashtable<String, ArrayList<Data>>();
		
		fragmentadorArchivos = new FragmentadorArchivo(this);
		
		manejadorClientes = new Vector<ThreadConexionOtros>();
		
		escharInfo = new EscuchaInfo(this);
		new Thread(escharInfo).start();
		
		escucharBroadCast = new EscuchaBroadCast(escharInfo.PUERTO_ESCUCHA, this);		
		new Thread(escucharBroadCast).start();
		
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
				case 2: enviarOrdenConsultar(); break;
				case 3: 
					System.out.print("Nombre Archivo a eliminar: ");enviarOrdenEliminarArchivo(in.readLine());break;
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

	private void insertarArchivo()
	{
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
	
	private void enviarOrdenEliminarArchivo( String nombreArchivoEliminar ) throws IOException{
		
		for (int i = 0; i < manejadorClientes.size(); i++) {
			ThreadConexionOtros tc = (ThreadConexionOtros) manejadorClientes.get(i);
			Data dataEliminarArchivo = new Data( ); 
			dataEliminarArchivo.setTarea(P2P.ELIMINAR_ARCHIVO);
			dataEliminarArchivo.setIpOrigen(getHost());
			dataEliminarArchivo.setPuertoOrigen(getPuerto());
			dataEliminarArchivo.setNombreArchivo(nombreArchivoEliminar);
			tc.enviarData(dataEliminarArchivo);
		}
		eliminarArchivo(nombreArchivoEliminar);
	}
	
	private void enviarOrdenConsultar(  ) throws IOException{
		for (int i = 0; i < manejadorClientes.size(); i++) {
			Data dataConsultar = new Data();
			dataConsultar.setTarea(CONSULTAR_ARCHIVOS);
			dataConsultar.setIpOrigen(getHost());
			dataConsultar.setPuertoOrigen(getPuerto());
			ThreadConexionOtros tc = (ThreadConexionOtros) manejadorClientes.get(i);
			tc.enviarData(dataConsultar);
		}
	}
	
	public synchronized void enviarPaqueteAHostAlAzar(Data data) throws IOException
	{
		if(paquetesArchivos.get(data.getNombreArchivo())==null)
			paquetesArchivos.put(data.getNombreArchivo(), new ArrayList<Data>());
		paquetesArchivos.get(data.getNombreArchivo()).add(data);
		
		int p = (int)(Math.random()*manejadorClientes.size());
		System.out.println("El elegido es "+ p + " cantadad manejadores "+ manejadorClientes.size());
		if(p < manejadorClientes.size()&& p>=0){
			manejadorClientes.get(p).enviarData(data);
		}else
			System.out.println("No hay personas conectadas a las que se le pueda enviar fragmentos del archivo");
	}
	
	public synchronized void recibirPaqueteArchivo(Data d) {
		if(paquetesArchivos.get(d.getNombreArchivo())==null)
			paquetesArchivos.put(d.getNombreArchivo(), new ArrayList<Data>());
		ArrayList<Data> fragmentosArchivo =	paquetesArchivos.get(d.getNombreArchivo());
		boolean tengoParteArchivo = false;
		for(int i=0;i<fragmentosArchivo.size()&& !tengoParteArchivo;++i){
			Data data = fragmentosArchivo.get(i);
			if(data.getCodigoArchivo().equals(d.getCodigoArchivo()))
				tengoParteArchivo = true;
		}
		if(!tengoParteArchivo){
			fragmentosArchivo.add(d);
			System.out.println("Tamanio de Fragmentos del Archivo : "+ d.getNombreArchivo() + " size "+ fragmentosArchivo.size() + " hashtable "+paquetesArchivos.get(d.getNombreArchivo()).size()+" deberian ser iguales");
		}else  System.out.println("Parte del Archivo ya lo tengo.");
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

	public void eliminarArchivo(String nombreArchivo) {
		if(paquetesArchivos.get(nombreArchivo)!=null){
			paquetesArchivos.remove(nombreArchivo);
			System.out.println("El archivo "+nombreArchivo+" fue removido");
		}else{
			System.out.println("No existe el archivo");
		}
	}

	public void consultarArchivos(String ipOrigen, int puertoOrigen) throws IOException {
//		StringBuilder sb = new StringBuilder();
//		Set<String> set = paquetesArchivos.keySet();
//		for (String x : set) {
//			sb.append(x);
//			sb.append(";");
//		}
//		
		HashSet<String> hashArchivos = new HashSet<String>(paquetesArchivos.keySet());
		contarCantSolic = 0;
		if(!ipOrigen.equals(getHost())){
			for (int i = 0; i < manejadorClientes.size(); i++) {
				ThreadConexionOtros tc = (ThreadConexionOtros) manejadorClientes.get(i);
				if(tc.getHostDestino().equals(ipOrigen) && tc.getPuertoDestino() == puertoOrigen){
					Data d = new Data();
					d.setIpOrigen(getHost());
					d.setPuertoOrigen(getPuerto());
					d.setTarea(RECIBIR_CONSULTA_ARCHIVOS);
					d.setHashArchivos(hashArchivos);
					tc.enviarData(d);
					contarCantSolic++;
				}else{
					System.out.println("noooooooooooooooooooooooo ");
				}
			}
		}
		else
			System.out.println("ESTE FUE EL MALOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOO");
		
	}

	public void recibirConsultaArchivos( HashSet<String> nombreArchivos, String ipOrigen, int puertoOrigen) {
		this.nombresArchivos.addAll(nombreArchivos);
		System.out.println("contarCanti SOlicitau " + contarCantSolic);
		if(contarCantSolic==0){
			System.out.println("Los Archivos en el sistemas son :");
			for(String s : this.nombresArchivos){
				System.out.println(s);
			}
		}
		--contarCantSolic;
	}
}
