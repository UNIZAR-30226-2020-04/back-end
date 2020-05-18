package com.music.backend.service;

import com.music.backend.entity.Usuario;
import com.music.backend.entity.keyLista;

public interface UsuarioService {

	public Boolean createUser(Usuario u) throws Exception;
	public Usuario getUser(String u) throws Exception;
	public Boolean deleteUser(String u, String pass, String passCheck) throws Exception;
	public Usuario loginUser(String c, String p) throws Exception;
	public Boolean suscribe(String user, keyLista kl) throws Exception;
	public Boolean changeName(String user, String name, String newName) throws Exception;
	public Boolean changeNick(String user, String nick, String newNick) throws Exception;
	public Boolean changePass(String correo, String pass, String newPass) throws Exception;
	public Usuario[] getUsersBySearch(String nombre) throws Exception;
}
