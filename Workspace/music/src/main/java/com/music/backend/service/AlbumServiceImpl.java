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

import com.music.backend.entity.Album;
import com.music.backend.entity.Cancion;
import com.music.backend.entity.keyLista;
import com.music.backend.repository.AlbumRepository;
import com.music.backend.repository.CancionRepository;

@Service
public class AlbumServiceImpl implements AlbumService{

	@Autowired
	AlbumRepository repository;
	@Autowired
	CancionService cancionService;
	
	@Override
	public Boolean createAlbum(Album a) throws Exception{
		
		try {
			repository.save(a);
			return true;
		}catch(Exception e) {
			System.out.println(e);
		}
		return false;
	}
	
	@Override
	public Boolean createAlbum(String email, String name, String date) throws Exception{
		
		try {
			Album a = new Album((int)repository.count()+1, email, name, null, date);
			repository.save(a);
			return true;
		}catch(Exception e) {
			System.out.println(e);
		}
		return false;
	}
	
	@Override
	public Album getAlbum(int i, String s) throws Exception{
		
		try {
			return repository.findById(i, s);
		}catch(Exception e) {
			System.out.println(e);
		}
		
		return new Album();
	}

	@Override
	public Boolean deleteAlbum(int i, String s) throws Exception {
		
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
			cancionService.deleteByUser(s);
			repository.deleteAll((repository.getByUser(s)));
		}catch(Exception e) {
			System.out.println(e);
		}
		return false;
	}
	
}
