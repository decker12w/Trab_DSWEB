package org.example.trab_dsweb.models;

import org.example.trab_dsweb.enums.Gender;
import java.time.LocalDate;


public class User {
    private boolean admin;
    private String email;
    private String password;
    private String cpf;
    private String name;
    private Gender gender;
    private LocalDate birthDate;
}
