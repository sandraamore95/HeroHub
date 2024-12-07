package com.HeroHub.HeroHub.services;

import com.HeroHub.HeroHub.models.Personaje;
import com.HeroHub.HeroHub.repository.PersonajeRepository;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
// la capa donde implementas la lógica de negocio, utilizando el repositorio.

public class PersonajeService {

    private final PersonajeRepository personajeRepository;

    // Constructor único: Spring lo usará automáticamente para la inyección
    public PersonajeService(PersonajeRepository personajeRepository) {
        this.personajeRepository = personajeRepository;
    }

    // Buscar personaje por nombre
    public Personaje buscarPersonajePorNombre(String nombre) {
        return personajeRepository.findByNombre(nombre)
                .orElseThrow(() -> new IllegalArgumentException("Personaje no encontrado: " + nombre));
    }

    // Obtener personajes relacionados a partir de los nombres
    public List<Personaje> obtenerPersonajesRelacionados(List<String> nombresRelacionados) {
        return nombresRelacionados.stream()
                .map(this::buscarPersonajePorNombre)
                .collect(Collectors.toList());
    }

    //metodo para cargar el json
    public void cargarPersonajesDesdeJson() {
        try {
            // Cargar el archivo JSON desde el classpath
            File archivo = new File(getClass().getClassLoader().getResource("personajes.json").toURI());
            ObjectMapper objectMapper = new ObjectMapper();

            // Leer los personajes desde el archivo JSON
            List<Personaje> personajes = objectMapper.readValue(archivo, new TypeReference<List<Personaje>>() {});

            // Guardar los personajes sin relaciones por ahora
            personajeRepository.saveAll(personajes);

            // Mapear personajes por nombre para luego asignar las relaciones
            Map<String, Personaje> personajesMap = personajeRepository.findAll().stream()
                    .collect(Collectors.toMap(Personaje::getNombre, p -> p));

            // Asignar personajes relacionados a cada personaje
            for (Personaje personaje : personajes) {
                // Verificar si hay nombres relacionados antes de asignarlos
                if (personaje.getNombresRelacionados() != null && !personaje.getNombresRelacionados().isEmpty()) {
                    List<Personaje> relacionados = personaje.getNombresRelacionados().stream()
                            .map(personajesMap::get)  // Buscar por nombre
                            .filter(Objects::nonNull)  // Filtrar personajes no encontrados (en caso de que el nombre no exista)
                            .collect(Collectors.toList());

                    // Asignar las relaciones al personaje
                    personaje.setPersonajesRelacionados(relacionados);
                }
            }

            // Guardar todos los personajes con sus relaciones
            personajeRepository.saveAll(personajes);

            System.out.println("Personajes y relaciones cargados exitosamente.");

        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("Error al leer el archivo JSON.");
        } catch (URISyntaxException e) {
            e.printStackTrace();
            System.out.println("Error en la URI del archivo JSON.");
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Error inesperado al cargar los personajes.");
        }
    }

    // Obtener todos los personajes
    public List<Personaje> getAllPersonajes() {
        // Obtiene todos los personajes desde el repositorio
        List<Personaje> personajes = personajeRepository.findAll();
        return personajes;
    }
    // Guardar un personaje
    public Personaje savePersonaje(Personaje personaje) {
        return personajeRepository.save(personaje);
    }


    // Buscar un personaje por ID
    public Personaje getPersonajeById(Long id) {
        Optional<Personaje> personaje = personajeRepository.findById(id);
        return personaje.orElse(null); // Esto no lanzará excepción
    }
    public Personaje findByNombre(String nombre) {
        Optional<Personaje> personaje = personajeRepository.findByNombre(nombre);
        return personaje.orElse(null); // Esto no lanzará excepción
    }

    //Buscar descripcion de un personaje por ID
    public String getDescripcionById(Long id){
        return this.personajeRepository.findDescripcionById(id);
    }
    //Buscar imagen de un personaje por ID
    public String getImagenById(Long id){
        return this.personajeRepository.findImagenById(id);
    }




    // Buscar un personaje por nombre
        public Personaje getPersonajeByNombre(String nombre) {
        Optional<Personaje> personaje = personajeRepository.findByNombre(nombre);
        // Si no lo encuentra, se retorna null
        return personaje.orElse(null); // Esto no lanzará excepción
    }

    // Eliminar un personaje
    public void deletePersonaje(Long id) {
        personajeRepository.deleteById(id);
    }
}