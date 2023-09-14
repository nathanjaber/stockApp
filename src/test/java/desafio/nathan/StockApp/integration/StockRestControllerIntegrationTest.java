package desafio.nathan.StockApp.integration;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import desafio.nathan.StockApp.application.DTOs.StockCreationDTO;
import desafio.nathan.StockApp.application.DTOs.StockUpdatePriceDTO;
import desafio.nathan.StockApp.application.exceptions.NotFoundException;
import desafio.nathan.StockApp.ports.IStockService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles(profiles = {"test"})
public class StockRestControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private IStockService stockService;

    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        this.objectMapper = new ObjectMapper();
        this.objectMapper.registerModule(new JavaTimeModule());
    }

    @Test
    public void whenGetAll_thenReturn200() throws Exception {
        this.mockMvc.perform(get("/stocks"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE));
    }

    @Test
    public void whenGetByIdOfExistentRecord_thenReturn200() throws Exception {

        this.mockMvc.perform(get("/stocks/{id}", 1))
                .andExpect(status().isOk());

    }

    @Test
    public void whenGetByIdOfNonfExistentRecord_thenReturn404() throws Exception {

        when(this.stockService.getById(anyLong())).thenThrow(NotFoundException.class);

        this.mockMvc.perform(get("/stocks/{id}", 1))
                .andExpect(status().isNotFound())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE));
    }

    @Test
    public void whenCreateWithValidInput_thenReturns201WithLocation() throws Exception {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("d MMM yyyy");
        LocalDate originalDate = LocalDate.of(2023, 9, 11);
        String formattedDate = originalDate.format(formatter);
        LocalDate date = LocalDate.parse(formattedDate, formatter);

        StockCreationDTO stockCreationDTO = new StockCreationDTO();
        stockCreationDTO.setSymbol("TEST");
        stockCreationDTO.setDate(date);
        stockCreationDTO.setPrice(BigDecimal.ONE);

        this.mockMvc.perform(post("/stocks")
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(this.objectMapper.writeValueAsString(stockCreationDTO)))
                .andExpect(status().isCreated())
                .andExpect(header().exists("location"));
    }

    @Test
    public void whenCreateWithNullValue_thenReturns400() throws Exception {
        StockCreationDTO stockCreationDTO = new StockCreationDTO();
        stockCreationDTO.setSymbol("TEST");
        stockCreationDTO.setDate(null);
        stockCreationDTO.setPrice(BigDecimal.ONE);

        this.mockMvc.perform(post("/stocks")
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(this.objectMapper.writeValueAsString(stockCreationDTO)))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void whenUpdatePriceWithValidValueAndExistentRecord_thenReturns204WithHeaderLocation() throws Exception {
        StockUpdatePriceDTO stockUpdatePriceDTO = new StockUpdatePriceDTO();
        stockUpdatePriceDTO.setPrice(BigDecimal.ONE);

        this.mockMvc.perform(patch("/stocks/{id}", 1)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(this.objectMapper.writeValueAsString(stockUpdatePriceDTO)))
                .andExpect(status().isNoContent())
                .andExpect(header().exists("location"));
    }

    @Test
    public void whenUpdatePriceWithNonExistentRecord_thenReturns404() throws Exception {
        StockUpdatePriceDTO stockUpdatePriceDTO = new StockUpdatePriceDTO();
        stockUpdatePriceDTO.setPrice(BigDecimal.ONE);


        when(this.stockService.updatePrice(anyLong(), any(BigDecimal.class))).thenThrow(NotFoundException.class);

        this.mockMvc.perform(patch("/stocks/{id}", 1)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(this.objectMapper.writeValueAsString(stockUpdatePriceDTO)))
                .andExpect(status().isNotFound());
    }

    @Test
    public void whenUpdatePriceWithNullValue_thenReturns400() throws Exception {
        StockUpdatePriceDTO stockUpdatePriceDTO = new StockUpdatePriceDTO();
        stockUpdatePriceDTO.setPrice(null);

        this.mockMvc.perform(patch("/stocks/{id}", 1)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(this.objectMapper.writeValueAsString(stockUpdatePriceDTO)))
                .andExpect(status().isBadRequest());
    }


}
