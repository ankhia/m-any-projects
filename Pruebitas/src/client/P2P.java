package client;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectOutputStream;
import java.io.PrintWriter;
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

	public static final String CONSULTAR_NODOS_ARCHIVO = "CONSULTAR NODOS ARCHIVO";

	public static final String RESPUESTA_ARCHIVOS_NODOS = "RESPUESTA_ARCHIVOS_NODOS";

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
				case 3: System.out.print("Nombre Archivo a eliminar: ");enviarOrdenEliminarArchivo(in.readLine());break;
				case 4: System.out.print("Nombre Archivo que desea consultar: ");consultarContenidoArchivo(in.readLine());break;
				case 5: System.out.print("Nombre Archivo a consultar en nodos: ");enviarOrdenConsultaNodos(in.readLine());break;
			}
			op = Integer.parseInt(in.readLine());
		}while(op!=-1);
	}

	private void consultarContenidoArchivo(String nombreArchivo) throws IOException {
		if(this.paquetesArchivos.get(nombreArchivo)!= null){
			ArrayList<Data> a = this.paquetesArchivos.get(nombreArchivo);
			if(a.size()>0){
				Data dato = a.get(0);
				int cantidadFragmentos = dato.getFragmentos();
				if(cantidadFragmentos==(a.size())){
					System.out.println("Tengo todos los fragmentos del archivo");
					byte[] ar = new byte[cantidadFragmentos*FragmentadorArchivo.MAXIMO_TAMANIO_BYTES];
					int cont = 0;
					for(int i=0;i<a.size();++i){
						byte[] aux = a.get(i).getDatos();
						for(int j=0;j<aux.length;++j)
							ar[cont++]=aux[j];
					}
					
					File archivoSalida = new File(dato.getNombreArchivo());
					archivoSalida.createNewFile();
					FileOutputStream fos = new FileOutputStream(archivoSalida);
					fos.write(ar);
					fos.flush();
					fos.close();
				}else{
					System.out.println("No tengo la cantidad completa de fragmentos");
				}
			}else{
				System.out.println("No tengo fragmentos del archivo");
			}
		}else{
			System.out.println("No tengo fragmentos del archivo");
		}
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
		JFileChooser chooser = new JFileChooser("C:\\Users\\Milena\\Desktop");
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
		contarCantSolic=0;
		for (int i = 0; i < manejadorClientes.size(); i++) {
			ThreadConexionOtros tc = (ThreadConexionOtros) manejadorClientes.get(i);
			if(!tc.getHostDestino().equals(getHost())){
				Data dataConsultar = new Data();
				dataConsultar.setTarea(CONSULTAR_ARCHIVOS);
				dataConsultar.setIpOrigen(getHost());
				dataConsultar.setPuertoOrigen(getPuerto());
				tc.enviarData(dataConsultar);
				contarCantSolic++;
			}
		}
	}
	
	private void enviarOrdenConsultaNodos(String nombreArchivo) throws IOException 
	{
		for(int i=0;i<manejadorClientes.size();++i){
			ThreadConexionOtros t = manejadorClientes.get(i);
			Data d = new Data();
			d.setTarea(CONSULTAR_NODOS_ARCHIVO);
			d.setIpOrigen(getHost());
			d.setPuertoOrigen(getPuerto());
			d.setNombreArchivo(nombreArchivo);
			t.enviarData(d);
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

	public synchronized void consultarArchivos(String ipOrigen, int puertoOrigen) throws IOException {
		HashSet<String> hashArchivos = new HashSet<String>(paquetesArchivos.keySet());
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
				}else
					System.out.println("No hay nodos diferentes a mi.");
			}
		}
		else
			System.out.println("El origen es igual al destino.");
	}

	public void recibirConsultaArchivos( HashSet<String> nombreArchivos, String ipOrigen, int puertoOrigen) {
		this.nombresArchivos.addAll(nombreArchivos);
		contarCantSolic--;
		System.out.println("Contador Solicitudes "+contarCantSolic);
		if(contarCantSolic==0){
			System.out.println("Los Archivos en el sistemas son :");
			for(String s : this.nombresArchivos){
				System.out.println(s);
			}
			this.nombresArchivos.clear();
		}else if(contarCantSolic==-1){
			System.out.println("No hay archivos aun en el sistema.");
			this.nombresArchivos.clear();
			contarCantSolic=0;
		}
	}

	public void consultarFragmentosPorNombreArchivo(String nombreArchivo, String hostOrigen, int puertoOrigen) throws IOException {
		for(int i=0;i<this.manejadorClientes.size();++i){
			ThreadConexionOtros t = manejadorClientes.get(i);
			if(hostOrigen.equals(t.getHostDestino())&& paquetesArchivos.get(nombreArchivo)!=null){
				Data d = new Data();
				d.setIpOrigen(getHost());
				d.setPuertoOrigen(getPuerto());
				d.setTarea(RESPUESTA_ARCHIVOS_NODOS);
				d.setNombreArchivo(nombreArchivo );
				t.enviarData(d);
			}
		}
	}

	public void archivosPorNodo(String ipOrigen, String nombreArchivo,	int puertoOrigen) {
		System.out.println("El nodo: "+ ipOrigen + " puerto : "+ puertoOrigen+ " tiene fragmentos del archivo : " + nombreArchivo);
	}
}
