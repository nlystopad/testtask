package com.lystopad.testtask.persistent.entity;

import jakarta.validation.constraints.Past;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@Builder
public class UserForPartialUpdate {
    private String email;
    private String firstName;
    private String lastName;
    @Past
    private LocalDate birthDate;
    private String address;
    private String phoneNumber;


}
