package com.music.backend.controller;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URI;
import java.net.URL;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.util.Optional;

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
			
			if(!cancionService.createCancion(c)) {
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
			if(!usuarioService.deleteUser(u.getCorreo())) {
				throw new Exception("DELETE usuario mal hecho");
			}
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
	public Usuario login(@RequestParam Object u, ModelMap model, HttpServletResponse response, BindingResult result){

		try {
			Usuario user = (Usuario) u;
			return usuarioService.loginUser(user.getCorreo(), user.getPass());
		}catch(Exception e) {
			System.out.println(e);
		}
		return new Usuario();
	}
	/*
		Método Register:
		/registerUser
		Parámetro: Usuario
		Devuelve true si se ha podido hacer el registro, sino false
	*/
	@PostMapping(value = "/registerUser", produces = "application/json", consumes = "application/json")
	@ResponseBody
	public Boolean register(@RequestParam Object u, ModelMap model, HttpServletResponse response, BindingResult result){

		try {
			Usuario user = (Usuario) u;
			return usuarioService.createUser(user);
		}catch(Exception e) {
			System.out.println(e);
		}
		return false;
	}
	
	
	@PostMapping(value = "/pruebaReact", produces = "application/json", consumes = "application/json")
	@ResponseBody
	public void pruebaReact(@RequestParam Object u, ModelMap model, HttpServletResponse response, BindingResult result){

		try {
			Usuario user = (Usuario) u;
			System.out.println("Object");
			System.out.println(u);
			System.out.println("Cast a Usuario");
			System.out.println(user.toString());
		}catch(Exception e) {
			System.out.println(e);
		}
	}
	
	
	
	
	
	@PostMapping(value = "/uploadSong")
	@ResponseBody
	public boolean uploadSong(@RequestBody MultipartFile f, ModelMap model, HttpServletResponse response, BindingResult result){
		try {
			if(!f.getContentType().equals("MIME_AUDIO_MPEG")) {
				throw new Exception("No es un archivo de audio");
			}
			
			byte[] b = f.getBytes();
			
			Cancion c = new Cancion(new keyLista(1,"usuario"), 1, "nombre", "genero", b);
			
			if(!cancionService.createCancion(c)) {
				throw new Exception("No se ha podido guardar la cancion");
			}
			
			return true;
		} catch (Exception e) {
			System.out.println(e.getMessage());
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
				
				Cancion c = new Cancion(new keyLista(1,"usuario"), 1, "nombre", "genero", b);
				
				if(!cancionService.createCancion(c)) {
					throw new Exception("No se ha podido guardar la cancion");
				}
			}
			
			return true;
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
		return false;
	}
	
	@PostMapping(value = "/getSong")
	@ResponseBody
	public URL getSong(@RequestBody String u, ModelMap model, HttpServletResponse response, BindingResult result){
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
			return url;
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
		return url;
	}
	
	
}
