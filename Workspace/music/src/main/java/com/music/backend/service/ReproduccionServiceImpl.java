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

import com.music.backend.entity.Reproduccion;
import com.music.backend.entity.keyLista;
import com.music.backend.repository.ReproduccionRepository;

@Service
public class ReproduccionServiceImpl implements ReproduccionService{

	@Autowired
	ReproduccionRepository repository;

	@Override
	public Boolean createReproduccion(Reproduccion r) throws Exception{
		
		int id = repository.getNumber(r.getIdRep().getU()).length;
		keyLista kl = new keyLista(id, r.getIdRep().getU());
		r.setIdRep(kl);
		LocalDate date = LocalDate.now();
		String fechaPub = Integer.toString(date.getDayOfMonth()) + "/" + Integer.toString(date.getMonthValue()) + "/" + Integer.toString(date.getYear());
		r.setFechaPublicacion(fechaPub);
		
		try {
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
	
}
