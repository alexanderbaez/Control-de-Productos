package ab.kiosco.gui;

import ab.kiosco.modelo.Productos;
import ab.kiosco.servicio.IProductoServicio;
import ab.kiosco.servicio.ProductoServicio;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

//hacemos que la clase KioscoFornma forme parte de la fabrica de spring
@Component
public class KioscoForma extends JFrame{
    private JPanel panelPrincipal;
    private JTable productosTabla;
    private JTextField nombreTexto;
    private JTextField precioTexto;
    private JTextField stockTexto;
    private JButton guardarButton;
    private JButton eliminarButton;
    private JButton limparButton;


    //variable par la modificacion del producto
    private Integer idProducto;

    //definimos el atrubito de la interfaz
    IProductoServicio productoServicio;

    //declarmos la variable de la tabla productos, maneja los datos de la tabla
    private DefaultTableModel tableModeloProducto;

    //creamos el constructor de la clase
    @Autowired //inyectamos la relacion de dependencia desde el constructor
    public KioscoForma(ProductoServicio productoServicio){
        this.productoServicio = productoServicio;//inicializamos el producto servicio

        //iniciamos la pestaña forma
        iniciarForma();

        //Guardamos productos
        guardarButton.addActionListener(e -> guardarProdutos());

        //desde la tabla hacemos click al producto que deseamos modificar
        productosTabla.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);

                //creamos el metodo modificar procuto seleccionado
                modificarProductoSeleccionado();
            }
        });

        //EliminarProductos
        eliminarButton.addActionListener(e -> eliminarProducto());

        //limpiamos los campos
        limparButton.addActionListener(e -> limpiarCamposTabla());
    }

    private void iniciarForma(){
        setContentPane(panelPrincipal);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);//al cerrar pestaña finaliza el programa
        setSize(900,700);//tamaño de la pestaña
        setLocationRelativeTo(null);//centramos la pestaña
    }

    //se crea desde la tabla (haciendo click en tabla) presionando el cuadrado (custom create)
    private void createUIComponents() {
        // TODO: place custom component creation code here
        //creamos el objeto y asignamos registro y columnas
        this.tableModeloProducto = new DefaultTableModel(0,3){// 3 columnas

            //verificamos si queremos que los datos puedan modificarse de la tabla
            @Override
            public boolean isCellEditable( int row, int column){//row -> renglon, column -> columna
                return false; //false -> no . true -> si
            }
        };
        //definimos el cabecero de la tabla
        String[] cabecero = {"Id", "Nombre", "Precio", "Stock"};
        //enviamos el cabecero a nuestra tabla swing
        this.tableModeloProducto.setColumnIdentifiers(cabecero);
        //creamos la tabla, con la variable de productosTabla -> que se nombre en swing
        this.productosTabla = new JTable(tableModeloProducto);//al objeto table le enviamos la tabla creada

        //restringimo el marcado con el mouse en la tabla, que marque solamente un producto
        this.productosTabla.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        //cargamos el listado de tipoProducto
        listarProductos();

    }

    //listamos los productos de la base de datos
    private void listarProductos(){
        //Realizamos el conteo de renglones que inicializa en 0
        this.tableModeloProducto.setRowCount(0);
        //definimos una variable producto
        var productos = this.productoServicio.listarProductos();//recibimnos el listado de producto
        productos.forEach(producto -> {//mostamos los productos
            Object[] renglonProductos = {
                    producto.getIdproductos(),// ponemos "," para seguir escribiendo abajo
                    producto.getNombre(),
                    producto.getPrecio(),
                    producto.getStock(),
            };
            //por cada objeto que tengamos le mandamos a la tablaModeloProducto (que depende de Jtable)
            this.tableModeloProducto.addRow(renglonProductos);
        });
    }

    //Guardar productos
    private void guardarProdutos(){

        //si los campos estan vacios. mostramos un mensaje que debe llenarlos
        if ((nombreTexto.getText().equals("")) && (precioTexto.getText().equals("")) && (stockTexto.getText().equals(""))) {
            mostrarMensaje("No hay datos que guardar");
            nombreTexto.requestFocusInWindow();

        }else if (nombreTexto.getText().equals("")){//si precionamos guardar y el campo de campo de nombre esta vacio. nos muestra el mensaje
            mostrarMensaje("Ingrese un Nombre.");
            //si aparece el mensaje, nos dirige el cursor al campo nombre
            nombreTexto.requestFocusInWindow();
        }
        else if (precioTexto.getText().equals("")){
            mostrarMensaje("Ingrese un Precio");
            precioTexto.requestFocusInWindow();
        }
        else if(stockTexto.getText().equals("")){
            mostrarMensaje("Ingrese la Cantidad de Producto");
            stockTexto.requestFocusInWindow();
        }

        //recuperamos los valores y los asignamos a la db y a productosTabla
        var nombre = this.nombreTexto.getText();
        var precio = Double.parseDouble(this.precioTexto.getText());
        var stock = Integer.parseInt(this.stockTexto.getText());

        //creamos un nuevo objeto producto
        //tambien mandamos el idProductos para la modificacion de producto
        var producto = new Productos();

        //ingresamos datos
        producto.setNombre(nombre);
        producto.setPrecio(precio);
        producto.setStock(stock);
        //y tambien agregamos el idProcutos que obtenemos al hacer clip para la modificacion del mismo
        producto.setIdproductos(this.idProducto);

        //llamamos al productoServico para guardar el producto
        this.productoServicio.guardarProductos(producto);//insertamos/modificamos el producto

        //verificamos

        if (this.idProducto==null)
            mostrarMensaje("Se agrego un nuevo Producto");
        else
            mostrarMensaje("Se modifico el producto correctamente");

        //una vez que se agrega el producto limpiamos los campos
        limpiarCamposTabla();

        //recargamos la lista de los productos en la tabla
        listarProductos();
    }

    //modificar producto seleccionado
    private void modificarProductoSeleccionado(){
        //guardamos el reglon del producto seleccionado
        var renglon = productosTabla.getSelectedRow();

        //verificamos si seleccionamos un producto
        if (renglon != -1){

            //recuperamos los atributo del producto seleccionado y lo mostramos en los campos
            //id en el indice 0 de la tabla
            var id = productosTabla.getModel().getValueAt(renglon,0).toString();
            this.idProducto = Integer.parseInt(id);
            var nombre = productosTabla.getModel().getValueAt(renglon,1).toString();
            this.nombreTexto.setText(nombre);
            var precio = productosTabla.getModel().getValueAt(renglon,2).toString();
            this.precioTexto.setText(precio);
            var stock = productosTabla.getModel().getValueAt(renglon,3).toString();
            this.stockTexto.setText(stock);
        }
    }

    //eliminar productos
    private void eliminarProducto(){
        //obtenemos el reglon selecionado de la tabla
        var renglon = productosTabla.getSelectedRow();

        if (renglon != -1){
            //creamos una variable para guardar el id del producto seleccionado
            var idproducto = productosTabla.getModel().getValueAt(renglon,0).toString();

            //asigno el id recuperado a la variable local idProducto
            this.idProducto = Integer.parseInt(idproducto);

            //creamos el objeto a eliminar
            var producto = new Productos();
            producto.setIdproductos(this.idProducto);

            //lo eliminamos
            productoServicio.eliminarProducto(producto);

            //mostramos una ventana emergente
            mostrarMensaje("Producto con Id "+ this.idProducto+ " eliminado");
            ///limpiamos los campos
            limpiarCamposTabla();
            // mostramos nuevamente la lista de productos
            listarProductos();

        }
    }

    //Limpiamos los campos despues de agregar/modificar un producto
    private void limpiarCamposTabla(){
        nombreTexto.setText("");
        precioTexto.setText("");
        stockTexto.setText("");

        //limpiamos el Idproducto del producto que fue modificado
        this.idProducto= null;

        //desmarcamos el producto seleccionado para modificar
        this.productosTabla.getSelectionModel().clearSelection();
    }

    private void mostrarMensaje( String mensaje){
        JOptionPane.showMessageDialog(this, mensaje);
    }
}
