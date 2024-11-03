package ab.kiosco.modelo;


import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.*;

@Entity//se lo conoce como JPA
@Data // generamos los metodos getters y setters
@NoArgsConstructor // generamos un constructor vacio
@AllArgsConstructor // constructor con todos los argumentos
@ToString // generamos el toString
@EqualsAndHashCode // generamos los metodos equals y hashcode
public class Productos {

    //@id es lña primery de la base de datos y es autoincrementable
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer idproductos;
    private String nombre;
    private Double precio;
    private Integer stock;

}
