/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package filmboxapp;

import accesoaBD.AccesoaBD;
import filmboxapp.actividades.control.FXMLCompraReserContro;
import filmboxapp.actividades.control.FXMLreserva;
import filmboxapp.actividades.control.FXMLventa;
import java.io.IOException;
import java.net.URL;
import java.time.LocalDate;
import java.time.Month;
import java.util.ArrayList;
import java.util.ResourceBundle;
import javafx.beans.property.ReadOnlyStringWrapper;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableColumn.CellDataFeatures;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;
import javafx.util.Callback;
import modelo.Proyeccion;
import modelo.Pelicula;

/**
 *
 * @author repli
 */
public class FXMLmainController implements Initializable {

    @FXML
    private TableView<Proyeccion> tabla;
    @FXML
    private TableColumn<Proyeccion, String> horaCol;
    @FXML
    private TableColumn<Proyeccion, String> salaCol;
    @FXML
    private ChoiceBox<String> selectorPelicula;
    @FXML
    private ImageView imgViewPeli;
    @FXML
    private DatePicker selectorDia;

    @FXML
    private Label ins1;
    @FXML
    private Label ins2;
    @FXML
    private Label ins3;
    @FXML
    private Label labelTitulo;
    @FXML
    private Label labelDirector;
    @FXML
    private Label labelPais;
    @FXML
    private Label labelDuracion;

    @FXML
    private Button botonVenta;
    @FXML
    private Button botonReserva;
    @FXML
    private Button botonVentaReserva;
    @FXML
    private Button botonVerSala;

    public static Proyeccion proyecSelect = null;

    public static AccesoaBD bd = new AccesoaBD();
    private ObservableList<Proyeccion> data = null;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        //Dia per defecte
        selectorDia.setValue(LocalDate.of(2017, Month.APRIL, 1));
        seleccionDia(new ActionEvent(selectorDia, null));

        //Listener del selector de pelicula
        selectorPelicula.getSelectionModel().selectedIndexProperty().addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observableValue, Number oldValue, Number newValue) {
                try {
                    for (Pelicula peliAct : bd.getTodasPeliculas()) {
                        if (peliAct.getTitulo().equals(selectorPelicula.getItems().get((Integer) newValue))) {
                            imgViewPeli.setImage(peliAct.getImagen());
                        }
                    }
                    if (selectorPelicula.getItems().get((Integer) newValue) != null) {
                        muestraProyecciones(selectorPelicula.getItems().get((Integer) newValue));
                    }

                } catch (ArrayIndexOutOfBoundsException aioobe) {
                    System.out.println("IndexOutOfBounds en selección");
                }
            }
        });

        //Listener del selector de hora/sala
        tabla.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            if (newSelection != null) {

                //Coje la proyección seleccionada
                proyecSelect = tabla.getSelectionModel().getSelectedItem();

                botonVenta.setDisable(false);
                botonVenta.setOpacity(1);
                botonReserva.setDisable(false);
                botonReserva.setOpacity(1);
                botonVentaReserva.setDisable(false);
                botonVentaReserva.setOpacity(1);

                botonVerSala.setDisable(false);
                botonVerSala.setOpacity(1);

            } else {

                proyecSelect = null;

                botonVenta.setDisable(true);
                botonVenta.setOpacity(0.15);
                botonReserva.setDisable(true);
                botonReserva.setOpacity(0.15);
                botonVentaReserva.setDisable(true);
                botonVentaReserva.setOpacity(0.15);

                botonVerSala.setDisable(true);
                botonVerSala.setOpacity(0.15);
            }
        });

    }

    private void muestraProyecciones(String tituloPelicula) {

        ArrayList<Proyeccion> proyecciones = (ArrayList<Proyeccion>) bd.getProyeccion(tituloPelicula, selectorDia.getValue());

        if (proyecciones.get(0) != null) {
            labelTitulo.setText(tituloPelicula);

            labelDirector.setText(proyecciones.get(0).getPelicula().getDirector());
            labelDuracion.setText(proyecciones.get(0).getPelicula().getDuracion() + " mins");
            labelPais.setText(proyecciones.get(0).getPelicula().getPaisyAnyo());

        }

        data = FXCollections.observableArrayList(proyecciones);

        horaCol.setCellValueFactory(new PropertyValueFactory<Proyeccion, String>("horaInicio"));

        salaCol.setCellValueFactory(new Callback<CellDataFeatures<Proyeccion, String>, ObservableValue<String>>() {
            public ObservableValue<String> call(CellDataFeatures<Proyeccion, String> p) {
                return new ReadOnlyStringWrapper(p.getValue().getSala().getNombresala());
            }
        });

        tabla.setItems(data);

        selectorPelicula.setDisable(
                false);
    }

    @FXML
    private void seleccionDia(ActionEvent event) {

        selectorPelicula.getItems().remove(0, selectorPelicula.getItems().size());

        tabla.setItems(null);

        DatePicker dp = (DatePicker) event.getSource();

        LocalDate date = dp.getValue();

        ArrayList<Pelicula> peliculas = (ArrayList) bd.getPeliculas(date);

        if (peliculas.isEmpty()) {
            System.out.println("NO HAY ELEMENTOS");
            selectorPelicula.setDisable(true);
        } else {
            for (Pelicula pelicula : peliculas) {
                selectorPelicula.getItems().add(pelicula.getTitulo());
            }
            selectorPelicula.setDisable(false);
        }

    }

    @FXML
    private void efectuaVenta(ActionEvent event) throws IOException {
        FXMLLoader myLoad = new FXMLLoader(getClass().getResource("/filmboxapp/FXMLventa.fxml"));
        Parent root = myLoad.load();

        Scene scene = new Scene(root);
        Stage stage = new Stage();
        stage.setScene(scene);
        stage.setTitle("Efectuando venta: " + proyecSelect.getPelicula().getTitulo());

        stage.show();
        ((FXMLventa) myLoad.getController()).dameProyeccion(proyecSelect);

    }

    @FXML
    private void efectuaReserva(ActionEvent event) throws IOException {
        FXMLLoader myLoad = new FXMLLoader(getClass().getResource("/filmboxapp/FXMLreservar.fxml"));
        Parent root = myLoad.load();

        Scene scene = new Scene(root);
        Stage stage = new Stage();
        stage.setScene(scene);
        stage.setTitle("Efectuando reserva: " + proyecSelect.getPelicula().getTitulo());

        stage.show();
        ((FXMLreserva) myLoad.getController()).dameProyeccion(proyecSelect);
    }

    @FXML
    private void efectuaVentaReserva(ActionEvent event) throws IOException {
        FXMLLoader myLoad = new FXMLLoader(getClass().getResource("/filmboxapp/FXMLComprarDesdeReservar.fxml"));
        Parent root = myLoad.load();

        Scene scene = new Scene(root);
        Stage stage = new Stage();
        stage.setScene(scene);
        stage.setTitle("Efectuando venta desde reserva: " + proyecSelect.getPelicula().getTitulo());

        stage.show();
        ((FXMLCompraReserContro) myLoad.getController()).dameProyeccion(proyecSelect);

    }
}
