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
	public Boolean createAlbum(@RequestBody Object u, ModelMap model, HttpServletResponse response, BindingResult result){
		
		try {
			LinkedHashMap<String,String> lhm = (LinkedHashMap) u;
			
			String e = lhm.get("email");
			String n = lhm.get("name");
			String d = lhm.get("date");
			
			albumService.createAlbum(e, n, d);
			return true;
		}catch(Exception e) {
			System.out.println("Excepcion en createAlbum");
			System.out.println(e);
		}
		return false;
	}
	
	
	@PostMapping(value = "/uploadSong", produces = "application/json")
	@ResponseBody
	public boolean uploadSong(@RequestParam MultipartFile file, ModelMap model, HttpServletResponse response){
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
				
				if(!cancionService.createCancion(kLa, c) {
					throw new Exception("No se ha podido guardar la cancion");
				}
			}
			
			return true;
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
		return false;
	}
	
	@GetMapping(value = "/getSong")
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
			return url;
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
		return url;
	}
	
	public byte[] comprimir(byte[] b) {
		
	try
    {
      ByteArrayOutputStream byteStream =
        new ByteArrayOutputStream(b.length);
      try
      {
        GZIPOutputStream zipStream = new GZIPOutputStream(byteStream);
        try
        {
          zipStream.write(b);
        }
        finally
        {
          zipStream.close();
        }
      }
      finally
      {
        byteStream.close();
      }

      byte[] compressedData = byteStream.toByteArray();
      
      System.out.println("Comprimido con lo que yo he hecho: " + compressedData.length);
      return compressedData;
    }
    catch(Exception e)
    {
      e.printStackTrace();
    }
	return null;
  }
	
	
	// compress the image bytes before storing it in the database
	public static byte[] compressBytes(byte[] data) {
		Deflater deflater = new Deflater();
	    deflater.setInput(data);
	    deflater.finish();
	    ByteArrayOutputStream outputStream = new ByteArrayOutputStream(data.length);
	    byte[] buffer = new byte[1024];
	    while (!deflater.finished()) {
	    	int count = deflater.deflate(buffer);
	        outputStream.write(buffer, 0, count);
	    }
	    try {
	    	outputStream.close();
	    } catch (IOException e) {
	    }
	    System.out.println("Compressed Image Byte Size - " + outputStream.toByteArray().length);
	    return outputStream.toByteArray();
	}
	    // uncompress the image bytes before returning it to the angular application
	public static byte[] decompressBytes(byte[] data) {
	    Inflater inflater = new Inflater();
	    inflater.setInput(data);
	    ByteArrayOutputStream outputStream = new ByteArrayOutputStream(data.length);
	    byte[] buffer = new byte[1024];
	    try {
	    	while (!inflater.finished()) {
	    		int count = inflater.inflate(buffer);
	            outputStream.write(buffer, 0, count);
	        }
	            outputStream.close();
	     } catch (IOException ioe) {
	     } catch (DataFormatException e) {
	     }
	     return outputStream.toByteArray();
	}
}
