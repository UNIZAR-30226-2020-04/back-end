package com.music.backend.entity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import java.util.List;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;

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
	
	@Column(name = "FechaPublicacion", nullable = false,length=100)
	private String FechaPublicacion;
	
	@OneToMany(fetch = FetchType.LAZY)
	@JoinTable(name = "CancionesPlaylist", joinColumns = { 
			@JoinColumn(name = "reproduccion.lista_id", nullable = false, updatable = false), 
			@JoinColumn(name = "reproduccion.usuario_id", nullable = false, updatable = false) }, 
			inverseJoinColumns = { @JoinColumn(name = "cancion.cancion_id", nullable = false, updatable = false),
									@JoinColumn(name = "cancion.lista_id", nullable = false, updatable = false),
									@JoinColumn(name = "cancion.usuario_id", nullable = false, updatable = false)})
	public Set<Cancion> canciones = new HashSet<Cancion>(0);
	
	
	@OneToMany(fetch = FetchType.LAZY)
	@JoinTable(name = "PlaylistFollowedByUsers", joinColumns = { 
			@JoinColumn(name = "reproduccion.lista_id", nullable = false, updatable = false), 
			@JoinColumn(name = "reproduccion.usuario_id", nullable = false, updatable = false) }, 
			inverseJoinColumns = { @JoinColumn(name = "usuario.correo", nullable = false, updatable = false)})
	public List<Usuario> suscripciones = new ArrayList<>();

	//@ManyToMany(mappedBy = "seguidas")
	//	private List<Usuario> siguen;

	public Reproduccion() {
		super();
	}

	public Reproduccion(@NotBlank keyLista idRep, @NotBlank String nombre, String URLFoto, String fechaPublicacion) {
		super();
		this.idRep = idRep;
		Nombre = nombre;
		this.URLFoto = URLFoto;
		FechaPublicacion = fechaPublicacion;
	}
	
	public Reproduccion(int id, String u, String nombre, String URLFoto, String fechaPublicacion) {
		super();
		this.idRep = new keyLista(id,u);
		Nombre = nombre;
		this.URLFoto = URLFoto;
		FechaPublicacion = fechaPublicacion;
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

	public void setURLFoto(String URLFoto) {
		this.URLFoto = URLFoto;
	}

	public String getFechaPublicacion() {
		return FechaPublicacion;
	}

	public void setFechaPublicacion(String fechaPublicacion) {
		FechaPublicacion = fechaPublicacion;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((FechaPublicacion == null) ? 0 : FechaPublicacion.hashCode());
		result = prime * result + ((URLFoto == null) ? 0 : URLFoto.hashCode());
		result = prime * result + ((Nombre == null) ? 0 : Nombre.hashCode());
		result = prime * result + ((idRep == null) ? 0 : idRep.hashCode());
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
		if (FechaPublicacion == null) {
			if (other.FechaPublicacion != null)
				return false;
		} else if (!FechaPublicacion.equals(other.FechaPublicacion))
			return false;
		if (URLFoto == null) {
			if (other.URLFoto != null)
				return false;
		} else if (!URLFoto.equals(other.URLFoto))
			return false;
		if (Nombre == null) {
			if (other.Nombre != null)
				return false;
		} else if (!Nombre.equals(other.Nombre))
			return false;
		if (idRep == null) {
			if (other.idRep != null)
				return false;
		} else if (!idRep.equals(other.idRep))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "Reproduccion [idRep=" + idRep + ", Nombre=" + Nombre + ", URLFoto=" + URLFoto
				+ ", FechaPublicacion=" + FechaPublicacion + "]";
	}
	
}
