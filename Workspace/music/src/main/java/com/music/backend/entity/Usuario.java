package com.music.backend.entity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

@SuppressWarnings("serial")
@Entity
@Table(name = "Usuario")
public class Usuario implements Serializable{
	
	@Id
	@Column(name = "Correo", nullable = false,length=100)
	@NotBlank
	@Email
	private String Correo;
	
	@Column(name = "Nombre", nullable = false,length=100)
	private String Nombre;
	
	@Column(name = "URLFoto",length=1024)
	private String URLFoto;
	
	@Column(name = "Pass", nullable = false,length=100)
	@NotBlank
	private String Pass;
	
	@Column(name = "Nick", nullable = false,length=100)
	@NotBlank
	private String Nick;
	
	@Column(name = "fNacimiento", nullable = false,length=100)
	private String fNacimiento;
	
	
	@ManyToMany(fetch = FetchType.LAZY)
	@JoinTable(name = "usuarioSuscritoPodcast", joinColumns = { 
			@JoinColumn(name = "usuario.correo", nullable = false, updatable = false)}, 
			inverseJoinColumns = { @JoinColumn(name = "podcast.lista_id", nullable = false, updatable = false),
									@JoinColumn(name = "podcast.usuario_id", nullable = false, updatable = false)})
	public List<Podcast> suscripciones = new ArrayList<>();
	
	@OneToMany(fetch = FetchType.LAZY)
	@JoinTable(name = "usersFollowPlaylist", joinColumns = { 
			@JoinColumn(name = "usuario.correo", nullable = false, updatable = false)}, 
			inverseJoinColumns = { @JoinColumn(name = "reproduccion.lista_id", nullable = false, updatable = false), 
					@JoinColumn(name = "reproduccion.usuario_id", nullable = false, updatable = false)})
	public List<Reproduccion> usersFollow = new ArrayList<>();
	
	@OneToMany(fetch = FetchType.LAZY)
	@JoinTable(name = "likedSongs", joinColumns = { 
			@JoinColumn(name = "usuario.correo", nullable = false, updatable = false)}, 
			inverseJoinColumns = { @JoinColumn(name = "cancion.cancion_id", nullable = false, updatable = false),
					@JoinColumn(name = "cancion.lista_id", nullable = false, updatable = false),
					@JoinColumn(name = "cancion.usuario_id", nullable = false, updatable = false)})
	public List<Cancion> likedSongs = new ArrayList<>();
	/*
	public Boolean addPodcast(Podcast podcast){
        if(suscripciones == null){
            suscripciones = new ArrayList<>();
        }
		suscripciones.add(podcast);
		return true;
	}
	
	public Boolean removePodcast(Podcast podcast){
		if(suscripciones == null){
			return false;
		}else{
			suscripciones.remove(podcast);
			return true;
		}
	}

	public Boolean findPodcast(Podcast podcast){
		return suscripciones.contains(podcast);
	}

	public Podcast[] listSubscriptions(){
		return (Podcast[]) suscripciones.toArray();
	}

	@JoinTable(
        name = "userSiguePlaylist",
        joinColumns = @JoinColumn(name = "correo", nullable = false),
        inverseJoinColumns = @JoinColumn(name="idRep", nullable = false)
    )
	@ManyToMany(cascade = CascadeType.ALL)
	private List<Reproduccion> seguidas = new ArrayList<>();

	public Boolean addPlayList(Reproduccion playlist){
        if(seguidas == null){
            seguidas = new ArrayList<>();
        }
		seguidas.add(playlist);
		return true;
	}
	
	public Boolean removePlayList(Reproduccion playlist){
		if(seguidas == null){
			return false;
		}else{
			seguidas.remove(playlist);
			return true;
		}
	}

	public Boolean findPlaylist(Reproduccion playlist){
		return seguidas.contains(playlist);
	}

	public Reproduccion[] listFollows(){
		return (Reproduccion[]) seguidas.toArray();
	}

	@JoinTable(
        name = "userLikeSong",
        joinColumns = @JoinColumn(name = "correo", nullable = false),
        inverseJoinColumns = @JoinColumn(name="idCancion", nullable = false)
    )
	@ManyToMany(cascade = CascadeType.ALL)
	private List<Cancion> likes = new ArrayList<>();

	public Boolean addSong(Cancion cancion){
		if(likes == null){
			likes = new ArrayList<>();
		}
		likes.add(cancion);
		return true;
	}

	public Boolean removeSong(Cancion cancion){
		if(likes == null){
			return null;
		}else{
			likes.remove(cancion);
			return true;
		}
	}

	public Boolean findSong(Cancion cancion){
		return likes.contains(cancion);
	}

	public Cancion[] listLikes(){
		return (Cancion[]) likes.toArray();
	}
*/
	public Usuario() {
		super();

	}

	public Usuario(@NotBlank @Email String correo, String nombre, String URLFoto, @NotBlank String pass,
			@NotBlank String nick, String fNacimiento) {
		super();
		Correo = correo;
		Nombre = nombre;
		this.URLFoto = URLFoto;
		Pass = pass;
		Nick = nick;
		this.fNacimiento = fNacimiento;
	}

	public String getCorreo() {
		return Correo;
	}

	public void setCorreo(String correo) {
		Correo = correo;
	}

	public String getNombre() {
		return Nombre;
	}

	public void setNombre(String nombre) {
		Nombre = nombre;
	}

	public String getURLFoto() {
		return URLFoto;
	}

	public void setFoto(String URLFoto) {
		this.URLFoto = URLFoto;
	}

	public String getPass() {
		return Pass;
	}

	public void setPass(String pass) {
		Pass = pass;
	}

	public String getNick() {
		return Nick;
	}

	public void setNick(String nick) {
		Nick = nick;
	}

	public String getfNacimiento() {
		return fNacimiento;
	}

	public void setfNacimiento(String fNacimiento) {
		this.fNacimiento = fNacimiento;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((Correo == null) ? 0 : Correo.hashCode());
		result = prime * result + ((URLFoto == null) ? 0 : URLFoto.hashCode());
		result = prime * result + ((Nick == null) ? 0 : Nick.hashCode());
		result = prime * result + ((Nombre == null) ? 0 : Nombre.hashCode());
		result = prime * result + ((Pass == null) ? 0 : Pass.hashCode());
		result = prime * result + ((fNacimiento == null) ? 0 : fNacimiento.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Usuario other = (Usuario) obj;
		if (Correo == null) {
			if (other.Correo != null)
				return false;
		} else if (!Correo.equals(other.Correo))
			return false;
		if (Nombre == null) {
				if (other.Nombre != null)
					return false;
			} else if (!Nombre.equals(other.Nombre))
				return false;
		if (Nick == null) {
			if (other.Nick != null)
				return false;
		} else if (!Nick.equals(other.Nick))
			return false;
		if (URLFoto == null) {
			if (other.URLFoto != null)
				return false;
		} else if (!URLFoto.equals(other.URLFoto))
			return false;
		if (Pass == null) {
			if (other.Pass != null)
				return false;
		} else if (!Pass.equals(other.Pass))
			return false;
		if (fNacimiento == null) {
			if (other.fNacimiento != null)
				return false;
		} else if (!fNacimiento.equals(other.fNacimiento))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "Usuario [Correo=" + Correo + ", Nombre=" + Nombre + ", URLFoto=" + URLFoto + ", Pass=" + Pass
				+ ", Nick=" + Nick + ", fNacimiento=" + fNacimiento + "]";
	} 
	
}
