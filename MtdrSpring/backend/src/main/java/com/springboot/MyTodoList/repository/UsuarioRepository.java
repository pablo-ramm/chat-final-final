package com.springboot.MyTodoList.repository;

import com.springboot.MyTodoList.model.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface UsuarioRepository extends JpaRepository<Usuario, Integer> {
    @Query(value = "SELECT " +
            "CASE " +
            "   WHEN EXISTS (SELECT 1 FROM manager WHERE idusuario = (SELECT MIN(idusuario) FROM admin.usuario WHERE telegram_id = :telegramId)) THEN 'Manager' " +
            "   WHEN EXISTS (SELECT 1 FROM desarrollador WHERE idusuario = (SELECT MIN(idusuario) FROM admin.usuario WHERE telegram_id = :telegramId)) THEN 'Desarrollador' " +
            "   ELSE 'No es Manager ni Desarrollador' " +
            "END AS Rol " +
            "FROM DUAL", nativeQuery = true)
    String findRolByTelegramId(@Param("telegramId") long telegramId);

    @Query(value = "SELECT DESARROLLADOR.IDDesarrollador " +
            "FROM DESARROLLADOR, Usuario " +
            "WHERE DESARROLLADOR.IDUSUARIO = Usuario.IDUSUARIO " +
            "AND Usuario.TELEGRAM_ID = :telegramId", nativeQuery = true)
    int findDesarrolladorIdByTelegramId(@Param("telegramId") long telegramId);

    @Query(value = "SELECT DESARROLLADOR.IDPROYECTO " +
            "FROM DESARROLLADOR, Usuario " +
            "WHERE DESARROLLADOR.IDUSUARIO = Usuario.IDUSUARIO " +
            "AND Usuario.TELEGRAM_ID = :telegramId", nativeQuery = true)
    int findProyectoIdByTelegramId(@Param("telegramId") long telegramId);
}