package com.music.backend.controller;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Paths;
import java.util.LinkedHashMap;
import java.util.Properties;
import java.util.Random;

import javax.mail.Message;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import javax.activation.*;

import com.music.backend.entity.*;
import com.music.backend.service.*;

@Controller
@CrossOrigin(origins = "*")
public class UserController {

	@Autowired
	UsuarioService usuarioService;
	@Autowired
	ReproduccionService repService;
	@Autowired
	PodcastService podService;
	@Autowired
	CancionService cancionService;
	@Autowired
	AlbumService albumService;
	@Autowired
	CapituloService capService;
	
	
	@Autowired
    public JavaMailSender emailSender;
	
	
	@GetMapping(value = "/pruebasBD")
	public void pruebasBD(ModelMap model, HttpServletResponse response){
		
		try {
			//Pruebas de INSERT
			
			//Usuario
			String correo = "a@a.com";
			Usuario u = new Usuario(correo, "Usuario", null, "pass", "nick", "fNacimiento");
			
			if(!usuarioService.createUser(u)) {
				throw new Exception("INSERT usuario mal hecho");
			}
			
			//Reproduccion
			keyLista kLr = new keyLista(1,correo);
			
			Reproduccion r = new Reproduccion(kLr, "Reproduccion", null, "fechaPublicacion");
			
			if(!repService.createReproduccion(r)) {
				throw new Exception("INSERT reproduccion mal hecho");
			}
			
			//Podcast
			keyLista kLp = new keyLista(2,correo);
			
			Podcast p = new Podcast(kLp, "Podcast", null, "fechaPublicacion");
			
			if(!podService.createPodcast(p, null)) {
				throw new Exception("INSERT podcast mal hecho");
			}
			
			//Album
			keyLista kLa = new keyLista(3,correo);
			
			Album a = new Album(kLa, "Album", "Autor", null, "fechaPublicacion");
			
			if(!albumService.createAlbum(a)) {
				throw new Exception("INSERT album mal hecho");
			}
			
			//Cancion
			
			Cancion c = new Cancion(kLa, 1, "Cancion", "Genero", null);
			
			if(!cancionService.createCancion( kLa, c)) {
				throw new Exception("INSERT cancion mal hecho");
			}
			
			//Pruebas de SELECT
			
			//Usuario
			Usuario _u = usuarioService.getUser(u.getCorreo());
			if(!u.equals(_u)) {
				throw new Exception("SELECT usuario mal hecho");
			}
			
			//Reproduccion
			Reproduccion _r = repService.getReproduccion(r.getIdRep().getL_id(), r.getIdRep().getU());
			if(!r.equals(_r)) {
				throw new Exception("SELECT reproduccion mal hecho");
			}
			
			//Podcast
			Podcast _p = podService.getPodcast(p.getIdPodcast().getL_id(), p.getIdPodcast().getU());
			if(!p.equals(_p)) {
				throw new Exception("SELECT podcast mal hecho");
			}
			
			//Album
			Album _a = albumService.getAlbum(a.getIdAlbum().getL_id(), a.getIdAlbum().getU());
			if(!a.equals(_a)) {
				throw new Exception("SELECT album mal hecho");
			}
			
			//Cancion
			Cancion _c = cancionService.getCancion(c.getIdCancion().getL_id().getL_id(), c.getIdCancion().getL_id().getU(), c.getIdCancion().getC_id());
			if(!c.equals(_c)) {
				throw new Exception("SELECT cancion mal hecho");
			}
			
			
			//Pruebas de DELETE
			
			//Usuario
			//if(!usuarioService.deleteUser(u.getCorreo())) {
			//	throw new Exception("DELETE usuario mal hecho");
			//}
			/*
			//Cancion
			if(!cancionService.deleteCancion(c.getIdCancion().getL_id().getL_id(), c.getIdCancion().getL_id().getU(), c.getIdCancion().getC_id())) {
				throw new Exception("DELETE cancion mal hecho");
			}
			
			//Reproduccion
			if(!repService.deleteReproduccion(r.getIdRep().getL_id(), r.getIdRep().getU())) {
				throw new Exception("DELETE reproduccion mal hecho");
			}
			
			//Podcast
			if(!podService.deletePodcast(p.getIdPodcast().getL_id(),  p.getIdPodcast().getU())) {
				throw new Exception("DELETE podcast mal hecho");
			}
			
			//Album
			if(!albumService.deleteAlbum(a.getIdAlbum().getL_id(), a.getIdAlbum().getU())) {
				throw new Exception("DELETE album mal hecho");
			}
			*/
			System.out.println("Prueba pasada");
			
		} catch (Exception e) {
			System.out.println("Error:");
			System.out.println(e.getMessage());
			e.printStackTrace();
		}
	}
	
	/*
		Método Login:
		/loginUser
		Parámetro: Usuario (Correo + Pass)	
		Devuelve el Usuario si se ha podido hacer login, sino un Usuario vacío
	 */
	
	@PostMapping(value = "/loginUser", produces = "application/json", consumes = "application/json")
	@ResponseBody
	public ResponseEntity<Usuario> login(@RequestBody Object u, ModelMap model, HttpServletResponse response, BindingResult result){

		try {
			LinkedHashMap<String,String> lhm = (LinkedHashMap) u;
			String c = lhm.get("email");
			String p = lhm.get("password");
			
			System.out.println("Correo: " + c);
			System.out.println("Password: " + p);
			
			Usuario res = usuarioService.loginUser(c,p);
			
			System.out.println(res.toString());
			ResponseEntity<Usuario> r = new ResponseEntity<Usuario>(res, HttpStatus.OK);
			return new ResponseEntity<Usuario>(res, HttpStatus.OK);
		}catch(Exception e) {
			System.out.println(e);
		}
		Usuario user = new Usuario();
		System.out.println(user.getCorreo());
		return new ResponseEntity<Usuario>(HttpStatus.BAD_REQUEST);
	}
	
	
	@PostMapping(value = "/p")
	public ResponseEntity<Usuario> p(ModelMap model, HttpServletResponse response){

		try {
			return null;
		}catch(Exception e) {
			System.out.println(e);
		}
		return null;
	}
	
	
	/*
		Método Register:
		/registerUser
		Parámetro: Usuario
		Devuelve true si se ha podido hacer el registro, sino false
	*/
	@PostMapping(value = "/registerUser", produces = "application/json")
	@ResponseBody
	public Boolean register(@RequestParam("name") String name, @RequestParam("surname") String surname,
			@RequestParam("username") String username, @RequestParam("email") String email,
			@RequestParam("password") String password, @RequestParam("dateOfBirth") String dateOfBirth, 
			@RequestParam(name = "foto", required = false) MultipartFile f , ModelMap model, HttpServletResponse response){
			System.out.println("Entro en el register");
		try {
			//LinkedHashMap<String,Object> lhm = (LinkedHashMap) u;
			//String name = (String) lhm.get("name");
			//String surname = (String) lhm.get("surname");
			//String username = (String) lhm.get("username");
			//String email = (String) lhm.get("email");
			//String password = (String) lhm.get("password");
			//String dateOfBirth = (String) lhm.get("dateOfBirth");

			String path = "./src/main/resources/static/assets/images/";
			String imageName = String.valueOf("usr" + email + ".jpg");
			
			FileOutputStream fos = new FileOutputStream(path + imageName);
			fos.write(f.getBytes());
			fos.close();

			String URLFoto = String.valueOf("Image?idfoto=" + imageName);
			
			System.out.println("name: " + name);
			System.out.println("surname: " + surname);
			System.out.println("username: " + username);
			System.out.println("email: " + email);
			System.out.println("password: " + password);
			System.out.println("dateOfBirth: " + dateOfBirth);
		    
		    Usuario user = new Usuario(email, name, URLFoto, password, username, dateOfBirth);
		    System.out.println(user.toString());
			return usuarioService.createUser(user);
		}catch(Exception e) {
			e.printStackTrace();
		}
		return false;
	}
	
	//Función para que los de móvil puedan seguir haciendo cosas
	//Sin necesidad de meter las fotos
	@PostMapping(value = "/registerUser1", produces = "application/json")
	@ResponseBody
	public Boolean register1(@RequestParam("name") String name, @RequestParam("surname") String surname,
			@RequestParam("username") String username, @RequestParam("email") String email,
			@RequestParam("password") String password, @RequestParam("dateOfBirth") String dateOfBirth,
			ModelMap model, HttpServletResponse response){
			System.out.println("Entro en el register");
		try {
			//LinkedHashMap<String,Object> lhm = (LinkedHashMap) u;
			//String name = (String) lhm.get("name");
			//String surname = (String) lhm.get("surname");
			//String username = (String) lhm.get("username");
			//String email = (String) lhm.get("email");
			//String password = (String) lhm.get("password");
			//String dateOfBirth = (String) lhm.get("dateOfBirth");

			String path = "./src/main/resources/static/assets/images/";
			String imageName = String.valueOf("usr" + email + ".jpg");
			
			FileOutputStream fos = new FileOutputStream(path + imageName);
			//fos.write(null);
			fos.close();

			String URLFoto = String.valueOf("Image?idfoto=" + imageName);
			
			System.out.println("name: " + name);
			System.out.println("surname: " + surname);
			System.out.println("username: " + username);
			System.out.println("email: " + email);
			System.out.println("password: " + password);
			System.out.println("dateOfBirth: " + dateOfBirth);
		    
		    Usuario user = new Usuario(email, name, URLFoto, password, username, dateOfBirth);
		    System.out.println(user.toString());
			return usuarioService.createUser(user);
		}catch(Exception e) {
			e.printStackTrace();
		}
		return false;
	}
	
	
	@PostMapping(value = "/pruebaReact", produces = "application/json")
	@ResponseBody
	public void pruebaReact(@RequestBody Object u, ModelMap model, HttpServletResponse response, BindingResult result){
		try {
			LinkedHashMap<String,String> lhm = (LinkedHashMap) u;
			
			String c = lhm.get("email");
			String p = lhm.get("password");
			System.out.println("Correo: " + c);
			System.out.println("Password: " + p);
			
		}catch(Exception e) {
			System.out.println("Excepcion en PruebaReact");
			System.out.println(e);
		}
	}
	
	
	@PostMapping(value = "/createAlbum", produces = "application/json")
	@ResponseBody
	public keyLista createAlbum( @RequestParam(name = "foto", required = false) MultipartFile f, @RequestParam("email") String e,
			@RequestParam("name") String n, ModelMap model, HttpServletResponse response){
		
		try {
			String a = usuarioService.getUser(e).getNick();
			return albumService.createAlbum(e, n, a, f);
		}catch(Exception ex) {
			System.out.println(ex);
		}
		return null;
	}
	
	@PostMapping(value = "/createPlaylist", produces = "application/json")
	@ResponseBody
	public keyLista createPlaylist(@RequestParam(name = "foto", required = false) MultipartFile f, @RequestParam("email") String user,
			@RequestParam("playlist") String p, ModelMap model, HttpServletResponse response){
		
		try {
			
			Reproduccion r = new Reproduccion();
			keyLista kl = new keyLista(-1,user);
			r.setIdRep(kl);
			r.setNombre(p);

			String path = "./src/main/resources/static/assets/images/";
			String imageName = String.valueOf("pl" + kl.getL_id() + kl.getU() + ".jpg");

			FileOutputStream fos = new FileOutputStream(path + imageName);

			String URLFoto = String.valueOf("Image?idfoto=" + imageName);
			r.setURLFoto(URLFoto);

			if(!repService.createReproduccion(r)) {
				throw new Exception("No se ha podido crear la playlist");
			}
			if(f!=null) {
				fos.write(f.getBytes());
			}
			fos.close();
			
			return  r.getIdRep();
		}catch(Exception e) {
			System.out.println(e);
		}
		
		return null;
	}
	
	
	@PostMapping(value = "/addToPlaylist", produces = "application/json")
	@ResponseBody
	public Boolean addToPlaylist(@RequestBody Object u, 
			ModelMap model, HttpServletResponse response, BindingResult result){
		
		try {
			
			LinkedHashMap<String,String> lhm = (LinkedHashMap) u;
			String user = lhm.get("user");
			String nombre = lhm.get("nombre");
			String user_c = lhm.get("usercancion");
			int id_a = Integer.parseInt(lhm.get("idalbum"));
			int id_p = Integer.parseInt(lhm.get("idplaylist"));
			int id_c = Integer.parseInt(lhm.get("idcancion"));
			
			keyLista kl = new keyLista(id_p,user);
			keyCancion kc = new keyCancion(id_a, user_c, id_c);
			
			return repService.addSong(kl, kc);
			
		}catch(Exception e) {
			System.out.println(e);
		}
		return false;
	}
	
	@PostMapping(value = "/createPodcast", produces = "application/json")
	@ResponseBody
	public keyLista createPodcast(@RequestParam(name = "foto", required = false) MultipartFile f, @RequestParam("email") String user,
			@RequestParam("podcast") String nombre, ModelMap model, HttpServletResponse response){
		
		try {
			
			Podcast p = new Podcast(-1, user, nombre, null, null);
			if(f!=null) {
				if(!podService.createPodcast(p, f.getBytes())) {
					throw new Exception("No se ha podido crear el Podcast");
				}
			}else {
				if(!podService.createPodcast(p, null)) {
					throw new Exception("No se ha podido crear el Podcast");
				}
			}
			
			
			return p.getIdPodcast();
		}catch(Exception e) {
			System.out.println(e);
		}
		return null;
	}
	
	
	
	@PostMapping(value = "/deleteUser", produces = "application/json")
	@ResponseBody
	public Boolean deleteUser(@RequestBody Object u, ModelMap model, HttpServletResponse response, BindingResult result){
		
		try {
			LinkedHashMap<String,String> lhm = (LinkedHashMap) u;
			
			String user = lhm.get("user");
			String pass = lhm.get("pass");
			String passCheck = lhm.get("passCheck");
			
			return usuarioService.deleteUser(user, pass, passCheck);
		}catch(Exception e) {
			System.out.println(e);
		}
		return false;
	}
	
	
	@PostMapping(value = "/cambiarNombre", produces = "application/json")
	@ResponseBody
	public Boolean cambiarNombre(@RequestBody Object u, ModelMap model, HttpServletResponse response, BindingResult result){
		
		try {
			LinkedHashMap<String,String> lhm = (LinkedHashMap) u;
			
			String user = lhm.get("user");
			String name = lhm.get("name");
			String newName = lhm.get("newName");
			return usuarioService.changeName(user, name, newName);
		}catch(Exception e) {
			System.out.println(e);
		}
		return false;
	}
	
	
	@PostMapping(value = "/cambiarNick", produces = "application/json")
	@ResponseBody
	public Boolean cambiarNick(@RequestBody Object u, ModelMap model, HttpServletResponse response, BindingResult result){
		
		try {
			LinkedHashMap<String,String> lhm = (LinkedHashMap) u;
			
			String user = lhm.get("user");
			String nick = lhm.get("nick");
			String newNick = lhm.get("newNick");
			return usuarioService.changeNick(user, nick, newNick);
		}catch(Exception e) {
			System.out.println(e);
		}
		return false;
	}
	
	@PostMapping(value = "/cambiarPass", produces = "application/json")
	@ResponseBody
	public Boolean cambiarPass(@RequestBody Object u, ModelMap model, HttpServletResponse response, BindingResult result){
		
		try {
			LinkedHashMap<String,String> lhm = (LinkedHashMap) u;
			
			String user = lhm.get("user");
			String pass = lhm.get("pass");
			String newPass = lhm.get("newPass");
			return usuarioService.changePass(user,pass,newPass);
		}catch(Exception e) {
			System.out.println(e);
		}
		return false;
	}
	
	
	@PostMapping(value = "/listSongsAlbum", produces = "application/json")
	@ResponseBody
	public Cancion[] listSongsAlbum(@RequestBody Object u, ModelMap model, HttpServletResponse response, BindingResult result){
		
		try {
			LinkedHashMap<String,String> lhm = (LinkedHashMap) u;
			
			String user = lhm.get("user");
			int id_a = Integer.parseInt(lhm.get("idalbum"));
			
			return cancionService.listSongs(user, id_a);
		}catch(Exception e) {
			System.out.println(e);
		}
		return null;
	}
	
	
	@PostMapping(value = "/listSongsPlaylist", produces = "application/json")
	@ResponseBody
	public Cancion[] listSongsPlaylist(@RequestBody Object u, ModelMap model, HttpServletResponse response, BindingResult result){
		
		try {
			LinkedHashMap<String,String> lhm = (LinkedHashMap) u;
			
			String user = lhm.get("user");
			int id_p = Integer.parseInt(lhm.get("idplaylist"));
			
			return repService.listSongs(id_p, user);
		}catch(Exception e) {
			System.out.println(e);
		}
		return null;
	}
	
	
	@PostMapping(value = "/listPodcast", produces = "application/json")
	@ResponseBody
	public Capitulo[] listPodcast(@RequestBody Object u, ModelMap model, HttpServletResponse response, BindingResult result){
		
		try {
			LinkedHashMap<String,String> lhm = (LinkedHashMap) u;
			
			String user = lhm.get("user");
			int id_a = Integer.parseInt(lhm.get("idalbum"));
			
			return capService.listSongs(user, id_a);
		}catch(Exception e) {
			System.out.println(e);
		}
		return null;
	}

	@PostMapping(value = "/deleteCancion", produces = "application/json")
	@ResponseBody
	public Boolean deleteCancion(@RequestBody Object u, ModelMap model, HttpServletResponse response, BindingResult result){
		
		try {
			LinkedHashMap<String,String> lhm = (LinkedHashMap) u;
			
			String user = lhm.get("user");
			int id_a = Integer.parseInt(lhm.get("idalbum"));
			int id_c = Integer.parseInt(lhm.get("idcancion"));

			Boolean cancion = cancionService.deleteCancion(id_a, user, id_c);
			Boolean success = true; 

			Reproduccion[] playlists = repService.getPlaylistsContainsSong(id_c);
			keyCancion kc = new keyCancion(new keyLista(id_a, user), id_c);

			for(int i=0; playlists[i] != null && success; i++){
				keyLista kl = playlists[i].getIdRep();
				repService.deleteSongPlaylist(kl, kc);
			}

			return cancion && success;
		}catch(Exception e) {
			System.out.println(e);
		}
		return false;
	}

	
	
	@PostMapping(value = "/deleteAlbum", produces = "application/json")
	@ResponseBody
	public Boolean deleteAlbum(@RequestBody Object u, ModelMap model, HttpServletResponse response, BindingResult result){
		
		try {
			LinkedHashMap<String,String> lhm = (LinkedHashMap) u;
			
			String user = lhm.get("user");
			int id_a = Integer.parseInt(lhm.get("idalbum"));
			return albumService.deleteAlbum(id_a, user);
		}catch(Exception e) {
			System.out.println(e);
		}
		return false;
	}
	
	
	@PostMapping(value = "/deletePodcast", produces = "application/json")
	@ResponseBody
	public Boolean deletePodcast(@RequestBody Object u, ModelMap model, HttpServletResponse response, BindingResult result){
		
		try {
			LinkedHashMap<String,String> lhm = (LinkedHashMap) u;
			
			String user = lhm.get("user");
			int id_p = Integer.parseInt(lhm.get("idpodcast"));
			return podService.deletePodcast(id_p, user);
		}catch(Exception e) {
			System.out.println(e);
		}
		return false;
	}
	
	
	@PostMapping(value = "/deletePlaylist", produces = "application/json")
	@ResponseBody
	public Boolean deletePlaylist(@RequestBody Object u, ModelMap model, HttpServletResponse response, BindingResult result){
		
		try {
			LinkedHashMap<String,String> lhm = (LinkedHashMap) u;
			
			String user = lhm.get("user");
			int id_r = Integer.parseInt(lhm.get("idplaylist"));
			return repService.deleteReproduccion(id_r, user);
		}catch(Exception e) {
			System.out.println(e);
		}
		return false;
	}

	/*
	@PostMapping(value = "/subscribeUser", produces = "application/json")
	@ResponseBody
	public Boolean subscribeUser(@RequestBody Object u, ModelMap model, HttpServletResponse response, BindingResult result){
		
		try {
			LinkedHashMap<String,String> lhm = (LinkedHashMap) u;
			String user = lhm.get("user");
			String user_p = lhm.get("userpodcast");
			int id_p = Integer.parseInt(lhm.get("idpodcast"));
			
			keyLista kl = new keyLista(id_p, user_p);
			
			return usuarioService.subscribePodcast(user, kl);
		}catch(Exception e) {
			System.out.println(e);
		}
		return false;
	}

	@PostMapping(value = "/unsubscribeUser", produces = "application/json")
	@ResponseBody
	public Boolean unsubscribeUser(@RequestBody Object u, ModelMap model, HttpServletResponse response, BindingResult result){
		
		try {
			LinkedHashMap<String,String> lhm = (LinkedHashMap) u;
			String user = lhm.get("user");
			String user_p = lhm.get("userpodcast");
			int id_p = Integer.parseInt(lhm.get("idpodcast"));
			
			keyLista kl = new keyLista(id_p, user_p);
			
			return usuarioService.unsubscribePodcast(user, kl);
		}catch(Exception e) {
			System.out.println(e);
		}
		return false;
	}

	@PostMapping(value = "/checkSubscription", produces = "application/json")
	@ResponseBody
	public Boolean checkSubscription(@RequestBody Object u, ModelMap model, HttpServletResponse response, BindingResult result){
		
		try {
			LinkedHashMap<String,String> lhm = (LinkedHashMap) u;
			String user = lhm.get("user");
			String user_p = lhm.get("userpodcast");
			int id_p = Integer.parseInt(lhm.get("idpodcast"));
			
			keyLista kl = new keyLista(id_p, user_p);
			
			return usuarioService.checkSubscription(user, kl);
		}catch(Exception e) {
			System.out.println(e);
		}
		return false;
	}

	@PostMapping(value = "/listSubscriptions", produces = "application/json")
	@ResponseBody
	public Podcast[] listSubscriptions(@RequestBody Object u, ModelMap model, HttpServletResponse response, BindingResult result){
		
		try {
			String user = (String) u;
			
			return usuarioService.listSubscriptions(user);
		}catch(Exception e) {
			System.out.println(e);
		}
		return null;
	}

	@PostMapping(value = "/followPlayList", produces = "application/json")
	@ResponseBody
	public Boolean followPlayList(@RequestBody Object u, ModelMap model, HttpServletResponse response, BindingResult result) {
		try{
			LinkedHashMap<String,String> lhm = (LinkedHashMap) u;
			String user = lhm.get("correo");
			int id_r = Integer.parseInt(lhm.get("idplaylist"));
			return usuarioService.followPlaylist(user, id_r);
		}catch(Exception e){
			System.out.println(e.getMessage());
		}
		return null;
	}

	@PostMapping(value = "/unfollowPlayList", produces = "application/json")
	@ResponseBody
	public Boolean unfollowPlayList(@RequestBody Object u, ModelMap model, HttpServletResponse response, BindingResult result) {
		try{
			LinkedHashMap<String,String> lhm = (LinkedHashMap) u;
			String user = lhm.get("correo");
			int id_r = Integer.parseInt(lhm.get("idplaylist"));
			return usuarioService.unfollowPlaylist(user, id_r);
		}catch(Exception e){
			System.out.println(e.getMessage());
		}
		return null;
	}

	@PostMapping(value = "/checkFollow", produces = "application/json")
	@ResponseBody
	public Boolean checkFollow(@RequestBody Object u, ModelMap model, HttpServletResponse response, BindingResult result) {
		try{
			LinkedHashMap<String,String> lhm = (LinkedHashMap) u;
			String user = lhm.get("correo");
			int id_r = Integer.parseInt(lhm.get("idplaylist"));
			return usuarioService.checkFollow(user, id_r);
		}catch(Exception e){
			System.out.println(e.getMessage());
		}
		return null;
	}

	@PostMapping(value = "/listFollows", produces = "application/json")
	@ResponseBody
	public Reproduccion[] listFollows(@RequestBody Object u, ModelMap model, HttpServletResponse response, BindingResult result){
		
		try {
			String user = (String) u;
			
			return usuarioService.listFollows(user);
		}catch(Exception e) {
			System.out.println(e);
		}
		return null;
	}

	@PostMapping(value = "/likeSong", produces = "application/json")
	@ResponseBody
	public Boolean likeSong(@RequestBody Object u, ModelMap model, HttpServletResponse response, BindingResult result) {
		try{
			LinkedHashMap<String,String> lhm = (LinkedHashMap) u;
			String user = lhm.get("correo");
			int id_a = Integer.parseInt(lhm.get("idalbum"));
			int id_c = Integer.parseInt(lhm.get("idcancion"));
			return usuarioService.likeSong(id_a, user, id_c);
		}catch(Exception e){
			System.out.println(e.getMessage());
		}
		return null;
	}

	@PostMapping(value = "/unlikeSong", produces = "application/json")
	@ResponseBody
	public Boolean unlikeSong(@RequestBody Object u, ModelMap model, HttpServletResponse response, BindingResult result) {
		try{
			LinkedHashMap<String,String> lhm = (LinkedHashMap) u;
			String user = lhm.get("correo");
			int id_a = Integer.parseInt(lhm.get("idalbum"));
			int id_c = Integer.parseInt(lhm.get("idcancion"));
			return usuarioService.unlikeSong(id_a, user, id_c);
		}catch(Exception e){
			System.out.println(e.getMessage());
		}
		return null;
	}

	@PostMapping(value = "/checkLike", produces = "application/json")
	@ResponseBody
	public Boolean checkLike(@RequestBody Object u, ModelMap model, HttpServletResponse response, BindingResult result) {
		try{
			LinkedHashMap<String,String> lhm = (LinkedHashMap) u;
			String user = lhm.get("correo");
			int id_a = Integer.parseInt(lhm.get("idalbum"));
			int id_c = Integer.parseInt(lhm.get("idcancion"));
			return usuarioService.checkLike(user, id_c, id_a);
		}catch(Exception e){
			System.out.println(e.getMessage());
		}
		return null;
	}

	@PostMapping(value = "/listLikes", produces = "application/json")
	@ResponseBody
	public Cancion[] listLikes(@RequestBody Object u, ModelMap model, HttpServletResponse response, BindingResult result){
		
		try {
			String user = (String) u;
			
			return usuarioService.listLikes(user);
		}catch(Exception e) {
			System.out.println(e);
		}
		return null;
	}

	@PostMapping(value = "/listAlbumsLikes", produces = "application/json")
	@ResponseBody
	public Album[] listAlbumsLikes(@RequestBody Object u, ModelMap model, HttpServletResponse response, BindingResult result){
		
		try {
			String user = (String) u;
			
			return usuarioService.listAlbumsLikes(user);
		}catch(Exception e) {
			System.out.println(e);
		}
		return null;
	}

	@PostMapping(value = "/listUsersLikes", produces = "application/json")
	@ResponseBody
	public Usuario[] listUsersLikes(@RequestBody Object u, ModelMap model, HttpServletResponse response, BindingResult result){
		
		try {
			String user = (String) u;
			
			return usuarioService.listUsersLikes(user);
		}catch(Exception e) {
			System.out.println(e);
		}
		return null;
	}
	*/
	@PostMapping(value = "/deleteSongPlaylist", produces = "application/json")
	@ResponseBody
	public Boolean deleteSongPlaylist(@RequestBody Object u, ModelMap model, HttpServletResponse response, BindingResult result){
		
		try {
			LinkedHashMap<String,String> lhm = (LinkedHashMap) u;
			String user = lhm.get("user");
			String nombre = lhm.get("nombre");
			String user_c = lhm.get("usercancion");
			int id_p = Integer.parseInt(lhm.get("idplaylist"));
			int id_a = Integer.parseInt(lhm.get("idalbum"));
			int id_c = Integer.parseInt(lhm.get("idcancion"));
			
			keyLista kl = new keyLista(id_p,user);
			keyCancion kc = new keyCancion(id_a,user_c,id_c);
			
			return repService.deleteSongPlaylist(kl, kc);
		}catch(Exception e) {
			System.out.println(e);
		}
		return false;
	}
//////////////////////////////////	
	
	
	@PostMapping(value = "/followPlayList", produces = "application/json")
	@ResponseBody
	public Boolean followPlayList(@RequestBody Object u, ModelMap model, HttpServletResponse response, BindingResult result) {
		try{
			LinkedHashMap<String,String> lhm = (LinkedHashMap) u;
			String user = lhm.get("correo");
			int id_r = Integer.parseInt(lhm.get("idplaylist"));
			String correo_r = lhm.get("correoplaylist");
			Reproduccion r = repService.getReproduccion(id_r, correo_r);
			Usuario usuario = usuarioService.getUser(user);
			return usuarioService.followPlaylist(user, r) && repService.followByUser(usuario,r);
		}catch(Exception e){
			System.out.println(e.getMessage());
		}
		return false;
	}
	
	@PostMapping(value = "/unfollowPlayList", produces = "application/json")
	@ResponseBody
	public Boolean unfollowPlayList(@RequestBody Object u, ModelMap model, HttpServletResponse response, BindingResult result) {
		try{
			LinkedHashMap<String,String> lhm = (LinkedHashMap) u;
			String user = lhm.get("correo");
			int id_r = Integer.parseInt(lhm.get("idplaylist"));
			String correo_r = lhm.get("correoplaylist");
			Reproduccion r = repService.getReproduccion(id_r, correo_r);
			Usuario usuario = usuarioService.getUser(user);
			return usuarioService.unFollowPlaylist(user, r) && repService.unFollowByUser(usuario,r);
		}catch(Exception e){
			System.out.println(e.getMessage());
		}
		return false;
	}
	
	
	@PostMapping(value = "/likeSong", produces = "application/json")
	@ResponseBody
	public Boolean likeSong(@RequestBody Object u, ModelMap model, HttpServletResponse response, BindingResult result) {
		try{
			LinkedHashMap<String,String> lhm = (LinkedHashMap) u;
			String user = lhm.get("correo");
			int id_a = Integer.parseInt(lhm.get("idalbum"));
			String correo_album = lhm.get("correoalbum");
			int id_c = Integer.parseInt(lhm.get("idcancion"));
			Usuario usuario = usuarioService.getUser(user);
			Cancion c = cancionService.getCancion(id_a, correo_album, id_c);
			return usuarioService.likeSong(usuario, c);
		}catch(Exception e){
			System.out.println(e.getMessage());
		}
		return false;
	}
	
	@PostMapping(value = "/unlikeSong", produces = "application/json")
	@ResponseBody
	public Boolean unlikeSong(@RequestBody Object u, ModelMap model, HttpServletResponse response, BindingResult result) {
		try{
			LinkedHashMap<String,String> lhm = (LinkedHashMap) u;
			String user = lhm.get("correo");
			int id_a = Integer.parseInt(lhm.get("idalbum"));
			String correo_album = lhm.get("correoalbum");
			int id_c = Integer.parseInt(lhm.get("idcancion"));
			Usuario usuario = usuarioService.getUser(user);
			Cancion c = cancionService.getCancion(id_a, correo_album, id_c);
			return usuarioService.likeSong(usuario, c);
		}catch(Exception e){
			System.out.println(e.getMessage());
		}
		return false;
	}
	

	
	@PostMapping(value = "/uploadSong", produces = "application/json")
	@ResponseBody
	public Boolean uploadSong(@RequestParam("file") MultipartFile file, ModelMap model, HttpServletResponse response){
		System.out.println("Entro en uploadSong");
		try {
			/*if(!f.getContentType().equals("MIME_AUDIO_MPEG")) {
				throw new Exception("No es un archivo de audio");
			}
			*/
			System.out.println(file.getContentType());
			System.out.println(file.getName());
			System.out.println(file.getOriginalFilename());
			System.out.println(file.getSize());
			System.out.println(file.getClass());
			System.out.println(file.getResource());
			byte[] b = file.getBytes();

  		System.out.println("Bytes antes:");
			System.out.println(b.length);
			/*
			byte[] b_compressed = compressBytes(b);
			
			System.out.println("Bytes despues:");
			System.out.println(b_compressed.length);
			System.out.println("Bytes uncompressed:");
			System.out.println(decompressBytes(b_compressed).length);
			*/
			/*
			System.out.println("He pillado los bytes");
      */
			keyLista kLa = new keyLista(1,"usuario");
			Cancion c = new Cancion(kLa, 1, "nombre", "genero", null); 	// Se crea el objeto con un 1 como id_cancion temporalmente, 
			System.out.println("He construido la nueva cancion");							// se actualiza en el metodo repository.createCancion()
			if(!cancionService.createCancion( kLa, c )) {
				throw new Exception("No se ha podido guardar la cancion");
			}
      
			System.out.println("He guardado la cancion");
			/*
			String directory = Paths.get("").toAbsolutePath().toString();
			
			System.out.println("Directory: " + directory);
			String path = directory + "\\src\\main\\resources\\files\\";
			String songName = c.getNombre() + c.getIdCancion().getL_id().getU() + ".mp3";
			
			FileOutputStream fos = new FileOutputStream(path + songName);
			fos.write(b);
			*/
			return true;
		} catch (Exception e) {
			System.out.println(e.getMessage());
			e.printStackTrace();
		}
		return false;
	}
	
	
	/*
	@PostMapping(value = "/subirCancion", produces = "application/json")
	@ResponseBody
	public keyCancion subirCancion(@RequestParam("file") MultipartFile file, @RequestParam("idalbum") String id, @RequestParam("user") String user, 
								@RequestParam("nombreC") String nombre, ModelMap model, HttpServletResponse response){
		
		try {
			int id_a = Integer.parseInt(id);
			keyLista kl = new keyLista(id_a,user);
			Cancion c = new Cancion(kl, -1, nombre, "genero", null); 	// Se crea el objeto con un 1 como id_cancion temporalmente, 
			System.out.println("He construido la nueva cancion");							// se actualiza en el metodo repository.createCancion()
			if(!cancionService.createCancion( kl, c )) {
				throw new Exception("No se ha podido guardar la cancion");
			}
			
			String directory = Paths.get("").toAbsolutePath().toString();
			
			//String path = directory + "/src/main/resources/files/";
			//String path = "\\app\\app\\src\\main\\resources\\static\\assets\\";
			//String songsPath = directory + "/src/main/resources/static/assets/";
			//String path = songsPath;
			// Id Cancion + Id Album + Id Usuario
			System.out.println("Es del tipo: " + file.getContentType());
			String path = directory + "/src/main/resources/static/assets/";
			String songName = String.valueOf(c.getIdCancion().getC_id()) + String.valueOf(c.getIdCancion().getL_id().getL_id()) + 
								c.getIdCancion().getL_id().getU() + ".mp3";
			
			FileOutputStream fos = new FileOutputStream(path + songName);
			fos.write(file.getBytes());
			fos.close();
			
			return c.getIdCancion();
		}catch(Exception e) {
			System.out.println(e);
		}
		return null;
	}
	*/
	
	@PostMapping(value = "/subirCancion", produces = "application/json")
	@ResponseBody
	public keyCancion subirCancion(@RequestParam("file") MultipartFile file, @RequestParam("idalbum") String id, @RequestParam("user") String user, 
								@RequestParam("nombreC") String nombre, ModelMap model, HttpServletResponse response){
		
		try {
			int id_a = Integer.parseInt(id);
			keyLista kl = new keyLista(id_a,user);
			Cancion c = new Cancion(kl, -1, nombre, "genero", null); 	// Se crea el objeto con un 1 como id_cancion temporalmente, 
			System.out.println("He construido la nueva cancion");							// se actualiza en el metodo repository.createCancion()
			if(!cancionService.createCancion( kl, c )) {
				throw new Exception("No se ha podido guardar la cancion");
			}
			
			String directory = Paths.get("").toAbsolutePath().toString();
			System.out.println("Directorio de Trabajoooooo: " + System.getProperty("user.dir"));
			String prueba = System.getProperty("user.dir") + "/src/main/resources/static/assets/";
			// Id Cancion + Id Album + Id Usuario
			String path = "./src/main/resources/static/assets/";
			String songName = String.valueOf(c.getIdCancion().getC_id()) + String.valueOf(c.getIdCancion().getL_id().getL_id()) + 
								c.getIdCancion().getL_id().getU() + ".mp3";
			
			FileOutputStream fos = new FileOutputStream(path + songName);
			fos.write(file.getBytes());
			fos.close();
			File f = new File(path + songName);
			if(!f.exists()) {
				System.out.println("No existeeeeeeee!!!");
			}
			songName = "Cancion?idsong=" + songName;
			c.setMp3(songName);
			return c.getIdCancion();
		}catch(Exception e) {
			System.out.println(e);
		}
		return null;
	}
	
	
	@PostMapping(value = "/subirCapitulo", produces = "application/json")
	@ResponseBody
	public keyCancion subirCapitulo(@RequestParam("file") MultipartFile file, @RequestParam("idalbum") String id, @RequestParam("user") String user, 
								@RequestParam("nombreC") String nombre, ModelMap model, HttpServletResponse response){
		
		try {
			int id_a = Integer.parseInt(id);
			keyLista kl = new keyLista(id_a,user);
			Capitulo c = new Capitulo(kl, -1, nombre, "genero", null); 	// Se crea el objeto con un 1 como id_cancion temporalmente, 
			System.out.println("He construido la nueva cancion");							// se actualiza en el metodo repository.createCancion()
			if(!capService.createCapitulo( kl, c )) {
				throw new Exception("No se ha podido guardar el capitulo");
			}
			
			String directory = Paths.get("").toAbsolutePath().toString();
			System.out.println("Directorio de Trabajoooooo: " + System.getProperty("user.dir"));
			String prueba = System.getProperty("user.dir") + "/src/main/resources/static/assets/";
			// Id Cancion + Id Album + Id Usuario
			String path = "./src/main/resources/static/assets/";
			String songName = "capitulo_" + String.valueOf(c.getIdCapitulo().getC_id()) + String.valueOf(c.getIdCapitulo().getL_id().getL_id()) + 
								c.getIdCapitulo().getL_id().getU() + ".mp3";
			
			FileOutputStream fos = new FileOutputStream(path + songName);
			fos.write(file.getBytes());
			fos.close();
			File f = new File(path + songName);
			if(!f.exists()) {
				System.out.println("No existeeeeeeee!!!");
			}
			return c.getIdCapitulo();
		}catch(Exception e) {
			System.out.println(e);
		}
		return null;
	}
	
	/*
	@PostMapping(value = "/URLCancion", produces = "application/json")
	@ResponseBody
	public File URLCancion(@RequestParam("idalbum") String id, @RequestParam("user") String user, 
								@RequestParam("idcancion") String idc, ModelMap model, HttpServletResponse response){
		
		try {
			String directory = Paths.get("").toAbsolutePath().toString();
			System.out.println("Directorio: " + directory);
			String songsPath = "/src/main/resources/static/assets/";
			//String path = directory.concat(songsPath);
			//String path = "\\app\\app\\src\\main\\resources\\static\\assets\\";
			// Id Cancion + Id Album + Id Usuario
			String songName = idc + id + user + ".mp3";
			String path = directory + "/src/main/resources/static/assets/";
			
			File f = new File(path + songName);
			
			System.out.println("Path: " + f.getPath());
			
			if(!f.exists()) {
				throw new Exception("La cancion no existe");
			}
			System.out.println("URL: " + f.toURI().toURL().toString());
			System.out.println("URI: " + f.toURI().toString());
			System.out.println("getAbsolutePath: " + f.getAbsolutePath());
			System.out.println("getCanonicalPath: " + f.getCanonicalPath());
			System.out.println("getPath: " + f.getPath());
			f.setReadable(true);
			return f;
		}catch(Exception e) {
			System.out.println(e);
		}
		return null;
	}
	*/
	
	@PostMapping(value = "/URLCancion", produces = "application/json")
	@ResponseBody
	public String URLCancion(@RequestParam("idalbum") String id, @RequestParam("user") String user, 
								@RequestParam("idcancion") String idc, ModelMap model, HttpServletResponse response){
		
		try {
			String directory = Paths.get("").toAbsolutePath().toString();
			System.out.println("Directorio: " + directory);
			String songsPath = "/src/main/resources/static/assets/";
			//String path = directory.concat(songsPath);
			//String path = "\\app\\app\\src\\main\\resources\\static\\assets\\";
			// Id Cancion + Id Album + Id Usuario
			String songName = "Cancion?idsong=";
			songName = songName + idc + id + user + ".mp3";
			System.out.println("songName: " + songName);
			return songName;
		}catch(Exception e) {
			System.out.println(e);
		}
		return null;
	}

	@PostMapping(value = "/URLFotoUsuario", produces = "application/json")
	@ResponseBody
	public String URLFotoUsuario(@RequestBody Object u, ModelMap model, HttpServletResponse response){
		
		try {

			String user = (String) u;
			Usuario usuario = usuarioService.getUser(user);

			return usuario.getURLFoto();
		}catch(Exception e) {
			System.out.println(e);
		}
		return null;
	}

	@PostMapping(value = "/URLFotoAlbum", produces = "application/json")
	@ResponseBody
	public String URLFotoAlbum(@RequestBody Object u, ModelMap model, HttpServletResponse response){
		
		try {

			LinkedHashMap<String,Object> lhm = (LinkedHashMap) u;
			
			int i = (int) lhm.get("list_id");
			String s = (String) lhm.get("email");
			
			Album album = albumService.getAlbum(i, s);

			return album.getURLFoto();
		}catch(Exception e) {
			System.out.println(e);
		}
		return null;
	}

	@PostMapping(value = "/URLFotoPodcast", produces = "application/json")
	@ResponseBody
	public String URLFotoPodcast(@RequestBody Object u, ModelMap model, HttpServletResponse response){
		
		try {

			LinkedHashMap<String,Object> lhm = (LinkedHashMap) u;
			
			int i = (int) lhm.get("list_id");
			String s = (String) lhm.get("email");
			
			Podcast podcast = podService.getPodcast(i, s);

			return podcast.getURLFoto();
		}catch(Exception e) {
			System.out.println(e);
		}
		return null;
	}

	@PostMapping(value = "/URLFotoPlaylist", produces = "application/json")
	@ResponseBody
	public String URLFotoPlaylist(@RequestBody Object u, ModelMap model, HttpServletResponse response){
		
		try {

			LinkedHashMap<String,Object> lhm = (LinkedHashMap) u;
			
			int i = (int) lhm.get("list_id");
			String s = (String) lhm.get("email");
			
			Reproduccion playlist = repService.getReproduccion(i, s);

			return playlist.getURLFoto();
		}catch(Exception e) {
			System.out.println(e);
		}
		return null;
	}
	
	@GetMapping(value = "/Cancion", produces = MediaType.APPLICATION_OCTET_STREAM_VALUE)
	@ResponseBody
	public ResponseEntity requestSong(@RequestParam("idsong") String song, ModelMap model, HttpServletResponse response) throws IOException{
		
		try {
			System.out.println("Cancion con id: " + song);
			String path = System.getProperty("user.dir") + "/src/main/resources/static/assets/";
			
			InputStreamResource isr = new InputStreamResource(new FileInputStream(path + song));
			
			return new ResponseEntity(isr, HttpStatus.OK);
		}catch(Exception e) {
			System.out.println(e);
		}
		return new ResponseEntity(null, HttpStatus.BAD_REQUEST);
	}

	@GetMapping(value = "/Image", produces = MediaType.APPLICATION_OCTET_STREAM_VALUE)
	@ResponseBody
	public ResponseEntity requestFoto(@RequestParam("idfoto") String foto, ModelMap model, HttpServletResponse response) throws IOException{
		
		try {
			System.out.println("Foto con id: " + foto);
			String path = System.getProperty("user.dir") + "/src/main/resources/static/assets/images/";
			
			InputStreamResource isr = new InputStreamResource(new FileInputStream(path + foto));
			
			return new ResponseEntity(isr, HttpStatus.OK);
		}catch(Exception e) {
			System.out.println(e);
		}
		return new ResponseEntity(null, HttpStatus.BAD_REQUEST);
	}
	
	@PostMapping(value = "/recoverEmail", produces = "application/json")
	@ResponseBody
	public Boolean recoverEmail(@RequestBody String u, ModelMap model, HttpServletResponse response){
		try {
			String to = u;
			
			int leftLimit = 97; // letter 'a'
	        int rightLimit = 122; // letter 'z'
	        int targetStringLength = 10;
	        Random random = new Random();
	     
	        String generatedString = random.ints(leftLimit, rightLimit + 1)
	          .limit(targetStringLength)
	          .collect(StringBuilder::new, StringBuilder::appendCodePoint, StringBuilder::append)
	          .toString();
	        
	        System.out.println("Cambiando la contraseña a: " + generatedString);
	        Usuario user = usuarioService.getUser(to);
	        
			
			//String to = "email@gmail.com";//change accordingly
			
			String subject = "ListenIt Recuperación de la Contraseña";
			String text = "Este correo se te ha enviado porque has pedido la recuperación de la contraseña, tu nueva contraseña generada aleatoriamente es la siguiente: ";
			text = text + generatedString;
	        SimpleMailMessage message = new SimpleMailMessage(); 
	        message.setTo(to); 
	        message.setSubject(subject); 
	        message.setText(text);
	        emailSender.send(message);
	        System.out.println("message sent successfully....");  
	        user.setPass(generatedString);
	        
	       
	        return true;
		}catch(Exception e) {
			System.out.println(e);
		}
		return false;
	}
	
	@PostMapping(value = "/uploadSongs")
	@ResponseBody
	public boolean uploadSongs(@RequestBody MultipartFile[] f, ModelMap model, HttpServletResponse response, BindingResult result){
		try {
			for(int i = 0; i < f.length; i++) {
				MultipartFile f_ = f[i];
				if(!f_.getContentType().equals("MIME_AUDIO_MPEG")) {
					throw new Exception("No es un archivo de audio");
				}
				
				byte[] b = f_.getBytes();
				
				keyLista kLa = new keyLista(1,"usuario");
				Cancion c = new Cancion(kLa, 1, "nombre", "genero", null);
				
				if(!cancionService.createCancion(kLa, c)) {
					throw new Exception("No se ha podido guardar la cancion");
				}
			}
			
			return true;
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
		return false;
	}
	
	@GetMapping(value = "/getSong",  produces = "application/json")
	public URL getSong(ModelMap model, HttpServletResponse response){
		URL url = null;
		try {
			
			Cancion c = cancionService.getCancion(1, "usuario", 1);
			
			String directory = Paths.get("").toAbsolutePath().toString();
			
			System.out.println("Directory: " + directory);
			String path = directory + "\\src\\main\\resources\\files\\";
			String songName = c.getNombre() + c.getIdCancion().getL_id().getU() + ".mp3";
			
			FileOutputStream fos = new FileOutputStream(directory + songName);
			//fos.write(c.getMp3());
			File f = new File(directory + songName);
			url = f.toURI().toURL();
			String u;
			return url;
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
		return url;
	}

	@PostMapping(value = "/getAlbumsByUser", produces = "application/json")
	@ResponseBody
	public Album[] getAlbumsByUser(@RequestBody Object u, ModelMap model, HttpServletResponse response, BindingResult result){
		try {
			//LinkedHashMap<String,String> lhm = (LinkedHashMap) u;
			
			//String user = lhm.get("correo");
			String user = (String) u;
			System.out.println("Correo" + user);
			
			return albumService.getAlbumsByUser(user);
			
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
		return null;
	}

	@PostMapping(value = "/getPodcastsByUser", produces = "application/json")
	@ResponseBody
	public Podcast[] getPodcastsByUser(@RequestBody Object u, ModelMap model, HttpServletResponse response, BindingResult result){
		try {
			//LinkedHashMap<String,String> lhm = (LinkedHashMap) u;
			
			String user = (String) u;
			
			return podService.getPodcastByUser(user);
			
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
		return null;
	}

	@PostMapping(value = "/getPlaylistByUser", produces = "application/json")
	@ResponseBody
	public Reproduccion[] getPlaylistByUser(@RequestBody Object u, ModelMap model, HttpServletResponse response, BindingResult result){
		try {
			//LinkedHashMap<String,String> lhm = (LinkedHashMap) u;
			
			//String user = lhm.get("user");
			String user = (String) u;
			
			return repService.getPlaylistByUser(user);
			
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
		return null;
	}
	
	//Métodos de búsqueda:
	
	@PostMapping(value = "/searchAlbum", produces = "application/json")
	@ResponseBody
	public Album[] searchAlbum(@RequestBody String s, ModelMap model, HttpServletResponse response, BindingResult result) {
		try {
			String[] divide = s.split("\"");
			if(divide.length > 1) {
				s = divide[1];
			}else {
				s = divide[0];
			}
			return albumService.getAlbumsBySearch(s);
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
		return null;
	}
	
	@PostMapping(value = "/searchUser", produces = "application/json")
	@ResponseBody
	public Usuario[] searchUser(@RequestBody String s, ModelMap model, HttpServletResponse response, BindingResult result) {
		try {
			String[] divide = s.split("\"");
			if(divide.length > 1) {
				s = divide[1];
			}else {
				s = divide[0];
			}
			
			return usuarioService.getUsersBySearch(s);
			
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
		return null;
	}
	
	@PostMapping(value = "/searchPlaylist", produces = "application/json")
	@ResponseBody
	public Reproduccion[] searchPlaylist(@RequestBody String s, ModelMap model, HttpServletResponse response, BindingResult result) {
		try {
			String[] divide = s.split("\"");
			if(divide.length > 1) {
				s = divide[1];
			}else {
				s = divide[0];
			}
			return repService.getPlaylistsBySearch(s);
			
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
		return null;
	}
	
	@PostMapping(value = "/searchPodcast", produces = "application/json")
	@ResponseBody
	public Podcast[] searchPodcast(@RequestBody String s, ModelMap model, HttpServletResponse response, BindingResult result) {
		try {
			String[] divide = s.split("\"");
			if(divide.length > 1) {
				s = divide[1];
			}else {
				s = divide[0];
			}
			return podService.getPodcastsBySearch(s);
			
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
		return null;
	}
	
	
	@PostMapping(value = "/searchSong", produces = "application/json")
	@ResponseBody
	public Cancion[] searchSong(@RequestBody String s, ModelMap model, HttpServletResponse response, BindingResult result) {
		try {
			String[] divide = s.split("\"");
			if(divide.length > 1) {
				s = divide[1];
			}else {
				s = divide[0];
			}
			return cancionService.getSongsBySearch(s);
			
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
		return null;
	}


	
}
