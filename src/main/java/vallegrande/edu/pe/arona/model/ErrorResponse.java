package vallegrande.edu.pe.arona.model;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * DTO para respuestas de error
 */
@Data
@AllArgsConstructor
public class ErrorResponse {
    private String message;
}
