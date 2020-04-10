package com.music.backend.service;

import com.music.backend.entity.Usuario;

public interface UsuarioService {

	public Boolean createUser(Usuario u) throws Exception;
	public Usuario getUser(String u) throws Exception;
	public Boolean deleteUser(String u) throws Exception;
	public Usuario loginUser(String c, String p) throws Exception;
	public Boolean updateUser(String correo, String nombre, String nick) throws Exception;
	public Boolean updatePass(String correo, String oldPass, String newPass) throws Exception;
}
