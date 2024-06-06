package com.springboot.MyTodoList.controller;

import com.springboot.MyTodoList.model.Tarea;
import com.springboot.MyTodoList.service.TareaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class TareaController {

    @Autowired
    private TareaService tareaService;

    @GetMapping(value = "/tareas")
    public List<Tarea> getAllTareas() {
        return tareaService.findAll();
    }

    @GetMapping(value = "/tareas/{id}")
    public ResponseEntity<Tarea> getTareaById(@PathVariable int id) {
        ResponseEntity<Tarea> responseEntity = tareaService.getTareaById(id);
        return new ResponseEntity<>(responseEntity.getBody(), responseEntity.getStatusCode());
    }

    @PostMapping(value = "/tareas")
    public ResponseEntity<?> addTarea(@RequestBody Tarea tarea) {
        Tarea nuevaTarea = tareaService.addTarea(tarea);
        HttpHeaders responseHeaders = new HttpHeaders();
        responseHeaders.add("Location", "/tareas/" + nuevaTarea.getIdTarea());
        return ResponseEntity.status(HttpStatus.CREATED).headers(responseHeaders).build();
    }

    @PutMapping(value = "/tareas/{id}")
    public ResponseEntity<Tarea> updateTarea(@RequestBody Tarea tarea, @PathVariable int id) {
        Tarea tareaActualizada = tareaService.updateTarea(id, tarea);
        if (tareaActualizada != null) {
            return new ResponseEntity<>(tareaActualizada, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @DeleteMapping(value = "/tareas/{id}")
    public ResponseEntity<Boolean> deleteTarea(@PathVariable int id) {
        boolean eliminada = tareaService.deleteTarea(id);
        if (eliminada) {
            return new ResponseEntity<>(true, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(false, HttpStatus.NOT_FOUND);
        }
    }
}
