package vallegrande.edu.pe.arona.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import vallegrande.edu.pe.arona.model.Usuario;
import vallegrande.edu.pe.arona.repository.UsuarioRepository;
import vallegrande.edu.pe.arona.service.IUsuarioService;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.List;
import java.util.Optional;

@Service
public class UsuarioServiceImpl implements IUsuarioService {

    private static final ZoneId ZONA_LIMA = ZoneId.of("America/Lima");

    @Autowired
    private UsuarioRepository repository;

    @Override
    public List<Usuario> listarTodos() {
        return repository.findAll();
    }

    @Override
    public Optional<Usuario> buscarPorId(Integer id) {
        return repository.findById(id);
    }

    @Override
    public List<Usuario> listarPorEstado(Boolean estado) {
        return repository.findByEstado(estado);
    }

    @Override
    public List<Usuario> listarPorArea(String area) {
        return repository.findByArea(area);
    }

    @Override
    public Usuario guardar(Usuario usuario) {
        LocalDateTime now = LocalDateTime.now(ZONA_LIMA);
        usuario.setEstado(true);
        usuario.setCreatedAt(now);
        usuario.setUpdatedAt(null);
        usuario.setDeletedAt(null);
        usuario.setRestoredAt(null);
        return repository.save(usuario);
    }

    @Override
    public Usuario actualizar(Integer id, Usuario datos) {
        return repository.findById(id).map(u -> {
            u.setNombreCompleto(datos.getNombreCompleto());
            u.setDni(datos.getDni());
            u.setTelefono(datos.getTelefono());
            u.setDireccion(datos.getDireccion());
            u.setPassword(datos.getPassword());
            u.setCorreo(datos.getCorreo());
            u.setRol(datos.getRol());
            u.setArea(datos.getArea());
            u.setUpdatedAt(LocalDateTime.now(ZONA_LIMA));
            return repository.save(u);
        }).orElseThrow(() -> new RuntimeException("Usuario no encontrado"));
    }

    @Override
    public Usuario eliminarLogico(Integer id) {
        return repository.findById(id).map(u -> {
            u.setEstado(false);
            u.setDeletedAt(LocalDateTime.now(ZONA_LIMA));
            return repository.save(u);
        }).orElse(null);
    }

    @Override
    public Usuario restaurarLogico(Integer id) {
        return repository.findById(id).map(u -> {
            u.setEstado(true);
            u.setRestoredAt(LocalDateTime.now(ZONA_LIMA));
            return repository.save(u);
        }).orElse(null);
    }

    @Override
    public Optional<Usuario> login(String correo, String password) {
        Optional<Usuario> usuario = repository.findByCorreo(correo);
        if (usuario.isPresent() && usuario.get().getPassword().equals(password) && usuario.get().getEstado()) {
            return usuario;
        }
        return Optional.empty();
    }
}
