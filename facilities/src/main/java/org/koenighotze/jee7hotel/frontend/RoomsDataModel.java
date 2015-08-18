package org.koenighotze.jee7hotel.frontend;

import org.koenighotze.jee7hotel.domain.Room;

import javax.faces.model.ListDataModel;
import java.util.List;

/**
 * Created by dschmitz on 18.12.14.
 */
public class RoomsDataModel extends ListDataModel<Room> {
    public RoomsDataModel(List<Room> list) {
        super(list);
    }
}
