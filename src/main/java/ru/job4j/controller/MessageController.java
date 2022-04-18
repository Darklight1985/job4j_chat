package ru.job4j.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import ru.job4j.domain.Message;
import ru.job4j.handlers.Operation;
import ru.job4j.repository.MessageRepository;
import ru.job4j.repository.RoomRepository;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;

@RestController
@RequestMapping("/message")
public class MessageController {

    private static final Logger LOGGER =
            LoggerFactory.getLogger(UserController.class.getSimpleName());
    private final RoomRepository roomRepository;
    private final MessageRepository messages;
    private final ObjectMapper objectMapper;

    public MessageController(RoomRepository roomRepository,
                             MessageRepository messages, ObjectMapper objectMapper) {
        this.roomRepository = roomRepository;
        this.messages = messages;
        this.objectMapper = objectMapper;
    }

    /**
     * Опубликовать сообщение в комнате
     * @param id
     * @param message
     * @return
     */
    @PostMapping("/{id}")
    @Validated(Operation.OnCreate.class)
    public ResponseEntity<Message> create(@Valid @PathVariable int id,
                                          @RequestBody Message message) {
        if (message.getDescription().equals("")) {
            throw new IllegalArgumentException("Message is empty");
        }
        var room = roomRepository.findById(id);
        if (room.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND,
                    "Room is not found. Please check name of room");
        } else {
          room.get().addMessage(message);
          return new ResponseEntity<>(
            message,
            HttpStatus.ACCEPTED
          );
        }
    }

    @PatchMapping("/send")
    public ResponseEntity<Message> send(@RequestBody Message message)
            throws InvocationTargetException, IllegalAccessException {
        var current = messages.findById(message.getId());
        if (current == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(messages.save(message), HttpStatus.OK);

    }

    @ExceptionHandler(value = { IllegalArgumentException.class })
    public void exceptionHandler(Exception e, HttpServletRequest request,
                                 HttpServletResponse response) throws IOException {
        response.setStatus(HttpStatus.BAD_REQUEST.value());
        response.setContentType("application/json");
        response.getWriter().write(objectMapper.writeValueAsString(new HashMap<>() { {
            put("message", e.getMessage());
            put("type", e.getClass());
        }}));
        LOGGER.error(e.getLocalizedMessage());
    }
}
