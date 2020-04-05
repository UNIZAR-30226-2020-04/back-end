package com.music.backend.service;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
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
import com.music.backend.entity.keyCancion;
import com.music.backend.repository.CancionRepository;
import com.music.backend.repository.UsuarioRepository;

@Service
public class CancionServiceImpl implements CancionService{

	@Autowired
	CancionRepository repository;
	
	@Override
	public Boolean createCancion(Cancion c) throws Exception{
		
		try {
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
			repository.delete(repository.findById(i, s, c));
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
