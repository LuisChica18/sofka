package com.advanceit.bankservice.controller;

import com.advanceit.bankservice.async.dto.ClientDTO;
import com.advanceit.bankservice.async.listener.ClientMessageListener;
import com.advanceit.bankservice.client.ClientClient;
import com.advanceit.bankservice.dto.AccountDTO;
import com.advanceit.bankservice.entity.Account;
import com.advanceit.bankservice.entity.Transaction;
import com.advanceit.bankservice.enums.AccountTypeEnum;
import com.advanceit.bankservice.enums.MovementTypeEnum;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import java.math.BigDecimal;
import java.util.Collections;

import static org.hamcrest.CoreMatchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
//@Import(TestConfig.class)
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
class AccountControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private ClientMessageListener messageListener;

    @MockitoBean
    private ClientClient clientClient;

    private AccountDTO accountDTO, accountDTO2;
    private Transaction transaction;
    private ClientDTO clientDTO;

    @BeforeEach
    void setUp() {

        // Create ClientDTO
        clientDTO = new ClientDTO();
        clientDTO.setId(1L);
        clientDTO.setName("Test Client");
        clientDTO.setClientId("1");

        accountDTO = new AccountDTO();
        accountDTO.setAccountNumber("TEST001");
        accountDTO.setAccountType(AccountTypeEnum.SAVINGS);
        accountDTO.setInitialBalance(new BigDecimal("1000"));
        accountDTO.setStatus(true);
        accountDTO.setClient(clientDTO);

        accountDTO2 = new AccountDTO();
        accountDTO2.setAccountNumber("TEST002");
        accountDTO2.setAccountType(AccountTypeEnum.SAVINGS);
        accountDTO2.setInitialBalance(new BigDecimal("1000"));
        accountDTO2.setStatus(true);
        accountDTO2.setClient(clientDTO);

        transaction = new Transaction();
        transaction.setAmount(new BigDecimal("100"));

        // Configure mocks
        Mockito.reset(messageListener, clientClient);
        Mockito.when(messageListener.getClientList()).thenReturn(Collections.singletonList(clientDTO));
        Mockito.when(clientClient.getClientById(Mockito.anyLong())).thenReturn(clientDTO);
    }

    @Test
    @DisplayName("Should create account successfully")
    void shouldCreateAccountSuccessfully() throws Exception {

        mockMvc.perform(post("/accounts")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(accountDTO)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.accountNumber", is(accountDTO.getAccountNumber())))
                .andExpect(jsonPath("$.accountType", is(accountDTO.getAccountType().toString())))
                .andExpect(jsonPath("$.initialBalance", is(accountDTO.getInitialBalance().intValue())))
                .andExpect(jsonPath("$.client.id", is(clientDTO.getId().intValue())));
    }

    @Test
    @DisplayName("Should fail creating account with duplicate number")
    void shouldFailCreatingDuplicateAccount() throws Exception {

        createAccount(accountDTO);

        mockMvc.perform(post("/accounts")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(accountDTO)))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message", containsString("duplicate account number")));
    }

    @Test
    @DisplayName("Should get account successfully")
    void shouldGetAccountSuccessfully() throws Exception {
        ResultActions createResult = createAccount(accountDTO);
        String accountId = getAccountIdFromResponse(createResult);

        mockMvc.perform(get("/accounts/{id}", accountId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.accountNumber", is(accountDTO.getAccountNumber())));
    }

    @Test
    @DisplayName("Should get all accounts successfully")
    void shouldGetAllAccountsSuccessfully() throws Exception {
        createAccount(accountDTO);

        mockMvc.perform(get("/accounts"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$[0].accountNumber", is(accountDTO.getAccountNumber())));
    }

    @Test
    @DisplayName("Should update account successfully")
    void shouldUpdateAccountSuccessfully() throws Exception {

        ResultActions createResult = createAccount(accountDTO);
        String accountId = getAccountIdFromResponse(createResult);
        accountDTO.setClient(clientDTO);

        mockMvc.perform(put("/accounts/{id}", accountId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(accountDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.initialBalance", is(accountDTO.getInitialBalance().intValue())));
    }

    @Test
    @DisplayName("No Should update account successfully")
    void noShouldUpdateAccountSuccessfully() throws Exception {

        ResultActions createResult = createAccount(accountDTO);
        String accountId = getAccountIdFromResponse(createResult);
        accountDTO.setInitialBalance(new BigDecimal("2000"));

        mockMvc.perform(put("/accounts/{id}", accountId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(accountDTO)))
                .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("Should delete account successfully")
    void shouldDeleteAccountSuccessfully() throws Exception {
        ResultActions createResult = createAccount(accountDTO);
        String accountId = getAccountIdFromResponse(createResult);

        mockMvc.perform(delete("/accounts/{id}", accountId))
                .andExpect(status().isNoContent());
    }

    @Test
    @DisplayName("Should register transaction successfully")
    void shouldRegisterTransactionSuccessfully() throws Exception {

        ResultActions createResult = createAccount(accountDTO2);
        String accountId = getAccountIdFromResponse(createResult);
        String numberAccount = getNumberAccountIdFromResponse(createResult);

        Account account = new Account();
        account.setId(Long.valueOf(accountId));
        account.setAccountNumber(numberAccount);
        transaction.setAccount(account);

        // Act & Assert
        mockMvc.perform(post("/accounts/movimientos")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(transaction)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.amount", is(transaction.getAmount().intValue())));
    }

    private ResultActions createAccount(AccountDTO account) throws Exception {
        return mockMvc.perform(post("/accounts")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(account)));
    }

    private String getAccountIdFromResponse(ResultActions result) throws Exception {
        String content = result.andReturn().getResponse().getContentAsString();
        return String.valueOf(objectMapper.readTree(content).get("id").asLong());
    }

    private String getNumberAccountIdFromResponse(ResultActions result) throws Exception {
        String content = result.andReturn().getResponse().getContentAsString();
        return String.valueOf(objectMapper.readTree(content).get("accountNumber").asText());
    }
}