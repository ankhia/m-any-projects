import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JPanel;

public class P2P{
	public static int[] puertos = new int[]{ 9990, 9991 };
	
	public static String INSERTAR_ARCHIVO = "INSERTAR_ARCHIVO";
	public static String CONSULTAR_LISTA_ARCHIVOS = "CONSULTAR_LISTA_ARCHIVOS";
	public static String RECIBIR_LISTA_ARCHIVOS = "RECIBIR_LISTA_ARCHIVOS";
	
	Servidor servidor;
	FragmentadorArchivo fragmentador;
	SondaArchivos sondaArchivos;
	
	public static void main(String[] args) throws Throwable {
		BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
		int puerto = Integer.parseInt(in.readLine());
		P2P p2p = new P2P(puerto);
		imprimirMenu(in, p2p);
	}

	private static void imprimirMenu(BufferedReader in, P2P p2p)throws IOException {
		int op = 0;
		do{
			System.out.println("1. Insertar Archivo");
			System.out.println("2. Consultar Lista de Archivos");
//			System.out.println("3. Eliminar Archivo");
//			System.out.println("4. Consultar contenido Archivo");
//			System.out.println("5. Consultar Lista de Nodos que tienen Fragmentos de Archivo");

			switch( op ){
				case 1: p2p.insertarArchivo(); break;
				case 2: System.out.println(p2p.consultarListaArchivos()); break;
//				case 3: break;
//				case 4: break;
//				case 5: break;
			}
			op = Integer.parseInt(in.readLine());
		}while(op!=-1);
	}

	
	public P2P(int puerto) {
		servidor = new Servidor(this, puerto);
		fragmentador = new FragmentadorArchivo(puerto);
		sondaArchivos = new SondaArchivos();
	}
	
	
	private void insertarArchivo() {
		JFileChooser chooser = new JFileChooser();
		chooser.setDialogTitle("choose");
		chooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
		
		JFrame jf = new JFrame();
		jf.setVisible(true);
		
		int returnVal = chooser.showOpenDialog(jf);
		if(returnVal== JFileChooser.APPROVE_OPTION){
			fragmentador.procesarArchivo(chooser.getSelectedFile());
		}
		jf.dispose();
	}

	private String consultarListaArchivos() {
		sondaArchivos.consultarArchivosSistema();
		while( sondaArchivos.isEstaSondeando() );
		HashSet<String> listaArchivos = (HashSet<String>) sondaArchivos.getListaArchivos();

		StringBuilder sb = new StringBuilder();
		sb.append("El sistema contiene los siguientes archivos: ");
		for (Iterator iterator = listaArchivos.iterator(); iterator.hasNext();) {
			sb.append("     " + iterator.next() );
		}
		return new String(sb);
	}
	
	public void recibirListaArchivo(Set<String> archivos){
		sondaArchivos.recibirPeticion(archivos);
	}
}
