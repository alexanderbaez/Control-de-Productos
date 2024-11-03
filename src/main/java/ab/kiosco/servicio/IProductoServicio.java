package ab.kiosco.servicio;

import ab.kiosco.modelo.Productos;

import java.util.List;

public interface IProductoServicio {

    public List<Productos> listarProductos();

    //para buscar un producto le mandamos la idproductos directamente
    public Productos buscarProductoPorId(Integer idproductos);

    public void guardarProductos(Productos producto);

    public void eliminarProducto(Productos producto);

    public void actualizarStock(Integer idproductos);
}
