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
 * Entidad RevisionCalidad - Revisión de calidad de lote.
 * HU6: Revisión de calidad antes del despacho.
 * Relacionada con Cosecha y Usuario (supervisor) mediante @ManyToOne.
 */
@Entity
@Table(name = "revisiones_calidad")
@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
@Schema(description = "Revisión de calidad de lote antes del despacho")
public class RevisionCalidad {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_revision")
    @Schema(description = "ID único de la revisión", example = "1", accessMode = Schema.AccessMode.READ_ONLY)
    private Long idRevision;

    /**
     * Relación con Cosecha. El JSON devuelve el objeto Cosecha (que incluye campo y usuario).
     * La FK en la BD sigue siendo "id_cosecha".
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_cosecha", nullable = false)
    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
    @Schema(description = "Cosecha revisada")
    private Cosecha cosecha;

    @Column(name = "cumple_requisitos", nullable = false)
    @Schema(description = "Indica si el lote cumple los requisitos de calidad", example = "true")
    private Boolean cumpleRequisitos;

    @Column(name = "observaciones", columnDefinition = "TEXT")
    @Schema(description = "Observaciones de la revisión")
    private String observaciones;

    /**
     * Relación con Usuario (supervisor de calidad).
     * El JSON devuelve el objeto Usuario con nombreCompleto en lugar de solo el ID.
     * La FK en la BD sigue siendo "id_usuario_supervisor".
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_usuario_supervisor", nullable = false)
    @JsonIgnoreProperties({"password", "hibernateLazyInitializer", "handler"})
    @Schema(description = "Supervisor que realizó la revisión")
    private Usuario supervisor;

    @JsonFormat(pattern = "dd/MM/yyyy")
    @Column(name = "fecha", nullable = false)
    @Schema(description = "Fecha de la revisión", example = "10/05/2026")
    private LocalDate fecha;

    @Column(name = "notificado")
    @Schema(description = "Indica si se notificó al encargado (automático cuando no cumple requisitos)", example = "false")
    private Boolean notificado = false;

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
