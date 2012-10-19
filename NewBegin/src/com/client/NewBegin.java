package com.client;

import com.client.bean.Usuario;
import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.ui.RootPanel;

/**
 * Entry point classes define <code>onModuleLoad()</code>.
 */
public class NewBegin implements EntryPoint {

	/**
	 * Create a remote service proxy to talk to the server-side Greeting service.
	 */
	public static final GreetingServiceAsync greetingService = GWT.create(GreetingService.class);
	
	private Usuario usuarioLogeado;

	public void onModuleLoad() 
	{
		RootPanel.get("nameFieldContainer").add(new LoginUI(this));
	}
	
	public void cargarInterfazPrincipal(Usuario usuLogeado)
	{
		this.usuarioLogeado = usuLogeado;
		RootPanel.get("nameFieldContainer").remove(0);
		RootPanel.get("nameFieldContainer").add(new PanelPrincipalUI(this));
	}
	
	public Usuario getUsuarioLogeado(){
		return this.usuarioLogeado;
	}
}
