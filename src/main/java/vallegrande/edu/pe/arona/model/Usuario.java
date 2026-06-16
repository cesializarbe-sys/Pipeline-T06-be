package vallegrande.edu.pe.arona.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * Entidad Usuario - Personal de Sociedad Agrícola Arona S.A.
 * Roles: ENCARGADO, ADMINISTRADOR
 * Áreas: CAMPO, PLANTA, ALMACEN, ADMINISTRACION, CALIDAD
 */
@Entity
@Table(name = "usuarios")
@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
@Schema(description = "Usuario del sistema Arona S.A.")
public class Usuario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_usuario")
    @Schema(description = "ID único del usuario", example = "1", accessMode = Schema.AccessMode.READ_ONLY)
    private Integer idUsuario;

    @Column(name = "nombre_completo", nullable = false, length = 150)
    @Schema(description = "Nombre completo del usuario", example = "Juan Pérez García")
    private String nombreCompleto;

    @Column(name = "dni", unique = true, length = 8)
    @Size(min = 8, max = 8, message = "El DNI debe tener exactamente 8 caracteres")
    @Schema(description = "DNI del usuario (único, 8 dígitos)", example = "12345678")
    private String dni;

    @Column(name = "telefono", length = 15)
    @Schema(description = "Número de teléfono del usuario", example = "987654321")
    private String telefono;

    @Column(name = "direccion", length = 200)
    @Schema(description = "Dirección del usuario", example = "Av. San Martín 123, Cañete")
    private String direccion;

    @Column(name = "correo", nullable = false, length = 100)
    @Schema(description = "Correo electrónico del usuario", example = "juan.perez@arona.com.pe")
    private String correo;

    @Column(name = "password", length = 255)
    @Schema(description = "Contraseña (hash)", accessMode = Schema.AccessMode.WRITE_ONLY)
    private String password;

    @Column(name = "rol", length = 30)
    @Schema(description = "Rol del usuario", example = "ENCARGADO", allowableValues = {"ENCARGADO", "ADMINISTRADOR"})
    private String rol;

    @Column(name = "area", length = 20)
    @Schema(description = "Área de trabajo", example = "CAMPO", allowableValues = {"CAMPO", "PLANTA", "ALMACEN", "ADMINISTRACION", "CALIDAD"})
    private String area;

    @Column(name = "estado")
    @Schema(description = "Estado del usuario (true=activo, false=inactivo)", example = "true")
    private Boolean estado;

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
