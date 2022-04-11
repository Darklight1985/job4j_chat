package ru.job4j.domain;

import ru.job4j.handlers.Operation;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Entity
public class Room {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @NotNull(message = "Id must be non null",
            groups = {Operation.OnUpdate.class, Operation.OnDelete.class})
    private int id;

    @NotBlank(message = "Name of room must be not empty")
    private String name;
    @Transient
    private List<@NotBlank(message = "Need to set person") Person> persons = new ArrayList<>();
    @Transient
    private List<Message> messages = new ArrayList<>();

    public void addPerson(Person person) {
        persons.add(person);
    }

    public void addMessage(Message message) {
        messages.add(message);
    }

    public void deletePerson(Person person) {
        persons.remove(person);
    }

    public List<Person> getPersons() {
        return persons;
    }

    public void setPersons(List<Person> persons) {
        this.persons = persons;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<Message> getMessages() {
        return messages;
    }

    public void setMessages(List<Message> messages) {
        this.messages = messages;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Room room = (Room) o;
        return id == room.id && Objects.equals(name, room.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name);
    }
}
