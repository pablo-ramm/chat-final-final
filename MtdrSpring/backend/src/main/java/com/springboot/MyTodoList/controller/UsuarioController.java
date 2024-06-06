package com.springboot.MyTodoList.controller;

import com.springboot.MyTodoList.model.Usuario;
import com.springboot.MyTodoList.service.UsuarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/usuarios")
public class UsuarioController {

    @Autowired
    private UsuarioService usuarioService;

    @GetMapping("usuarios")
    public ResponseEntity<List<Usuario>> getAllUsuarios() {
        List<Usuario> usuarios = usuarioService.findAllUsuarios();
        return new ResponseEntity<>(usuarios, HttpStatus.OK);
    }

    @GetMapping("usuarios/{id}")
    public ResponseEntity<Usuario> getUsuarioById(@PathVariable int id) {
        Optional<Usuario> usuario = usuarioService.findUsuarioById(id);
        return usuario.map(value -> new ResponseEntity<>(value, HttpStatus.OK))
                .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }
    @PostMapping(value = "/usuarios")
    public ResponseEntity<Usuario> createUsuario(@RequestBody Usuario usuario) {
        Usuario createdUsuario = usuarioService.saveUsuario(usuario);
        return new ResponseEntity<>(createdUsuario, HttpStatus.CREATED);
    }

    @DeleteMapping("usuarios/{id}")
    public ResponseEntity<Void> deleteUsuario(@PathVariable int id) {
        usuarioService.deleteUsuario(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @GetMapping("usuarios/rol/{telegramId}")
    public ResponseEntity<String> getUsuarioRolByTelegramId(@PathVariable long telegramId) {
        String rol = usuarioService.findRolByTelegramId(telegramId);
        return new ResponseEntity<>(rol, HttpStatus.OK);
    }

    @GetMapping("usuarios/proyecto/{telegramId}")
    public ResponseEntity<Integer> findDesarrolladorIdByTelegramId(@PathVariable long telegramId) {
        int desarrolladorID = usuarioService.findDesarrolladorIdByTelegramId(telegramId);
        return new ResponseEntity<>(desarrolladorID, HttpStatus.OK);
    }

    @GetMapping("usuarios/usuario/{telegramId}")
    public ResponseEntity<Integer> findProyectoIdByTelegramId(@PathVariable long telegramId) {
        int proyectoID = usuarioService.findProyectoIdByTelegramId(telegramId);
        return new ResponseEntity<>(proyectoID, HttpStatus.OK);
    }

    // Puedes agregar métodos adicionales según sea necesario
}