package ru.job4j.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.job4j.domain.Message;
import ru.job4j.domain.Room;
import ru.job4j.repository.RoomRepository;

@RestController
@RequestMapping("/message")
public class MessageController {

    private final RoomRepository roomRepository;

    public MessageController(RoomRepository roomRepository) {
        this.roomRepository = roomRepository;
    }

    /**
     * Опубликовать сообщение в комнате
     * @param id
     * @param message
     * @return
     */
    @PostMapping("/{id}")
    public ResponseEntity<Message> create(@PathVariable int id, @RequestBody Message message) {
        var room = roomRepository.findById(id);
        if (room.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } else {
          room.get().addMessage(message);
          return new ResponseEntity<>(
            message,
            HttpStatus.ACCEPTED
          );
        }

    }
}
