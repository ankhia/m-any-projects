package client;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.util.Enumeration;

public class EscuchaBroadCast implements Runnable {

	private final int PUERTO_BROADCAST = 55557;

	private final int TAMANIO_BYTES=18;

	private final String INTERFACE = "net4";

	private String ipOrigen;

	private String ipBroadCast;

	private int puertoOrigen;

	private int MTU;

	private P2P principal;

	public EscuchaBroadCast(int puertoOrigen, P2P principal) throws IOException
	{
		this.puertoOrigen = puertoOrigen;
		this.principal = principal;
		
		Enumeration<NetworkInterface> e =NetworkInterface.getNetworkInterfaces();

		while(e.hasMoreElements()){
			NetworkInterface n = e.nextElement();
			System.out.println("DisplayName "+n.getDisplayName());
			System.out.println("Name " + n.getName());
		}

		if(NetworkInterface.getByName(INTERFACE).getInterfaceAddresses().size()>1)
		{
			ipOrigen = NetworkInterface.getByName(INTERFACE).getInterfaceAddresses().get(1).getAddress().getHostAddress();
			ipBroadCast = NetworkInterface.getByName(INTERFACE).getInterfaceAddresses().get(1).getBroadcast().getHostAddress();
			MTU = NetworkInterface.getByName(INTERFACE).getMTU();
		}
		else
		{
			ipOrigen = NetworkInterface.getByName(INTERFACE).getInterfaceAddresses().get(0).getAddress().getHostAddress();
			ipBroadCast = NetworkInterface.getByName(INTERFACE).getInterfaceAddresses().get(0).getBroadcast().getHostAddress();
			MTU = NetworkInterface.getByName(INTERFACE).getMTU();
		}
		enviarInformacionBroadCast();
	}

	private void enviarInformacionBroadCast() throws IOException
	{
		System.out.println("IP LOCAL " + ipOrigen);
		System.out.println("PUERTO LOCAL " + puertoOrigen);
		System.out.println("IP BROADCAST "+ ipBroadCast);
		System.out.println("PUERTO BROADCAST " + PUERTO_BROADCAST);

		DatagramSocket enviador = new DatagramSocket();
		enviador.setBroadcast(true);
		String mensaje  = ipOrigen+";"+puertoOrigen; 
		System.out.println("Tamanio Mensaje en Bytes:" + mensaje.getBytes().length);
		byte [] dato = mensaje.getBytes();
		DatagramPacket dgp = new DatagramPacket(dato, TAMANIO_BYTES, InetAddress.getByName(ipBroadCast), PUERTO_BROADCAST);
		enviador.send(dgp);
		System.out.println("Envio BroadCast ");
	}

	public void run() 
	{
		try
		{
			DatagramSocket escucha = new DatagramSocket(PUERTO_BROADCAST);
			while(true)
			{
				System.out.println("Esperando BroadCast ");
				DatagramPacket dgp = new DatagramPacket(new byte [TAMANIO_BYTES],TAMANIO_BYTES);
				escucha.receive(dgp);
				byte[] datos = dgp.getData();
				String datoLlegaron  = new String(datos);
				System.out.println("Recibi BroadCast " + datoLlegaron);
				if(datoLlegaron.indexOf(";")!=-1)
				{
					String aux[] = datoLlegaron.split(";");
					String ipDestino = aux[0];
					int puertoDestino = Integer.parseInt(aux[1]);
					if(!ipDestino.equalsIgnoreCase(ipOrigen))
						principal.agregarConexionP2P(ipDestino, puertoDestino);
					else
						System.out.println("No agrego conexion a mi mismo ");
				}
				else
					System.out.println("Pailasss Los datos no llegaron ");
			}
		}
		catch (Exception e) 
		{
			e.printStackTrace();
		}
	}

	public String getIpOrigen() {
		return ipOrigen;
	}

	public void setIpOrigen(String ipOrigen) {
		this.ipOrigen = ipOrigen;
	}

	public int getPuertoOrigen() {
		return puertoOrigen;
	}

	public void setPuertoOrigen(int puertoOrigen) {
		this.puertoOrigen = puertoOrigen;
	}

	
	
}
