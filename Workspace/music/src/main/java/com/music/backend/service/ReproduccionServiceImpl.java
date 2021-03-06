package com.music.backend.service;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.zip.DataFormatException;
import java.util.zip.Deflater;
import java.util.zip.Inflater;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.music.backend.entity.Album;
import com.music.backend.entity.Cancion;
import com.music.backend.entity.Reproduccion;
import com.music.backend.entity.Usuario;
import com.music.backend.entity.keyCancion;
import com.music.backend.entity.keyLista;
import com.music.backend.repository.ReproduccionRepository;
import com.music.backend.service.CancionService;

@Service
public class ReproduccionServiceImpl implements ReproduccionService{

	@Autowired
	ReproduccionRepository repository;
	
	@Autowired
	CancionService canService;
	
	@Autowired
	UsuarioService userService;

	@Override
	public Boolean createReproduccion(Reproduccion r) throws Exception{
		try {
			int id = repository.listPlaylistsUser(r.getIdRep().getU()).length +1;
			keyLista kl = new keyLista(id, r.getIdRep().getU());
			r.setIdRep(kl);
			LocalDate date = LocalDate.now();
			String fechaPub = Integer.toString(date.getDayOfMonth()) + "/" + Integer.toString(date.getMonthValue()) + "/" + Integer.toString(date.getYear());
			r.setFechaPublicacion(fechaPub);
			repository.save(r);
			return true;
		}catch(Exception e) {
			System.out.println(e);
		}
		return false;
	}
	
	@Override
	public Reproduccion getReproduccion(int i, String s) throws Exception{
		
		try {
			return repository.findById(i, s);
		}catch(Exception e) {
			System.out.println(e);
		}
		return new Reproduccion();
	}

	@Override
	public Boolean deleteReproduccion(int i, String s) throws Exception {
		Reproduccion r = repository.findById(i, s);
		try {
			Usuario[] uu = userService.getAllUsers();
			if(uu != null) {
				for(Usuario u : uu) {
					if(u.followingPlaylist.contains(r)) {
						u.followingPlaylist.remove(r);
						userService.saveUser(u);
					}
				}	
			}
			repository.delete(r);
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
	public Boolean addSong(keyLista kl,keyCancion kc) throws Exception {
		
		try {
			Reproduccion r = repository.findById(kl.getL_id(), kl.getU());
			Cancion c = canService.getCancion(kc.getL_id().getL_id(), kc.getL_id().getU(), kc.getC_id());
			r.canciones.add(c);
			r = repository.save(r);
			return true;
		}catch(Exception e) {
			System.out.println(e);
		}
		return false;
	}

	@Override
	public Boolean deleteSongPlaylist(keyLista kl, keyCancion kc) throws Exception {
		try {
			Reproduccion r = repository.findById(kl.getL_id(), kl.getU());
			Cancion c = canService.getCancion(kc.getL_id().getL_id(), kc.getL_id().getU(), kc.getC_id());
			r.canciones.remove(c);
			r = repository.save(r);
			return true;
		}catch(Exception e) {
			System.out.println(e);
		}
		return false;
	}

	@Override
	public Cancion[] listSongs(int i, String s) throws Exception {
		try {
			Reproduccion r = repository.findById(i, s);
			Cancion[] c = r.canciones.toArray(new Cancion[r.canciones.size()]);
			return c;
		}catch(Exception e) {
			System.out.println(e);
		}
		return null;
	}

	@Override
	public Reproduccion[] getPlaylistByUser(String s) throws Exception{
		try{
			return repository.listPlaylistsUser(s);
		}catch(Exception e){
			System.out.println(e);
		}return null;
	}

	@Override
	public Reproduccion[] getPlaylistsBySearch(String nombre) throws Exception {
		try {
			return repository.getPlaylistsBySearch(nombre);
		}catch(Exception e) {
			System.out.println(e);
		}
		return null;
	}

	@Override
	public  Boolean deleteSongAllPlaylists(Cancion c) throws Exception{
		try{
			Reproduccion[] allPl = repository.getAllPlaylists();
			for(Reproduccion r : allPl) {
				if(r.canciones.contains(c)) {
					r.canciones.remove(c);
					repository.save(r);
				}
			}
			return true;
		}catch(Exception e){
			e.printStackTrace();
		}
		return false;
	}

	@Override
	public Boolean followByUser(Usuario u, Reproduccion r) throws Exception {
		try {
			u.followingPlaylist.add(r);
			//TODO
			return true;
		}catch(Exception e) {
			e.printStackTrace();
		}
		return false;
	}

	@Override
	public Boolean unFollowByUser(Usuario u, Reproduccion r) throws Exception {
		try {
			u.followingPlaylist.remove(r);
			//TODO
			return true;
		}catch(Exception e) {
			e.printStackTrace();
		}
		return false;
	}
	
	@Override
	public Boolean changeImage(MultipartFile f, String correo, int id) throws Exception{
		try {
			Reproduccion r = repository.findById(id, correo);
			String path = "./src/main/resources/static/assets/images/";
			String imageName = String.valueOf("pl" + id + correo + ".jpg");
			String URLFoto = String.valueOf("Image?idfoto=" + imageName);
			r.setURLFoto(URLFoto);
			FileOutputStream fos = new FileOutputStream(path + imageName);
			if(f!=null) {
				fos.write(f.getBytes());
			}
			fos.close();
			return true;
		}catch(Exception e) {
			e.printStackTrace();
		}
		return false;
	}

	@Override
	public Boolean saveRep(Reproduccion r) throws Exception {
		try {
			repository.save(r);
			return true;
		}catch(Exception e) {
			e.printStackTrace();
		}
		return false;
	}
	
}
