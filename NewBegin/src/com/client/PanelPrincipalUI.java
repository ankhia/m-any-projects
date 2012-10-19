package com.client;

import com.client.bean.Characters;
import com.client.bean.PersonajesCuenta;
import com.client.bean.Usuario;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.http.client.URL;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.DockPanel;
import com.google.gwt.user.client.ui.Frame;
import com.google.gwt.user.client.ui.Grid;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.PasswordTextBox;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.uibinder.client.UiHandler;

public class PanelPrincipalUI extends Composite  {

	private static PanelPrincipalUIUiBinder uiBinder = GWT.create(PanelPrincipalUIUiBinder.class);
	@UiField Label lblEncabezado;
	@UiField DockPanel panelDock;
	@UiField Button btnCambiarClave;
	@UiField VerticalPanel panelCenter;
	@UiField VerticalPanel panelMenu;
	@UiField Button btnRegistrarEmail;
	
	private NewBegin principal;
	
	private VerticalPanel fp;

	interface PanelPrincipalUIUiBinder extends
			UiBinder<Widget, PanelPrincipalUI> {
	}

	public PanelPrincipalUI(final NewBegin principal) {
		initWidget(uiBinder.createAndBindUi(this));
		this.principal = principal;
		
		fp = new VerticalPanel();
		
		lblEncabezado.setText(lblEncabezado.getText()+ " " +this.principal.getUsuarioLogeado().getUsername() );
		if(this.principal.getUsuarioLogeado().isAdmin()){
			lblEncabezado.setText(lblEncabezado.getText()+ " ( ADMIN )");
			Button btn = new Button("Consultar Cuenta");
			btn.setWidth("139px");
			btn.setHeight("31px");
			btn.addClickHandler(new ClickHandler() 
			{
				public void onClick(ClickEvent event) 
				{
					panelCenter.clear();
					
					VerticalPanel vp =new VerticalPanel();
					HorizontalPanel h = new HorizontalPanel();
					Label lbl = new Label("Nombre Cuenta: ");
					final TextBox txt = new TextBox();
					Button btnBuscar = new Button("Buscar");
					btnBuscar.addClickHandler(new ClickHandler() {
						public void onClick(ClickEvent event) {
							
							principal.greetingService.consultarPersonajes(principal.getUsuarioLogeado(),txt.getValue(), new AsyncCallback<Characters[]>() 
							{
								@Override
								public void onFailure(Throwable caught) {
									// TODO Auto-generated method stub
									
								}
								@Override
								public void onSuccess(Characters[] result) {
									Grid g = new Grid(result.length+1, 6);
									g.setBorderWidth(1);
									g.setHTML(0, 0, "<b>Id</b>");
									g.setHTML(0, 1, "<b>Nombre</b>");
									g.setHTML(0, 2, "<b>Level</b>");
									g.setHTML(0, 3, "<b>Gender</b>");
									g.setHTML(0, 4, "<b>Money</b>");
									g.setHTML(0, 5, "<b>Raza</b>");
									
									for(int i=0;i<result.length;++i){
										g.setHTML(i+1, 0, result[i].getId()+"");
										g.setHTML(i+1, 1, result[i].getNombre()+"");
										g.setHTML(i+1, 2, result[i].getNivel()+"");
										g.setHTML(i+1, 3, result[i].getGenero()+"");
										g.setHTML(i+1, 4, result[i].getPlata()+"");
										g.setHTML(i+1, 5, result[i].getPlata()+"");
									}
									panelCenter.add(g);
								}
							});
							
						}
					});
					h.add(lbl);
					h.add(txt);
					h.add(btnBuscar);
					
					vp.add(h);
					panelCenter.add(vp);
					
				}
			});
			panelMenu.add(btn);
			
			
			btn = new Button("Personajes por Cuenta");
			btn.setWidth("139px");
			btn.setHeight("31px");
			btn.addClickHandler(new ClickHandler() 
			{
				public void onClick(ClickEvent event) 
				{
					panelCenter.clear();
					VerticalPanel vp =new VerticalPanel();
					HorizontalPanel h = new HorizontalPanel();
					Button btnBuscar = new Button("Buscar");
					btnBuscar.addClickHandler(new ClickHandler() {
						public void onClick(ClickEvent event) {
							principal.greetingService.consultarPersonajesPorCuenta( new AsyncCallback<PersonajesCuenta[]>() 
							{
								@Override
								public void onFailure(Throwable caught) {
									// TODO Auto-generated method stub
									
								}
								@Override
								public void onSuccess(PersonajesCuenta[] result) {
									Grid g = new Grid(result.length+1, 5);
									g.setBorderWidth(1);
									g.setHTML(0, 0, "<b>Id</b>");
									g.setHTML(0, 1, "<b>Cuenta</b>");
									g.setHTML(0, 2, "<b>Personaje</b>");
									g.setHTML(0, 3, "<b>Ultima Vez         </b>");
									g.setHTML(0, 4, "<b>Email</b>");
									
									for(int i=0;i<result.length;++i){
										g.setHTML(i+1, 0, result[i].getId_cuenta()+"");
										g.setHTML(i+1, 1, result[i].getCuenta()+"");
										g.setHTML(i+1, 2, result[i].getPersonaje()+"");
										g.setHTML(i+1, 3, result[i].getUltimaVez()+"");
										g.setHTML(i+1, 4, result[i].getEmail()+"");
									}
									panelCenter.add(g);
								}
							});
							
						}
					});
					h.add(btnBuscar);
					vp.add(h);
					panelCenter.add(vp);
					
				}
			});
			panelMenu.add(btn);
		}
		Label x = new Label("Login: "+ this.principal.getUsuarioLogeado().getUsername());
		x.setStyleName("style-Label");
		fp.add(x);
		
		x = new Label("Email: "+ this.principal.getUsuarioLogeado().getEmail());
		x.setStyleName("style-Label");
		fp.add(x);
		
		if(this.principal.getUsuarioLogeado().getEmail() == null ||this.principal.getUsuarioLogeado().getEmail().trim().length()==0 ){
			x = new Label("Porfavor actualiza tu correo! ");
			x.setStyleName("style-LabelError");
			fp.add(x);
		}
		panelCenter.add(fp);
	}

	@UiHandler("btnCambiarClave")
	void onBtnCambiarClaveClick(ClickEvent event) {
		panelCenter.clear();
		VerticalPanel vp = new VerticalPanel();
		HorizontalPanel h = new HorizontalPanel();
		h.add(new Label("Password Anterior:"));
		final PasswordTextBox txtPassAnterior = new PasswordTextBox();
		h.add(txtPassAnterior);
		vp.add(h);
		h = new HorizontalPanel();
		h.add(new Label("Nuevo Password:"));
		final PasswordTextBox txtNuevoPassword = new PasswordTextBox();
		h.add(txtNuevoPassword);
		vp.add(h);
		h = new HorizontalPanel();
		h.add(new Label("Confirmar Nuevo Password:"));
		final PasswordTextBox txtNuevoPasswordConfirmacion = new PasswordTextBox();
		h.add(txtNuevoPasswordConfirmacion);
		vp.add(h);
		Button btnCambiar = new Button("Cambiar Password");
		btnCambiar.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) 
			{
				String anteriorPass = txtPassAnterior.getValue();
				String nuevoPass = txtNuevoPassword.getValue();
				final String confirmacionPass = txtNuevoPasswordConfirmacion.getValue();
				if(anteriorPass == null || nuevoPass == null || confirmacionPass== null)
					Window.alert("Ninguno de los campos puede ser vacio");
				if(nuevoPass.trim().length()==0 )
					Window.alert("El nuevo password no puede ser vacio o espacios");
				else if(!nuevoPass.equals(confirmacionPass))
					Window.alert("El nuevo password y la confirmacion no son iguales porfavor verifique");
				else if(!anteriorPass.equals(principal.getUsuarioLogeado().getPassword()))
					Window.alert("El anterior password es incorrecto!");
				else 
					principal.greetingService.cambiarPassword(principal.getUsuarioLogeado(), confirmacionPass, new AsyncCallback<Boolean>() 
					{
						public void onFailure(Throwable caught) {
							System.out.println("ERROR : " + caught);
						}
						public void onSuccess(Boolean result) 
						{
							if(result )
							{
								principal.getUsuarioLogeado().setPassword(confirmacionPass);
								txtNuevoPassword.setValue("");
								txtNuevoPasswordConfirmacion.setValue("");
								txtPassAnterior.setValue("");
								Window.alert("Listo");
							}
							else
								Window.alert("No se pudo cambiar el password");
						}
					});
				
				
				
			}
		});
		vp.add(btnCambiar);
		panelCenter.add(vp);
		
	}
	@UiHandler("btnRegistrarEmail")
	void onBtnRegistrarEmailClick(ClickEvent event) {
		Window.open("http://www.newbeginningwow.com/admin/registro.php", "Actualiza tu Correo ","");
	}
}
