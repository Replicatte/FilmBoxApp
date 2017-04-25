/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package filmboxapp.actividades.control;

import java.net.URL;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Platform;
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
import modelo.Sala.localidad;

/**
 * FXML Controller class
 *
 * @author repli
 */
public class FXMLventa implements Initializable {

    @FXML
    private Label asientosDisponibles;
    @FXML
    private Label entradasAdquiridas;
    @FXML
    private Button botonComprar;
    @FXML
    private Button botonCancelar;
    @FXML
    private GridPane butacas;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }

    //[18][12]
    private localidad[][] loc = null;
    private localidad[][] seleccionadas = null;
    private int butacasPilladas = 0;

    @FXML
    private void clicadaButaca(ActionEvent event) {
        //Agafes el boto clickat
        Button but = (Button) event.getSource();

        //Agafes la posició d'aquest boto respecte al gridpanel
        int fila = 0;
        if (butacas.getRowIndex(but) != null) {
            fila = butacas.getRowIndex(but);
        }
        int columna = 0;
        if (butacas.getColumnIndex(but) != null) {
            columna = butacas.getColumnIndex(but);
        }
        //loc[posEnGridPanel][posEnGridPanel]
        if (loc[fila][columna] == localidad.libre && proyecSelect.getSala().getCapacidad() - proyecSelect.getSala().getEntradasVendidas() >= 1) {
            if (seleccionadas[fila][columna] == localidad.vendida) {
                seleccionadas[fila][columna] = localidad.libre;
                but.setStyle("-fx-background-color: #00cc66;");//Color verd
                butacasPilladas--;
                proyecSelect.getSala().setEntradasVendidas(proyecSelect.getSala().getEntradasVendidas() - 1);

            } else {
                seleccionadas[fila][columna] = localidad.vendida;
                but.setStyle("-fx-background-color:orange;");
                butacasPilladas++;
                proyecSelect.getSala().setEntradasVendidas(proyecSelect.getSala().getEntradasVendidas() + 1);

            }
            entradasAdquiridas.setText(butacasPilladas + "");
            if (proyecSelect.getSala().getCapacidad() - proyecSelect.getSala().getEntradasVendidas() == 0) {
                asientosDisponibles.setText("ENTRADAS AGOTADAS!");
            } else {
                asientosDisponibles.setText((proyecSelect.getSala().getCapacidad() - proyecSelect.getSala().getEntradasVendidas()) + "");
            }

            //proyecSelect.getSala().updateLocalidad(fila, columna, localidad.vendida);
        }
        event.consume();
    }

    private Proyeccion proyecSelect;

    public void dameProyeccion(Proyeccion pro) {
        this.proyecSelect = pro;
        if (proyecSelect.getSala().getCapacidad() - proyecSelect.getSala().getEntradasVendidas() == 0) {
            asientosDisponibles.setText("ENTRADAS AGOTADAS!");
        } else {
            asientosDisponibles.setText((proyecSelect.getSala().getCapacidad() - proyecSelect.getSala().getEntradasVendidas()) + "");
        }
        loc = proyecSelect.getSala().getLocalidades();
        seleccionadas = new localidad[loc.length][loc[0].length];

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
        for (int columna = 0; columna < seleccionadas[0].length; columna++) {
            for (int fila = 0; fila < seleccionadas.length; fila++) {
                if (seleccionadas[fila][columna] == localidad.vendida) {
                    proyecSelect.getSala().updateLocalidad(fila, columna, localidad.vendida);
                }

            }
        }
        ((Node) (event.getSource())).getScene().getWindow().hide();
        event.consume();
    }

    @FXML
    private void listenCancelar(ActionEvent event) {
        ((Node) (event.getSource())).getScene().getWindow().hide();
    }

}
