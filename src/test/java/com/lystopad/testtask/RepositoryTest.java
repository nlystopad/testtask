package com.lystopad.testtask;

import com.lystopad.testtask.persistent.entity.Users;
import com.lystopad.testtask.persistent.repository.UsersRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.annotation.Rollback;

import java.time.LocalDate;
import java.util.List;

@DataJpaTest
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class RepositoryTest {

    @Autowired
    private UsersRepository repository;

    @Test
    @Order(1)
    @Rollback(value = false)
    public void saveUser() {
        Users user = Users.builder().email("user@example.com").firstName("John").lastName("Johnson")
                .birthDate(LocalDate.of(2000, 1, 1)).build();
        repository.save(user);

        Assertions.assertThat(user.getId()).isGreaterThan(0);
    }


    @Test
    @Order(2)
    public void getUsersTest() {
        Users user = repository.findById(1L).orElseThrow();

        Assertions.assertThat(user.getId()).isEqualTo(1);
        Assertions.assertThat(user.getEmail()).isEqualTo("user@example.com");
        Assertions.assertThat(user.getFirstName()).isEqualTo("John");
        Assertions.assertThat(user.getLastName()).isEqualTo("Johnson");
        Assertions.assertThat(user.getBirthDate()).isEqualTo("2000-01-01");

    }

    @Test
    @Order(3)
    public void getListOfPlanes() {
        List<Users> usersList = repository.findAll();

        Assertions.assertThat(usersList.size()).isGreaterThan(0);
    }

    @Test
    @Order(4)
    public void findByDate() {
        List<Users> usersByDateBetween = repository.getUsersByDateBetween(LocalDate.of(1999, 1, 1), LocalDate.of(2000, 1, 2));

        Assertions.assertThat(usersByDateBetween.size()).isEqualTo(1);
    }

    @Test
    @Order(5)
    public void updatePlaneTest() {
        Users user = repository.findById(1L).get();
        user.setAddress("Some random address");
        Users updated = repository.save(user);

        Assertions.assertThat(updated.getAddress()).isEqualTo("Some random address");
    }
}
