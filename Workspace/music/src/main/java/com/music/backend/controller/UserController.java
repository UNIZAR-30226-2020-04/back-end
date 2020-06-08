package com.music.backend.controller;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.util.LinkedHashMap;
import java.util.Random;

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
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.music.backend.entity.*;
import com.music.backend.service.*;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiImplicitParams;

@RestController 
@Api(value = "listenit", description = "API creada para el funcionamento de la web ListenIt")
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
	
	
	@ApiOperation(value = "Operación para iniciar sesión", response = Usuario.class)
	@ApiImplicitParams({@ApiImplicitParam(name = "email", value = "Correo del usuario"),
							@ApiImplicitParam(name = "password", value = "Contraseña del usuario")
							})
	@PostMapping(value = "/loginUser", produces = "application/json", consumes = "application/json")
	@ResponseBody
	public ResponseEntity<Usuario> login(@RequestBody Object u, HttpServletResponse response, BindingResult result){

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
	
	
	@PostMapping(value = "/registerUser", produces = "application/json")
	@ResponseBody
	public Usuario register(@RequestParam("name") String name, @RequestParam("surname") String surname,
			@RequestParam("username") String username, @RequestParam("email") String email,
			@RequestParam("password") String password, @RequestParam("dateOfBirth") String dateOfBirth, 
			@RequestParam(name = "foto", required = false) MultipartFile f , HttpServletResponse response){
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
		    usuarioService.createUser(user);
		    
		    //Creación de la Playlist de "Me gusta"
		    /*
		    Reproduccion r = new Reproduccion();
		    
		    int id = 1;
			keyLista kl = new keyLista(id, email);
			r.setIdRep(kl);
			LocalDate date = LocalDate.now();
			String fechaPub = Integer.toString(date.getDayOfMonth()) + "/" + Integer.toString(date.getMonthValue()) + "/" + Integer.toString(date.getYear());
			r.setFechaPublicacion(fechaPub);
			r.setNombre("PlaylistMeGusta");
			r.setURLFoto(null);
		    repService.saveRep(r);
		    */
			return user;
		}catch(Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	//Función para que los de móvil puedan seguir haciendo cosas
	//Sin necesidad de meter las fotos
	@PostMapping(value = "/registerUser1", produces = "application/json")
	@ResponseBody
	public Boolean register1(@RequestParam("name") String name, @RequestParam("surname") String surname,
			@RequestParam("username") String username, @RequestParam("email") String email,
			@RequestParam("password") String password, @RequestParam("dateOfBirth") String dateOfBirth,
			HttpServletResponse response){
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
	
	
	
	@PostMapping(value = "/createAlbum", produces = "application/json")
	@ResponseBody
	public keyLista createAlbum( @RequestParam(name = "foto", required = false) MultipartFile f, @RequestParam("email") String e,
			@RequestParam("name") String n, HttpServletResponse response){
		
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
			@RequestParam("playlist") String p, HttpServletResponse response){
		
		try {
			Usuario usuario = usuarioService.getUser(user);
			Reproduccion r = new Reproduccion();
			keyLista kl = new keyLista(-1,user);
			r.setIdRep(kl);
			r.setNombre(p);

			String path = "./src/main/resources/static/assets/images/";
			String imageName = String.valueOf("pl" + kl.getL_id() + kl.getU() + ".jpg");

			FileOutputStream fos = new FileOutputStream(path + imageName);

			String URLFoto = String.valueOf("Image?idfoto=" + imageName);
			r.setURLFoto(URLFoto);
			r.setAutor(usuario.getNick());

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
	
	@ApiOperation(value = "Operación para añadir canciones a una lista de reproducción" , response = Boolean.class)
	@ApiImplicitParams({@ApiImplicitParam(name = "user", value = "Correo del quien quiere añadir la canción"),
							@ApiImplicitParam(name = "usercancion", value = "Correo del dueño de la canción"),
							@ApiImplicitParam(name = "idalbum", value = "ID numérico del álbum al que pertenece la canción"),
							@ApiImplicitParam(name = "idplaylist", value = "ID numérico de la playlist"),
							@ApiImplicitParam(name = "idcancion", value = "ID numérico de la canción")
							})
	@PostMapping(value = "/addToPlaylist", produces = "application/json")
	@ResponseBody
	public Boolean addToPlaylist(@RequestBody Object u, 
			HttpServletResponse response, BindingResult result){
		
		try {
			
			LinkedHashMap<String,String> lhm = (LinkedHashMap) u;
			String user = lhm.get("user");	//Usuario dueño de la playlist
			String user_c = lhm.get("usercancion");	//Usuario dueño de la canción
			int id_a = Integer.parseInt(lhm.get("idalbum"));	//ID del álbum al que pertenece la canción
			int id_p = Integer.parseInt(lhm.get("idplaylist"));	//ID de la playlist a la que añadir la canción
			int id_c = Integer.parseInt(lhm.get("idcancion"));	//ID de la canción que añadir
			
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
			@RequestParam("podcast") String nombre, HttpServletResponse response){
		
		try {
			Usuario usuario = usuarioService.getUser(user);
			Podcast p = new Podcast(-1, user, nombre, null, null, usuario.getNick());
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
	
	
	@ApiOperation(value = "Operación para eliminar un Usuario", response = Boolean.class)
	@ApiImplicitParams({@ApiImplicitParam(name = "user", value = "Correo del usuario"),
							@ApiImplicitParam(name = "pass", value = "Contraseña del usuario"),
							@ApiImplicitParam(name = "passCheck", value = "Confirmación de la contraseña")
							})
	@PostMapping(value = "/deleteUser", produces = "application/json")
	@ResponseBody
	public Boolean deleteUser(@RequestBody Object u, HttpServletResponse response, BindingResult result){
		
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
	
	
	@ApiOperation(value = "Operación para cambiar el nombre de un Usuario", response = Boolean.class)
	@ApiImplicitParams({@ApiImplicitParam(name = "user", value = "Correo del usuario"),
							@ApiImplicitParam(name = "name", value = "Nombre antiguo"),
							@ApiImplicitParam(name = "newName", value = "Nuevo nombre")
							})
	@PostMapping(value = "/cambiarNombre", produces = "application/json")
	@ResponseBody
	public Boolean cambiarNombre(@RequestBody Object u, HttpServletResponse response, BindingResult result){
		
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
	
	
	@ApiOperation(value = "Operación para cambiar el nick de un Usuario", response = Boolean.class)
	@ApiImplicitParams({@ApiImplicitParam(name = "user", value = "Correo del usuario"),
							@ApiImplicitParam(name = "nick", value = "Nick antiguo"),
							@ApiImplicitParam(name = "newNick", value = "Nuevo nick")
							})
	@PostMapping(value = "/cambiarNick", produces = "application/json")
	@ResponseBody
	public Boolean cambiarNick(@RequestBody Object u, HttpServletResponse response, BindingResult result){
		
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
	
	
	@ApiOperation(value = "Operación para cambiar la contraseña de un Usuario", response = Boolean.class)
	@ApiImplicitParams({@ApiImplicitParam(name = "user", value = "Correo del usuario"),
							@ApiImplicitParam(name = "pass", value = "Contraseña antiguo"),
							@ApiImplicitParam(name = "newPass", value = "Nueva contraseña")
							})
	@PostMapping(value = "/cambiarPass", produces = "application/json")
	@ResponseBody
	public Boolean cambiarPass(@RequestBody Object u, HttpServletResponse response, BindingResult result){
		
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
	
	@PostMapping(value = "/cambiarFotoUsuario", produces = "application/json")
	@ResponseBody
	public Boolean cambiarFotoUsuario(@RequestParam("foto")MultipartFile f, @RequestParam("correo")String correo,
								HttpServletResponse response){
		
		try {
			return usuarioService.changeImage(f, correo);
		}catch(Exception e) {
			System.out.println(e);
		}
		return false;
	}
	
	@PostMapping(value = "/cambiarFotoAlbum", produces = "application/json")
	@ResponseBody
	public Boolean cambiarFotoAlbum(@RequestParam("foto")MultipartFile f, @RequestParam("correo")String correo,
								@RequestParam("idalbum")String id, HttpServletResponse response){
		
		try {
			return albumService.changeImage(f, correo, Integer.parseInt(id));
		}catch(Exception e) {
			System.out.println(e);
		}
		return false;
	}
	
	@PostMapping(value = "/cambiarFotoRep", produces = "application/json")
	@ResponseBody
	public Boolean cambiarFotoRep(@RequestParam("foto")MultipartFile f, @RequestParam("correo")String correo,
			@RequestParam("idplaylist")String id, HttpServletResponse response){
		
		try {
			return repService.changeImage(f, correo, Integer.parseInt(id));
		}catch(Exception e) {
			System.out.println(e);
		}
		return false;
	}
	
	@PostMapping(value = "/cambiarFotoPod", produces = "application/json")
	@ResponseBody
	public Boolean cambiarFotoPod(@RequestParam("foto")MultipartFile f, @RequestParam("correo")String correo,
			@RequestParam("idpodcast")String id, HttpServletResponse response){
		
		try {
			return podService.changeImage(f, correo, Integer.parseInt(id));
		}catch(Exception e) {
			System.out.println(e);
		}
		return false;
	}
	
	
	@ApiOperation(value = "Operación para listar las Canciones de un Album", response = Cancion[].class)
	@ApiImplicitParams({@ApiImplicitParam(name = "user", value = "Correo del usuario"),
							@ApiImplicitParam(name = "idalbum", value = "ID numérico del álbum")
							})
	@PostMapping(value = "/listSongsAlbum", produces = "application/json")
	@ResponseBody
	public Cancion[] listSongsAlbum(@RequestBody Object u, HttpServletResponse response, BindingResult result){
		
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
	
	@ApiOperation(value = "Operación para listar las Canciones de una Playlist(Reproduccion)", response = Cancion[].class)
	@ApiImplicitParams({@ApiImplicitParam(name = "user", value = "Correo del usuario"),
							@ApiImplicitParam(name = "idplaylist", value = "ID numérico de la Playlist")
							})
	@PostMapping(value = "/listSongsPlaylist", produces = "application/json")
	@ResponseBody
	public Cancion[] listSongsPlaylist(@RequestBody Object u, HttpServletResponse response, BindingResult result){
		
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
	
	@ApiOperation(value = "Operación para listar los Capitulos de un Podcast", response = Capitulo[].class)
	@ApiImplicitParams({@ApiImplicitParam(name = "user", value = "Correo del usuario"),
							@ApiImplicitParam(name = "idalbum", value = "ID numérico del Podcast")
							})
	@PostMapping(value = "/listPodcast", produces = "application/json")
	@ResponseBody
	public Capitulo[] listPodcast(@RequestBody Object u, HttpServletResponse response, BindingResult result){
		
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

	@ApiOperation(value = "Operación para eliminar una Cancion", response = Boolean.class)
	@ApiImplicitParams({@ApiImplicitParam(name = "user", value = "Correo del usuario"),
							@ApiImplicitParam(name = "idalbum", value = "ID numérico del Album"),
							@ApiImplicitParam(name = "idcancion", value = "ID numérico de la Cancion")
							})
	@PostMapping(value = "/deleteCancion", produces = "application/json")
	@ResponseBody
	public Boolean deleteCancion(@RequestBody Object u, HttpServletResponse response, BindingResult result){
		
		try {
			LinkedHashMap<String,String> lhm = (LinkedHashMap) u;
			
			String user = lhm.get("user");
			int id_a = Integer.parseInt(lhm.get("idalbum"));
			int id_c = Integer.parseInt(lhm.get("idcancion"));

			keyCancion kc = new keyCancion(new keyLista(id_a, user), id_c);

			Cancion c = cancionService.getCancion(id_a, user, id_c);
			
			if(!repService.deleteSongAllPlaylists(c)) {
				throw new Exception("No se ha podido borrar la Canción de las playlists");
			}
			
			return cancionService.deleteCancion(id_a, user, id_c);
			
		}catch(Exception e) {
			e.printStackTrace();
		}
		return false;
	}

	
	@ApiOperation(value = "Operación para eliminar un Capitulo", response = Boolean.class)
	@ApiImplicitParams({@ApiImplicitParam(name = "user", value = "Correo del usuario"),
							@ApiImplicitParam(name = "idpodcast", value = "ID numérico del Podcast"),
							@ApiImplicitParam(name = "idcapitulo", value = "ID numérico del Capitulo")
							})
	@PostMapping(value = "/deleteCapitulo", produces = "application/json")
	@ResponseBody
	public Boolean deleteCapitulo(@RequestBody Object u, HttpServletResponse response, BindingResult result){
		
		try {
			LinkedHashMap<String,String> lhm = (LinkedHashMap) u;
			
			String user = lhm.get("user");
			int id_a = Integer.parseInt(lhm.get("idpodcast"));
			int id_c = Integer.parseInt(lhm.get("idcapitulo"));

			return capService.deleteCapitulo(id_a, user, id_c);
		}catch(Exception e) {
			System.out.println(e);
		}
		return false;
	}
	
	@ApiOperation(value = "Operación para eliminar un Album", response = Boolean.class)
	@ApiImplicitParams({@ApiImplicitParam(name = "user", value = "Correo del usuario"),
							@ApiImplicitParam(name = "idalbum", value = "ID numérico del Album")
							})
	@PostMapping(value = "/deleteAlbum", produces = "application/json")
	@ResponseBody
	public Boolean deleteAlbum(@RequestBody Object u, HttpServletResponse response, BindingResult result){
		
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
	
	@ApiOperation(value = "Operación para eliminar un Podcast", response = Boolean.class)
	@ApiImplicitParams({@ApiImplicitParam(name = "user", value = "Correo del usuario"),
							@ApiImplicitParam(name = "idpodcast", value = "ID numérico del Podcast")
							})
	@PostMapping(value = "/deletePodcast", produces = "application/json")
	@ResponseBody
	public Boolean deletePodcast(@RequestBody Object u, HttpServletResponse response, BindingResult result){
		
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
	
	@ApiOperation(value = "Operación para eliminar una Playlist", response = Boolean.class)
	@ApiImplicitParams({@ApiImplicitParam(name = "user", value = "Correo del usuario"),
							@ApiImplicitParam(name = "idplaylist", value = "ID numérico de la Playlist")
							})
	@PostMapping(value = "/deletePlaylist", produces = "application/json")
	@ResponseBody
	public Boolean deletePlaylist(@RequestBody Object u, HttpServletResponse response, BindingResult result){
		
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

	@ApiOperation(value = "Operación para eliminar una Cancion de una Playlist", response = Boolean.class)
	@ApiImplicitParams({@ApiImplicitParam(name = "user", value = "Correo del usuario"),
							@ApiImplicitParam(name = "usercancion", value = "Correo del dueño de la Cancion"),
							@ApiImplicitParam(name = "idplaylist", value = "ID numérico de la Playlist"),
							@ApiImplicitParam(name = "idalbum", value = "ID numérico del Album al que pertenece la Cancion"),
							@ApiImplicitParam(name = "idcancion", value = "ID numérico de la Cancion")
							})
	@PostMapping(value = "/deleteSongPlaylist", produces = "application/json")
	@ResponseBody
	public Boolean deleteSongPlaylist(@RequestBody Object u, HttpServletResponse response, BindingResult result){
		
		try {
			LinkedHashMap<String,String> lhm = (LinkedHashMap) u;
			String user = lhm.get("user");
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

	
	@ApiOperation(value = "Operación para suscribir un Usuario a un Podcast", response = Boolean.class)
	@ApiImplicitParams({@ApiImplicitParam(name = "user", value = "Correo del usuario"),
							@ApiImplicitParam(name = "userpodcast", value = "Correo del dueño del Podcast"),
							@ApiImplicitParam(name = "idpodcast", value = "ID numérico del Podcast")
							})
	@PostMapping(value = "/subscribeUser", produces = "application/json")
	@ResponseBody
	public Boolean subscribeUser(@RequestBody Object u, HttpServletResponse response, BindingResult result){
		
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

	@ApiOperation(value = "Operación para eliminar la suscripción de un Usuario a un Podcast", response = Boolean.class)
	@ApiImplicitParams({@ApiImplicitParam(name = "user", value = "Correo del usuario"),
							@ApiImplicitParam(name = "userpodcast", value = "Correo del dueño del Podcast"),
							@ApiImplicitParam(name = "idpodcast", value = "ID numérico del Podcast")
							})
	@PostMapping(value = "/unsubscribeUser", produces = "application/json")
	@ResponseBody
	public Boolean unsubscribeUser(@RequestBody Object u, HttpServletResponse response, BindingResult result){
		
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

	@ApiOperation(value = "Operación para comprobar la suscripción de un Usuario a un Podcast", response = Boolean.class)
	@ApiImplicitParams({@ApiImplicitParam(name = "user", value = "Correo del usuario"),
							@ApiImplicitParam(name = "userpodcast", value = "Correo del dueño del Podcast"),
							@ApiImplicitParam(name = "idpodcast", value = "ID numérico del Podcast")
							})
	@PostMapping(value = "/checkSubscription", produces = "application/json")
	@ResponseBody
	public Boolean checkSubscription(@RequestBody Object u, HttpServletResponse response, BindingResult result){
		
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

	@ApiOperation(value = "Operación para listar las suscripciones a Podcasts de un Usuario", response = Podcast[].class)
	@ApiImplicitParams({@ApiImplicitParam(name = "user", value = "Correo del usuario")
							})
	@PostMapping(value = "/listSubscriptions", produces = "application/json")
	@ResponseBody
	public Podcast[] listSubscriptions(@RequestBody Object u, HttpServletResponse response, BindingResult result){
		
		try {
			String user = (String) u;
			
			return usuarioService.listSubscriptions(user);
		}catch(Exception e) {
			System.out.println(e);
		}
		return null;
	}
	
	@ApiOperation(value = "Operación para que un Usuario siga una Playlist", response = Boolean.class)
	@ApiImplicitParams({@ApiImplicitParam(name = "correo", value = "Correo del usuario"),
							@ApiImplicitParam(name = "idplaylist", value = "Correo del dueño de la Playlist"),
							@ApiImplicitParam(name = "correoplaylist", value = "ID numérico de la Playlist")
							})
	@PostMapping(value = "/followPlayList", produces = "application/json")
	@ResponseBody
	public Boolean followPlayList(@RequestBody Object u, HttpServletResponse response, BindingResult result) {
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
	
	@ApiOperation(value = "Operación para que un Usuario deje de seguir una Playlist", response = Boolean.class)
	@ApiImplicitParams({@ApiImplicitParam(name = "correo", value = "Correo del usuario"),
							@ApiImplicitParam(name = "idplaylist", value = "Correo del dueño de la Playlist"),
							@ApiImplicitParam(name = "correoplaylist", value = "ID numérico de la Playlist")
							})
	@PostMapping(value = "/unfollowPlayList", produces = "application/json")
	@ResponseBody
	public Boolean unfollowPlayList(@RequestBody Object u, HttpServletResponse response, BindingResult result) {
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

	@ApiOperation(value = "Operación para comprobar si un Usuario sigue una Playlist", response = Boolean.class)
	@ApiImplicitParams({@ApiImplicitParam(name = "correo", value = "Correo del usuario"),
							@ApiImplicitParam(name = "idplaylist", value = "Correo del dueño de la Playlist"),
							@ApiImplicitParam(name = "correoplaylist", value = "ID numérico de la Playlist")
							})
	@PostMapping(value = "/checkFollow", produces = "application/json")
	@ResponseBody
	public Boolean checkFollow(@RequestBody Object u, HttpServletResponse response, BindingResult result) {
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
	
	@ApiOperation(value = "Operación para que un Usuario siga a un Usuario", response = Boolean.class)
	@ApiImplicitParams({@ApiImplicitParam(name = "sessionUser", value = "Correo del usuario"),
							@ApiImplicitParam(name = "targetUser", value = "Correo del Usuario al que seguir")
							})
	@PostMapping(value = "/followUser", produces = "application/json")
	@ResponseBody
	public Boolean followUser(@RequestBody Object u, HttpServletResponse response, BindingResult result){
	
		try {
			LinkedHashMap<String,String> lhm = (LinkedHashMap) u;
			String sessionUser = lhm.get("sessionUser");
			String targetUser = lhm.get("targetUser");
			return usuarioService.followUser(sessionUser, targetUser);
		}catch(Exception e) {
			e.printStackTrace();
		}
		return false;
	}
	
	@ApiOperation(value = "Operación para comprobar si un Usuario sigue a un Usuario", response = Boolean.class)
	@ApiImplicitParams({@ApiImplicitParam(name = "sessionUser", value = "Correo del usuario"),
							@ApiImplicitParam(name = "targetUser", value = "Correo del Usuario al que seguir")
							})
	@PostMapping(value = "/checkFollowUser", produces = "application/json")
	@ResponseBody
	public Boolean checkFollowUser(@RequestBody Object u, HttpServletResponse response, BindingResult result){
	
		try {
			LinkedHashMap<String,String> lhm = (LinkedHashMap) u;
			String sessionUser = lhm.get("sessionUser");
			String targetUser = lhm.get("targetUser");
			return usuarioService.checkFollowUser(sessionUser, targetUser);
		}catch(Exception e) {
			e.printStackTrace();
		}
		return false;
	}
	
	
	@ApiOperation(value = "Operación para que un Usuario deje de seguir a un Usuario", response = Boolean.class)
	@ApiImplicitParams({@ApiImplicitParam(name = "sessionUser", value = "Correo del usuario"),
							@ApiImplicitParam(name = "targetUser", value = "Correo del Usuario al que dejar de seguir")
							})
	@PostMapping(value = "/unfollowUser", produces = "application/json")
	@ResponseBody
	public Boolean unfollowUser(@RequestBody Object u, HttpServletResponse response, BindingResult result){
	
		try {
			LinkedHashMap<String,String> lhm = (LinkedHashMap) u;
			String sessionUser = lhm.get("sessionUser");
			String targetUser = lhm.get("targetUser");
			return usuarioService.unfollowUser(sessionUser, targetUser);
		}catch(Exception e) {
			e.printStackTrace();
		}
		return false;
	}
	
	
	@ApiOperation(value = "Operación para listar los Usuarios que sigue un Usuario", response = Usuario[].class)
	@ApiImplicitParams({@ApiImplicitParam(name = "user", value = "Correo del usuario")
							})
	@PostMapping(value = "/listFollowingUsers", produces = "application/json")
	@ResponseBody
	public Usuario[] listFollowingUsers(@RequestBody String u, HttpServletResponse response, BindingResult result){
	
		try {
			
			return usuarioService.listFollowedUsers(u);
		}catch(Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	@ApiOperation(value = "Operación para listar los Usuarios que siguen a un Usuario", response = Usuario[].class)
	@ApiImplicitParams({@ApiImplicitParam(name = "user", value = "Correo del usuario")
							})
	@PostMapping(value = "/listFollowers", produces = "application/json")
	@ResponseBody
	public Usuario[] listFollowers(@RequestBody String u, HttpServletResponse response, BindingResult result){
	
		try {
			
			return usuarioService.followers(u);
		}catch(Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	@ApiOperation(value = "Operación para listar las Playlists que sigue un Usuario", response = Reproduccion[].class)
	@ApiImplicitParams({@ApiImplicitParam(name = "sessionUser", value = "Correo del usuario")
							})
	@PostMapping(value = "/listFollowsPlaylists", produces = "application/json")
	@ResponseBody
	public Reproduccion[] listFollowsPlaylists(@RequestBody Object u, HttpServletResponse response, BindingResult result){
		
		try {
			String user = (String) u;
			
			return usuarioService.listFollowsPlaylists(user);
		}catch(Exception e) {
			System.out.println(e);
		}
		return null;
	}
	
	@ApiOperation(value = "Operación para que un Usuario le de Me Gusta a una Cancion", response = Boolean.class)
	@ApiImplicitParams({@ApiImplicitParam(name = "correo", value = "Correo del usuario"),
							@ApiImplicitParam(name = "idalbum", value = "ID numérico del Album"),
							@ApiImplicitParam(name = "correoalbum", value = "Correo del Usuario dueño del Album"),
							@ApiImplicitParam(name = "idcancion", value = "ID numérico de la Cancion")
						})
	@PostMapping(value = "/likeSong", produces = "application/json")
	@ResponseBody
	public Boolean likeSong(@RequestBody Object u, HttpServletResponse response, BindingResult result) {
		try{
			LinkedHashMap<String,String> lhm = (LinkedHashMap) u;
			String user = lhm.get("correo");
			int id_a = Integer.parseInt(lhm.get("idalbum"));
			String correo_album = lhm.get("correoalbum");
			int id_c = Integer.parseInt(lhm.get("idcancion"));
			return usuarioService.likeSong(user, correo_album, id_c, id_a);
		}catch(Exception e){
			System.out.println(e.getMessage());
		}
		return false;
	}
	
	@ApiOperation(value = "Operación para que un Usuario le quite el Me Gusta a una Cancion", response = Boolean.class)
	@ApiImplicitParams({@ApiImplicitParam(name = "correo", value = "Correo del usuario"),
							@ApiImplicitParam(name = "idalbum", value = "ID numérico del Album"),
							@ApiImplicitParam(name = "correoalbum", value = "Correo del Usuario dueño del Album"),
							@ApiImplicitParam(name = "idcancion", value = "ID numérico de la Cancion")
						})
	@PostMapping(value = "/unlikeSong", produces = "application/json")
	@ResponseBody
	public Boolean unlikeSong(@RequestBody Object u, HttpServletResponse response, BindingResult result) {
		try{
			LinkedHashMap<String,String> lhm = (LinkedHashMap) u;
			String user = lhm.get("correo");
			int id_a = Integer.parseInt(lhm.get("idalbum"));
			String correo_album = lhm.get("correoalbum");
			int id_c = Integer.parseInt(lhm.get("idcancion"));
			Usuario usuario = usuarioService.getUser(user);
			Cancion c = cancionService.getCancion(id_a, correo_album, id_c);
			return usuarioService.unlikeSong(user, correo_album, id_c, id_a);
		}catch(Exception e){
			System.out.println(e.getMessage());
		}
		return false;
	}
	
	
	@ApiOperation(value = "Operación para comprobar si a un Usuario Le Gusta una Cancion", response = Boolean.class)
	@ApiImplicitParams({@ApiImplicitParam(name = "correo", value = "Correo del usuario"),
							@ApiImplicitParam(name = "idalbum", value = "ID numérico del Album"),
							@ApiImplicitParam(name = "correoalbum", value = "Correo del Usuario dueño del Album"),
							@ApiImplicitParam(name = "idcancion", value = "ID numérico de la Cancion")
						})
	@PostMapping(value = "/checkLike", produces = "application/json")
	@ResponseBody
	public Boolean checkLike(@RequestBody Object u, HttpServletResponse response, BindingResult result) {
		try{
			LinkedHashMap<String,String> lhm = (LinkedHashMap) u;
			String user = lhm.get("correo");
			String useralbum = lhm.get("correoalbum");
			int id_a = Integer.parseInt(lhm.get("idalbum"));
			int id_c = Integer.parseInt(lhm.get("idcancion"));
			return usuarioService.checkLike(user,useralbum, id_c, id_a);
		}catch(Exception e){
			System.out.println(e.getMessage());
		}
		return null;
	}

	@ApiOperation(value = "Operación para listar las Canciones que Le Gustan a un Usuario", response = Cancion[].class)
	@ApiImplicitParams({@ApiImplicitParam(name = "user", value = "Correo del usuario")
						})
	@PostMapping(value = "/listLikes", produces = "application/json")
	@ResponseBody
	public Cancion[] listLikes(@RequestBody Object u, HttpServletResponse response, BindingResult result){
		
		try {
			String user = (String) u;
			
			return usuarioService.listLikes(user);
		}catch(Exception e) {
			System.out.println(e);
		}
		return null;
	}

	@ApiOperation(value = "Operación para listar los Albums que Le Gustan a un Usuario", response = Album[].class)
	@ApiImplicitParams({@ApiImplicitParam(name = "user", value = "Correo del usuario")
						})
	@PostMapping(value = "/listAlbumsLikes", produces = "application/json")
	@ResponseBody
	public Album[] listAlbumsLikes(@RequestBody Object u, HttpServletResponse response, BindingResult result){
		
		try {
			String user = (String) u;
			
			return usuarioService.listAlbumsLikes(user);
		}catch(Exception e) {
			System.out.println(e);
		}
		return null;
	}

	@ApiOperation(value = "Operación para listar los Usuarios que Le Gustan a un Usuario", response = Usuario[].class)
	@ApiImplicitParams({@ApiImplicitParam(name = "user", value = "Correo del usuario")
						})
	@PostMapping(value = "/listUsersLikes", produces = "application/json")
	@ResponseBody
	public Usuario[] listUsersLikes(@RequestBody Object u, HttpServletResponse response, BindingResult result){
		
		try {
			String user = (String) u;
			
			return usuarioService.listUsersLikes(user);
		}catch(Exception e) {
			System.out.println(e);
		}
		return null;
	}
	
	@ApiOperation(value = "Operación para subir una Cancion a un Album", response = keyCancion.class)
	@PostMapping(value = "/subirCancion", produces = "application/json")
	@ResponseBody
	public keyCancion subirCancion(@RequestParam("file") MultipartFile file, @RequestParam("idalbum") String id, @RequestParam("user") String user, 
								@RequestParam("nombreC") String nombre, @RequestParam("genero") String gen, HttpServletResponse response){
		
		try {
			int id_a = Integer.parseInt(id);
			keyLista kl = new keyLista(id_a,user);
			Cancion c = new Cancion(kl, -1, nombre, gen, null); 	// Se crea el objeto con un 1 como id_cancion temporalmente, 																
			keyCancion kc = cancionService.createCancion( kl, c );	// se actualiza en el metodo repository.createCancion()
			if(kc == null) {
				throw new Exception("No se ha podido guardar la cancion");
			}
			Cancion guardada = cancionService.getCancion(kc.getL_id().getL_id(), kc.getL_id().getU(), kc.getC_id());
			String songName = String.valueOf(guardada.getIdCancion().getC_id()) + 
					String.valueOf(guardada.getIdCancion().getL_id().getL_id()) + 
					guardada.getIdCancion().getL_id().getU() + ".mp3";
			String urlsong = "Cancion?idsong=" + songName;
			guardada.setMp3(urlsong);
			if(!cancionService.saveSong(guardada)) {
				throw new Exception("No se ha podido añadir el mp3 a la canción");
			}
			// Id Cancion + Id Album + Id Usuario
			String path = "./src/main/resources/static/assets/";
			
			
			FileOutputStream fos = new FileOutputStream(path + songName);
			fos.write(file.getBytes());
			fos.close();
			
			return guardada.getIdCancion();
		}catch(Exception e) {
			System.out.println(e);
		}
		return null;
	}
	
	@ApiOperation(value = "Operación para subir un Capitulo a un Podcast", response = keyCancion.class)
	@PostMapping(value = "/subirCapitulo", produces = "application/json")
	@ResponseBody
	public keyCancion subirCapitulo(@RequestParam("file") MultipartFile file, @RequestParam("idalbum") String id, @RequestParam("user") String user, 
								@RequestParam("nombreC") String nombre, @RequestParam("genero") String gen, HttpServletResponse response){
		
		try {
			int id_a = Integer.parseInt(id);
			keyLista kl = new keyLista(id_a,user);
			Capitulo c = new Capitulo(kl, -1, nombre, gen, null); 	// Se crea el objeto con un 1 como id_cancion temporalmente, 
			System.out.println("He construido la nueva cancion");							// se actualiza en el metodo repository.createCancion()
			keyCancion kc = capService.createCapitulo( kl, c );
			if(kc == null) {
				throw new Exception("No se ha podido guardar el capitulo");
			}
			Capitulo guardado = capService.getCapitulo(kc.getL_id().getL_id(), kc.getL_id().getU(), kc.getC_id());
			String path = "./src/main/resources/static/assets/";
			String songName = "capitulo_" + String.valueOf(guardado.getIdCapitulo().getC_id()) + 
								String.valueOf(guardado.getIdCapitulo().getL_id().getL_id()) + 
								guardado.getIdCapitulo().getL_id().getU() + ".mp3";
			String urlsong = "Cancion?idsong=" + songName;
			guardado.setMp3(urlsong);
			if(!capService.saveChapter(guardado)) {
				throw new Exception("No se ha podido añadir el mp3 a la canción");
			}
			// Id Cancion + Id Album + Id Usuario
			
			
			FileOutputStream fos = new FileOutputStream(path + songName);
			fos.write(file.getBytes());
			fos.close();
			
			return guardado.getIdCapitulo();
		}catch(Exception e) {
			System.out.println(e);
		}
		return null;
	}
	
	
	@ApiOperation(value = "Operación para obtener la URL a una Cancion", response = String.class)
	@PostMapping(value = "/URLCancion", produces = "application/json")
	@ResponseBody
	public String URLCancion(@RequestParam("idalbum") String id, @RequestParam("user") String user, 
								@RequestParam("idcancion") String idc, HttpServletResponse response){
		
		try {
			String directory = Paths.get("").toAbsolutePath().toString();
			System.out.println("Directorio: " + directory);
			String songsPath = "/src/main/resources/static/assets/";
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

	@ApiOperation(value = "Operación para obtener la URL a una Foto de un Usuario", response = String.class)
	@PostMapping(value = "/URLFotoUsuario", produces = "application/json")
	@ResponseBody
	public String URLFotoUsuario(@RequestBody Object u, HttpServletResponse response){
		
		try {

			String user = (String) u;
			Usuario usuario = usuarioService.getUser(user);

			return usuario.getURLFoto();
		}catch(Exception e) {
			System.out.println(e);
		}
		return null;
	}

	@ApiOperation(value = "Operación para obtener la URL a una Foto de un Album", response = String.class)
	@PostMapping(value = "/URLFotoAlbum", produces = "application/json")
	@ResponseBody
	public String URLFotoAlbum(@RequestBody Object u, HttpServletResponse response){
		
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

	@ApiOperation(value = "Operación para obtener la URL a una Foto de un Podcast", response = String.class)
	@PostMapping(value = "/URLFotoPodcast", produces = "application/json")
	@ResponseBody
	public String URLFotoPodcast(@RequestBody Object u, HttpServletResponse response){
		
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

	@ApiOperation(value = "Operación para obtener la URL a una Foto de una Playlist", response = String.class)
	@PostMapping(value = "/URLFotoPlaylist", produces = "application/json")
	@ResponseBody
	public String URLFotoPlaylist(@RequestBody Object u, HttpServletResponse response){
		
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
	
	@ApiOperation(value = "Operación que devuelve la Cancion especificada en idsong", response = InputStreamResource.class)
	@GetMapping(value = "/Cancion", produces = MediaType.APPLICATION_OCTET_STREAM_VALUE)
	@ResponseBody
	public ResponseEntity requestSong(@RequestParam("idsong") String song, HttpServletResponse response) throws IOException{
		
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

	@ApiOperation(value = "Operación que devuelve la Foto especificada en idfoto", response = InputStreamResource.class)
	@GetMapping(value = "/Image", produces = MediaType.APPLICATION_OCTET_STREAM_VALUE)
	@ResponseBody
	public ResponseEntity requestFoto(@RequestParam("idfoto") String foto, HttpServletResponse response) throws IOException{
		
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
	
	@ApiOperation(value = "Operación para recuperar la Contraseña dado un Correo", response = Boolean.class)
	@ApiImplicitParams({@ApiImplicitParam(name = "user", value = "Correo del usuario")
						})
	@PostMapping(value = "/recoverEmail", produces = "application/json")
	@ResponseBody
	public Boolean recoverEmail(@RequestBody String u, HttpServletResponse response){
		try {
			String to = u;
			System.out.println("El correo: " + u + "quiere recuperar la contraseña");
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
	        
	        System.out.println("El Objeto Usuario: " + user.toString());
	        
			
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
	        usuarioService.changePass(user.getCorreo(), user.getPass(), generatedString);
	        return true;
		}catch(Exception e) {
			e.printStackTrace();
		}
		return false;
	}

	@ApiOperation(value = "Operación para listar los Albums de un Usuario", response = Album[].class)
	@ApiImplicitParams({@ApiImplicitParam(name = "correo", value = "Correo del usuario")
						})
	@PostMapping(value = "/getAlbumsByUser", produces = "application/json")
	@ResponseBody
	public Album[] getAlbumsByUser(@RequestBody Object u, HttpServletResponse response, BindingResult result){
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

	@ApiOperation(value = "Operación para listar los Podcasts de un Usuario", response = Podcast[].class)
	@ApiImplicitParams({@ApiImplicitParam(name = "correo", value = "Correo del usuario")
						})
	@PostMapping(value = "/getPodcastsByUser", produces = "application/json")
	@ResponseBody
	public Podcast[] getPodcastsByUser(@RequestBody Object u, HttpServletResponse response, BindingResult result){
		try {
			//LinkedHashMap<String,String> lhm = (LinkedHashMap) u;
			
			String user = (String) u;
			
			return podService.getPodcastByUser(user);
			
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
		return null;
	}

	@ApiOperation(value = "Operación para listar las Playlists de un Usuario", response = Reproduccion[].class)
	@ApiImplicitParams({@ApiImplicitParam(name = "correo", value = "Correo del usuario")
						})
	@PostMapping(value = "/getPlaylistByUser", produces = "application/json")
	@ResponseBody
	public Reproduccion[] getPlaylistByUser(@RequestBody Object u, HttpServletResponse response, BindingResult result){
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
	
	@ApiOperation(value = "Operación para buscar Albums dado un nombre", response = Album[].class)
	@ApiImplicitParams({@ApiImplicitParam(name = "nombre", value = "Nombre del Album")
						})
	@PostMapping(value = "/searchAlbum", produces = "application/json")
	@ResponseBody
	public Album[] searchAlbum(@RequestBody String s, HttpServletResponse response, BindingResult result) {
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
	
	@ApiOperation(value = "Operación para buscar Usuarios dado un nombre", response = Usuario[].class)
	@ApiImplicitParams({@ApiImplicitParam(name = "nombre", value = "Nombre del Usuario")
						})
	@PostMapping(value = "/searchUser", produces = "application/json")
	@ResponseBody
	public Usuario[] searchUser(@RequestBody String s, HttpServletResponse response, BindingResult result) {
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
	
	@ApiOperation(value = "Operación para buscar Playlists dado un nombre", response = Reproduccion[].class)
	@ApiImplicitParams({@ApiImplicitParam(name = "nombre", value = "Nombre de la Playlist")
						})
	@PostMapping(value = "/searchPlaylist", produces = "application/json")
	@ResponseBody
	public Reproduccion[] searchPlaylist(@RequestBody String s, HttpServletResponse response, BindingResult result) {
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
	
	@ApiOperation(value = "Operación para buscar Podcasts dado un nombre", response = Podcast[].class)
	@ApiImplicitParams({@ApiImplicitParam(name = "nombre", value = "Nombre del Podcast")
						})
	@PostMapping(value = "/searchPodcast", produces = "application/json")
	@ResponseBody
	public Podcast[] searchPodcast(@RequestBody String s, HttpServletResponse response, BindingResult result) {
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
	
	@ApiOperation(value = "Operación para buscar Canciones dado un nombre", response = Cancion[].class)
	@ApiImplicitParams({@ApiImplicitParam(name = "nombre", value = "Nombre de la Cancion")
						})
	@PostMapping(value = "/searchSong", produces = "application/json")
	@ResponseBody
	public Cancion[] searchSong(@RequestBody String s, HttpServletResponse response, BindingResult result) {
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
