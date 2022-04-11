package ru.job4j.controller;

import org.junit.Ignore;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.util.LinkedMultiValueMap;
import ru.job4j.domain.Message;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import ru.job4j.Job4jChatApplication;
import ru.job4j.repository.MessageRepository;

import java.util.Optional;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest(classes = Job4jChatApplication.class)
@AutoConfigureMockMvc
public class MessageControllerTest {
    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private MessageRepository messageRepository;

    @Test
    @Ignore
    @WithMockUser
    public void whenAddPatch() throws Exception {
        LinkedMultiValueMap<String, String> requestParams = new LinkedMultiValueMap<>();
        requestParams.add("id", "1");
        requestParams.add("description","message");
        requestParams.add("personId", "1");
        Message message = new Message();
        message.setId(1);
        message.setDescription("message");
        message.setPersonId(1);
        Mockito.when(messageRepository.findById(Mockito.anyInt())).thenReturn(Optional.of(message));
        this.mockMvc.perform(post("/message/send").params(requestParams))
                .andDo(print())
                .andExpect(status().is2xxSuccessful());
        ArgumentCaptor<Message> argument = ArgumentCaptor.forClass(Message.class);
        verify(messageRepository).save(argument.capture());
        assertThat(argument.getValue().getDescription(), is("message"));
    }
}