package client;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.NetworkInterface;

public class EscuchaBroadCast implements Runnable {

	private final int PUERTO_BROADCAST = 55557;

	private final int TAMANIO_BYTES = 20;

	private final String INTERFACE = "eth0";

	private String ipOrigen;

	private String ipBroadCast;

	private int puertoOrigen;

	private int MTU;

	private P2P principal;

	public EscuchaBroadCast(int puertoOrigen, P2P principal) throws IOException
	{
		this.puertoOrigen = puertoOrigen;
		this.principal = principal;

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
			while(true){
				DatagramPacket dgp = new DatagramPacket(new byte [TAMANIO_BYTES],TAMANIO_BYTES);
				escucha.receive(dgp);
				byte[] datos = dgp.getData();
				String datoLlegaron  = new String(datos);
				if(datoLlegaron.indexOf(";")!=-1)
				{
					String aux[] = datoLlegaron.split(";");
					String ipDestino = aux[0];
					int puertoDestino = Integer.parseInt(aux[1]);
					if(!ipDestino.equalsIgnoreCase(ipOrigen) && puertoDestino != puertoOrigen)
					{
						principal.agregarConexionP2P(ipDestino, puertoDestino);
					}
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

}
