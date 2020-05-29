package com.music.backend.entity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import java.util.List;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.music.backend.entity.keyLista;

@SuppressWarnings("serial")
@Entity
@Table(name = "ZZReproduccion")
public class Reproduccion implements Serializable{
	
	@EmbeddedId
	private keyLista idRep;
	
	@Column(name = "Nombre", nullable = false,length=100)
	@NotBlank
	private String Nombre;
	
	@Column(name = "URLFoto",length=1024)
	private String URLFoto;
	
	@Column(name = "Autor", nullable = false, length =100)
	private String Autor;
	
	@Column(name = "FechaPublicacion", nullable = false,length=100)
	private String FechaPublicacion;
	
	@JsonIgnore
	@ManyToMany(fetch = FetchType.LAZY)
	@JoinTable(name = "CancionesPlaylist", joinColumns = { 
			@JoinColumn(name = "reproduccion.lista_id", nullable = false, updatable = false), 
			@JoinColumn(name = "reproduccion.usuario_id", nullable = false, updatable = false) }, 
			inverseJoinColumns = { @JoinColumn(name = "cancion.cancion_id", nullable = false, updatable = false),
									@JoinColumn(name = "cancion.lista_id", nullable = false, updatable = false),
									@JoinColumn(name = "cancion.usuario_id", nullable = false, updatable = false)})
	public Set<Cancion> canciones = new HashSet<Cancion>(0);
	
	@JsonIgnore
	@ManyToMany(fetch = FetchType.LAZY)
	@JoinTable(name = "PlaylistFollowedByUsers", joinColumns = { 
			@JoinColumn(name = "reproduccion.lista_id", nullable = false, updatable = false), 
			@JoinColumn(name = "reproduccion.usuario_id", nullable = false, updatable = false) }, 
			inverseJoinColumns = { @JoinColumn(name = "usuario.correo", nullable = false, updatable = false)})
	public Set<Usuario> suscripciones = new HashSet<Usuario>(0);

	//@ManyToMany(mappedBy = "seguidas")
	//	private List<Usuario> siguen;

	public Reproduccion() {
		super();
	}

	public Reproduccion(@NotBlank keyLista idRep, @NotBlank String nombre, String URLFoto, String fechaPublicacion, String autor) {
		super();
		this.idRep = idRep;
		Nombre = nombre;
		this.URLFoto = URLFoto;
		FechaPublicacion = fechaPublicacion;
		this.Autor = autor;
	}
	
	public Reproduccion(int id, String u, String nombre, String URLFoto, String fechaPublicacion, String autor) {
		super();
		this.idRep = new keyLista(id,u);
		Nombre = nombre;
		this.URLFoto = URLFoto;
		FechaPublicacion = fechaPublicacion;
		this.Autor = autor;
	}

	public keyLista getIdRep() {
		return idRep;
	}

	public void setIdRep(keyLista idRep) {
		this.idRep = idRep;
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

	public void setURLFoto(String uRLFoto) {
		URLFoto = uRLFoto;
	}

	public String getAutor() {
		return Autor;
	}

	public void setAutor(String autor) {
		Autor = autor;
	}

	public String getFechaPublicacion() {
		return FechaPublicacion;
	}

	public void setFechaPublicacion(String fechaPublicacion) {
		FechaPublicacion = fechaPublicacion;
	}

	public Set<Cancion> getCanciones() {
		return canciones;
	}

	public void setCanciones(Set<Cancion> canciones) {
		this.canciones = canciones;
	}

	public Set<Usuario> getSuscripciones() {
		return suscripciones;
	}

	public void setSuscripciones(Set<Usuario> suscripciones) {
		this.suscripciones = suscripciones;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((Autor == null) ? 0 : Autor.hashCode());
		result = prime * result + ((FechaPublicacion == null) ? 0 : FechaPublicacion.hashCode());
		result = prime * result + ((Nombre == null) ? 0 : Nombre.hashCode());
		result = prime * result + ((URLFoto == null) ? 0 : URLFoto.hashCode());
		result = prime * result + ((canciones == null) ? 0 : canciones.hashCode());
		result = prime * result + ((idRep == null) ? 0 : idRep.hashCode());
		result = prime * result + ((suscripciones == null) ? 0 : suscripciones.hashCode());
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
		Reproduccion other = (Reproduccion) obj;
		if (Autor == null) {
			if (other.Autor != null)
				return false;
		} else if (!Autor.equals(other.Autor))
			return false;
		if (FechaPublicacion == null) {
			if (other.FechaPublicacion != null)
				return false;
		} else if (!FechaPublicacion.equals(other.FechaPublicacion))
			return false;
		if (Nombre == null) {
			if (other.Nombre != null)
				return false;
		} else if (!Nombre.equals(other.Nombre))
			return false;
		if (URLFoto == null) {
			if (other.URLFoto != null)
				return false;
		} else if (!URLFoto.equals(other.URLFoto))
			return false;
		if (canciones == null) {
			if (other.canciones != null)
				return false;
		} else if (!canciones.equals(other.canciones))
			return false;
		if (idRep == null) {
			if (other.idRep != null)
				return false;
		} else if (!idRep.equals(other.idRep))
			return false;
		if (suscripciones == null) {
			if (other.suscripciones != null)
				return false;
		} else if (!suscripciones.equals(other.suscripciones))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "Reproduccion [idRep=" + idRep + ", Nombre=" + Nombre + ", URLFoto=" + URLFoto + ", Autor=" + Autor
				+ ", FechaPublicacion=" + FechaPublicacion + ", canciones=" + canciones + ", suscripciones="
				+ suscripciones + "]";
	}

	
}
