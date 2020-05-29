package com.music.backend.service;

import com.music.backend.entity.Podcast;
import com.music.backend.entity.Reproduccion;
import com.music.backend.entity.Usuario;
import com.music.backend.entity.keyLista;
import com.music.backend.entity.Cancion;

import org.springframework.web.multipart.MultipartFile;

import com.music.backend.entity.Album;

public interface UsuarioService {

	public Boolean createUser(Usuario u) throws Exception;
	public Usuario getUser(String u) throws Exception;
	public Boolean deleteUser(String u, String pass, String passCheck) throws Exception;
	public Usuario loginUser(String c, String p) throws Exception;
	/*
	public Boolean subscribePodcast(String user, keyLista kl) throws Exception;
	public Boolean unsubscribePodcast(String user, keyLista kl) throws Exception;
	public Boolean checkSubscription(String user, keyLista kl) throws Exception;
	public Podcast[] listSubscriptions(String user) throws Exception;
	public Boolean followPlaylist(String user, int i) throws Exception;
	public Boolean unfollowPlaylist(String user, int i) throws Exception;
	public Boolean checkFollow(String user, int i) throws Exception;
	public Reproduccion[] listFollows(String user) throws Exception;
	public Boolean likeSong(int idLista, String user, int cancion) throws Exception;
	public Boolean unlikeSong(int idLista, String user, int cancion) throws Exception;
	public Boolean checkLike(String user, int idLista, int cancion) throws Exception;
	public Cancion[] listLikes(String user) throws Exception;
	public Album[] listAlbumsLikes(String user) throws Exception;
	public Usuario[] listUsersLikes(String user) throws Exception;
	*/
	public Boolean changeName(String user, String name, String newName) throws Exception;
	public Boolean changeNick(String user, String nick, String newNick) throws Exception;
	public Boolean changePass(String correo, String pass, String newPass) throws Exception;
	public Boolean changeImage(MultipartFile f, String correo) throws Exception;
	public Usuario[] getUsersBySearch(String nombre) throws Exception;

	public Boolean subscribePodcast(String user, keyLista kl) throws Exception;
	public Boolean unsubscribePodcast(String user, keyLista kl) throws Exception;
	public Boolean checkSubscription(String user, keyLista kl) throws Exception;
	public Podcast[] listSubscriptions(String user) throws Exception;
	
	public Boolean followPlaylist(String correo, Reproduccion r) throws Exception;
	public Boolean unFollowPlaylist(String correo, Reproduccion r) throws Exception;
	public Reproduccion[] listFollowsPlaylists(String user) throws Exception;
	public Boolean checkFollow(String user, int i) throws Exception;

	public Boolean likeSong(String user, String correoalbum, int cancion, int idLista) throws Exception;
	public Boolean unlikeSong(String user, String correoalbum, int cancion, int idLista) throws Exception;
	public Boolean checkLike(String user, String correoalbum, int idLista, int cancion) throws Exception;
	public Cancion[] listLikes(String user) throws Exception;
	public Album[] listAlbumsLikes(String user) throws Exception;
	public Usuario[] listUsersLikes(String user) throws Exception;

	public Boolean followUser(String sessionUser, String targetUser) throws Exception;
	public Boolean unfollowUser(String sessionUser, String targetUser) throws Exception;
	public Usuario[] listFollowedUsers(String sessionUser) throws Exception;
	public Usuario[] followers(String user) throws Exception;
	public Boolean checkFollowUser(String sessionUser, String targetUser) throws Exception;
	
	public Boolean saveUser(Usuario u) throws Exception;
	public Usuario[] getAllUsers() throws Exception;
}
