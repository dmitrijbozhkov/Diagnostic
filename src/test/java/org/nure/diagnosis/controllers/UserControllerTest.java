package org.nure.diagnosis.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.nure.diagnosis.exchangemodels.usercontroller.AuthToken;
import org.nure.diagnosis.exchangemodels.usercontroller.ChangePassword;
import org.nure.diagnosis.exchangemodels.usercontroller.CreateUserCredentials;
import org.nure.diagnosis.exchangemodels.usercontroller.UserCredentials;
import org.nure.diagnosis.models.enums.Gender;
import org.nure.diagnosis.models.enums.PersonAuthorities;
import org.nure.diagnosis.security.JwtTokenProvider;
import org.nure.diagnosis.services.interfaces.IPersonService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.util.Arrays;
import java.util.Date;

import static org.junit.Assert.assertEquals;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@AutoConfigureMockMvc
@SpringBootTest
public class UserControllerTest {

    @Autowired
    private MockMvc mvc;
    @MockBean
    private IPersonService userServiceMock;
    @MockBean
    private JwtTokenProvider tokenProvider;
    private static ObjectMapper map;

    // Default user
    private final String email = "matviei@gmail.com";
    private final String password = "pass1234P";
    private final String name = "Matthew";
    private final String surname = "Serbull";
    private final String lastName = "Oleksandrovich";
    private final Gender gender = Gender.MALE;
    private final Date birthday = new Date();

    @BeforeClass
    public static void setup() {
        map = new ObjectMapper();
    }

    @Test
    public void testSigninShouldReturnOk() throws Exception {
        CreateUserCredentials credentials = new CreateUserCredentials(name, surname, lastName, gender, birthday, email, password);
        mvc.perform(post("/api/user/signin")
                .contentType(MediaType.APPLICATION_JSON).content(map.writeValueAsString(credentials)))
                .andExpect(status().isOk());
    }

    @Test
    public void testSigninShouldCreateUser() throws Exception {
        CreateUserCredentials credentials = new CreateUserCredentials(name, surname, lastName, gender, birthday, email, password);
        mvc.perform(post("/api/user/signin")
                .contentType(MediaType.APPLICATION_JSON).content(map.writeValueAsString(credentials)));
        verify(userServiceMock).createUser(
                Arrays.asList(PersonAuthorities.PATIENT),
                credentials.getName(),
                credentials.getSurname(),
                credentials.getLastName(),
                credentials.getGender(),
                credentials.getBirthday(),
                credentials.getEmail(),
                credentials.getPassword()
        );
    }

    @Test
    public void testLoginShouldAuthenticateUser() throws Exception {
        UserCredentials credentials = new UserCredentials(email, password);
        String token = "sdfsdfwernmnlkdmfgkmk324k3j5knklkKNDLKDNFSoiw43j0";
        given(tokenProvider.generateToken(null)).willReturn(token);
        mvc.perform(post("/api/user/login").contentType(MediaType.APPLICATION_JSON).content(map.writeValueAsString(credentials)));
        verify(userServiceMock).authenticate(credentials.getEmail(), credentials.getPassword());
    }

    @Test
    public void testLoginShouldReturnAuthTokenWIthToken() throws Exception {
        UserCredentials credentials = new UserCredentials(email, password);
        String token = "sdfsdfwernmnlkdmfgkmk324k3j5knklkKNDLKDNFSoiw43j0";
        given(tokenProvider.generateToken(null)).willReturn(token);
        MvcResult result = mvc.perform(post("/api/user/login").contentType(MediaType.APPLICATION_JSON).content(map.writeValueAsString(credentials)))
                .andExpect(status().isOk())
                .andReturn();
        String tokenObj = result.getResponse().getContentAsString();
        assertEquals(map.writeValueAsString(new AuthToken(token)), tokenObj);
    }

    @Test
    @WithMockUser(username = email, authorities = { "ROLE_PATIENT" })
    public void testChangePasswordShouldCallChangePasswordWithSuppliedData() throws Exception {
        String nextPassword = "newpass1234P";
        ChangePassword changePassword = new ChangePassword(nextPassword, password);
        mvc.perform(post("/api/user/change-password").contentType(MediaType.APPLICATION_JSON).content(map.writeValueAsString(changePassword)))
                .andExpect(status().isOk());
        verify(userServiceMock).changePassword(email, nextPassword, password);
    }
}
