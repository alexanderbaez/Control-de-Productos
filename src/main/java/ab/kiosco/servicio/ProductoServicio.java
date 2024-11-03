package ab.kiosco.servicio;

import ab.kiosco.modelo.Productos;
import ab.kiosco.repositorio.ProductoRepositorio;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

//agregamos @Service para que forme parte de la fabriga de spring
@Service
public class ProductoServicio implements IProductoServicio{

    //Autoinyectamos uan referencia de la capa de datos de la clase ProductoRepositirio
    //para poder utilizarla dentro de la clasde clienteServicio
    @Autowired
    private ProductoRepositorio productoRepositorio;//nos conectamos a ProductoRepositorio

    @Override
    public List<Productos> listarProductos() {
        //listamos los productos. regresamos los productos que tenemos en nuestra base de datos
        List<Productos> productos = productoRepositorio.findAll();
        return productos;
    }

    @Override
    public Productos buscarProductoPorId(Integer idproductos) {
        //buscamos un Productos
        Productos producto = productoRepositorio.findById(idproductos).orElse(null);
        //si hay --> devolvemos producto sino devolvemos null
        return producto;
    }

    @Override
    public void guardarProductos(Productos producto) {

        //Guardamos el producto
        productoRepositorio.save(producto);
         /* si el idCliente es igual a null entonces de genera un INSERT
           De lo contrario se hace un UPDATE/ modificacion */
    }

    @Override
    public void eliminarProducto(Productos producto) {
        //eliminamos un producto
        productoRepositorio.delete(producto);//toma el valor de idproductos y lo elimina

    }

    @Transactional
    public void actualizarStock(Integer idProducto) {
        var producto = productoRepositorio.findById(idProducto);

        int nuevoStock = producto.get().getStock() - 1;
        if (nuevoStock < 0) {
            throw new RuntimeException("Stock insuficiente para el producto con ID: " + idProducto);
        }
        else {
            producto.get().setStock(nuevoStock);
        }



    }
}
