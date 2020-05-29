package com.music.backend.service;

import java.io.ByteArrayOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.zip.DataFormatException;
import java.util.zip.Deflater;
import java.util.zip.Inflater;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

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
	@Autowired
	CancionService cancionService;

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
	public Boolean deleteUser(String u, String pass, String passCheck) throws Exception {
		try {
			
			Usuario user = repository.findByEmail(u);
			
			if(!pass.equals(passCheck) || !user.getPass().equals(pass)) {
				throw new Exception("Ha habido un problema");
			}
			
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

	@Override
	public Usuario loginUser(String c, String p) throws Exception {
		
		return repository.findByEmailAndPass(c,p);
	}
	
	public Boolean updatePass(String correo, String oldPass, String newPass) throws Exception{
		try {
			Usuario u = repository.findByEmailAndPass(correo, oldPass);
			u.setPass(newPass);
			u = repository.save(u);
			return true;
		}catch(Exception e) {
			e.printStackTrace();
		}
		return false;
	}
	/*
	@Override
	public Boolean subscribePodcast(String user, keyLista kl) throws Exception {
		
		try {
			Usuario u = repository.findByEmail(user);
			Podcast p = podService.getPodcast(kl.getL_id(), kl.getU());
			return u.addPodcast(p);
		}catch(Exception e) {
			System.out.println(e);
		}
		return false;
	}

	
	
	@Override
	public Boolean unsubscribePodcast(String user, keyLista kl) throws Exception{
				
		try {
			Usuario u = repository.findByEmail(user);
			Podcast p = podService.getPodcast(kl.getL_id(), kl.getU());
			return u.removePodcast(p);
		}catch(Exception e) {
			System.out.println(e);
		}
		return false;
	}

	@Override
	public Boolean checkSubscription(String user, keyLista kl) throws Exception{
				
		try {
			Usuario u = repository.findByEmail(user);
			Podcast p = podService.getPodcast(kl.getL_id(), kl.getU());
			return u.findPodcast(p);
		}catch(Exception e) {
			System.out.println(e);
		}
		return false;
	}

	@Override
	public Podcast[] listSubscriptions(String user) throws Exception{
		try {
			Usuario u = repository.findByEmail(user);
			return u.listSubscriptions();
		}catch(Exception e) {
			System.out.println(e);
		}
		return null;
	}

	@Override
	public Boolean followPlaylist(String user, int i) throws Exception {
		try{
			Usuario u = repository.findByEmail(user);
			Reproduccion r = repService.getReproduccion(i, user);
			return u.addPlayList(r);
		}catch(Exception e){
			System.out.println(e);
		}
		return false;
	}

	@Override
	public Boolean unfollowPlaylist(String user, int i) throws Exception {
		try{
			Usuario u = repository.findByEmail(user);
			Reproduccion r = repService.getReproduccion(i, user);
			return u.removePlayList(r);
		}catch(Exception e){
			System.out.println(e);
		}
		return false;
	}

	@Override
	public Boolean checkFollow(String user, int i) throws Exception {
		try{
			Usuario u = repository.findByEmail(user);
			Reproduccion r = repService.getReproduccion(i, user);
			return u.findPlaylist(r);
		}catch(Exception e){
			System.out.println(e);
		}
		return false;
	}

	@Override
	public Reproduccion[] listFollows(String user) throws Exception{
		try {
			Usuario u = repository.findByEmail(user);
			return u.listFollows();
		}catch(Exception e) {
			System.out.println(e);
		}
		return null;
	}

	@Override
	public Boolean likeSong(int idLista, String user, int cancion) throws Exception{
		try{
			Usuario u = repository.findByEmail(user);
			Cancion c = cancionService.getCancion(idLista, user, cancion);
			return u.addSong(c);
		}catch(Exception e){
			System.out.println(e);
		}
		return false;
	}

	@Override
	public Boolean unlikeSong(int idLista, String user, int cancion) throws Exception{
		try{
			Usuario u = repository.findByEmail(user);
			Cancion c = cancionService.getCancion(idLista, user, cancion);
			return u.removeSong(c);
		}catch(Exception e){
			System.out.println(e);
		}
		return false;
	}

	@Override
	public Boolean checkLike(String user, int cancion, int idLista) throws Exception{
		try{
			Usuario u = repository.findByEmail(user);
			Cancion c = cancionService.getCancion(idLista, user, cancion);
			return u.findSong(c);
		}catch(Exception e){
			System.out.println(e);
		}
		return false;
	}

	@Override
	public Cancion[] listLikes(String user) throws Exception{
		try {
			Usuario u = repository.findByEmail(user);
			return u.listLikes();
		}catch(Exception e) {
			System.out.println(e);
		}
		return null;
	}

	public Album[] listAlbumsLikes(String user) throws Exception {
		try {
			Usuario u = repository.findByEmail(user);
			Cancion[] likes = u.listLikes();

			return cancionService.getAlbumsBySongs(likes);
		}catch(Exception e) {
			System.out.println(e);
		}
		return null;
	}

	public Usuario[] listUsersLikes(String user) throws Exception {
		try {
			Usuario u = repository.findByEmail(user);
			Cancion[] likes = u.listLikes();

			return cancionService.getUsersBySongs(likes);
		}catch(Exception e) {
			System.out.println(e);
		}
		return null;
	}
	*/
	@Override
	public Boolean changeName(String user, String name, String newName) throws Exception {
		try {
			
			Usuario u = repository.findByEmail(user);
			
			if(!u.getNombre().equals(name)) {
				throw new Exception("El nombre no es igual");
			}
			u.setNombre(newName);
			u = repository.save(u);
			return true;
		}catch(Exception e) {
			System.out.println(e);
		}
		return false;
	}

	@Override
	public Boolean changeNick(String user, String nick, String newNick) throws Exception {
		try {
			
			Usuario u = repository.findByEmail(user);
			
			if(!u.getNick().equals(nick)) {
				throw new Exception("El nick no es igual");
			}
			u.setNick(newNick);
			u = repository.save(u);
			return true;
		}catch(Exception e) {
			System.out.println(e);
		}
		return false;
	}
	
	
	@Override
	public Boolean changePass(String user, String pass, String newPass) throws Exception{
		
		try {
			Usuario u = repository.findByEmail(user);

			if(!u.getPass().equals(pass)) {
				throw new Exception("La contraseña no es la misma");
			}
			
			u.setPass(newPass);
			
			u = repository.save(u);
			return true;
		}catch(Exception e) {
			e.printStackTrace();
		}
		return false;
	}
	
	@Override
	public Boolean changeImage(MultipartFile f, String correo) throws Exception {
		try {
			Usuario u = repository.findByEmail(correo);
			String path = "./src/main/resources/static/assets/images/";
			String imageName = String.valueOf("usr" + correo + ".jpg");
			
			FileOutputStream fos = new FileOutputStream(path + imageName);
			fos.write(f.getBytes());
			fos.close();

			String URLFoto = String.valueOf("Image?idfoto=" + imageName);
			u.setFoto(URLFoto);
			u = repository.save(u);
			return true;
		}catch(Exception e) {
			e.printStackTrace();
		}
		return false;
	}
	
	@Override
	public Usuario[] getUsersBySearch(String nombre) {
		
		try {
			return repository.getUsersBySearch(nombre);
		}catch(Exception e) {
			System.out.println(e);
		}
		return null;
	}

	@Override
	public Boolean subscribePodcast(String user, keyLista kl) throws Exception {
		
		try {
			Usuario u = repository.findByEmail(user);
			Podcast p = podService.getPodcast(kl.getL_id(), kl.getU());
			u.suscripciones.add(p);
			u = repository.save(u);
			return true;
		}catch(Exception e) {
			System.out.println(e);
		}
		return false;
	}
	
	@Override
	public Boolean unsubscribePodcast(String user, keyLista kl) throws Exception{
				
		try {
			Usuario u = repository.findByEmail(user);
			Podcast p = podService.getPodcast(kl.getL_id(), kl.getU());
			u.suscripciones.remove(p);
			u = repository.save(u);
			return true;
		}catch(Exception e) {
			System.out.println(e);
		}
		return false;
	}

	@Override
	public Boolean checkSubscription(String user, keyLista kl) throws Exception{
				
		try {
			Usuario u = repository.findByEmail(user);
			Podcast p = podService.getPodcast(kl.getL_id(), kl.getU());
			return u.suscripciones.contains(p);
		}catch(Exception e) {
			System.out.println(e);
		}
		return false;
	}

	@Override
	public Podcast[] listSubscriptions(String user) throws Exception{
		try {
			Usuario u = repository.findByEmail(user);
			return u.suscripciones.toArray(new Podcast[u.suscripciones.size()]);
		}catch(Exception e) {
			System.out.println(e);
		}
		return null;
	}

	@Override
	public Boolean followPlaylist(String correo, Reproduccion r) throws Exception {
		try {
			Usuario u = repository.findByEmail(correo);
			r.suscripciones.add(u);
			u.followingPlaylist.add(r);
			u = repository.save(u);
			repService.saveRep(r);
			return true;
		}catch(Exception e) {
			e.printStackTrace();
		}
		return false;
	}

	@Override
	public Boolean unFollowPlaylist(String correo, Reproduccion r) throws Exception {
		try {
			Usuario u = repository.findByEmail(correo);
			r.suscripciones.remove(u);
			u.followingPlaylist.remove(r);
			u = repository.save(u);
			repService.saveRep(r);
			return true;
		}catch(Exception e) {
			e.printStackTrace();
		}
		return false;
	}

	@Override
	public Reproduccion[] listFollowsPlaylists(String user) throws Exception{
		try {
			Usuario u = repository.findByEmail(user);
			return u.followingPlaylist.toArray(new Reproduccion[u.followingPlaylist.size()]);
		}catch(Exception e) {
			System.out.println(e);
		}
		return null;
	}

	@Override
	public Boolean checkFollow(String user, int i) throws Exception {
		try{
			Usuario u = repository.findByEmail(user);
			Reproduccion r = repService.getReproduccion(i, user);
			return u.followingPlaylist.contains(r);
		}catch(Exception e){
			System.out.println(e);
		}
		return false;
	}

	@Override
	public Boolean likeSong(String user, String correoalbum, int cancion, int idLista) throws Exception {
		try {
			Usuario u = repository.findByEmail(user);
			Cancion c = cancionService.getCancion(idLista, correoalbum, cancion);
			u.likedSongs.add(c);
			u = repository.save(u);
			Reproduccion r = repService.getReproduccion(1, user);
			r.canciones.add(c);
			repService.saveRep(r);
			return true;
		}catch(Exception e) {
			e.printStackTrace();
		}
		return false;
	}
	
	@Override
	public Boolean unlikeSong(String user, String correoalbum, int cancion, int idLista) throws Exception {
		try {
			Usuario u = repository.findByEmail(user);
			Cancion c = cancionService.getCancion(idLista, correoalbum, cancion);
			u.likedSongs.remove(c);
			u = repository.save(u);
			Reproduccion r = repService.getReproduccion(1, user);
			r.canciones.remove(c);
			repService.saveRep(r);
			return true;
		}catch(Exception e) {
			e.printStackTrace();
		}
		return false;
	}

	@Override
	public Boolean checkLike(String user, String correoalbum, int cancion, int idLista) throws Exception{
		try{
			Usuario u = repository.findByEmail(user);
			Cancion c = cancionService.getCancion(idLista, correoalbum, cancion);
			keyCancion kc = c.getIdCancion();
			return u.likedSongs.contains(c);
		}catch(Exception e){
			e.printStackTrace();
		}
		return false;
	}

	@Override
	public Cancion[] listLikes(String user) throws Exception{
		try {
			Usuario u = repository.findByEmail(user);
			return u.likedSongs.toArray(new Cancion[u.likedSongs.size()]);
		}catch(Exception e) {
			System.out.println(e);
		}
		return null;
	}

	@Override
	public Album[] listAlbumsLikes(String user) throws Exception {
		try {
			Usuario u = repository.findByEmail(user);
			Cancion[] likes = u.likedSongs.toArray(new Cancion[u.likedSongs.size()]);

			return cancionService.getAlbumsBySongs(likes);
		}catch(Exception e) {
			System.out.println(e);
		}
		return null;
	}

	@Override
	public Usuario[] listUsersLikes(String user) throws Exception {
		try {
			Usuario u = repository.findByEmail(user);
			Cancion[] likes = u.likedSongs.toArray(new Cancion[u.likedSongs.size()]);

			return cancionService.getUsersBySongs(likes);
		}catch(Exception e) {
			System.out.println(e);
		}
		return null;
	}

	@Override
	public Boolean followUser(String sessionUser, String targetUser) throws Exception{
		try {
			Usuario u1 = repository.findByEmail(sessionUser);
			Usuario u2 = repository.findByEmail(targetUser);
			u1.followedUsers.add(u2);
			u2.usersFollowingMe.add(u1);
			u1 = repository.save(u1);
			u2 = repository.save(u2);
			return true;
		}catch(Exception e) {
			e.printStackTrace();
		}
		return false;
	}

	@Override
	public Boolean unfollowUser(String sessionUser, String targetUser) throws Exception{
		try {
			Usuario u1 = repository.findByEmail(sessionUser);
			Usuario u2 = repository.findByEmail(targetUser);
			u1.followedUsers.remove(u2);
			u2.usersFollowingMe.remove(u1);
			u1 = repository.save(u1);
			u2 = repository.save(u2);
			return true;
		}catch(Exception e) {
			System.out.println(e);
		}
		return false;
	}

	@Override
	public Usuario[] listFollowedUsers(String sessionUser) throws Exception{
		try {
			Usuario u = repository.findByEmail(sessionUser);
			return  u.followedUsers.toArray(new Usuario[u.followedUsers.size()]);
		}catch(Exception e) {
			System.out.println(e);
		}
		return null;
	}
	
	@Override
	public Usuario[] followers(String user) throws Exception{
		try {
			Usuario u = repository.findByEmail(user);
			return u.usersFollowingMe.toArray(new Usuario[u.usersFollowingMe.size()]);
		}catch(Exception e) {
			
		}
		return null;
	}

	@Override
	public Boolean checkFollowUser(String sessionUser, String targetUser) throws Exception{
		try {
			Usuario u1 = repository.findByEmail(sessionUser);
			Usuario u2 = repository.findByEmail(targetUser);
			return u1.followedUsers.contains(u2);
		}catch(Exception e) {
			System.out.println(e);
		}
		return false;
	}

	@Override
	public Boolean saveUser(Usuario u) throws Exception{
		try {
			u = repository.save(u);
			return true;
		}catch(Exception e) {
			e.printStackTrace();
		}
		return false;
	}
	
	@Override
	public Usuario[] getAllUsers() throws Exception {
		
		try {
			repository.getAllUsers();
		}catch(Exception e) {
			e.printStackTrace();
		}
		
		return null;
	}
	
}
