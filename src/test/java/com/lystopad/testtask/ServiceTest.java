package com.lystopad.testtask;

import com.lystopad.testtask.infrastructure.exceptions.UserNotFoundException;
import com.lystopad.testtask.persistent.entity.UserForPartialUpdate;
import com.lystopad.testtask.persistent.entity.Users;
import com.lystopad.testtask.persistent.repository.UsersRepository;
import com.lystopad.testtask.service.UsersServiceImpl;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)

public class ServiceTest {
    @Mock
    private UsersRepository repository;

    @InjectMocks
    private UsersServiceImpl service;

    @Test
    public void whenSaveUsersShouldReturnUser() {
        Users user = Users.builder().firstName("first name").lastName("last name").birthDate(LocalDate.of(2000, 1, 1))
                .email("name@mail.com").build();

        when(repository.save(any(Users.class))).thenReturn(user);

        Users created = service.addUser(user);

        assertThat(created.getFirstName()).isSameAs(user.getFirstName());
        assertThat(created.getLastName()).isSameAs(user.getLastName());
        assertThat(created.getBirthDate()).isSameAs(user.getBirthDate());
        assertThat(created.getEmail()).isSameAs(user.getEmail());

        verify(repository).save(user);

    }

    @Test
    public void whenUpdateByIdShouldReturnUser() {
        Users user = Users.builder().id(1L).firstName("first name").lastName("last name").birthDate(LocalDate.of(2000, 1, 1))
                .email("name@mail.com").build();
        Users updated = Users.builder().id(1L).firstName("first name 2").lastName("last name 2").birthDate(LocalDate.of(2000, 2, 2))
                .email("name2@mail.com").build();

        when(repository.findById(any())).thenReturn(Optional.of(user));
        when(repository.save(any())).thenReturn(updated);

        Users afterUpdate = service.updateUserById(user.getId(), updated);


        assertThat(afterUpdate.getId()).isEqualTo(user.getId());
        assertThat(afterUpdate.getFirstName()).isSameAs(updated.getFirstName());
        assertThat(afterUpdate.getLastName()).isSameAs(updated.getLastName());
        assertThat(afterUpdate.getBirthDate()).isSameAs(updated.getBirthDate());
        assertThat(afterUpdate.getEmail()).isSameAs(updated.getEmail());
        assertThat(afterUpdate.getAddress()).isNull();
        assertThat(afterUpdate.getPhoneNumber()).isNull();

    }

    @Test(expected = UserNotFoundException.class)
    public void shouldThrowExceptionWhenUserNotFound() {
        Users user = Users.builder().id(5L).firstName("first name").lastName("last name").birthDate(LocalDate.of(2000, 1, 1))
                .email("name@mail.com").build();

        given(repository.findById(anyLong())).willReturn(Optional.empty());

        service.updateUserById(user.getId(), user);
    }

    @Test
    public void whenPartialUpdateByIdShouldReturnUser() {
        Users user = Users.builder().id(1L).firstName("first name").lastName("last name").birthDate(LocalDate.of(2000, 1, 1))
                .email("name@mail.com").build();
        UserForPartialUpdate userForPartialUpdate = UserForPartialUpdate.builder().firstName("updated").lastName("updated 2")
                .birthDate(LocalDate.of(2000, 1, 4)).address("updated address").build();
        Users expected = Users.builder().id(user.getId()).firstName(userForPartialUpdate.getFirstName())
                .lastName(userForPartialUpdate.getLastName()).email(user.getEmail())
                .birthDate(userForPartialUpdate.getBirthDate()).address(userForPartialUpdate.getAddress()).build();

        when(repository.findById(any())).thenReturn(Optional.of(user));
        when(repository.save(any())).thenReturn(expected);

        Users updated = service.updateUserByIdPartial(user.getId(), userForPartialUpdate);

        assertThat(updated.getId()).isEqualTo(expected.getId());
        assertThat(updated.getFirstName()).isEqualTo(expected.getFirstName());
        assertThat(updated.getLastName()).isEqualTo(expected.getLastName());
        assertThat(updated.getBirthDate()).isEqualTo(expected.getBirthDate());
        assertThat(updated.getEmail()).isEqualTo(expected.getEmail());
        assertThat(updated.getAddress()).isEqualTo(expected.getAddress());
        assertThat(updated.getPhoneNumber()).isNull();

    }

    @Test
    public void shouldReturnNothingAfterDeleting() {
        Users user = Users.builder().id(1L).firstName("first name").lastName("last name").birthDate(LocalDate.of(2000, 1, 1))
                .email("name@mail.com").build();
        when(repository.findById(user.getId())).thenReturn(Optional.of(user));
        service.deleteUserById(user.getId());

        verify(repository).delete(user);
    }

    @Test
    public void whenGetUsersByDateShouldReturnArray() {
        Users user = Users.builder().id(1L).firstName("first name").lastName("last name").birthDate(LocalDate.of(2000, 1, 1))
                .email("name@mail.com").build();
        when(repository.getUsersByDateBetween(LocalDate.of(1999, 1, 1), LocalDate.of(2000, 2, 2))).thenReturn(List.of(user));

        List<Users> users = service.getUsersBirthDateBetween(LocalDate.of(1999, 1, 1), LocalDate.of(2000, 2, 2));
        assertThat(users.size()).isEqualTo(1);

        verify(repository).getUsersByDateBetween(LocalDate.of(1999, 1, 1), LocalDate.of(2000, 2, 2));
    }

}
