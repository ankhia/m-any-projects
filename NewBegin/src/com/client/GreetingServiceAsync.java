package com.client;

import com.client.bean.Characters;
import com.client.bean.PersonajesCuenta;
import com.client.bean.Usuario;
import com.google.gwt.user.client.rpc.AsyncCallback;


/**
 * The async counterpart of <code>GreetingService</code>.
 */
public interface GreetingServiceAsync {
	void greetServer(String input, AsyncCallback<String> callback)
			throws IllegalArgumentException;

	void validarUsuario(String user, String pass,		AsyncCallback<Usuario> asyncCallback);

	void cambiarPassword(Usuario usuarioLogeado, String confirmacionPass,AsyncCallback<Boolean> asyncCallback);

	void consultarPersonajes(Usuario usuarioLogeado, String nombreCuenta,	AsyncCallback<Characters[]> asyncCallback);

	void consultarPersonajesPorCuenta(AsyncCallback<PersonajesCuenta[]> asyncCallback);
}
