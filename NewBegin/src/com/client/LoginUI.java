package com.client;

import com.client.bean.Usuario;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.PasswordTextBox;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.user.client.ui.Label;

public class LoginUI extends Composite
{
	private static LoginUIUiBinder uiBinder = GWT.create(LoginUIUiBinder.class);
	@UiField Button btnLogin;
	@UiField TextBox txtLogin;
	@UiField PasswordTextBox txtPass;
	@UiField Label lblmsn;
	
	private NewBegin principal;

	interface LoginUIUiBinder extends UiBinder<Widget, LoginUI> {
	}

	public LoginUI(NewBegin principal) {
		initWidget(uiBinder.createAndBindUi(this));
		this.principal = principal;
		txtPass.addClickHandler(new ClickHandler() {
			
			@Override
			public void onClick(ClickEvent event) {
				validarInformacionLogin();
			}
		});
		
		
	}

	@UiHandler("btnLogin")
	void onBtnLoginClick(ClickEvent event) {
		validarInformacionLogin();
	}
	
	private void validarInformacionLogin()
	{
		NewBegin.greetingService.validarUsuario(txtLogin.getValue(),  txtPass.getValue() , new AsyncCallback<Usuario>() {
			public void onFailure(Throwable caught) {
				System.out.println("validarUsuario : " + caught);
			}
			public void onSuccess(Usuario result) {
				if(result!= null ){
					System.out.println("Resultado : " + result);
					lblmsn.setText("");
					principal.cargarInterfazPrincipal(result);
				}
				else
				{
					lblmsn.setText("Error en login o password.");
					lblmsn.setStyleName("style-LabelError");
				}
			}
		});
		System.out.println("Login : " + txtLogin.getValue());
		System.out.println("Pass : " + txtPass.getValue());
	}
}
