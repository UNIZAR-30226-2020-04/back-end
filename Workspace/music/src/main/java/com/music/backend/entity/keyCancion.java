package com.music.backend.entity;

import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.JoinColumn;
import com.music.backend.entity.keyLista;

@SuppressWarnings("serial")
@Embeddable
public class keyCancion implements Serializable {
	
	private keyLista l_id;
	
	@Column(name = "cancion_id")
	private int c_id;

	public keyCancion() {
		super();
	}

	public keyCancion(keyLista l_id, int c_id) {
		super();
		this.l_id = l_id;
		this.c_id = c_id;
	}
	
	public keyCancion(int l, String u, int c_id) {
		super();
		this.l_id = new keyLista(l,u);
		this.c_id = c_id;
	}

	public keyLista getL_id() {
		return l_id;
	}

	public void setL_id(keyLista l_id) {
		this.l_id = l_id;
	}

	public int getC_id() {
		return c_id;
	}

	public void setC_id(int c_id) {
		this.c_id = c_id;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + c_id;
		result = prime * result + ((l_id == null) ? 0 : l_id.hashCode());
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
		keyCancion other = (keyCancion) obj;
		if (c_id != other.c_id)
			return false;
		if (l_id == null) {
			if (other.l_id != null)
				return false;
		} else if (!l_id.equals(other.l_id))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "keyCancion [l_id=" + l_id + ", c_id=" + c_id + "]";
	}

}
