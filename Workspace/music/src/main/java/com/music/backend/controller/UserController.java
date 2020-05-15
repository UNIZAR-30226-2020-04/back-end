package com.music.backend.controller;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.net.URI;
import java.net.URL;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Optional;
import java.util.zip.DataFormatException;
import java.util.zip.Deflater;
import java.util.zip.GZIPOutputStream;
import java.util.zip.Inflater;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

import org.apache.commons.io.FileUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

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
			
			if(!podService.createPodcast(p)) {
				throw new Exception("INSERT podcast mal hecho");
			}
			
			//Album
			keyLista kLa = new keyLista(3,correo);
			
			Album a = new Album(kLa, "Album", null, "fechaPublicacion");
			
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
	@PostMapping(value = "/registerUser", produces = "application/json", consumes = "application/json")
	@ResponseBody
	public Boolean register(@RequestBody Object u, ModelMap model, HttpServletResponse response, BindingResult result){

		try {
			LinkedHashMap<String,String> lhm = (LinkedHashMap) u;
			String name = lhm.get("name");
			String surname = lhm.get("surname");
			String username = lhm.get("username");
			String email = lhm.get("email");
			String password = lhm.get("password");
			String dateOfBirth = lhm.get("dateOfBirth");
			
			System.out.println("name: " + name);
			System.out.println("surname: " + surname);
			System.out.println("username: " + username);
			System.out.println("email: " + email);
			System.out.println("password: " + password);
			System.out.println("dateOfBirth: " + dateOfBirth);
		    
		    Usuario user = new Usuario(email, name, null, password, username, dateOfBirth);
		    System.out.println(user.toString());
			return usuarioService.createUser(user);
		}catch(Exception e) {
			System.out.println(e);
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
	public keyLista createAlbum(@RequestBody Object u, ModelMap model, HttpServletResponse response, BindingResult result){
		
		try {
			LinkedHashMap<String,String> lhm = (LinkedHashMap) u;
			
			String e = lhm.get("email");
			String n = lhm.get("name");
			
			return albumService.createAlbum(e, n);
		}catch(Exception e) {
			System.out.println(e);
		}
		return null;
	}
	
	@PostMapping(value = "/createPlaylist", produces = "application/json")
	@ResponseBody
	public Boolean createPlaylist(@RequestBody Object u, ModelMap model, HttpServletResponse response, BindingResult result){
		
		try {
			LinkedHashMap<String,String> lhm = (LinkedHashMap) u;
			String user = lhm.get("user");
			String p = lhm.get("playlist");
			
			System.out.println("u: " + u);
			System.out.println("p: " + p);
			
			Reproduccion r = new Reproduccion();
			keyLista kl = new keyLista(-1,user);
			r.setIdRep(kl);
			r.setNombre(p);
			return repService.createReproduccion(r);
		}catch(Exception e) {
			System.out.println(e);
		}
		return false;
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
	
	@PostMapping(value = "/suscribeUser", produces = "application/json")
	@ResponseBody
	public Boolean suscribeUser(@RequestBody Object u,
				ModelMap model, HttpServletResponse response, BindingResult result){
		
		try {
			LinkedHashMap<String,String> lhm = (LinkedHashMap) u;
			String user = lhm.get("user");
			String user_p = lhm.get("userpodcast");
			int id_p = Integer.parseInt(lhm.get("idpodcast"));
			
			keyLista kl = new keyLista(id_p, user_p);
			
			return usuarioService.suscribe(user, kl);
		}catch(Exception e) {
			System.out.println(e);
		}
		return false;
	}
	
	@PostMapping(value = "/createPodcast", produces = "application/json")
	@ResponseBody
	public Boolean createPodcast(@RequestParam("user") String u, @RequestParam("nombre") String n, @RequestParam("nombrecap") String nC,
			@RequestParam("file") MultipartFile f, ModelMap model, HttpServletResponse response, BindingResult result){
		
		try {
			Podcast p = new Podcast(-1, u, n, null, null);
			if(!podService.createPodcast(p)) {
				throw new Exception("No se ha podido crear el Podcast");
			}
			
			Cancion c = new Cancion(p.getIdPodcast(), -1, nC, "CapituloPodcast", f.getBytes()) ;
			if(!cancionService.createCancion(p.getIdPodcast(),c)) {
				throw new Exception("No se ha podido crear el capitulo del Podcast");
			}
			
			return true;
		}catch(Exception e) {
			System.out.println(e);
		}
		return false;
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
	public Cancion[] listPodcast(@RequestBody Object u, ModelMap model, HttpServletResponse response, BindingResult result){
		
		try {
			LinkedHashMap<String,String> lhm = (LinkedHashMap) u;
			
			String user = lhm.get("user");
			int id_p = Integer.parseInt(lhm.get("idpodcast"));
			
			return podService.listPodcast(id_p, user);
		}catch(Exception e) {
			System.out.println(e);
		}
		return null;
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
	
	
	@PostMapping(value = "/uploadSong", produces = "application/json")
	@ResponseBody
	public boolean uploadSong(@RequestParam("file") MultipartFile file, ModelMap model, HttpServletResponse response){
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
			Cancion c = new Cancion(kLa, 1, "nombre", "genero", b); 	// Se crea el objeto con un 1 como id_cancion temporalmente, 
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
	
	
	@PostMapping(value = "/subirCancion", produces = "application/json")
	@ResponseBody
	public keyCancion subirCancion(@RequestParam("file") MultipartFile file, @RequestParam("idalbum") String id, @RequestParam("user") String user, 
								@RequestParam("nombreC") String nombre, ModelMap model, HttpServletResponse response){
		
		try {
			int id_a = Integer.parseInt(id);
			keyLista kl = new keyLista(id_a,user);
			Cancion c = new Cancion(kl, -1, nombre, "genero", file.getBytes()); 	// Se crea el objeto con un 1 como id_cancion temporalmente, 
			System.out.println("He construido la nueva cancion");							// se actualiza en el metodo repository.createCancion()
			if(!cancionService.createCancion( kl, c )) {
				throw new Exception("No se ha podido guardar la cancion");
			}
			
			String directory = Paths.get("").toAbsolutePath().toString();
			
			String path = directory + "\\src\\main\\resources\\files\\";
			// Id Cancion + Id Album + Id Usuario
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
	
	
	@PostMapping(value = "/URLCancion", produces = "application/json")
	@ResponseBody
	public String URLCancion(@RequestParam("idalbum") String id, @RequestParam("user") String user, 
								@RequestParam("idcancion") String idc, ModelMap model, HttpServletResponse response){
		
		try {
			String directory = Paths.get("").toAbsolutePath().toString();
			
			String path = directory + "\\src\\main\\resources\\files\\";
			// Id Cancion + Id Album + Id Usuario
			String songName = idc + id + user + ".mp3";
			
			File f = new File(path + songName);
			
			System.out.println("Path: " + f.getPath());
			
			if(!f.exists()) {
				throw new Exception("La cancion no existe");
			}
			
			return f.toURI().toURL().toString();
		}catch(Exception e) {
			System.out.println(e);
		}
		return null;
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
				Cancion c = new Cancion(kLa, 1, "nombre", "genero", b);
				
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
			fos.write(c.getMp3());
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
			LinkedHashMap<String,String> lhm = (LinkedHashMap) u;
			
			String user = lhm.get("user");
			
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
			LinkedHashMap<String,String> lhm = (LinkedHashMap) u;
			
			String user = lhm.get("user");
			
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
			LinkedHashMap<String,String> lhm = (LinkedHashMap) u;
			
			String user = lhm.get("user");
			
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
