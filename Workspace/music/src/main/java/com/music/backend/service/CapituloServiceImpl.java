package com.music.backend.service;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.ArrayList;
import java.util.Optional;
import java.util.zip.DataFormatException;
import java.util.zip.Deflater;
import java.util.zip.Inflater;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.music.backend.entity.Capitulo;
import com.music.backend.entity.Podcast;
import com.music.backend.entity.Usuario;
import com.music.backend.entity.keyCancion;
import com.music.backend.entity.keyLista;
import com.music.backend.entity.Album;
import com.music.backend.repository.CapituloRepository;
import com.music.backend.repository.UsuarioRepository;

@Service
public class CapituloServiceImpl implements CapituloService{

	@Autowired
	CapituloRepository repository;

	@Autowired
	PodcastService podService;
	@Autowired
	UsuarioService usuarioService;
	
	@Override
	public keyCancion createCapitulo(keyLista a, Capitulo c) throws Exception{
		
		try {
			// Calculo el número de canción dentro del album
			int c_id = repository.listSongsInAlbumUser(a.getL_id(), a.getU()).length + 1;
			keyCancion idCapitulo = new keyCancion(a, c_id);
			c.setIdCapitulo(idCapitulo);
			c = repository.save(c);
			return c.getIdCapitulo(); 
		}catch(Exception e) {
			System.out.println(e);
		}

		return null;
	}
	
	@Override
	public Capitulo getCapitulo(int i, String s, int c) throws Exception{
		
		try {
			return repository.findById(i, s, c);
		}catch(Exception e) {
			System.out.println(e);
		}
		
		return new Capitulo();
	}

	@Override
	public Boolean deleteCapitulo(int i, String s, int c) throws Exception {
		
		try {
			String path = System.getProperty("user.dir") + "/src/main/resources/static/assets/";
			String songName = "capitulo_" + String.valueOf(c) + String.valueOf(i) + s + ".mp3";
			File f = new File(path + songName);
			if(!f.delete()) {
				throw new Exception("No se ha podido borrar el capítulo");
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
	public Capitulo[] listSongs(String email, int id) throws Exception {
		
		try {
			return repository.listSongsInAlbumUser(id, email);
		}catch(Exception e) {
			System.out.println(e);
		}
		return null;
	}

	@Override
	public Capitulo[] getSongsBySearch(String nombre) throws Exception {
		try {
			return repository.getSongsBySearch(nombre);
		}catch(Exception e) {
			System.out.println(e);
		}
		return null;
	}

	public Podcast[] getAlbumsBySongs(Capitulo[] canciones) throws Exception{
		try {
			List<Podcast> albumes = new ArrayList<>();

			for(Capitulo i : canciones){
				Podcast actual = podService.getPodcast(i.getIdCapitulo().getL_id().getL_id(), i.getIdCapitulo().getL_id().getU());
				if(!albumes.contains(actual)){
					albumes.add(actual);
				}
			}
			return albumes.toArray(new Podcast[0]);

		}catch(Exception e) {
			System.out.println(e);
		}
		
		return null;
	}

	public Usuario[] getUsersBySongs(Capitulo[] canciones) throws Exception{
		try {
			List<Usuario> usuarios = new ArrayList<>();

			for(Capitulo i : canciones){
				Usuario actual = usuarioService.getUser(i.getIdCapitulo().getL_id().getU());
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

	@Override
	public Boolean saveChapter(Capitulo c) throws Exception {
		try {
			c = repository.save(c);
			return true;
		}catch(Exception e) {
			e.printStackTrace();
		}
		return false;
	}
}
