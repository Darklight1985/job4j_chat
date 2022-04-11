package ru.job4j.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;
import ru.job4j.domain.Person;
import ru.job4j.domain.Room;
import ru.job4j.handlers.Operation;
import ru.job4j.repository.RoomRepository;

import javax.validation.Valid;
import java.util.List;

import static java.util.Objects.isNull;

@RestController
@RequestMapping("/room")
public class RoomController {
    private final RoomRepository roomRepository;

    @Autowired
    private RestTemplate rest;

    public RoomController(RoomRepository rooms) {
        this.roomRepository = rooms;
    }

    @GetMapping("/")
    public List<Room> findAll() {
        List<Room> rooms = (List<Room>) roomRepository.findAll();
        return rooms;
    }

    /**
     * Получить всех пользователей в комнате
     */
    @GetMapping("/{id}")
    public List<Person> findAllPersons(@PathVariable int id) {
      Room room = roomRepository.findById(id).get();
      return room.getPersons();
    }

    /**
     * Добавить пользователя в комнату
     * @param id
     * @param person
     * @return
     */
    @PutMapping("/{id}")
    public ResponseEntity<Void> addPerson(@Valid @PathVariable int id, @RequestBody Person person) {
        if (isNull(person.getNickname())) {
            throw new NullPointerException("Nickname mustn't be empty");
        }
        var room = roomRepository.findById(id);
        if (isNull(room)) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } else {
            room.get().addPerson(person);
            return ResponseEntity.ok().build();
        }
    }

    /**
     * Удалить пользователя из комнаты
     * @param id
     * @param person
     * @return
     */
    @DeleteMapping("/{id}")
    @Validated(Operation.OnDelete.class)
    public ResponseEntity<Void> deletePerson(@Valid @PathVariable int id, @RequestBody Person person) {
        var room = roomRepository.findById(id);
        if (isNull(room)) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } else {
            room.get().deletePerson(person);
            return ResponseEntity.ok().build();
        }
    }
}
