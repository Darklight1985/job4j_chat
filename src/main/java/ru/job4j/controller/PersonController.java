package ru.job4j.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import ru.job4j.domain.Person;
import ru.job4j.handlers.Operation;
import ru.job4j.repository.PersonRepository;
import ru.job4j.repository.RoleRepository;

import javax.validation.Valid;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
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
    @Validated(Operation.OnCreate.class)
    public ResponseEntity<Person> create(@Valid @RequestBody Person person) {
        if (isNull(person.getNickname())) {
            throw new NullPointerException("Nickname mustn't be empty");
        }
        person.setRole(roles.findById(1).get());
        return new ResponseEntity<>(
                this.persons.save(person),
                HttpStatus.CREATED
        );
    }

    @PatchMapping("/update")
    public ResponseEntity<Person> patch(@RequestBody Person person)
            throws InvocationTargetException, IllegalAccessException {
            var current = persons.findById(person.getId());
            if (current.isEmpty()) {
                throw new ResponseStatusException(HttpStatus.NOT_FOUND);
            }
            var methods = current.getClass().getDeclaredMethods();
            var namePerMethod = new HashMap<String, Method>();
            for (var method: methods) {
                var name = method.getName();
                if (name.startsWith("get") || name.startsWith("set")) {
                    namePerMethod.put(name, method);
                }
            }
            for (var name : namePerMethod.keySet()) {
                if (name.startsWith("get")) {
                    var getMethod = namePerMethod.get(name);
                    var setMethod = namePerMethod.get(name.replace("get", "set"));
                    if (setMethod == null) {
                        throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                                "Invalid properties mapping");
                    }
                    var newValue = getMethod.invoke(person);
                    if (newValue != null) {
                        setMethod.invoke(current, newValue);
                    }
                }
            }
        return new ResponseEntity<>(
                this.persons.save(person),
                HttpStatus.CREATED
        );
    }

    @PutMapping("/")
    @Validated(Operation.OnUpdate.class)
    public ResponseEntity<Void> update(@Valid @RequestBody Person person) {
        if (isNull(person.getNickname())) {
            throw new NullPointerException("Nickname mustn't be empty");
        }
        this.persons.save(person);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{id}")
    @Validated(Operation.OnDelete.class)
    public ResponseEntity<Void> delete(@Valid @PathVariable int id) {
        Person person = new Person();
        person.setId(id);
        this.persons.delete(person);
        return ResponseEntity.ok().build();
    }
}
