package com.client;

import com.client.bean.Characters;
import com.client.bean.PersonajesCuenta;
import com.client.bean.Usuario;
import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

/**
 * The client side stub for the RPC service.
 */
@RemoteServiceRelativePath("greet")
public interface GreetingService extends RemoteService {
	String greetServer(String name) throws IllegalArgumentException;

	Usuario validarUsuario(String user, String pass);

	boolean cambiarPassword(Usuario usuarioLogeado, String confirmacionPass);

	Characters[] consultarPersonajes(Usuario usuarioLogeado, String nombreCuenta);

	PersonajesCuenta[] consultarPersonajesPorCuenta();
}
