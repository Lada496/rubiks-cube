package com.rubiks.rubiks_cube.controller;

import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.rubiks.rubiks_cube.exceptions.OperationException;
import com.rubiks.rubiks_cube.models.Cell;
import com.rubiks.rubiks_cube.models.Operation;
import com.rubiks.rubiks_cube.services.CubeService;

@RestController
@RequestMapping("/cube")
public class CubeController {
    private final CubeService cubeService;

    @Autowired
    public CubeController(CubeService cubeService) {
        this.cubeService = cubeService;
    }

    // Endpoint to get current cube
    @GetMapping
    public ArrayList<ArrayList<Cell>> getCube() {
        return cubeService.getCube();
    }

    @PostMapping("/operate") 
    public ResponseEntity<Void> operate(@RequestBody Operation operation) {
        try {
            cubeService.operate(operation);
            return ResponseEntity.noContent().build();
        } catch (OperationException e) {
            return ResponseEntity.badRequest().build();
        }
        
    }


    @PutMapping("/redo")
    public ResponseEntity<Void> redo() {
        try {
            cubeService.redo();
            return ResponseEntity.noContent().build();
        } catch (OperationException e) {
            return ResponseEntity.badRequest().build();
        }
        
    }

    @PutMapping("/undo")
    public ResponseEntity<Void> undo() {
        try {
            cubeService.undo();
            return ResponseEntity.noContent().build();
        } catch (OperationException e) {
            return ResponseEntity.badRequest().build();
        }
        
    }
}
