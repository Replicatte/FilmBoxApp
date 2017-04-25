/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package filmboxapp.actividades.control;

import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.GridPane;
import modelo.Proyeccion;
import modelo.Reserva;
import modelo.Sala.localidad;

/**
 * FXML Controller class
 *
 * @author repli
 */
public class FXMLreserva implements Initializable {

    @FXML
    private Label asientosDisponibles;
    @FXML
    private TextField campoNombre;
    @FXML
    private TextField campoTelefono;
    @FXML
    private TextField campoEntradas;
    @FXML
    private Button botonReservar;
    @FXML
    private Button botonCancelar;
    @FXML
    private GridPane butacas;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        campoTelefono.addEventFilter(KeyEvent.KEY_TYPED, numeric_Validation(10));
        // TODO
    }

    public EventHandler<KeyEvent> numeric_Validation(final Integer max_Lengh) {
        return new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent e) {
                TextField txt_TextField = (TextField) e.getSource();
                if (txt_TextField.getText().length() >= max_Lengh) {
                    e.consume();
                }
                if (e.getCharacter().matches("[0-9.]")) {
                    if (txt_TextField.getText().contains(".") && e.getCharacter().matches("[.]")) {
                        e.consume();
                    } else if (txt_TextField.getText().length() == 0 && e.getCharacter().matches("[.]")) {
                        e.consume();
                    }
                } else {
                    e.consume();
                }
            }
        };
    }

    @FXML
    private void clicadaButaca(ActionEvent event) {
    }

    private Proyeccion proyecSelect;
    private localidad[][] loc = null;

    public void dameProyeccion(Proyeccion pro) {
        this.proyecSelect = pro;
        if (proyecSelect.getSala().getCapacidad() - proyecSelect.getSala().getEntradasVendidas() == 0) {
            asientosDisponibles.setText("ENTRADAS AGOTADAS!");
        } else {
            asientosDisponibles.setText((proyecSelect.getSala().getCapacidad() - proyecSelect.getSala().getEntradasVendidas()) + "");
        }
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
    private void listenReservar(ActionEvent event) {
        if (campoNombre.getText().equals("") || campoTelefono.getText().equals("") || campoEntradas.getText().equals("")) {
            Alert alertaCampoEmpty = new Alert(Alert.AlertType.ERROR);
                alertaCampoEmpty.setTitle("Campo vacio");
                alertaCampoEmpty.setHeaderText("Rellena todos los campos.");
                alertaCampoEmpty.setContentText("Tanto el campo de nombre como el de telefono como el de nombre de entradas deben estar"
                        + " especificados.");
                alertaCampoEmpty.showAndWait();
        } else {

            boolean errorNombre = false, errorCapacidad = false;
            int disponibles = proyecSelect.getSala().getCapacidad() - proyecSelect.getSala().getEntradasVendidas();
            if (disponibles < Integer.parseInt(campoEntradas.getText())) {
                Alert alertaPlaza = new Alert(Alert.AlertType.ERROR);
                alertaPlaza.setTitle("Error en el número de entradas");
                alertaPlaza.setHeaderText("No existen suficientes plazas.");
                alertaPlaza.setContentText("Solo existen " + disponibles + " plazas disponibles, para efectual la reserva.");
                alertaPlaza.showAndWait();
                campoEntradas.setStyle("-fx-background-color:orange;");

            } else {
                ArrayList<Reserva> reservasProy = proyecSelect.getReservas();

                for (Reserva reserva : reservasProy) {
                    if (reserva.getNombre().equals(campoNombre.getText())) {
                        Alert alertaNombre = new Alert(Alert.AlertType.ERROR);
                        alertaNombre.setTitle("Error en el nombre");
                        alertaNombre.setHeaderText("Ya existe una reserva a este nombre!");
                        alertaNombre.setContentText("El nombre especificado ya se encuentra en las reservas de esta proyección.");
                        alertaNombre.showAndWait();
                        errorNombre = true;
                        campoNombre.setStyle("-fx-background-color:orange;");
                        break;
                    }

                }

                if (!errorNombre) {
                    proyecSelect.addReserva(new Reserva(campoNombre.getText(), campoTelefono.getText(), Integer.parseInt(campoEntradas.getText())));
                    proyecSelect.getSala().setEntradasVendidas(proyecSelect.getSala().getEntradasVendidas() + Integer.parseInt(campoEntradas.getText()));
                    ((Node) (event.getSource())).getScene().getWindow().hide();
                }

            }
        }
    }

    @FXML
    private void listenCancelar(ActionEvent event
    ) {
        ((Node) (event.getSource())).getScene().getWindow().hide();

    }

}
