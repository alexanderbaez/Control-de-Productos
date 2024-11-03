package ab.kiosco;

import ab.kiosco.gui.KioscoForma;
import com.formdev.flatlaf.FlatDarculaLaf;
import org.springframework.boot.WebApplicationType;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.ConfigurableApplicationContext;

import javax.swing.*;

@SpringBootApplication
public class KioscoSwing {

    public static void main(String[] args) {

        //configuramos el modo oscuro de la ventana
        FlatDarculaLaf.setup();

        //intanciamos la fabrica de spring
        ConfigurableApplicationContext contextoSpring = new SpringApplicationBuilder(KioscoSwing.class).headless(false).web(WebApplicationType.NONE).run(args);


        //creamos un objeto swing
        SwingUtilities.invokeLater(()-> {
           KioscoForma kioscoForma = contextoSpring.getBean(KioscoForma.class);
           kioscoForma.setVisible(true);
        });
    }
}
