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

import com.music.backend.entity.Reproduccion;
import com.music.backend.entity.keyLista;
import com.music.backend.repository.ReproduccionRepository;

@Service
public class ReproduccionServiceImpl implements ReproduccionService{

	@Autowired
	ReproduccionRepository repository;

	@Override
	public Boolean createReproduccion(Reproduccion r) throws Exception{
		
		try {
			repository.save(r);
			return true;
		}catch(Exception e) {
			System.out.println(e);
		}
		return false;
	}
	
}
