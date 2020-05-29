package com.music.backend.service;

import com.music.backend.entity.Capitulo;
import com.music.backend.entity.Podcast;
import com.music.backend.entity.Usuario;
import com.music.backend.entity.keyCancion;
import com.music.backend.entity.keyLista;
import com.music.backend.entity.Album;

public interface CapituloService {

	public keyCancion createCapitulo(keyLista a, Capitulo c) throws Exception;
	public Capitulo getCapitulo(int i, String s, int c) throws Exception;
	public Boolean deleteCapitulo(int i, String s, int c) throws Exception;
	public Boolean deleteByUser(String s) throws Exception;
	public Capitulo[] listSongs(String email, int id) throws Exception;
	public Boolean deleteAllFromAlbum(int i, String s) throws Exception;
	public Capitulo[] getSongsBySearch(String nombre) throws Exception;
	public Podcast[] getAlbumsBySongs(Capitulo[] capitulos) throws Exception;
	public Usuario[] getUsersBySongs(Capitulo[] capitulos) throws Exception;
	public Boolean saveChapter(Capitulo c) throws Exception;
}