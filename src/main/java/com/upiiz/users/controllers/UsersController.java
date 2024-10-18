package com.upiiz.users.controllers;

import com.upiiz.users.models.User;
import com.upiiz.users.services.UsersServices;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequestMapping("/api/v1/users")
@RestController
public class UsersController {
    // Requiero INYECTAR el servicio
    UsersServices usersServices;

    public UsersController(UsersServices usersService) {
        this.usersServices = usersService;
    }

    // GET - Todas las Suppliers
    @GetMapping
    public ResponseEntity<List<User>> getSuppliers() {
        return ResponseEntity.ok(usersServices.getAllSuppliers());  // Este también debería estar correcto
    }
    // GET - Solo un Supplier
    @GetMapping("/{id}")
    public ResponseEntity<User> getSupplier(@PathVariable Long id) {
        return ResponseEntity.ok(usersServices.getSupplierById(id));
    }

    // POST - Crear un Supplier
    @PostMapping
    public ResponseEntity<User> addSupplier(@RequestBody User user) {
        return ResponseEntity.ok(usersServices.createSupplier(user));
    }

    // PUT - Actualizar un Supplier
    @PutMapping("/{id}")
    public ResponseEntity<User> updateSupplier(@RequestBody User user, @PathVariable Long id) {
        user.setId(id);
        return ResponseEntity.ok(usersServices.updateSupplier(user));
    }

    // DELETE - Eliminar una Supplier por Id
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteSupplier(@PathVariable Long id) {
        usersServices.deleteSupplier(id);  // Ahora debería estar correcto
        return ResponseEntity.noContent().build();
    }
}
