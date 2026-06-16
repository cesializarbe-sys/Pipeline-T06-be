package vallegrande.edu.pe.arona.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * Entidad Cosecha - Registro de cosechas realizadas.
 * HU3: Registro de la cosecha.
 * Relacionada con Campo, Cultivo y Usuario mediante @ManyToOne.
 * NOTA: tipoCultivo fue eliminado; se obtiene desde la entidad Cultivo mediante idCultivo.
 */
@Entity
@Table(name = "cosechas")
@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
@Schema(description = "Registro de cosecha realizada en un campo")
public class Cosecha {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_cosecha")
    @Schema(description = "ID único de la cosecha", example = "1", accessMode = Schema.AccessMode.READ_ONLY)
    private Long idCosecha;

    @JsonFormat(pattern = "dd/MM/yyyy")
    @Column(name = "fecha", nullable = false)
    @Schema(description = "Fecha de la cosecha", example = "10/05/2026")
    private LocalDate fecha;

    /**
     * Relación con Campo. FK en BD: id_campo.
     */
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "id_campo", nullable = false)
    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
    @Schema(description = "Campo donde se realizó la cosecha")
    private Campo campo;

    /**
     * Relación con Cultivo. FK en BD: id_cultivo.
     * El tipo de cultivo se obtiene desde el objeto Cultivo.
     * NO existe columna tipo_cultivo en la entidad Cosecha.
     */
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "id_cultivo", nullable = false)
    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
    @Schema(description = "Cultivo cosechado (contiene tipo, nombre, etc.)")
    private Cultivo cultivo;

    @Column(name = "cantidad_kg", precision = 10, scale = 2)
    @Schema(description = "Cantidad cosechada en kilogramos", example = "1500.50")
    private BigDecimal cantidadKg;

    @Column(name = "cantidad_unidades")
    @Schema(description = "Cantidad cosechada en unidades", example = "3000")
    private Integer cantidadUnidades;

    /**
     * Relación con Usuario (encargado que registró la cosecha).
     * FK en BD: id_usuario.
     */
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "id_usuario", nullable = false)
    @JsonIgnoreProperties({"password", "hibernateLazyInitializer", "handler"})
    @Schema(description = "Usuario encargado que registró la cosecha")
    private Usuario usuario;

    @Column(name = "observaciones", columnDefinition = "TEXT")
    @Schema(description = "Observaciones de la cosecha")
    private String observaciones;

    @Column(name = "estado")
    @Schema(description = "Estado del registro (true=activo, false=inactivo)", example = "true")
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
