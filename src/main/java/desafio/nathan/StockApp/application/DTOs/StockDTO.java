package desafio.nathan.StockApp.application.DTOs;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.time.LocalDate;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class StockDTO {


    private Long id;

    @NotBlank
    private String symbol;


    @NotBlank
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "d MMM yyyy")
    private LocalDate date;


    @NotNull
    private BigDecimal price;
}
