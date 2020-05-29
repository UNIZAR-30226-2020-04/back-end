package com.music.backend.entity;

import java.io.Serializable;
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
@Table(name = "ZZPodcast")
public class Podcast implements Serializable{
	
	@EmbeddedId
	private keyLista idPodcast;
	
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
	@JoinTable(name = "CapitulosPodcast", joinColumns = { 
			@JoinColumn(name = "podcast.lista_id", nullable = false, updatable = false), 
			@JoinColumn(name = "podcast.usuario_id", nullable = false, updatable = false) }, 
			inverseJoinColumns = { @JoinColumn(name = "cancion.cancion_id", nullable = false, updatable = false),
									@JoinColumn(name = "cancion.lista_id", nullable = false, updatable = false),
									@JoinColumn(name = "cancion.usuario_id", nullable = false, updatable = false)})
	public Set<Cancion> capitulos = new HashSet<Cancion>(0);


	public Podcast() {
		super();
	}

	public Podcast(@NotBlank keyLista idPodcast, @NotBlank String nombre, String URLFoto, String fechaPublicacion, String autor) {
		super();
		this.idPodcast = idPodcast;
		Nombre = nombre;
		this.URLFoto = URLFoto;
		FechaPublicacion = fechaPublicacion;
		this.Autor = autor;
	}
	
	public Podcast(int id, String u, String nombre, String URLFoto, String fechaPublicacion, String autor) {
		super();
		this.idPodcast = new keyLista(id,u);
		Nombre = nombre;
		this.URLFoto = URLFoto;
		FechaPublicacion = fechaPublicacion;
		this.Autor = autor;
	}

	public keyLista getIdPodcast() {
		return idPodcast;
	}

	public void setIdPodcast(keyLista idPodcast) {
		this.idPodcast = idPodcast;
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

	public Set<Cancion> getCapitulos() {
		return capitulos;
	}

	public void setCapitulos(Set<Cancion> capitulos) {
		this.capitulos = capitulos;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((Autor == null) ? 0 : Autor.hashCode());
		result = prime * result + ((FechaPublicacion == null) ? 0 : FechaPublicacion.hashCode());
		result = prime * result + ((Nombre == null) ? 0 : Nombre.hashCode());
		result = prime * result + ((URLFoto == null) ? 0 : URLFoto.hashCode());
		result = prime * result + ((capitulos == null) ? 0 : capitulos.hashCode());
		result = prime * result + ((idPodcast == null) ? 0 : idPodcast.hashCode());
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
		Podcast other = (Podcast) obj;
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
		if (capitulos == null) {
			if (other.capitulos != null)
				return false;
		} else if (!capitulos.equals(other.capitulos))
			return false;
		if (idPodcast == null) {
			if (other.idPodcast != null)
				return false;
		} else if (!idPodcast.equals(other.idPodcast))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "Podcast [idPodcast=" + idPodcast + ", Nombre=" + Nombre + ", URLFoto=" + URLFoto + ", Autor=" + Autor
				+ ", FechaPublicacion=" + FechaPublicacion + ", capitulos=" + capitulos + "]";
	}

	
	
}
