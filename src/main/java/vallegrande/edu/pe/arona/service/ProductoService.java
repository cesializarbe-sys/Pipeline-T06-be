package vallegrande.edu.pe.arona.service;

import vallegrande.edu.pe.arona.model.Producto;

import java.util.List;
import java.util.Optional;

public interface ProductoService {
    List<Producto> listarTodos();
    Optional<Producto> listarPorId(Long id);
    List<Producto> listarPorEstado(Boolean estado);
    List<Producto> listarPorTipoCultivo(String tipoCultivo);
    List<Producto> listarAlertasStock();
    List<Producto> listarOrdenadoPorAntiguedad();
    List<Producto> buscarPorNombre(String nombre);
    Producto crear(Producto producto);
    Producto editar(Long id, Producto producto);
    Producto eliminar(Long id);
    Producto restaurar(Long id);
}

