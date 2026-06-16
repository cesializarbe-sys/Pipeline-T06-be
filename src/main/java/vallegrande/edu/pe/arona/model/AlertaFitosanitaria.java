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
 * Entidad AlertaFitosanitaria - Alertas de plagas o enfermedades en cultivos.
 * HU7: Aviso de plagas o enfermedades.
 * Tipos: PLAGA, ENFERMEDAD, OTRO
 * Estados: PENDIENTE, ATENDIDO
 */
@Entity
@Table(name = "alertas_fitosanitarias")
@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
@Schema(description = "Alerta de plaga o enfermedad detectada en un campo")
public class AlertaFitosanitaria {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_alerta")
    @Schema(description = "ID único de la alerta", example = "1", accessMode = Schema.AccessMode.READ_ONLY)
    private Long idAlerta;

    /**
     * Relación con Campo. El JSON devuelve el nombre del campo.
     * La FK en la BD sigue siendo "id_campo".
     */
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "id_campo", nullable = false)
    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
    @Schema(description = "Campo donde se detectó la alerta")
    private Campo campo;

    /**
     * Relación con Cultivo (opcional). El JSON devuelve el nombre del cultivo.
     * La FK en la BD sigue siendo "id_cultivo".
     */
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "id_cultivo")
    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
    @Schema(description = "Cultivo afectado (puede ser nulo si afecta al campo en general)")
    private Cultivo cultivo;

    @Column(name = "descripcion_problema", nullable = false, columnDefinition = "TEXT")
    @Schema(description = "Descripción detallada del problema detectado")
    private String descripcionProblema;

    @Column(name = "tipo_problema", nullable = false, length = 20)
    @Schema(description = "Tipo de problema fitosanitario", example = "PLAGA",
            allowableValues = {"PLAGA", "ENFERMEDAD", "OTRO"})
    private String tipoProblema;

    @Column(name = "estado_alerta", length = 20)
    @Schema(description = "Estado de la alerta", example = "PENDIENTE",
            allowableValues = {"PENDIENTE", "ATENDIDO"})
    private String estadoAlerta = "PENDIENTE";

    @Column(name = "solucion_aplicada", columnDefinition = "TEXT")
    @Schema(description = "Solución aplicada para resolver la alerta")
    private String solucionAplicada;

    @JsonFormat(pattern = "dd/MM/yyyy")
    @Column(name = "fecha_deteccion", nullable = false)
    @Schema(description = "Fecha en que se detectó el problema", example = "10/05/2026")
    private LocalDate fechaDeteccion;

    @JsonFormat(pattern = "dd/MM/yyyy")
    @Column(name = "fecha_resolucion")
    @Schema(description = "Fecha en que se resolvió el problema", example = "12/05/2026")
    private LocalDate fechaResolucion;

    /**
     * Relación con Usuario (quien reportó la alerta).
     * El JSON devuelve el nombreCompleto del usuario.
     * La FK en la BD sigue siendo "id_usuario_reporta".
     */
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "id_usuario_reporta", nullable = false)
    @JsonIgnoreProperties({"password", "hibernateLazyInitializer", "handler"})
    @Schema(description = "Usuario que reportó la alerta")
    private Usuario usuarioReporta;

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
