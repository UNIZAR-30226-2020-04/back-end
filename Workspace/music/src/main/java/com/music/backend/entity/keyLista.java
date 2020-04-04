package com.music.backend.entity;

import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.JoinColumn;
import com.music.backend.entity.Usuario;

@SuppressWarnings("serial")
@Embeddable
public class keyLista implements Serializable {

	@Column(name = "lista_id")
	private int l_id;
	
	@Column(name = "usuario_id")
	private String u;

	public keyLista() {
		super();
	}

	public keyLista(int l_id, String u) {
		super();
		this.l_id = l_id;
		this.u = u;
	}

	public int getL_id() {
		return l_id;
	}

	public void setL_id(int l_id) {
		this.l_id = l_id;
	}

	public String getU() {
		return u;
	}

	public void setU(String u) {
		this.u = u;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + l_id;
		result = prime * result + ((u == null) ? 0 : u.hashCode());
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
		keyLista other = (keyLista) obj;
		if (l_id != other.l_id)
			return false;
		if (u == null) {
			if (other.u != null)
				return false;
		} else if (!u.equals(other.u))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "keyLista [l_id=" + l_id + ", u=" + u + "]";
	}
	
}
