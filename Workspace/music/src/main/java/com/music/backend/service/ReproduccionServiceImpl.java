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

import com.music.backend.entity.Cancion;
import com.music.backend.entity.Reproduccion;
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

	@Override
	public Boolean createReproduccion(Reproduccion r) throws Exception{
		try {
			int id = repository.getNumber(r.getIdRep().getU()).length;
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
	public Boolean addSong(keyLista kl,keyCancion kc) throws Exception {
		
		try {
			Reproduccion r = repository.findById(kl.getL_id(), kl.getU());
			Cancion c = canService.getCancion(kc.getL_id().getL_id(), kc.getL_id().getU(), kc.getC_id());
			return r.canciones.add(c);
		}catch(Exception e) {
			System.out.println(e);
		}
		return false;
	}
	
}
