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
@Table(name = "Reproduccion")
public class Reproduccion implements Serializable{
	
	@EmbeddedId
	private keyLista idRep;
	
	@Column(name = "Nombre", nullable = false,length=100)
	@NotBlank
	private String Nombre;
	
	@Column(name = "Foto",length=1024)
	private byte[] Foto;
	
	@Column(name = "FechaPublicacion", nullable = false,length=100)
	private String FechaPublicacion;

	public Reproduccion() {
		super();
	}

	public Reproduccion(@NotBlank keyLista idRep, @NotBlank String nombre, byte[] foto, String fechaPublicacion) {
		super();
		this.idRep = idRep;
		Nombre = nombre;
		Foto = foto;
		FechaPublicacion = fechaPublicacion;
	}
	
	public Reproduccion(int id, String u, String nombre, byte[] foto, String fechaPublicacion) {
		super();
		this.idRep = new keyLista(id,u);
		Nombre = nombre;
		Foto = foto;
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
		if (!Arrays.equals(Foto, other.Foto))
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
		return "Reproduccion [idRep=" + idRep + ", Nombre=" + Nombre + ", Foto=" + Arrays.toString(Foto)
				+ ", FechaPublicacion=" + FechaPublicacion + "]";
	}
	
}
