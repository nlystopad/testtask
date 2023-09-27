package com.lystopad.testtask.persistent.repository;

import com.lystopad.testtask.persistent.entity.Users;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface UsersRepository extends JpaRepository<Users, Long> {

    @Query("SELECT u FROM Users u WHERE u.birthDate BETWEEN :start AND :end")
    List<Users> getUsersByDateBetween(@Param("start") LocalDate start, @Param("end") LocalDate end);
}
