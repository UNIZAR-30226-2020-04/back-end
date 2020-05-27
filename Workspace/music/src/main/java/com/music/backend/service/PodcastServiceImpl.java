package com.music.backend.service;

import java.io.ByteArrayOutputStream;
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

import com.music.backend.entity.Cancion;
import com.music.backend.entity.Podcast;
import com.music.backend.entity.Reproduccion;
import com.music.backend.entity.keyLista;
import com.music.backend.repository.PodcastRepository;
import com.music.backend.repository.UsuarioRepository;

@Service
public class PodcastServiceImpl implements PodcastService {

	@Autowired
	PodcastRepository repository;
	
	@Override
	public Boolean createPodcast(Podcast p, byte[] b) throws Exception{
		
		try {
			
			int id = repository.listPodcastsUser(p.getIdPodcast().getU()).length +1;
			keyLista kl = new keyLista(id,p.getIdPodcast().getU());
			p.setIdPodcast(kl);
			LocalDate date = LocalDate.now();
			String fechaPub = Integer.toString(date.getDayOfMonth()) + "/" + Integer.toString(date.getMonthValue()) + "/" + Integer.toString(date.getYear());
			p.setFechaPublicacion(fechaPub);

			String path = "./src/main/resources/static/assets/images";
			String imageName = String.valueOf("pd" + kl.getL_id() + kl.getU() + ".jpg");

			FileOutputStream fos = new FileOutputStream(path + imageName);

			String URLFoto = String.valueOf("pruebaslistenit.herokuapp.com/Image?idfoto=" + imageName);

			p.setURLFoto(URLFoto);
			
			fos.write(b);
			fos.close();

			repository.save(p);
			return true;
		}catch(Exception e) {
			System.out.println(e);
		}
		return false;
	}
	
	@Override
	public Podcast getPodcast(int i, String s) throws Exception{
		
		try {
			return repository.findById(i, s);
		}catch(Exception e) {
			System.out.println(e);
		}
		return new Podcast();
	}

	@Override
	public Boolean deletePodcast(int i, String s) throws Exception {
		
		try {
			repository.delete(repository.findById(i, s));
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
	public Cancion[] listPodcast(int i, String s) throws Exception {

		try {
			
			Podcast p = repository.findById(i, s);
			Cancion[] c = p.capitulos.toArray(new Cancion[0]);
			return c;
		}catch(Exception e) {
			System.out.println(e);
		}
		return null;
	}

	@Override
	public Podcast[] getPodcastByUser(String s) throws Exception{
		try{
			return repository.listPodcastsUser(s);
		}catch(Exception e){
			System.out.println(e);
		}
		return null;
	}

	@Override
	public Podcast[] getPodcastsBySearch(String nombre) throws Exception {
		try {
			return repository.getPodcastsBySearch(nombre);
		}catch(Exception e) {
			System.out.println(e);
		}
		return null;
	}
}
