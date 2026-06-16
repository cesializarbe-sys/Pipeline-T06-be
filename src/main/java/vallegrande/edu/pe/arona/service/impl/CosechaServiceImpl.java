package vallegrande.edu.pe.arona.service.impl;

import vallegrande.edu.pe.arona.dto.CosechaDTO;
import vallegrande.edu.pe.arona.model.Campo;
import vallegrande.edu.pe.arona.model.Cosecha;
import vallegrande.edu.pe.arona.model.Cultivo;
import vallegrande.edu.pe.arona.model.Usuario;
import vallegrande.edu.pe.arona.repository.CampoRepository;
import vallegrande.edu.pe.arona.repository.CosechaRepository;
import vallegrande.edu.pe.arona.repository.CultivoRepository;
import vallegrande.edu.pe.arona.repository.UsuarioRepository;
import vallegrande.edu.pe.arona.service.CosechaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Implementación del servicio de Cosechas.
 * REGLAS:
 * - tipoCultivo NO se usa en ninguna capa de Cosecha.
 * - La relación con Cultivo se realiza únicamente mediante idCultivo.
 * - cantidadKg se mantiene sin cambios ni conversiones.
 * - El usuario se asocia desde idUsuario recibido en el DTO.
 * - Se validan idCampo, idCultivo e idUsuario antes de persistir.
 */
@Service
public class CosechaServiceImpl implements CosechaService {

    private static final ZoneId ZONA_LIMA = ZoneId.of("America/Lima");

    @Autowired
    private CosechaRepository repository;

    @Autowired
    private CampoRepository campoRepository;

    @Autowired
    private CultivoRepository cultivoRepository;

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Override
    @Transactional(readOnly = true)
    public List<Cosecha> listarTodos() {
        return repository.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Cosecha> listarPorId(Long id) {
        if (id == null) {
            throw new IllegalArgumentException("El ID de la cosecha no puede ser nulo");
        }
        return repository.findById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Cosecha> listarPorEstado(Boolean estado) {
        return repository.findByEstado(estado);
    }

    /**
     * Resumen de cosechas agrupado por nombre de cultivo (ya no por tipoCultivo).
     * Se utiliza cultivo.getNombre() para agrupar.
     */
    @Override
    @Transactional(readOnly = true)
    public List<Map<String, Object>> obtenerResumen() {
        List<Cosecha> cosechas = repository.findByEstado(true);

        // Agrupar por nombre del cultivo (obtenido desde la relación @ManyToOne)
        Map<String, List<Cosecha>> porCultivo = cosechas.stream()
                .filter(c -> c.getCultivo() != null)
                .collect(Collectors.groupingBy(c -> c.getCultivo().getNombre()));

        List<Map<String, Object>> resumen = new ArrayList<>();
        porCultivo.forEach((nombreCultivo, lista) -> {
            Map<String, Object> item = new HashMap<>();
            // Se expone el nombre del cultivo (no tipoCultivo)
            item.put("cultivo", nombreCultivo);
            item.put("totalCosechas", lista.size());
            item.put("totalKg", lista.stream()
                    .map(Cosecha::getCantidadKg)
                    .filter(Objects::nonNull)
                    .reduce(BigDecimal.ZERO, BigDecimal::add));
            item.put("totalUnidades", lista.stream()
                    .map(Cosecha::getCantidadUnidades)
                    .filter(Objects::nonNull)
                    .reduce(0, Integer::sum));
            resumen.add(item);
        });
        return resumen;
    }

    /**
     * Crea una nueva cosecha.
     * Valida que idCampo, idCultivo e idUsuario sean no nulos y existan en BD.
     * tipoCultivo NO se asigna en Cosecha; se consulta desde Cultivo.
     */
    @Override
    @Transactional
    public Cosecha crear(CosechaDTO dto) {
        // Validación de campos obligatorios
        if (dto.getIdCampo() == null) {
            throw new IllegalArgumentException("El idCampo es obligatorio para registrar una cosecha");
        }
        if (dto.getIdCultivo() == null) {
            throw new IllegalArgumentException("El idCultivo es obligatorio para registrar una cosecha");
        }
        if (dto.getIdUsuario() == null) {
            throw new IllegalArgumentException("El idUsuario es obligatorio para registrar una cosecha");
        }

        Cosecha cosecha = new Cosecha();

        // Resolver campo por ID — lanza excepción si no existe
        Campo campo = campoRepository.findById(dto.getIdCampo())
                .orElseThrow(() -> new RuntimeException("Campo no encontrado con ID: " + dto.getIdCampo()));
        cosecha.setCampo(campo);

        // Resolver cultivo por ID — lanza excepción si no existe
        Cultivo cultivo = cultivoRepository.findById(dto.getIdCultivo())
                .orElseThrow(() -> new RuntimeException("Cultivo no encontrado con ID: " + dto.getIdCultivo()));
        cosecha.setCultivo(cultivo);

        // Resolver usuario por ID — lanza excepción si no existe
        Usuario usuario = usuarioRepository.findById(dto.getIdUsuario())
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado con ID: " + dto.getIdUsuario()));
        cosecha.setUsuario(usuario);

        // Mapeo de campos simples — cantidadKg sin conversión
        cosecha.setFecha(dto.getFecha());
        cosecha.setCantidadKg(dto.getCantidadKg());
        cosecha.setCantidadUnidades(dto.getCantidadUnidades());
        cosecha.setObservaciones(dto.getObservaciones());
        cosecha.setEstado(true);
        cosecha.setCreatedAt(LocalDateTime.now(ZONA_LIMA));

        return repository.save(cosecha);
    }

    /**
     * Edita una cosecha existente.
     * Valida que idCampo, idCultivo e idUsuario sean no nulos si se envían.
     * tipoCultivo NO se asigna ni consulta en Cosecha.
     */
    @Override
    @Transactional
    public Cosecha editar(Long id, CosechaDTO dto) {
        if (id == null) {
            throw new IllegalArgumentException("El ID de la cosecha no puede ser nulo");
        }

        return repository.findById(id).map(c -> {
            if (dto.getFecha() != null) {
                c.setFecha(dto.getFecha());
            }

            // Resolver campo por ID si se envía
            if (dto.getIdCampo() != null) {
                Campo campo = campoRepository.findById(dto.getIdCampo())
                        .orElseThrow(() -> new RuntimeException("Campo no encontrado con ID: " + dto.getIdCampo()));
                c.setCampo(campo);
            }

            // Resolver cultivo por ID si se envía
            if (dto.getIdCultivo() != null) {
                Cultivo cultivo = cultivoRepository.findById(dto.getIdCultivo())
                        .orElseThrow(() -> new RuntimeException("Cultivo no encontrado con ID: " + dto.getIdCultivo()));
                c.setCultivo(cultivo);
            }

            // Resolver usuario por ID si se envía
            if (dto.getIdUsuario() != null) {
                Usuario usuario = usuarioRepository.findById(dto.getIdUsuario())
                        .orElseThrow(() -> new RuntimeException("Usuario no encontrado con ID: " + dto.getIdUsuario()));
                c.setUsuario(usuario);
            }

            // cantidadKg se actualiza sin conversión
            if (dto.getCantidadKg() != null) {
                c.setCantidadKg(dto.getCantidadKg());
            }
            if (dto.getCantidadUnidades() != null) {
                c.setCantidadUnidades(dto.getCantidadUnidades());
            }
            if (dto.getObservaciones() != null) {
                c.setObservaciones(dto.getObservaciones());
            }

            c.setUpdatedAt(LocalDateTime.now(ZONA_LIMA));
            return repository.save(c);
        }).orElseThrow(() -> new RuntimeException("Cosecha no encontrada con ID: " + id));
    }

    /**
     * Eliminación lógica: cambia estado a false y registra deletedAt.
     */
    @Override
    @Transactional
    public Cosecha eliminar(Long id) {
        if (id == null) {
            throw new IllegalArgumentException("El ID de la cosecha no puede ser nulo");
        }
        return repository.findById(id).map(c -> {
            c.setEstado(false);
            c.setDeletedAt(LocalDateTime.now(ZONA_LIMA));
            return repository.save(c);
        }).orElse(null);
    }

    /**
     * Restaurar: cambia estado a true y registra restoredAt.
     */
    @Override
    @Transactional
    public Cosecha restaurar(Long id) {
        if (id == null) {
            throw new IllegalArgumentException("El ID de la cosecha no puede ser nulo");
        }
        return repository.findById(id).map(c -> {
            c.setEstado(true);
            c.setRestoredAt(LocalDateTime.now(ZONA_LIMA));
            return repository.save(c);
        }).orElse(null);
    }
}
