package ru.job4j.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.fasterxml.jackson.databind.SerializationFeature;
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
import ru.job4j.model.Person;
import ru.job4j.repository.MessageRepository;
import ru.job4j.repository.UserStore;

import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpServletResponseWrapper;
import javax.xml.bind.SchemaOutputResolver;
import java.util.Optional;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.verify;
import static org.springframework.http.MediaType.APPLICATION_JSON_UTF8;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest(classes = Job4jChatApplication.class)
@AutoConfigureMockMvc
public class MessageControllerTest {
    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private MessageRepository messageRepository;
    @MockBean
    private UserStore userStore;

    @Test
    @WithMockUser
    @Ignore
    public void whenAddPatch() throws Exception {
        LinkedMultiValueMap<String, String> requestParams = new LinkedMultiValueMap<>();
        requestParams.add("id", "1");
        requestParams.add("description","message");
        requestParams.add("personId", "1");
        Message message = new Message();
        message.setId(1);
        message.setDescription("message");
        message.setPersonId(1);
        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(SerializationFeature.WRAP_ROOT_VALUE, false);
        ObjectWriter ow = mapper.writer().withDefaultPrettyPrinter();
        String requestJson=ow.writeValueAsString(message);
        Mockito.when(messageRepository.findById(Mockito.anyInt())).thenReturn(Optional.of(message));
        this.mockMvc.perform(patch("/message/send")
                        .contentType(APPLICATION_JSON_UTF8)
                        .content(requestJson))
                .andDo(print())
                .andExpect(status().isOk());
        ArgumentCaptor<Message> argument = ArgumentCaptor.forClass(Message.class);
        verify(messageRepository).save(argument.capture());
        assertThat(argument.getValue().getDescription(), is("message"));
    }

    @Test
    public void whenWriteMApper() throws Exception {
        Message message = new Message();
        message.setId(1);
        message.setDescription("message");
        message.setPersonId(1);
        ObjectMapper mapper = new ObjectMapper();
        String str = mapper.writeValueAsString(message);
        String json  = "{"
        + "\"id\":1,"
                + "\"description\":\"message\","
                + "\"personId\":1}";
        System.out.println(str);
        assertThat(str, is(json));
    }
}