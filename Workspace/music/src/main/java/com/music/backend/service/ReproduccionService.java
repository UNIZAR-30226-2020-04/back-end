package com.music.backend.service;

import java.io.File;

import org.springframework.web.multipart.MultipartFile;

import com.music.backend.entity.Usuario;
import com.music.backend.entity.Cancion;
import com.music.backend.entity.Reproduccion;
import com.music.backend.entity.keyCancion;
import com.music.backend.entity.keyLista;


public interface ReproduccionService {

	public Boolean createReproduccion(Reproduccion r) throws Exception;
	public Reproduccion getReproduccion(int i, String s) throws Exception;
	public Boolean deleteReproduccion(int i, String s) throws Exception;
	public Boolean deleteByUser(String s) throws Exception;
	public Boolean addSong(keyLista kl, keyCancion kc) throws Exception;
	public Boolean deleteSongPlaylist(keyLista kl, keyCancion kc) throws Exception;
	public Cancion[] listSongs(int i, String s) throws Exception;
	public Reproduccion[] getPlaylistByUser(String s) throws Exception;
	public Reproduccion[] getPlaylistsBySearch(String nombre) throws Exception;
	public Boolean deleteSongAllPlaylists(Cancion c) throws Exception;
	public Boolean followByUser(Usuario u, Reproduccion r) throws Exception;
	public Boolean unFollowByUser(Usuario u, Reproduccion r) throws Exception;
	public Boolean changeImage(MultipartFile f, String correo, int id) throws Exception;
	public Boolean saveRep(Reproduccion r) throws Exception;
}
