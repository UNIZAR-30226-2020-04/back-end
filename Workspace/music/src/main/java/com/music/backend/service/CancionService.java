package com.music.backend.service;

import com.music.backend.entity.Cancion;
import com.music.backend.entity.Usuario;
import com.music.backend.entity.keyCancion;
import com.music.backend.entity.keyLista;
import com.music.backend.entity.Album;

public interface CancionService {

	public keyCancion createCancion(keyLista a, Cancion c) throws Exception;
	public Cancion getCancion(int i, String s, int c) throws Exception;
	public Boolean deleteCancion(int i, String s, int c) throws Exception;
	public Boolean deleteByUser(String s) throws Exception;
	public Cancion[] listSongs(String email, int id) throws Exception;
	public Boolean deleteAllFromAlbum(int i, String s) throws Exception;
	public Cancion[] getSongsBySearch(String nombre) throws Exception;
	public Album[] getAlbumsBySongs(Cancion[] canciones) throws Exception;
	public Usuario[] getUsersBySongs(Cancion[] canciones) throws Exception;
	public Boolean saveSong(Cancion c) throws Exception;
	public Cancion[] getAll() throws Exception;
}
