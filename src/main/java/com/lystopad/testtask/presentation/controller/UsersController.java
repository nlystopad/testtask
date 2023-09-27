package com.lystopad.testtask.presentation.controller;

import com.lystopad.testtask.persistent.entity.UserForPartialUpdate;
import com.lystopad.testtask.persistent.entity.Users;
import com.lystopad.testtask.service.UsersServiceImpl;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@Validated
@RequestMapping(value = "/api/v1", produces = MediaType.APPLICATION_JSON_VALUE)
public class UsersController {

    @Autowired
    private UsersServiceImpl userService;

    @PostMapping(value = "/users")
    public ResponseEntity<Users> createUser(@Valid @RequestBody Users user) {
        Users created = userService.addUser(user);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    @PatchMapping(value = "/user/{id}")
    public ResponseEntity<Users> updateUserByIdPartial(@PathVariable Long id,
                                                       @Valid @RequestBody UserForPartialUpdate users) {
        Users updated = userService.updateUserByIdPartial(id, users);
        return ResponseEntity.ok(updated);
    }

    @PutMapping(value = "/user/{id}")
    public ResponseEntity<Users> updateUserById(@PathVariable Long id,
                                                @Valid @RequestBody Users users) {
        Users updated = userService.updateUserById(id, users);
        return ResponseEntity.ok(updated);
    }

    @DeleteMapping(value = "/user/{id}")
    public ResponseEntity<String> deleteUserById(@PathVariable Long id) {
        userService.deleteUserById(id);
        return ResponseEntity.ok(String.format("User %d deleted", id));
    }

    @GetMapping(value = "/users")
    public ResponseEntity<List<Users>> getUsersByDateRange(@RequestParam("start")
                                                           @DateTimeFormat() String start,
                                                           @RequestParam("end")
                                                           @DateTimeFormat String end) {
        LocalDate startDate = LocalDate.parse(start);
        LocalDate endDate = LocalDate.parse(end);
        List<Users> users = userService.getUsersBirthDateBetween(startDate, endDate);
        return ResponseEntity.ok(users);
    }
}
