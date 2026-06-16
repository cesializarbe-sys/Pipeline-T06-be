package vallegrande.edu.pe.arona.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * Entidad Cultivo - Cultivo registrado en un Campo de Arona S.A.
 * Relacionado con Campo mediante @ManyToOne.
 * EstadoSalud: BUENO, EN_RIESGO, CON_PROBLEMAS
 */
@Entity
@Table(name = "cultivos")
@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
@Schema(description = "Cultivo registrado en un campo de Arona S.A.")
public class Cultivo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_cultivo")
    @Schema(description = "ID único del cultivo", example = "1", accessMode = Schema.AccessMode.READ_ONLY)
    private Long idCultivo;

    @Column(name = "nombre", nullable = false, length = 100)
    @Schema(description = "Nombre del cultivo", example = "Mandarina W. Murcott")
    private String nombre;

    @Column(name = "tipo_cultivo", nullable = false, length = 80)
    @Schema(description = "Tipo de fruta/cultivo", example = "MANDARINA",
            allowableValues = {"MANDARINA", "PALTA", "ARANDANO", "CAQUI", "UVA"})
    private String tipoCultivo;

    @Column(name = "frecuencia_riego_dias", nullable = false)
    @Schema(description = "Frecuencia de riego en días", example = "3")
    private Integer frecuenciaRiegoDias;

    @Column(name = "temperatura_ideal", nullable = false)
    @Schema(description = "Temperatura ideal de cultivo en °C", example = "22.5")
    private Double temperaturaIdeal;

    @JsonFormat(pattern = "dd/MM/yyyy")
    @Column(name = "fecha_siembra")
    @Schema(description = "Fecha de siembra", example = "15/01/2026")
    private LocalDate fechaSiembra;

    @Column(name = "requiere_sombra")
    @Schema(description = "Indica si el cultivo requiere sombra", example = "false")
    private Boolean requiereSombra = false;

    @Column(name = "estado_salud", length = 20)
    @Schema(description = "Estado de salud del cultivo", example = "BUENO",
            allowableValues = {"BUENO", "EN_RIESGO", "CON_PROBLEMAS"})
    private String estadoSalud = "BUENO";

    @Column(name = "observaciones", columnDefinition = "TEXT")
    @Schema(description = "Observaciones sobre el cultivo")
    private String observaciones;

    /**
     * Relación con Campo. El JSON devuelve el objeto Campo completo
     * (incluyendo nombre) en lugar de solo el id_campo.
     * La columna FK en la BD sigue siendo "id_campo".
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_campo")
    @JsonIgnoreProperties({"cultivos", "hibernateLazyInitializer", "handler"})
    @Schema(description = "Campo al que pertenece este cultivo")
    private Campo campo;

    @Column(name = "estado")
    @Schema(description = "Estado del cultivo (true=activo, false=inactivo)", example = "true")
    private Boolean estado = true;

    // ===================== CAMPOS DE AUDITORÍA =====================

    @JsonFormat(pattern = "dd/MM/yyyy HH:mm:ss", timezone = "America/Lima")
    @Column(name = "created_at")
    @Schema(description = "Fecha y hora de creación del registro", example = "04/05/2026 10:30:00", accessMode = Schema.AccessMode.READ_ONLY)
    private LocalDateTime createdAt;

    @JsonFormat(pattern = "dd/MM/yyyy HH:mm:ss", timezone = "America/Lima")
    @Column(name = "updated_at")
    @Schema(description = "Fecha y hora de la última modificación", example = "04/05/2026 14:15:00", accessMode = Schema.AccessMode.READ_ONLY)
    private LocalDateTime updatedAt;

    @JsonFormat(pattern = "dd/MM/yyyy HH:mm:ss", timezone = "America/Lima")
    @Column(name = "deleted_at")
    @Schema(description = "Fecha y hora de eliminación lógica", example = "05/05/2026 09:00:00", accessMode = Schema.AccessMode.READ_ONLY)
    private LocalDateTime deletedAt;

    @JsonFormat(pattern = "dd/MM/yyyy HH:mm:ss", timezone = "America/Lima")
    @Column(name = "restored_at")
    @Schema(description = "Fecha y hora de restauración del registro", example = "06/05/2026 08:00:00", accessMode = Schema.AccessMode.READ_ONLY)
    private LocalDateTime restoredAt;
}
