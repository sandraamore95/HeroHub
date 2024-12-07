package com.HeroHub.HeroHub.models;
import com.HeroHub.HeroHub.services.PersonajeService;
import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

@Entity
public class Personaje {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String nombre;
    private int edad;
    private String lugar;
    private String imagen;
    private String descripcion;
    private int popularity;

// Mangel estuvo aqui

    @Embedded
    private Atributos atributos;  // Los campos de Atributos se colocan en la tabla de Personaje en vez de ser una tabla

    @ElementCollection
    private List<String> peliculas;// Lista simple que se almacena en una tabla secundaria.

    @ManyToMany
    @JoinTable(
            name = "personaje_relaciones", // Nombre de la tabla intermedia
            joinColumns = @JoinColumn(name = "personaje_id"), // Columna que referencia a este personaje
            inverseJoinColumns = @JoinColumn(name = "relacionado_id") // Columna que referencia al personaje relacionado
    )
    @JsonIgnore
    private List<Personaje> personajesRelacionados; // 1 personaje puede estar relacionado con varios Personajes

    @Transient
    private List<String> nombresRelacionados; // Campo temporal para deserialización


    //GETTERS Y SETTERS

    public List<String> getNombresRelacionados() {
        return nombresRelacionados;
    }

    public void setNombresRelacionados(List<String> nombresRelacionados) {
        this.nombresRelacionados = nombresRelacionados;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public int getEdad() {
        return edad;
    }

    public void setEdad(int edad) {
        this.edad = edad;
    }

    public String getLugar() {
        return lugar;
    }

    public void setLugar(String lugar) {
        this.lugar = lugar;
    }

    public String getImagen() {
        return imagen;
    }

    public void setImagen(String imagen) {
        this.imagen = imagen;
    }

    public Atributos getAtributos() {
        return atributos;
    }

    public void setAtributos(Atributos atributos) {
        this.atributos = atributos;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public List<String> getPeliculas() {
        return peliculas;
    }

    public void setPeliculas(List<String> peliculas) {
        this.peliculas = peliculas;
    }

    public List<Personaje> getPersonajesRelacionados() {
        return personajesRelacionados;
    }

    public void setPersonajesRelacionados(List<Personaje> personajesRelacionados) {
        this.personajesRelacionados = personajesRelacionados;
    }

    public int getPopularity() {
        return popularity;
    }

    public void setPopularity(int popularity) {
        this.popularity = popularity;
    }
    @Override
    public String toString() {
        return this.nombre; // Asegúrate de que devuelva el nombre
    }
}

@Embeddable
class Atributos {
    private int poder;
    private int inteligencia;
    private boolean puedeVolar;
    private int economia;
    private int fuerza;

    public int getPoder() {
        return poder;
    }

    public void setPoder(int poder) {
        this.poder = poder;
    }

    public int getInteligencia() {
        return inteligencia;
    }

    public void setInteligencia(int inteligencia) {
        this.inteligencia = inteligencia;
    }

    public boolean isPuedeVolar() {
        return puedeVolar;
    }

    public void setPuedeVolar(boolean puedeVolar) {
        this.puedeVolar = puedeVolar;
    }

    public int getEconomia() {
        return economia;
    }

    public void setEconomia(int economia) {
        this.economia = economia;
    }

    public int getFuerza() {
        return fuerza;
    }

    public void setFuerza(int fuerza) {
        this.fuerza = fuerza;
    }
}