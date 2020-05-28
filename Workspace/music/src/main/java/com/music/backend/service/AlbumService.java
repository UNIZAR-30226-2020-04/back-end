package com.music.backend.service;

import java.io.File;

import org.springframework.web.multipart.MultipartFile;

import com.music.backend.entity.Album;
import com.music.backend.entity.keyLista;

public interface AlbumService {

	public Boolean createAlbum(Album a) throws Exception;
	public Album getAlbum(int i, String s) throws Exception;
	public Boolean deleteAlbum(int i, String s) throws Exception;
	public Boolean deleteByUser(String s) throws Exception;
	public keyLista createAlbum(String email, String name, String autor, MultipartFile foto) throws Exception;
	public Album[] getAlbumsByUser(String s) throws Exception;
	public Album[] getAlbumsBySearch(String nombre) throws Exception;
	public Boolean changeImage(MultipartFile f, String correo, int id) throws Exception;
}
