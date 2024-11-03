package ab.kiosco;

import ab.kiosco.modelo.Productos;
import ab.kiosco.servicio.IProductoServicio;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

//@SpringBootApplication
public class KioscoApplication implements CommandLineRunner {

	//agregamos una notacio de dependencia de IProductoServicio para que tenga conexion con las demas clases
	@Autowired
	private IProductoServicio productoServicio;

	//empezamos a utilizar logger.info() para mostrar por pantalla la informacion
	private static final Logger logger = LoggerFactory.getLogger(KioscoApplication.class);

	//generamos un salto de linea \n para generarlo como contatenacion de cadenas
	String nl = System.lineSeparator();

	public static void main(String[] args) {

		logger.info("Iniciando la Aplicacion\n");
		SpringApplication.run(KioscoApplication.class, args);
		logger.info("Finalizando la Aplicacion\n");
	}


	@Override
	public void run(String... args) throws Exception {

		kioscoApp();
	}

	private void kioscoApp() {

		var salir = false;
		Scanner entrada = new Scanner(System.in);

		//utilizamos el objeto producto servicio que agregamos arriba

		while (!salir) {

			//Lista de productos a vender
			List<Productos> productosVender = new ArrayList<>();

			//agregamos un salto de linea
			logger.info(nl);

			logger.info("""
				----- Kioico Application -----
				
				1. Agregar productos al Inventario
				2. Vender Productos
				3. Salir 
				Elije una Opcion:\s""");
			var opcionGeneral = Integer.parseInt(entrada.nextLine());

			switch (opcionGeneral) {

					case 1 -> {//1. agregar al inventario
						boolean continuar = false;
						while (!continuar) {

							logger.info("""
									----- Kioico Application -----
									1. Listar productos
									2. Buscar Productos
									3. Agregar Productos
									4. Modificar Productos
									5. Eliminar Productos
									6. Volver al Menu Principal
									Elije una Opcion:\s""");
							var opcion = Integer.parseInt(entrada.nextLine());

							switch(opcion){

								case 1 -> {//1. Listar Productos
									logger.info("----- Lista de Productos -----");
									//recuperamos el listado de productos
									List<Productos> productos = productoServicio.listarProductos();
									//mostramos los productos
									productos.forEach(producto -> logger.info(nl+producto.toString()));
								}
								case 2 -> { //2. Buscar Productos
									logger.info("----- Buscar producto por Id -----");
									logger.info(nl+"Introduce el Id del Productos a buscar: ");
									var idProducto = Integer.parseInt(entrada.nextLine());
									Productos buscarProduto = productoServicio.buscarProductoPorId(idProducto);
									if (buscarProduto!=null)
										logger.info("Productos Encontrado: "+buscarProduto+nl);
									else
										logger.info("Productos no encontrado: "+buscarProduto+nl);
								}
								case 3 ->{//3. Agregar Productos
									logger.info("-----Agregar Productos -----"+nl);
									logger.info("Nombre: ");
									var nombre = entrada.nextLine();
									logger.info("Precio: ");
									var precio = Double.parseDouble(entrada.nextLine());
									logger.info("Stock: ");
									var stock = Integer.parseInt(entrada.nextLine());

									//creamos un objeto de tipo producto
									Productos producto = new Productos();
									//utilizamos los metodos setters para agregar producto
									producto.setNombre(nombre);
									producto.setPrecio(precio);
									producto.setStock(stock);

									//guardamos el producto den la base de datos
									productoServicio.guardarProductos(producto);
									logger.info("Productos Agregado: "+producto+nl);
								}
								case 4 -> {//4. Modificar Productos
									logger.info("-----Modificar Productos -----");
									logger.info(nl+"Ingrese el Id del Productos a Modificar: ");
									var idProducto = Integer.parseInt(entrada.nextLine());

									//verificamos si el producto esta en la base de dato
									Productos producto = productoServicio.buscarProductoPorId(idProducto);

									//verificamos
									if (producto!=null){
										logger.info("Nombre: ");
										var nombre = entrada.nextLine();
										logger.info("Precio: ");
										var precio = Double.parseDouble(entrada.nextLine());
										logger.info("Stock: ");
										var stock = Integer.parseInt(entrada.nextLine());

										//utilizamos los metodos setters para agregar producto
										producto.setNombre(nombre);
										producto.setPrecio(precio);
										producto.setStock(stock);

										//guardamos el producto den la base de datos
										productoServicio.guardarProductos(producto);
										logger.info("Productos Modificado: "+producto+nl);
									}
									else
										logger.info("Productos no encontrado: "+producto+nl);
								}
								case 5 ->{// 5. Eliminar Productos
									logger.info("----- Eliminar Productos -----");
									logger.info(nl+"Introduce el Id del producto a Eliminar: ");
									var idProducto = Integer.parseInt(entrada.nextLine());

									//buscamos el producto que deseamos eliminar
									Productos producto =  productoServicio.buscarProductoPorId(idProducto);

									//verificamos
									if (producto!=null){
										productoServicio.eliminarProducto(producto);
										logger.info("Productos Eliminado: "+producto+nl);
									}
									else
										logger.info("Productos no Encontrado: "+producto+nl);
								}
								case 6 -> {// 6. Salir
									logger.info("----- Muchas Gracias -----");
									continuar = true;
								}
								default -> logger.info("Opcion Invalida: "+opcion+nl);
							}
						}
					}
					case 2 -> { //2 vender Productos
						boolean continur = false;
						while (!continur) {

							logger.info("""
								----- Kioico Application -----
								1. Comprar productos
								2. Mostrar lista de Productos a comprar
								3. Ticket de Compra
								4. Eliminar Productos
								5. Volver al Menu Principal
								Elije una Opcion:\s""");
							var opcion = Integer.parseInt(entrada.nextLine());

							switch (opcion) {
								case 1 -> { // 2. Comprar Producto
									System.out.println("Que producto deseas comprar (id)? ");
									var idproducto = Integer.parseInt(entrada.nextLine());

									//validar que el producto exista en la lista de producto
									var productoEncontrado = false;

									for (var producto : productoServicio.listarProductos()) {
										if (idproducto == producto.getIdproductos()) {
											//Agregamos el producto a la lista de producto
											productosVender.add(producto);
											productoServicio.actualizarStock(idproducto);
											System.out.println("Compra realizada con éxito para el producto con ID: " + idproducto);
										}
									}
								}
								case 2 -> { //1.Listar Producotos a comprar

									logger.info("----- Lista de Productos a comprar -----"+nl);
									//recuperamos el listado de productos
									productosVender.forEach(System.out::println);
								}
								case 3 -> { // 3. ticket de compras

									var ticket = "---- Ticket de Venta ----";
									var total = 0.0;
									for (var producto : productosVender){
										ticket += "\n\t- " + producto.getNombre() + " - $" + producto.getPrecio();
										total += producto.getPrecio();
									}
									ticket += "\n\tTotal --> $" + total;
									System.out.println(ticket);
								}
								case 4 -> { //4. Eliminar Producto

									logger.info("----- Eliminar Productos -----");
									logger.info(nl+"Introduce el Id del producto a Eliminar: ");
									var idProducto = Integer.parseInt(entrada.nextLine());

									for (var producto : productosVender){
										//buscamos el producto que deseamos eliminar
										//verificamos
										if (producto.getIdproductos() == idProducto){
											productosVender.remove(idProducto);
											logger.info("Productos Eliminado: "+producto+nl);
										}
										else
											logger.info("Productos no Encontrado: "+producto+nl);
									}
								}
								case 5-> { //5. Salir
									logger.info("----- Muchas Gracias -----" +nl);
									continur = true;
								}
								default -> logger.info("Opcion Invalida: "+opcion+nl);
							}
						}
					}
					case 3 -> { // 3. Salir
						logger.info("----- Muchas Gracias -----" + nl);
						salir = true;
					}
					default -> logger.info("Opcion Invalida: " + opcionGeneral);

			}
		}
	}
}

