package com.lystopad.testtask.service;

import com.lystopad.testtask.persistent.entity.UserForPartialUpdate;
import com.lystopad.testtask.persistent.entity.Users;

import java.time.LocalDate;
import java.util.List;

public interface UsersService {
    Users addUser(Users user);
    Users updateUserById(Long id, Users newUser);
    Users updateUserByIdPartial(Long id, UserForPartialUpdate newUser);
    void deleteUserById(Long id);
    List<Users> getUsersBirthDateBetween(LocalDate start, LocalDate end);
}
