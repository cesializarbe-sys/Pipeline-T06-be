package vallegrande.edu.pe.arona.model;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO para solicitud de login
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Solicitud de autenticación")
public class LoginRequest {
    
    @Schema(description = "Correo del usuario", example = "admin@arona.com")
    private String correo;
    
    @Schema(description = "Contraseña", example = "password123")
    private String password;
}
