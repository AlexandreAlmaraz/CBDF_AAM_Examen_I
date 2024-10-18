package com.upiiz.users.repository;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.upiiz.users.models.User;
import org.springframework.stereotype.Repository;


import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;

@Repository
public class UsersRepository {
    private static final String FILE_PATH = "src/main/resources/usuarios.json";
    private List<User> users = new ArrayList<>();
    private AtomicLong id = new AtomicLong();

    public UsersRepository() {
        // Cargar usuarios del archivo JSON cuando se inicialice el repositorio
        this.leerUsuariosDeArchivo();
    }

    // Método para guardar un nuevo usuario
    public User guardar(User user) {
        user.setId(this.id.incrementAndGet());
        this.users.add(user);
        this.guardarUsuariosEnArchivo();  // Guardar cambios en el archivo JSON
        return user;
    }

    // Método para obtener todos los usuarios
    public List<User> obtenerTodas() {
        return this.users;
    }

    // Método para obtener un usuario por su ID
    public User obtenerPorId(Long id) {
        return this.users.stream().filter(user -> user.getId().equals(id))
                .findFirst()
                .orElse(null);
    }

    // Método para eliminar un usuario por su ID
    public void eliminar(Long id) {
        this.users.removeIf(user -> user.getId().equals(id));
        this.guardarUsuariosEnArchivo();  // Guardar cambios en el archivo JSON
    }

    // Método para actualizar un usuario
    public User actualizar(User user) {
        this.eliminar(user.getId());
        this.users.add(user);
        this.guardarUsuariosEnArchivo();  // Guardar cambios en el archivo JSON
        return user;
    }

    // Método privado para leer los usuarios desde el archivo JSON
    private void leerUsuariosDeArchivo() {
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            // Registrar el módulo para manejar Java 8 fechas
            objectMapper.registerModule(new JavaTimeModule());

            File archivo = new File(FILE_PATH);
            if (archivo.exists()) {
                User[] usersArray = objectMapper.readValue(archivo, User[].class);
                this.users = new ArrayList<>(Arrays.asList(usersArray));

                // Ajustar el contador de IDs al último ID en la lista
                if (!this.users.isEmpty()) {
                    Long maxId = this.users.stream().mapToLong(User::getId).max().orElse(0L);
                    this.id.set(maxId);
                }
            }
        } catch (IOException e) {
            throw new RuntimeException("Error al leer el archivo JSON", e);
        }
    }

    // Método privado para guardar los usuarios en el archivo JSON
    private void guardarUsuariosEnArchivo() {
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            // Registrar el módulo para manejar Java 8 fechas
            objectMapper.registerModule(new JavaTimeModule());

            objectMapper.writerWithDefaultPrettyPrinter().writeValue(new File(FILE_PATH), this.users);
        } catch (IOException e) {
            throw new RuntimeException("Error al guardar el archivo JSON", e);
        }
    }
}
