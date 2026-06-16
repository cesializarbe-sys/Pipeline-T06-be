package vallegrande.edu.pe.arona.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;

/**
 * DTO para crear/editar Cosechas.
 * Recibe únicamente IDs de entidades relacionadas en lugar de objetos completos.
 * REGLAS:
 * - tipoCultivo NO existe en este DTO; se obtiene automáticamente desde la entidad Cultivo.
 * - cantidadKg se mantiene exactamente como está (sin conversión en backend).
 * - idCultivo es obligatorio para obtener el Cultivo y su tipo.
 * - idCampo e idUsuario son obligatorios para la integridad referencial.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "DTO para registrar o editar una cosecha. " +
        "Solo se envían IDs de relaciones. " +
        "El tipo de cultivo se obtiene automáticamente desde el Cultivo relacionado (idCultivo).")
public class CosechaDTO {

    @NotNull(message = "La fecha de la cosecha es obligatoria")
    @JsonFormat(pattern = "dd/MM/yyyy")
    @Schema(description = "Fecha de la cosecha", example = "10/05/2026")
    private LocalDate fecha;

    @NotNull(message = "El idCampo es obligatorio")
    @Schema(description = "ID del campo donde se realizó la cosecha", example = "1")
    private Long idCampo;

    @NotNull(message = "El idCultivo es obligatorio")
    @Schema(description = "ID del cultivo cosechado. El tipo de cultivo se obtiene automáticamente desde esta relación.", example = "2")
    private Long idCultivo;

    @Schema(description = "Cantidad cosechada en kilogramos (sin conversión de unidades en backend)", example = "1500.50")
    private BigDecimal cantidadKg;

    @Schema(description = "Cantidad cosechada en unidades", example = "3000")
    private Integer cantidadUnidades;

    @NotNull(message = "El idUsuario es obligatorio")
    @Schema(description = "ID del usuario encargado que registra la cosecha", example = "3")
    private Integer idUsuario;

    @Schema(description = "Observaciones de la cosecha")
    private String observaciones;
}
