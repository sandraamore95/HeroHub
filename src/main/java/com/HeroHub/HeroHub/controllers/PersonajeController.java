package com.HeroHub.HeroHub.controllers;

import com.HeroHub.HeroHub.models.Personaje;
import com.HeroHub.HeroHub.services.PersonajeService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import com.HeroHub.HeroHub.responses.ErrorResponse;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/personajes")
public class PersonajeController {

    private final PersonajeService personajeService;

    public PersonajeController(PersonajeService personajeService) {
        this.personajeService = personajeService;
    }

    @GetMapping("/all")
    public List<Personaje> getAllPersonajes() {
        List<Personaje> personajes = personajeService.getAllPersonajes();

        // Procesar cada personaje para evitar la serialización recursiva
        personajes.forEach(personaje -> {
            // Crear una lista con solo los nombres de los personajes relacionados, convertidos a String
            List<String> nombresRelacionados = personaje.getPersonajesRelacionados().stream()
                    .map(p -> p.getNombre())  // Cambiar de ID a Nombre
                    .collect(Collectors.toList());

            // Establecer los ids/nombres de los personajes relacionados
            personaje.setNombresRelacionados(nombresRelacionados);  // Usar este campo temporal para almacenar los nombres como Strings
        });

        // Regresar la lista de personajes modificada
        return personajes;
    }

    @GetMapping("{id}/descripcion")
    public ResponseEntity<String> getDescripcionById(@PathVariable Long id) {
        try {
            // Intenta obtener la descripción desde el servicio
            String descripcion = personajeService.getDescripcionById(id);

            if (descripcion != null) {
                // Caso exitoso: se encontró la descripción
                return ResponseEntity.ok("La descripción del personaje es: " + descripcion);
            } else {
                // Caso en el que no se encuentra el personaje
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body("No se encontró un personaje con el ID: " + id);
            }
        } catch (Exception e) {
            // Caso en que ocurre un error inesperado
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Ocurrió un error al buscar la descripción del personaje: " + e.getMessage());
        }
    }

    @GetMapping("{id}/imagen")
    public ResponseEntity<String> getImagenById(@PathVariable Long id) {
        try {
            // Intenta obtener la descripción desde el servicio
            String imagen = personajeService.getImagenById(id);

            if (imagen != null) {
                // Caso exitoso: se encontró la descripción
                return ResponseEntity.ok("La imagen del personaje es: " + imagen);
            } else {
                // Caso en el que no se encuentra el personaje
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body("No se encontró un personaje con el ID: " + id);
            }
        } catch (Exception e) {
            // Caso en que ocurre un error inesperado
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Ocurrió un error al buscar la imagen del personaje: " + e.getMessage());
        }
    }


//select * from personaje where  sort

    @PostMapping
    public String  createPersonaje(@RequestBody Personaje personaje) {
        personajeService.savePersonaje(personaje);
        return "El persona ya ha sido creado";
    }

    @GetMapping("/nombre/{nombre}")
    public ResponseEntity<?> getPersonajeByNombre(@PathVariable String nombre) {
        Personaje personaje = personajeService.getPersonajeByNombre(nombre);

        // Verifica si el personaje existe
        if (personaje != null) {
            List<String> nombresRelacionados = personaje.getPersonajesRelacionados().stream()
                    .map(p -> p.getNombre())
                    .collect(Collectors.toList());
            personaje.setNombresRelacionados(nombresRelacionados);
            return ResponseEntity.ok(personaje);
        } else {
            // Si el personaje no existe, devuelves un mensaje claro con un 404
            ErrorResponse errorResponse = new ErrorResponse("Personaje no encontrado");
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResponse);
        }
    }




    @GetMapping("/id/{id}")
    public ResponseEntity<?> getPersonajeById(@PathVariable Long id) {
        // Llamar al servicio para obtener el personaje por su ID
        Personaje personaje = personajeService.getPersonajeById(id);

        // Verifica si el personaje existe
        if (personaje != null) {
            // Si el personaje existe, procesar los personajes relacionados
            List<String> nombresRelacionados = personaje.getPersonajesRelacionados().stream()
                    .map(p -> p.getNombre())
                    .collect(Collectors.toList());
            personaje.setNombresRelacionados(nombresRelacionados);

            return ResponseEntity.ok(personaje);
        } else {
            // Si el personaje no existe, devolver un mensaje de error con un 404
            ErrorResponse errorResponse = new ErrorResponse("Personaje no encontrado");
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResponse);
        }
    }


    @DeleteMapping("/{id}")
    public void deletePersonaje(@PathVariable Long id) {
        personajeService.deletePersonaje(id);
    }
}