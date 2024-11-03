package ab.kiosco.repositorio;

import ab.kiosco.modelo.Productos;
import org.springframework.data.jpa.repository.JpaRepository;

//utilizamos la interfaz de spring JpaReposotory<le pasamos el tipo de objeto y el tipo del id primary>
// y obtemos los metodos CRUD
public interface ProductoRepositorio extends JpaRepository<Productos, Integer> {
}
