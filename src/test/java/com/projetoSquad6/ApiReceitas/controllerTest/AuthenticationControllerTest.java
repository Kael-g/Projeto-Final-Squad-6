package com.projetoSquad6.ApiReceitas.controllerTest;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.projetoSquad6.ApiReceitas.controller.AuthenticationController;
import com.projetoSquad6.ApiReceitas.mapper.RecipesMapper;
import com.projetoSquad6.ApiReceitas.model.UserModel;
import com.projetoSquad6.ApiReceitas.model.dto.AuthenticationDto;
import com.projetoSquad6.ApiReceitas.repository.UserRepository;
import com.projetoSquad6.ApiReceitas.security.TokenService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = AuthenticationController.class)
public class AuthenticationControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;


    @MockBean
    private TokenService tokenService;

    @MockBean
    private RecipesMapper recipesMapper;

    @MockBean
    private UserRepository userRepository;

    @MockBean
    private AuthenticationManager authenticationManager;

    @Test
    @WithMockUser(username = "yourUsername", password = "yourPassword")
    public void testSuccessfulLogin() throws Exception {

        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
                .thenReturn(new UsernamePasswordAuthenticationToken(new UserModel(), null));


        mockMvc.perform(post("/auth/login")
                        .content("{\"login\":\"yourUsername\", \"password\":\"yourPassword\"}").with(csrf())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

    }


    @Test
    @WithMockUser(username = "yourUsername", password = "yourPassword")
    public void testRegisterUser() throws Exception {
        AuthenticationDto data = new AuthenticationDto("testuser", "testpassword");
        String encryptedPassword = new BCryptPasswordEncoder().encode(data.getPassword());

        when(userRepository.findByLogin(any())).thenReturn(null);

        mockMvc.perform(MockMvcRequestBuilders.post("/auth/register")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(data)))
                .andDo(print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().string("Usuário Cadastrado com sucesso."));

        verify(userRepository, times(1)).findByLogin("testuser");
        verify(userRepository, times(1)).save(any(UserModel.class));
    }

    @Test
    @WithMockUser(username = "yourUsername", password = "yourPassword")
    public void testRegisterUserAlreadyExists() throws Exception {
        AuthenticationDto data = new AuthenticationDto("existinguser", "password");

        when(userRepository.findByLogin("existinguser")).thenReturn(new UserModel("existinguser", "encryptedPassword"));

        mockMvc.perform(MockMvcRequestBuilders.post("/auth/register")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(data)))
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andExpect(MockMvcResultMatchers.content().string("Usuário já existe."));
    }

}
