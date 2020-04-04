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

@Service
public class AlbumServiceImpl implements AlbumService{

	@Autowired
	AlbumRepository repository;
	
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
}
