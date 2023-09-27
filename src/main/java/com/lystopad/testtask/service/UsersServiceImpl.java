package com.lystopad.testtask.service;

import com.lystopad.testtask.infrastructure.exceptions.AgeIsLessThanEighteenException;
import com.lystopad.testtask.infrastructure.exceptions.UserNotFoundException;
import com.lystopad.testtask.persistent.entity.UserForPartialUpdate;
import com.lystopad.testtask.persistent.entity.Users;
import com.lystopad.testtask.persistent.repository.UsersRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class UsersServiceImpl implements UsersService {
    @Value("${environment.age}")
    private int age;
    @Autowired
    private UsersRepository repository;

    @Override
    public Users addUser(Users user) {
        if (user.getBirthDate().isAfter(LocalDate.now().minusYears(age))) {
            throw new AgeIsLessThanEighteenException(String.format("Sorry, but user must be at least %d years old", age));
        }
        return repository.save(user);
    }

    @Override
    public Users updateUserById(Long id, Users newUser) {
        return repository.findById(id).map(entity -> {
                    entity.setAddress(newUser.getAddress());
                    entity.setEmail(newUser.getEmail());
                    entity.setBirthDate(newUser.getBirthDate());
                    entity.setFirstName(newUser.getFirstName());
                    entity.setLastName(newUser.getLastName());
                    entity.setPhoneNumber(newUser.getPhoneNumber());
                    return repository.save(entity);
                })
                .orElseThrow(() -> new UserNotFoundException(String.format("Sorry, but user with id %d was not found", id)));
    }

    @Override
    public Users updateUserByIdPartial(Long id, UserForPartialUpdate newUser) {
        Users oldUser = repository.findById(id).orElseThrow(() -> new UserNotFoundException(String.format("Sorry, but user with id %d was not found", id)));
        if (newUser.getFirstName() != null) oldUser.setFirstName(newUser.getFirstName());
        if (newUser.getLastName() != null) oldUser.setLastName(newUser.getLastName());
        if (newUser.getEmail() != null) oldUser.setEmail(newUser.getEmail());
        if (newUser.getBirthDate() != null) oldUser.setBirthDate(newUser.getBirthDate());
        if (newUser.getAddress() != null) oldUser.setAddress(newUser.getAddress());
        if (newUser.getPhoneNumber() != null) oldUser.setPhoneNumber(newUser.getPhoneNumber());
        return repository.save(oldUser);
    }

    @Override
    public void deleteUserById(Long id) {
        Users user = repository.findById(id).
                orElseThrow(() -> new UserNotFoundException(String.format("Sorry, but user with id %d was not found", id)));
        repository.delete(user);
    }

    @Override
    public List<Users> getUsersBirthDateBetween(LocalDate start, LocalDate end) {
        if (end.isBefore(start)) {
            throw new AgeIsLessThanEighteenException("Sorry, you put wrong date");
        }
        return repository.getUsersByDateBetween(start, end);
    }
}
