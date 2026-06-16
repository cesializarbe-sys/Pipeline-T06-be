package vallegrande.edu.pe.arona.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

/**
 * DTO para crear/editar Revisiones de Calidad.
 * Recibe únicamente IDs de entidades relacionadas en lugar de objetos completos.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "DTO para registrar o editar una revisión de calidad (solo IDs de relaciones)")
public class RevisionCalidadDTO {

    @Schema(description = "ID de la cosecha revisada", example = "1")
    private Long idCosecha;

    @Schema(description = "Indica si el lote cumple los requisitos de calidad", example = "true")
    private Boolean cumpleRequisitos;

    @Schema(description = "Observaciones de la revisión")
    private String observaciones;

    @Schema(description = "ID del supervisor que realizó la revisión", example = "3")
    private Integer idSupervisor;

    @JsonFormat(pattern = "dd/MM/yyyy")
    @Schema(description = "Fecha de la revisión", example = "10/05/2026")
    private LocalDate fecha;
}
