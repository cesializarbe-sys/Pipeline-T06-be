package vallegrande.edu.pe.arona.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

/**
 * DTO para crear/editar Alertas Fitosanitarias.
 * Recibe únicamente IDs de entidades relacionadas en lugar de objetos completos.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "DTO para registrar o editar una alerta fitosanitaria (solo IDs de relaciones)")
public class AlertaFitosanitariaDTO {

    @NotNull(message = "El ID del campo es obligatorio")
    @Schema(description = "ID del campo donde se detectó la alerta", example = "1")
    private Long idCampo;

    @Schema(description = "ID del cultivo afectado (puede ser nulo)", example = "2")
    private Long idCultivo;

    @NotBlank(message = "La descripción del problema es obligatoria")
    @Size(min = 5, max = 500, message = "La descripción debe tener entre 5 y 500 caracteres")
    @Schema(description = "Descripción detallada del problema detectado")
    private String descripcionProblema;

    @NotBlank(message = "El tipo de problema es obligatorio")
    @Schema(description = "Tipo de problema fitosanitario", example = "PLAGA",
            allowableValues = {"PLAGA", "ENFERMEDAD", "OTRO"})
    private String tipoProblema;

    @NotNull(message = "La fecha de detección es obligatoria")
    @JsonFormat(pattern = "dd/MM/yyyy")
    @Schema(description = "Fecha en que se detectó el problema", example = "10/05/2026")
    private LocalDate fechaDeteccion;

    @NotNull(message = "El ID del usuario que reporta es obligatorio")
    @Schema(description = "ID del usuario que reporta la alerta", example = "3")
    private Integer idUsuarioReporta;
}
