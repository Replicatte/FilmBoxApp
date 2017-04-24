/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package filmboxapp.cellsManage;

import javafx.scene.control.ListCell;
import modelo.Proyeccion;

/**
 *
 * @author repli
 */
public class ProyHoraListCell extends ListCell<Proyeccion> {

    @Override
    protected void updateItem(Proyeccion item, boolean empty) {

        super.updateItem(item, empty); //To change body of generated methods, choose Tools | Templates.
        if (item == null) {
            setText("NO HAY VALOR");
        } else {
            setText(item.getHoraInicio());
        }

    }

}
