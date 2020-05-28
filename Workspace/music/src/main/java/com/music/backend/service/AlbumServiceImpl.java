package com.music.backend.service;

import java.io.File;
import java.io.FileOutputStream;
import java.nio.file.Files;
import java.time.LocalDate;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.music.backend.entity.Album;
import com.music.backend.entity.keyLista;
import com.music.backend.repository.AlbumRepository;

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
	public keyLista createAlbum(String email, String name, String autor, MultipartFile foto) throws Exception{
		
		try {
			LocalDate date = LocalDate.now();
			String fechaPub = Integer.toString(date.getDayOfMonth()) + "/" + Integer.toString(date.getMonthValue()) + "/" + Integer.toString(date.getYear());
			int id = repository.listAlbumsUser(email).length + 1;

			String path = "./src/main/resources/static/assets/images/";
			String imageName = String.valueOf("alb" + id + email + ".jpg");

			FileOutputStream fos = new FileOutputStream(path + imageName);
			if(foto!=null) {
				fos.write(foto.getBytes());
			}
			fos.close();

			String URLFoto = String.valueOf("Image?idfoto=" + imageName);

			Album a = new Album(id, email, name, autor, URLFoto, fechaPub);
			repository.save(a);
			return a.getIdAlbum();
		}catch(Exception e) {
			System.out.println(e);
		}
		return null;
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
			cancionService.deleteAllFromAlbum(i, s);
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

	@Override
	public Album[] getAlbumsByUser(String s) throws Exception{
		try{
			return repository.listAlbumsUser(s);
			
		}catch(Exception e){
			System.out.println(e);
		}
		return null;
	}

	@Override
	public Album[] getAlbumsBySearch(String nombre) throws Exception {
		try {
			return repository.getAlbumsBySearch(nombre);
		}catch(Exception e) {
			System.out.println(e);
		}
		return null;
	}

	@Override
	public Boolean changeImage(MultipartFile f, String correo, int id) throws Exception {
		try {
			Album a = repository.findById(id, correo);
			String path = "./src/main/resources/static/assets/images/";
			String imageName = String.valueOf("alb" + id + correo + ".jpg");
			String URLFoto = String.valueOf("Image?idfoto=" + imageName);
			a.setURLFoto(URLFoto);
			FileOutputStream fos = new FileOutputStream(path + imageName);
			if(f!=null) {
				fos.write(f.getBytes());
			}
			fos.close();
		}catch(Exception e) {
			e.printStackTrace();
		}
		return false;
	}
}
