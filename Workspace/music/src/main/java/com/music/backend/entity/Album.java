package com.music.backend.entity;

import java.io.Serializable;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.persistence.EmbeddedId;
import com.music.backend.entity.keyLista;

@SuppressWarnings("serial")
@Entity
@Table(name = "Album")
public class Album implements Serializable{
	
	@EmbeddedId
	private keyLista idAlbum;
	
	@Column(name = "Nombre", nullable = false,length=100)
	@NotBlank
	private String Nombre;
	
	@Column(name = "Foto",length=1024)
	private byte[] Foto;
	
	@Column(name = "FechaPublicacion", nullable = false,length=100)
	private String FechaPublicacion;

	public Album() {
		super();
	}

	public Album(keyLista idAlbum, String nombre, byte[] foto, String fechaPublicacion) {
		super();
		this.idAlbum = idAlbum;
		Nombre = nombre;
		Foto = foto;
		FechaPublicacion = fechaPublicacion;
	}
	
	public Album(int id, String u, String nombre, byte[] foto, String fechaPublicacion) {
		super();
		this.idAlbum = new keyLista(id,u);
		Nombre = nombre;
		Foto = foto;
		FechaPublicacion = fechaPublicacion;
	}

	public keyLista getIdAlbum() {
		return idAlbum;
	}

	public void setIdAlbum(keyLista idAlbum) {
		this.idAlbum = idAlbum;
	}

	public String getNombre() {
		return Nombre;
	}

	public void setNombre(String nombre) {
		Nombre = nombre;
	}

	public byte[] getFoto() {
		return Foto;
	}

	public void setFoto(byte[] foto) {
		Foto = foto;
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
		result = prime * result + Arrays.hashCode(Foto);
		result = prime * result + ((Nombre == null) ? 0 : Nombre.hashCode());
		result = prime * result + ((idAlbum == null) ? 0 : idAlbum.hashCode());
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
		Album other = (Album) obj;
		if (FechaPublicacion == null) {
			if (other.FechaPublicacion != null)
				return false;
		} else if (!FechaPublicacion.equals(other.FechaPublicacion))
			return false;
		if (!Arrays.equals(Foto, other.Foto))
			return false;
		if (Nombre == null) {
			if (other.Nombre != null)
				return false;
		} else if (!Nombre.equals(other.Nombre))
			return false;
		if (idAlbum == null) {
			if (other.idAlbum != null)
				return false;
		} else if (!idAlbum.equals(other.idAlbum))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "Album [idAlbum=" + idAlbum + ", Nombre=" + Nombre + ", Foto=" + Arrays.toString(Foto)
				+ ", FechaPublicacion=" + FechaPublicacion + "]";
	}
	
}
