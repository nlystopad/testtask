package com.lystopad.testtask;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.lystopad.testtask.persistent.entity.UserForPartialUpdate;
import com.lystopad.testtask.persistent.entity.Users;
import com.lystopad.testtask.service.UsersServiceImpl;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;

import java.time.LocalDate;
import java.util.List;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = UsersManagerApplication.class)
@AutoConfigureMockMvc
public class ControllerTest {
    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private UsersServiceImpl service;
    @Autowired
    ObjectMapper mapper;

    @Test
    public void createUserSuccess() throws Exception {
        Users user = Users.builder().firstName("name 1").lastName("name 2").email("email@mail.com")
                .birthDate(LocalDate.of(1990, 5, 5)).build();
        when(service.addUser(any(Users.class))).thenReturn(user);

        MockHttpServletRequestBuilder mockRequest = post("/api/v1/users")
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writer().withDefaultPrettyPrinter().writeValueAsString(user));

        mockMvc.perform(mockRequest)
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().is(201))
                .andExpect(jsonPath("$.email").value("email@mail.com"));
    }

    @Test
    public void updateUserByIdPartialSuccess() throws Exception {
        UserForPartialUpdate userForPartialUpdate = UserForPartialUpdate.builder().firstName("updated 1")
                .lastName("updated 2").email("email@mail.com").build();
        Users user = Users.builder().id(1L).firstName("updated 1").lastName("updated 2").email("email@mail.com")
                .birthDate(LocalDate.of(1990, 5, 5)).build();
        when(service.updateUserByIdPartial(anyLong(), any(UserForPartialUpdate.class))).thenReturn(user);

        MockHttpServletRequestBuilder mockRequest = patch("/api/v1/user/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writer().withDefaultPrettyPrinter().writeValueAsString(userForPartialUpdate));

        mockMvc.perform(mockRequest)
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().is(200))
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.firstName", is(userForPartialUpdate.getFirstName())));

    }

    @Test
    public void updateUserByIdSuccess() throws Exception {
        Users user = Users.builder().id(1L).firstName("updated 1").lastName("updated 2").email("email@mail.com")
                .birthDate(LocalDate.of(1990, 5, 5)).build();
        when(service.updateUserById(anyLong(), any(Users.class))).thenReturn(user);

        MockHttpServletRequestBuilder mockRequest = put("/api/v1/user/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writer().withDefaultPrettyPrinter().writeValueAsString(user));

        mockMvc.perform(mockRequest)
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().is(200))
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.firstName", is(user.getFirstName())));

    }

    @Test
    public void deleteUserByIdSuccess() throws Exception {
        Users user = Users.builder().id(1L).firstName("updated 1").lastName("updated 2").email("email@mail.com")
                .birthDate(LocalDate.of(1990, 5, 5)).build();

        MockHttpServletRequestBuilder mockRequest = delete("/api/v1/user/1");

        mockMvc.perform(mockRequest)
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().is(200))
                .andExpect(content().string("User 1 deleted"));

    }

    @Test
    public void getUsersByDateRangeSuccess() throws Exception {
        Users user = Users.builder().id(1L).firstName("updated 1").lastName("updated 2").email("email@mail.com")
                .birthDate(LocalDate.of(1990, 5, 5)).build();
        when(service.getUsersBirthDateBetween(LocalDate.of(1990, 1, 1), LocalDate.of(2000, 1, 1))).
                thenReturn(List.of(user));

        MockHttpServletRequestBuilder mockRequest = get("/api/v1/users?start=1990-01-01&end=2000-01-01");

        mockMvc.perform(mockRequest)
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().is(200))
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].id", is(1)))
                .andExpect(jsonPath("$[0].firstName", is("updated 1")));

    }
}
