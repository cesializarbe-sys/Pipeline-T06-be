package vallegrande.edu.pe.arona.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

/**
 * DTO para crear/editar Cultivos.
 * Recibe únicamente el ID del campo en lugar del objeto Campo completo.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "DTO para registrar o editar un cultivo (solo ID del campo)")
public class CultivoDTO {

    @Schema(description = "Nombre del cultivo", example = "Mandarina W. Murcott")
    private String nombre;

    @Schema(description = "Tipo de fruta/cultivo", example = "MANDARINA",
            allowableValues = {"MANDARINA", "PALTA", "ARANDANO", "CAQUI", "UVA"})
    private String tipoCultivo;

    @Schema(description = "Frecuencia de riego en días", example = "3")
    private Integer frecuenciaRiegoDias;

    @Schema(description = "Temperatura ideal de cultivo en °C", example = "22.5")
    private Double temperaturaIdeal;

    @JsonFormat(pattern = "dd/MM/yyyy")
    @Schema(description = "Fecha de siembra", example = "15/01/2026")
    private LocalDate fechaSiembra;

    @Schema(description = "Indica si el cultivo requiere sombra", example = "false")
    private Boolean requiereSombra;

    @Schema(description = "Estado de salud del cultivo", example = "BUENO",
            allowableValues = {"BUENO", "EN_RIESGO", "CON_PROBLEMAS"})
    private String estadoSalud;

    @Schema(description = "Observaciones sobre el cultivo")
    private String observaciones;

    @Schema(description = "ID del campo al que pertenece este cultivo", example = "1")
    private Long idCampo;
}
