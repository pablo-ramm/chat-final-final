package com.springboot.MyTodoList.service;


import com.springboot.MyTodoList.model.Usuario;
import com.springboot.MyTodoList.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UsuarioService {

    @Autowired
    private UsuarioRepository usuarioRepository;

    public List<Usuario> findAllUsuarios() {
        return usuarioRepository.findAll();
    }

    public Optional<Usuario> findUsuarioById(int id) {
        return usuarioRepository.findById(id);
    }

    public Usuario saveUsuario(Usuario usuario) {
        return usuarioRepository.save(usuario);
    }

    public void deleteUsuario(int id) {
        usuarioRepository.deleteById(id);
    }
    public String findRolByTelegramId(long telegramId) {
        return usuarioRepository.findRolByTelegramId(telegramId);
    }

    public int  findDesarrolladorIdByTelegramId(long telegramId) {
        return usuarioRepository. findDesarrolladorIdByTelegramId(telegramId);
    }

    public int  findProyectoIdByTelegramId(long telegramId) {
        return usuarioRepository. findProyectoIdByTelegramId(telegramId);
    }
    // Aquí puedes agregar otros métodos según sea necesario
}
