/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package filmboxapp.actividades.control;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import modelo.Proyeccion;
import modelo.Reserva;
import modelo.Sala.localidad;

/**
 * FXML Controller class
 *
 * @author repli
 */
public class FXMLCompraReserContro implements Initializable {

   // private Label asientosDisponibles;
    @FXML
    private TextField nombreBuscado;
    @FXML
    private Button botonBusquedaNombre;
    @FXML
    private TextField telefonoBuscado;
    @FXML
    private Button botonBusquedaTelefono;
    @FXML
    private Button botonComprar;
    @FXML
    private Button botonCancelar;
    @FXML
    private GridPane butacas;
    @FXML
    private Label mostrarNombre;
    @FXML
    private Label mostrarTelefono;
    @FXML
    private Label mostrarAsientosReserva;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }

    @FXML
    private void clicadaButaca(ActionEvent event) {

    }

    private Proyeccion proyecSelect;
    localidad[][] loc = null;

    public void dameProyeccion(Proyeccion pro) {
        this.proyecSelect = pro;
        loc = proyecSelect.getSala().getLocalidades();
        

        //Contamos con las localidades libres y con las ocupadas.
        for (int columna = 0; columna < 12; columna++) {

            for (int fila = 0; fila < 18; fila++) {
                Button but = (Button) getNodeByRowColumnIndex(fila, columna, butacas);
                if (loc[fila][columna] == localidad.vendida) {
                    but.setStyle("-fx-background-color:red;");
                }
            }
        }

    }

    public Node getNodeByRowColumnIndex(int fila, int columna, GridPane gridPane) {

        Node result = null;
        ObservableList<Node> childrens = gridPane.getChildren();

        for (Node node : childrens) {

            int filaNode = 0;
            if (gridPane.getRowIndex(node) != null) {
                filaNode = gridPane.getRowIndex(node);
            }
            int columNode = 0;
            if (gridPane.getColumnIndex(node) != null) {
                columNode = gridPane.getColumnIndex(node);
            }

            if (filaNode == fila && columNode == columna) {
                result = node;
                break;
            }
        }

        return result;

//        return gridPane.getChildren().get(fila*12 + columna);
    }

    @FXML
    private void listenComprar(ActionEvent event) {
        //A completar
        
        
        ((Node) (event.getSource())).getScene().getWindow().hide();
    }

    @FXML
    private void listenCancelar(ActionEvent event) {
        ((Node) (event.getSource())).getScene().getWindow().hide();
    }

    @FXML
    private void buscarNombre(ActionEvent event) {
        for(Reserva reserva : proyecSelect.getReservas()){
            if(nombreBuscado.getText().equals(reserva.getNombre())) {
                System.out.println("Trobat");
            }
        }
    }

    @FXML
    private void buscarTelefono(ActionEvent event) {
        for(Reserva reserva : proyecSelect.getReservas()){
            if(telefonoBuscado.getText().equals(reserva.getTelefono())) {
                System.out.println("Trobat");
            }
        }
    }

}
