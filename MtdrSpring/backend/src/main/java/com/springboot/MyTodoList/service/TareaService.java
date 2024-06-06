package com.springboot.MyTodoList.service;

import com.springboot.MyTodoList.model.Tarea;
import com.springboot.MyTodoList.repository.TareaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class TareaService {

    @Autowired
    private TareaRepository tareaRepository;

    public List<Tarea> findAll() {
        return tareaRepository.findAll();
    }

    public ResponseEntity<Tarea> getTareaById(int id) {
        Optional<Tarea> tareaData = tareaRepository.findById(id);
        return tareaData.map(tarea -> new ResponseEntity<>(tarea, HttpStatus.OK))
                .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    public Tarea addTarea(Tarea tarea) {
        return tareaRepository.save(tarea);
    }

    public boolean deleteTarea(int id) {
        try {
            tareaRepository.deleteById(id);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public Tarea updateTarea(int id, Tarea nuevaTarea) {
        Optional<Tarea> tareaData = tareaRepository.findById(id);
        if (tareaData.isPresent()) {
            Tarea tarea = tareaData.get();
            tarea.setIdTarea(id);
            tarea.setNombre(nuevaTarea.getNombre());
            tarea.setDescripcion(nuevaTarea.getDescripcion());
            tarea.setEstado(nuevaTarea.getEstado());
            tarea.setFechaInicio(nuevaTarea.getFechaInicio());
            tarea.setFechaFinal(nuevaTarea.getFechaFinal());
            tarea.setPrioridad(nuevaTarea.getPrioridad());
            tarea.setComentarios(nuevaTarea.getComentarios());
            tarea.setDesarrollador(nuevaTarea.getDesarrollador());
            tarea.setProyecto(nuevaTarea.getProyecto());
            return tareaRepository.save(tarea);
        } else {
            return null;
        }
    }
}
