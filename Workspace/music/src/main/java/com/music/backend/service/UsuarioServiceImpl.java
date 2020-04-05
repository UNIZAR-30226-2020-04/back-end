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

import com.music.backend.entity.*;
import com.music.backend.repository.AlbumRepository;
import com.music.backend.repository.PodcastRepository;
import com.music.backend.repository.ReproduccionRepository;
import com.music.backend.repository.UsuarioRepository;

@Service
public class UsuarioServiceImpl implements UsuarioService{

	@Autowired
	UsuarioRepository repository;

	@Autowired
	ReproduccionService repService;
	@Autowired
	PodcastService podService;
	@Autowired
	AlbumService albumService;

	@Override
	public Boolean createUser(Usuario u) throws Exception {
		try {
			repository.save(u);
			return true;
		}catch(Exception e) {
			System.out.println(e);
		}
		return false;
	}

	@Override
	public Usuario getUser(String u) throws Exception {
		try {
			return repository.findByEmail(u);
		}catch(Exception e) {
			System.out.println(e);
		}
		return new Usuario();
	}

	@Override
	public Boolean deleteUser(String u) throws Exception {
		try {
			repService.deleteByUser(u);
			albumService.deleteByUser(u);
			podService.deleteByUser(u);
			repository.delete(repository.findByEmail(u));
			return true;
		}catch(Exception e) {
			System.out.println(e);
			e.printStackTrace();
		}
		return false;
	}
	
}
