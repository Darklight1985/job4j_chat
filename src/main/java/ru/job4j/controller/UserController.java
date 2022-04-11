package ru.job4j.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.job4j.handlers.Operation;
import ru.job4j.model.Person;
import ru.job4j.repository.UserStore;

import javax.validation.Valid;
import java.util.List;

import static java.util.Objects.isNull;

@RestController
@RequestMapping("/users")
public class UserController {

    private UserStore users;
    private BCryptPasswordEncoder encoder;

    public UserController(UserStore users,
                          BCryptPasswordEncoder encoder) {
        this.users = users;
        this.encoder = encoder;
    }

    @PostMapping("/sign-up")
    @Validated(Operation.OnCreate.class)
    public ResponseEntity<String> signUp(@Valid @RequestBody Person person) {
        if (isNull(person.getUsername()) || isNull(person.getPassword().isEmpty())) {
            throw new NullPointerException("Username and password mustn't be empty");
        }
        person.setPassword(encoder.encode(person.getPassword()));
        users.save(person);
        return new ResponseEntity<>(
                "Пользователь зарегистрирован",
                HttpStatus.ACCEPTED
        );
    }

    @GetMapping("/all")
    public ResponseEntity<List<Person>> findAll() {
        return new ResponseEntity<>(users.findAll(),
                HttpStatus.OK
                );
    }
}
