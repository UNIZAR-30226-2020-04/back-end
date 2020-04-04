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
			repository.save(p);
			return true;
		}catch(Exception e) {
			System.out.println(e);
		}
		return false;
	}
}
