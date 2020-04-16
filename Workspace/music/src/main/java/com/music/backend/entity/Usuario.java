package com.music.backend.entity;

import java.io.Serializable;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

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
	
	@Column(name = "Foto",length=1024)
	private byte[] Foto;
	
	@Column(name = "Pass", nullable = false,length=100)
	@NotBlank
	private String Pass;
	
	@Column(name = "Nick", nullable = false,length=100)
	@NotBlank
	private String Nick;
	
	@Column(name = "fNacimiento", nullable = false,length=100)
	private String fNacimiento;
	
	public Set<keyLista> podcasts = new HashSet<keyLista>(0);

	public Usuario() {
		super();
	}

	public Usuario(@NotBlank @Email String correo, String nombre, byte[] foto, @NotBlank String pass,
			@NotBlank String nick, String fNacimiento) {
		super();
		Correo = correo;
		Nombre = nombre;
		Foto = foto;
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

	public byte[] getFoto() {
		return Foto;
	}

	public void setFoto(byte[] foto) {
		Foto = foto;
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
		result = prime * result + Arrays.hashCode(Foto);
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
		if (!Arrays.equals(Foto, other.Foto))
			return false;
		if (Nick == null) {
			if (other.Nick != null)
				return false;
		} else if (!Nick.equals(other.Nick))
			return false;
		if (Nombre == null) {
			if (other.Nombre != null)
				return false;
		} else if (!Nombre.equals(other.Nombre))
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
		return "Usuario [Correo=" + Correo + ", Nombre=" + Nombre + ", Foto=" + Arrays.toString(Foto) + ", Pass=" + Pass
				+ ", Nick=" + Nick + ", fNacimiento=" + fNacimiento + "]";
	}
	
}
