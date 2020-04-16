package com.music.backend.service;

import java.io.ByteArrayOutputStream;
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
	public Boolean createPodcast(Podcast p) throws Exception{
		
		try {
			
			int id = repository.getNumber(p.getIdPodcast().getU()).length;
			keyLista kl = new keyLista(id,p.getIdPodcast().getU());
			p.setIdPodcast(kl);
			LocalDate date = LocalDate.now();
			String fechaPub = Integer.toString(date.getDayOfMonth()) + "/" + Integer.toString(date.getMonthValue()) + "/" + Integer.toString(date.getYear());
			p.setFechaPublicacion(fechaPub);
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
}
