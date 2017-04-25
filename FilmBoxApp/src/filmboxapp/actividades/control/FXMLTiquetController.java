/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package filmboxapp.actividades.control;

import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.print.Printer;
import javafx.print.PrinterJob;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceDialog;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundImage;
import javafx.scene.layout.BackgroundPosition;
import javafx.scene.layout.BackgroundRepeat;
import javafx.scene.layout.BackgroundSize;
import javafx.scene.layout.HBox;
import modelo.Proyeccion;

/**
 * FXML Controller class
 *
 * @author saulc
 */
public class FXMLTiquetController implements Initializable {

    @FXML
    private Label mostrarPelicula;
    @FXML
    private Label mostrarDia;
    @FXML
    private Label mostrarSala;
    @FXML
    private Label mostrarHoraInicio;
    @FXML
    private ImageView mostrarCartel;

    private Printer printer = Printer.getDefaultPrinter();

    @FXML
    private Button seleccionImpresora;
    @FXML
    private AnchorPane impressPane;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }

    @FXML
    private void seleccionarImpresora(ActionEvent event) {// Selecciona una impresora de las disponibles en la máquina.    
        ChoiceDialog dialog = new ChoiceDialog(Printer.getDefaultPrinter(), Printer.getAllPrinters());
        dialog.setHeaderText("Seleccionar la impresora!");
        dialog.setContentText("Seleccionar una impresora de las disponibles");
        dialog.setTitle("Selección Impresora");
        Optional<Printer> opt = dialog.showAndWait();
        if (opt.isPresent()) {
            printer = opt.get();
        }
    }

    @FXML
    private void imprimir(ActionEvent event) {
        print(impressPane);
    }

    private void print(Node node) {
        PrinterJob job = PrinterJob.createPrinterJob(printer);
        if (job != null) {
            boolean printed = job.printPage(node);
            if (printed) {
                job.endJob();
            } else {
                System.out.println("Fallo al imprimir");
            }
        } else {
            System.out.println("No puede crearse el job de impresión.");
        }
    }

    private void noImprimir(ActionEvent event) {
        
        
        ((Node) (event.getSource())).getScene().getWindow().hide();

    }

    @FXML
    private void cancelar(ActionEvent event) {
        ((Node) (event.getSource())).getScene().getWindow().hide();

    }

    private Proyeccion proyecSelect = null;

    void dameProyeccion(Proyeccion proyecSelect) {
        this.proyecSelect = proyecSelect;

        mostrarSala.setText(proyecSelect.getSala().getNombresala());
        mostrarDia.setText(proyecSelect.getDia().toString());
        mostrarHoraInicio.setText(proyecSelect.getHoraInicio());
        mostrarPelicula.setText(proyecSelect.getPelicula().getTitulo());
        mostrarCartel.setImage(proyecSelect.getPelicula().getImagen());

        impressPane.setBackground(new Background(new BackgroundImage(new Image("/filmboxapp/resources/ticketFondo.png"), BackgroundRepeat.NO_REPEAT, BackgroundRepeat.NO_REPEAT, BackgroundPosition.DEFAULT, BackgroundSize.DEFAULT)));

    }
}
