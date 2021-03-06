package com.music.backend.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.music.backend.entity.Usuario;


@Repository
public interface UsuarioRepository extends CrudRepository<Usuario,String> {

	@Query(value = "SELECT * FROM zzusuario WHERE correo = ?1", nativeQuery = true)
	public Usuario findByEmail(String email);
	@Query(value = "SELECT * FROM zzusuario WHERE correo = ?1 AND pass = ?2", nativeQuery = true)
	public Usuario findByEmailAndPass(String email, String pass);
	@Query(value = "SELECT * FROM zzusuario WHERE nombre LIKE %?1%", nativeQuery = true)
	public Usuario[] getUsersBySearch(String c);
	@Query(value = "SELECT * FROM zzusuario", nativeQuery = true)
	public Usuario[] getAllUsers();
}
