package vallegrande.edu.pe.arona.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;

/**
 * Configuración centralizada de Swagger / OpenAPI 3.
 * Acceder a la documentación en: /swagger-ui.html
 *
 * Módulo Cosecha (HU3):
 * - tipoCultivo NO existe en ningún endpoint de Cosecha.
 * - La relación con Cultivo se realiza únicamente mediante idCultivo.
 * - cantidadKg se mantiene sin conversión en el backend.
 * - El usuario se asocia desde idUsuario enviado en el DTO.
 */
@Configuration
public class SwaggerConfig {

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("Sistema Agrícola Arona S.A.")
                        .description("""
                                API REST — Gestión de Campo y Control de Calidad.

                                Sistema orientado a la gestión agrícola, producción,
                                control de cultivos, cosechas y procesos de calidad.
                                """)
                        .version("3.1.0")
                        .contact(new Contact()
                                .name("Sociedad Agrícola Arona S.A.")
                                .email("admin@arona.com.pe")));
    }
}