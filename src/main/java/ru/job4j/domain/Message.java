package ru.job4j.domain;

import ru.job4j.handlers.Operation;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.Objects;

@Entity
public class Message {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @NotNull(message = "Id must be non null",
            groups = {Operation.OnUpdate.class, Operation.OnDelete.class})
    private int id;

    @Size(min = 1, max = 200,
            message = "The message should not be empty, the maximum message length is 200 characters")
    private String description;

    @Column(name = "person_id")
    @NotNull(message = "You need to set the User")
    private int personId;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getPersonId() {
        return personId;
    }

    public void setPersonId(int personId) {
        this.personId = personId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Message message = (Message) o;
        return id == message.id && personId == message.personId
                && Objects.equals(description, message.description);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, description, personId);
    }
}
