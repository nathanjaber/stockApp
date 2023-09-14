package desafio.nathan.StockApp.application.DTOs;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.math.BigDecimal;
import java.time.LocalDate;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class StockCreationDTO {

    @NotBlank
    @Size(min=4, max=4)
    private String symbol;

    @NotNull
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "d MMM yyyy")
    private LocalDate date;


    @NotNull
    private BigDecimal price;
}
