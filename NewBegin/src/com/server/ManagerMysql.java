package com.server;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.StringTokenizer;

import com.client.bean.Characters;
import com.client.bean.PersonajesCuenta;
import com.client.bean.Usuario;


public class ManagerMysql {

	private static final String LOG_ERROR_CLASS = "Error->ManagerDatabaseMysql-> ";

	private static final String LOG_OK_CLASS = "OK->ManagerDatabaseMysql-> ";

	private static final String nombreDataBase = "databaseservidor";

	private static  ManagerMysql instance; 

	public static final String puerto = "3306";

	public static final String host = "newbeginningwow.com";

	public static final String usuario = "wow";

	public static final String password = "111111";

	public static final String AUTH_DB = "auth";

	public static final String CHARACTERS_DB = "characters";

	public static final String WORLD_DB = "world";

	private ConnectionDataBase connection;

	private ManagerMysql()
	{
		connection = new ConnectionDataBase(puerto, host, usuario, password);
	}

	public static  ManagerMysql getInstance(){
		if(instance == null)
			instance = new ManagerMysql();
		return instance;
	}
	
	public static String sha1(String input) throws NoSuchAlgorithmException 
	{
		MessageDigest mDigest = MessageDigest.getInstance("SHA1");
		byte[] result = mDigest.digest(input.getBytes());
		StringBuffer sb = new StringBuffer();
		for (int i = 0; i < result.length; i++) {
			sb.append(Integer.toString((result[i] & 0xff) + 0x100, 16).substring(1));
		}
		return sb.toString();
	}
	
	
	
	public static String toUpper(String in){
		String out = "";
		for(int i=0;i<in.length();++i)
			out += Character.toUpperCase(in.charAt(i));
		return out;
	}

	public Usuario validarUsuario(String usuario, String password)
	{
		Connection con = null;
		try
		{
			con =  connection.getConnection(AUTH_DB);
			String union = toUpper(usuario)+":"+toUpper(password);
			String sql = "SELECT * FROM account WHERE sha_pass_hash = ?";
			System.out.println("Consulta :  " + sql);
			PreparedStatement st = con.prepareStatement(sql);
			st.setString(1,sha1(union));
			ResultSet rs  = st.executeQuery();
			if(rs.next()){
				String passDB = rs.getString("passwd");
				Usuario usu = new Usuario(rs.getInt("id"),rs.getString("username"), rs.getString("email"), rs.getInt("failed_logins"), passDB, rs.getBoolean("isAdmin"));
				if(passDB==null)
				{
					sql = "UPDATE account SET passwd= ? WHERE id= ?";
					PreparedStatement st2 = con.prepareStatement(sql);
					st2.setString(1,password);
					st2.setInt(2,usu.getId());
					st.executeUpdate();
				}
				return usu;
			}
			return null;
		}
		catch (Exception e) 
		{
			System.out.println(LOG_ERROR_CLASS +"dragAndDrop_insertarNuevoUsuario:"+ e.getMessage());
			e.printStackTrace();
		}
		finally
		{
			try{if(con != null ) con.close();}catch (Exception e) {System.out.println(LOG_ERROR_CLASS +"dragAndDrop_insertarNuevoUsuario:"+ e.getMessage());e.printStackTrace();}
		}
		return null;
	}

	public boolean cambiarPassword(Usuario usuarioLogeado,String confirmacionPass) 
	{
		Connection con = null;
		try
		{
			con =  connection.getConnection(AUTH_DB);
			String union = toUpper(toUpper(usuarioLogeado.getUsername()))+":"+toUpper(toUpper(confirmacionPass));
			String sql = "UPDATE account SET passwd = ? , sha_pass_hash = ?  WHERE id= ?" ;
			System.out.println("Consulta :  " + sql);
			PreparedStatement st = con.prepareStatement(sql);
			st.setString(1, confirmacionPass);
			st.setString(2,sha1(union));
			st.setInt(3,usuarioLogeado.getId() );
			st.executeUpdate();
			return true;
		}
		catch (Exception e) 
		{
			System.out.println(LOG_ERROR_CLASS +"dragAndDrop_insertarNuevoUsuario:"+ e.getMessage());
			e.printStackTrace();
		}
		finally
		{
			try{if(con != null ) con.close();}catch (Exception e) {System.out.println(LOG_ERROR_CLASS +"dragAndDrop_insertarNuevoUsuario:"+ e.getMessage());e.printStackTrace();}
		}
		return false;
	}

	public Characters[] consultarPersonajes(Usuario usuarioLogeado,	String nombreCuenta) 
	{
	
		Connection con = null;
		try
		{
			con =  connection.getConnection(CHARACTERS_DB);
			String sql = "SELECT * FROM characters  WHERE account = ?" ;
			System.out.println("Consulta :  " + sql);
			PreparedStatement st = con.prepareStatement(sql);
			st.setInt(1, usuarioLogeado.getId());
			ResultSet rs = st.executeQuery();
			ArrayList<Characters>  c = new ArrayList<Characters>();
			while(rs.next()){
				Characters x = new Characters();
				x.setId(rs.getInt("guid"));
				x.setGenero(rs.getString("gender"));
				x.setNombre(rs.getString("name"));
				x.setNivel(rs.getInt("level"));
				x.setPlata(rs.getLong("money"));
				c.add(x);
			}
			return c.toArray(new Characters[0]);
		}
		catch (Exception e) 
		{
			System.out.println(LOG_ERROR_CLASS +"dragAndDrop_insertarNuevoUsuario:"+ e.getMessage());
			e.printStackTrace();
		}
		finally
		{
			try{if(con != null ) con.close();}catch (Exception e) {System.out.println(LOG_ERROR_CLASS +"dragAndDrop_insertarNuevoUsuario:"+ e.getMessage());e.printStackTrace();}
		}
		return null;
	}

	public PersonajesCuenta[] consultarPersonajesPorCuenta() 
	{
		Connection con = null;
		try
		{
			con =  connection.getConnection(AUTH_DB);
			String sql = "SELECT * FROM account ORDER BY last_login " ;
			System.out.println("Consulta :  " + sql);
			PreparedStatement st = con.prepareStatement(sql);
			ResultSet rs = st.executeQuery();
			ArrayList<String>  c = new ArrayList<String>();
			while(rs.next()){
				c.add(rs.getString("id")+"+"+rs.getString("username")+"+" +rs.getTimestamp("last_login")+"+"+rs.getString("email"));
			}
			st.close();
			con.close();
			con =  connection.getConnection(CHARACTERS_DB);
			ArrayList<PersonajesCuenta> fin = new ArrayList<PersonajesCuenta>();
			for(int i=0;i<c.size();++i){
				StringTokenizer stoken = new StringTokenizer(c.get(i),"+");
				int id_cuenta = Integer.parseInt(stoken.nextToken());
				String cuenta = stoken.nextToken();
				String ultimaVez = (stoken.nextToken());
				String email ="No tiene email registrado";
				if(stoken.hasMoreTokens())
					email = stoken.nextToken();
				
				sql = "SELECT * FROM characters WHERE account = ?" ;
				st = con.prepareStatement(sql);
				st.setInt(1, id_cuenta);
				rs = st.executeQuery();
				while(rs.next()){
					PersonajesCuenta x = new PersonajesCuenta();
					x.setId_cuenta(id_cuenta+"");
					x.setCuenta(cuenta);
					x.setUltimaVez(ultimaVez);
					x.setPersonaje(rs.getString("name"));
					x.setEmail(email);
					fin.add(x);
				}
				st.close();
			}
			con.close();
			return fin.toArray(new PersonajesCuenta[0] );
		}
		catch (Exception e) 
		{
			System.out.println(LOG_ERROR_CLASS +"consultarPersonajesPorCuenta:"+ e.getMessage());
			e.printStackTrace();
		}
		finally
		{
			try{if(con != null ) con.close();}catch (Exception e) {System.out.println(LOG_ERROR_CLASS +"dragAndDrop_insertarNuevoUsuario:"+ e.getMessage());e.printStackTrace();}
		}
		return null;
	}

	

	
}
