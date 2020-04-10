package com.music.backend.service;

import com.music.backend.entity.Album;

public interface AlbumService {

	public Boolean createAlbum(Album a) throws Exception;
	public Album getAlbum(int i, String s) throws Exception;
	public Boolean deleteAlbum(int i, String s) throws Exception;
	public Boolean deleteByUser(String s) throws Exception;
	public Boolean createAlbum(String email, String name, String date) throws Exception;
}
