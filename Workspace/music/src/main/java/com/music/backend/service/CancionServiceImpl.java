package com.music.backend.service;

import java.io.File;
import java.util.List;
import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.music.backend.entity.Cancion;
import com.music.backend.entity.Usuario;
import com.music.backend.entity.keyCancion;
import com.music.backend.entity.keyLista;
import com.music.backend.entity.Album;
import com.music.backend.repository.CancionRepository;

@Service
public class CancionServiceImpl implements CancionService{

	@Autowired
	CancionRepository repository;

	@Autowired
	AlbumService albumService;
	@Autowired
	UsuarioService usuarioService;
	
	@Override
	public Boolean createCancion(keyLista a, Cancion c) throws Exception{
		
		try {
			// Calculo el número de canción dentro del album
			int c_id = repository.listSongsInAlbumUser(a.getL_id(), a.getU()).length + 1;
			keyCancion idCancion = new keyCancion(a, c_id);
			c.setIdCancion(idCancion);
			repository.save(c);
			return true;
		}catch(Exception e) {
			System.out.println(e);
		}

		return false;
	}
	
	@Override
	public Cancion getCancion(int i, String s, int c) throws Exception{
		
		try {
			return repository.findById(i, s, c);
		}catch(Exception e) {
			System.out.println(e);
		}
		
		return new Cancion();
	}

	@Override
	public Boolean deleteCancion(int i, String s, int c) throws Exception {
		
		try {
			String path = System.getProperty("user.dir") + "/src/main/resources/static/assets/";
			String songName = c + i + s + ".mp3";
			File f = new File(path + songName);
			if(!f.delete()) {
				throw new Exception("No se ha podido borrar la canción");
			}
			repository.delete(repository.findById(i, s, c));
			return true;
		}catch(Exception e) {
			System.out.println(e);
		}
		
		return false;
	}
	
	@Override
	public Boolean deleteAllFromAlbum(int i, String s) throws Exception {
		
		try {
			repository.deleteAll(repository.getListFromAlbum(i, s));
			return true;
		}catch(Exception e) {
			System.out.println(e);
		}
		return false;
	}
	
	@Override
	public Boolean deleteByUser(String s) throws Exception {

		try {
			repository.deleteAll((repository.getByUser(s)));
		}catch(Exception e) {
			System.out.println(e);
		}
		return false;
	}

	@Override
	public Cancion[] listSongs(String email, int id) throws Exception {
		
		try {
			return repository.listSongsInAlbumUser(id, email);
		}catch(Exception e) {
			System.out.println(e);
		}
		return null;
	}

	@Override
	public Cancion[] getSongsBySearch(String nombre) throws Exception {
		try {
			return repository.getSongsBySearch(nombre);
		}catch(Exception e) {
			System.out.println(e);
		}
		return null;
	}

	public Album[] getAlbumsBySongs(Cancion[] canciones) throws Exception{
		try {
			List<Album> albumes = new ArrayList<>();

			for(Cancion i : canciones){
				Album actual = albumService.getAlbum(i.getIdCancion().getL_id().getL_id(), i.getIdCancion().getL_id().getU());
				if(!albumes.contains(actual)){
					albumes.add(actual);
				}
			}
			return (Album[]) albumes.toArray();

		}catch(Exception e) {
			System.out.println(e);
		}
		
		return null;
	}

	public Usuario[] getUsersBySongs(Cancion[] canciones) throws Exception{
		try {
			List<Usuario> usuarios = new ArrayList<>();

			for(Cancion i : canciones){
				Usuario actual = usuarioService.getUser(i.getIdCancion().getL_id().getU());
				if(!usuarios.contains(actual)){
					usuarios.add(actual);
				}
			}
			return (Usuario[]) usuarios.toArray();

		}catch(Exception e) {
			System.out.println(e);
		}
		
		return null;
	}
}
