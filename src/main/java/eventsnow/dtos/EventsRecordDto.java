package eventsnow.dtos;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;
import java.time.LocalTime;

public record EventsRecordDto(@NotBlank String description, @NotBlank String summary, @NotNull LocalDate dataEvento, @NotNull LocalTime horaInicio, @NotNull LocalTime horaFim ) {
}
