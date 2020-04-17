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
@Table(name = "Cancion")
public class Cancion implements Serializable{
	
	@EmbeddedId
	private keyCancion idCancion;
	
	@Column(name = "Nombre", nullable = false,length=100)
	@NotBlank
	private String Nombre;
	
	@Column(name = "Genero", nullable = false,length=100)
	private String Genero;
	
	@Column(name = "mp3", length = 50000000)
	//@NotBlank
	private byte[] mp3;

	public Cancion() {
		super();
	}

	public Cancion(@NotBlank keyCancion idCancion, @NotBlank String nombre, String genero, byte[] mp3) {
		super();
		this.idCancion = idCancion;
		Nombre = nombre;
		Genero = genero;
		this.mp3 = mp3;
	}
	
	public Cancion(keyLista kl, int c, @NotBlank String nombre, String genero, byte[] mp3) {
		super();
		this.idCancion = new keyCancion(kl,c);
		Nombre = nombre;
		Genero = genero;
		this.mp3 = mp3;
	}
	
	public Cancion(int l, String u, int c, @NotBlank String nombre, String genero, byte[] mp3) {
		super();
		this.idCancion = new keyCancion(new keyLista(l,u), c);
		Nombre = nombre;
		Genero = genero;
		this.mp3 = mp3;
	}

	public keyCancion getIdCancion() {
		return idCancion;
	}

	public void setIdCancion(keyCancion idCancion) {
		this.idCancion = idCancion;
	}

	public String getNombre() {
		return Nombre;
	}

	public void setNombre(String nombre) {
		Nombre = nombre;
	}

	public String getGenero() {
		return Genero;
	}

	public void setGenero(String genero) {
		Genero = genero;
	}

	public byte[] getMp3() {
		return mp3;
	}

	public void setMp3(byte[] mp3) {
		this.mp3 = mp3;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((Genero == null) ? 0 : Genero.hashCode());
		result = prime * result + ((Nombre == null) ? 0 : Nombre.hashCode());
		result = prime * result + ((idCancion == null) ? 0 : idCancion.hashCode());
		result = prime * result + Arrays.hashCode(mp3);
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
		Cancion other = (Cancion) obj;
		if (Genero == null) {
			if (other.Genero != null)
				return false;
		} else if (!Genero.equals(other.Genero))
			return false;
		if (Nombre == null) {
			if (other.Nombre != null)
				return false;
		} else if (!Nombre.equals(other.Nombre))
			return false;
		if (idCancion == null) {
			if (other.idCancion != null)
				return false;
		} else if (!idCancion.equals(other.idCancion))
			return false;
		if (!Arrays.equals(mp3, other.mp3))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "Cancion [idCancion=" + idCancion + ", Nombre=" + Nombre + ", Genero=" + Genero + ", mp3="
				+ Arrays.toString(mp3) + "]";
	}
}
