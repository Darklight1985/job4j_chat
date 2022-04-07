package ru.job4j.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.job4j.domain.Person;
import ru.job4j.repository.PersonRepository;
import ru.job4j.repository.RoleRepository;

import java.util.List;

import static java.util.Objects.isNull;

@RestController
@RequestMapping("/person")
public class PersonController {
    private final PersonRepository persons;
    private final RoleRepository roles;

    public PersonController(final PersonRepository persons, final RoleRepository roles) {
        this.persons = persons;
        this.roles = roles;
    }

    /**
     * Получить список всех зарегестрированных пользователей
     * @return
     */
    @GetMapping("/")
    public List<Person> findAll() {
        List<Person> personList = (List<Person>) persons.findAll();
        return personList;
    }

    @GetMapping("/{id}")
    public ResponseEntity<Person> findById(@PathVariable int id) {
        var person = this.persons.findById(id);
        ResponseEntity responseEntity = new ResponseEntity<>(
                person.orElse(new Person()),
                person.isPresent() ? HttpStatus.OK : HttpStatus.NOT_FOUND
        );
        return  responseEntity;
    }

    @PostMapping("/")
    public ResponseEntity<Person> create(@RequestBody Person person) {
        if (isNull(person.getNickname())) {
            throw new NullPointerException("Nickname mustn't be empty");
        }
        person.setRole(roles.findById(1).get());
        return new ResponseEntity<>(
                this.persons.save(person),
                HttpStatus.CREATED
        );
    }

    @PutMapping("/")
    public ResponseEntity<Void> update(@RequestBody Person person) {
        if (isNull(person.getNickname())) {
            throw new NullPointerException("Nickname mustn't be empty");
        }
        this.persons.save(person);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable int id) {
        Person person = new Person();
        person.setId(id);
        this.persons.delete(person);
        return ResponseEntity.ok().build();
    }
}
