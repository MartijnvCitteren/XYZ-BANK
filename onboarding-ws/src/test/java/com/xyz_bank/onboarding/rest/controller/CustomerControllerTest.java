package com.xyz_bank.onboarding.rest.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.xyz_bank.onboarding.exception.IbanGenerationException;
import com.xyz_bank.onboarding.exception.XyzDataAccessException;
import com.xyz_bank.onboarding.factory.AddressDtoFactory;
import com.xyz_bank.onboarding.factory.RegistrationRequestDtoFactory;
import com.xyz_bank.onboarding.rest.CustomerController;
import com.xyz_bank.onboarding.rest.dto.LoginRequestDto;
import com.xyz_bank.onboarding.rest.dto.LoginResponseDto;
import com.xyz_bank.onboarding.rest.dto.RegistrationRequestDto;
import com.xyz_bank.onboarding.rest.dto.RegistrationResponseDto;
import com.xyz_bank.onboarding.service.CustomerRegistrationService;
import com.xyz_bank.onboarding.service.CustomerService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(CustomerController.class)
class CustomerControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private CustomerRegistrationService customerRegistrationService;

    @MockitoBean
    private CustomerService customerService;

    private RegistrationResponseDto responseDto;

    @BeforeEach
    void setUp() {
        responseDto = RegistrationResponseDto.builder().username("username").iban("iban").password("password").build();
    }

    @Test
    void givenValidRegistrationRequest_whenRegister_thenReturn201Created() throws Exception {
        //given
        var registration = RegistrationRequestDtoFactory.createRegistrationRequestDto().build();
        when(customerRegistrationService.register(registration)).thenReturn(responseDto);

        //when
        ResultActions response = mockMvc.perform(post("/customer/register").contentType(MediaType.APPLICATION_JSON)
                                                         .content(objectMapper.writeValueAsString(registration)));

        //then
        response.andDo(print())
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.username").value("username"))
                .andExpect(jsonPath("$.iban").value("iban"))
                .andExpect(jsonPath("$.password").value("password"));
    }

    @Test
    void givenXyzDataAccessException_whenRegisterAndCallingService_thenReturn500() throws Exception {
        //given
        String errorMessage = "error this went wrong";
        var registration = RegistrationRequestDtoFactory.createRegistrationRequestDto().build();
        when(customerRegistrationService.register(registration)).thenThrow(new XyzDataAccessException(errorMessage));

        //when
        ResultActions response = mockMvc.perform(post("/customer/register")
                                                         .contentType(MediaType.APPLICATION_JSON)
                                                         .content(objectMapper.writeValueAsString(registration)));

        //then
        response.andDo(print()).andExpect(status().isInternalServerError());
        assertEquals(errorMessage, response.andReturn().getResponse().getContentAsString());
    }

    @Test
    void givenIbanGenerationException_whenRegisterAndCallingService_thenReturn500() throws Exception {
        //given
        String errorMessage = "error this IBAN went wrong";
        var registration = RegistrationRequestDtoFactory.createRegistrationRequestDto().build();
        when(customerRegistrationService.register(registration)).thenThrow(new IbanGenerationException(errorMessage));

        //when
        ResultActions response = mockMvc.perform(post("/customer/register")
                                                         .contentType(MediaType.APPLICATION_JSON)
                                                         .content(objectMapper.writeValueAsString(registration)));

        //then
        response.andDo(print()).andExpect(status().isInternalServerError());
        assertEquals(errorMessage, response.andReturn().getResponse().getContentAsString());
    }

    @ParameterizedTest
    @MethodSource()
    void givenInvalidRegistrationRequest_whenRegister_thenReturn400BadRequest(RegistrationRequestDto request)
            throws Exception {
        //when
        ResultActions response = mockMvc.perform(post("/customer/register")
                                                         .contentType(MediaType.APPLICATION_JSON)
                                                         .content(objectMapper.writeValueAsString(request)));

        //then
        response.andDo(print()).andExpect(status().isBadRequest());
    }


    @Test
    void givenValidLoginRequest_whenLogin_thenReturn200Ok() throws Exception {
        //given
        LoginRequestDto loginRequest = new LoginRequestDto("username", "password");
        LoginResponseDto loginResponseDto = new LoginResponseDto("token123");
        when(customerService.login(loginRequest)).thenReturn(loginResponseDto);

        //when
        ResultActions response = mockMvc.perform(post("/customer/login")
                                                         .contentType(MediaType.APPLICATION_JSON)
                                                         .content(objectMapper.writeValueAsString(loginRequest)));
        //then
        response.andDo(print()).andExpect(status().isOk());
    }

    @Test
    void givenInvalidLoginRequest_whenLogin_thenReturn400BadRequest() throws Exception {
        //given
        LoginRequestDto loginRequest = new LoginRequestDto(null, "password");

        //when
        ResultActions response = mockMvc.perform(post("/customer/login")
                                                         .contentType(MediaType.APPLICATION_JSON)
                                                         .content(objectMapper.writeValueAsString(loginRequest)));

        //then
        response.andDo(print()).andExpect(status().isBadRequest());
    }




    private static Stream<Arguments> givenInvalidRegistrationRequest_whenRegister_thenReturn400BadRequest() {
        return Stream.of(Arguments.of(RegistrationRequestDtoFactory.createRegistrationRequestDto().email("").build()),
                         Arguments.of(RegistrationRequestDtoFactory.createRegistrationRequestDto().email(null).build()),
                         Arguments.of(
                                 RegistrationRequestDtoFactory.createRegistrationRequestDto().email("x@mail.").build()),
                         Arguments.of(
                                 RegistrationRequestDtoFactory.createRegistrationRequestDto().lastname("").build()),
                         Arguments.of(
                                 RegistrationRequestDtoFactory.createRegistrationRequestDto().lastname(null).build()),
                         Arguments.of(
                                 RegistrationRequestDtoFactory.createRegistrationRequestDto().firstname("").build()),
                         Arguments.of(
                                 RegistrationRequestDtoFactory.createRegistrationRequestDto().dateOfBirth("").build()),
                         Arguments.of(RegistrationRequestDtoFactory.createRegistrationRequestDto().dateOfBirth("91-01/01")
                                              .build()),
                         Arguments.of(RegistrationRequestDtoFactory.createRegistrationRequestDto()
                                              .dateOfBirth("01-01-199").build()),

                         Arguments.of(RegistrationRequestDtoFactory.createRegistrationRequestDto()
                                              .dateOfBirth(null)
                                              .build()),
                         Arguments.of(RegistrationRequestDtoFactory.createRegistrationRequestDto().address(null).build()),
                         Arguments.of(RegistrationRequestDtoFactory.createRegistrationRequestDto()
                                              .address(AddressDtoFactory.createAddressDto().street("").build())
                                              .build()),
                         Arguments.of(RegistrationRequestDtoFactory.createRegistrationRequestDto()
                                .address(AddressDtoFactory.createAddressDto().country(null).build())
                                .build()),
                         Arguments.of(RegistrationRequestDtoFactory.createRegistrationRequestDto()
                                                                .address(AddressDtoFactory.createAddressDto()
                                                                                 .houseNumber("")
                                                                                 .build())
                                                                .build()),
                         Arguments.of(RegistrationRequestDtoFactory.createRegistrationRequestDto()
                                .address(AddressDtoFactory.createAddressDto().city("").build())
                                .build()));
    }


}