package client;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;


public class ManejadorCliente {

	private Socket socketCliente;
	
	private ObjectOutputStream oos;
	
	private ObjectInputStream ois;

	public ManejadorCliente(Socket socketCliente) throws IOException
	{
		this.socketCliente = socketCliente;
		oos = new ObjectOutputStream(socketCliente.getOutputStream());
		ois = new ObjectInputStream(socketCliente.getInputStream());
	}
	
	
}
