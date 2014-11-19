package org.koenighotze.jee7hotel.frontend.converter;

import org.koenighotze.jee7hotel.domain.RoomEquipment;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;

/**
 * Created by dschmitz on 18.11.14.
 */
@FacesConverter(forClass = RoomEquipment.class)
public class RoomEquipmentConverter  implements Converter {
    @Override
    public Object getAsObject(FacesContext facesContext, UIComponent uiComponent, String s) {
        if (null == s) {
            return null;
        }

        for (RoomEquipment e : RoomEquipment.values()) {
            if (e.name().equals(s)) {
                return e;
            }
        }

        return null;
    }

    @Override
    public String getAsString(FacesContext facesContext, UIComponent uiComponent, Object o) {
        if (!(o instanceof RoomEquipment)) {
            return null;
        }

        return ((RoomEquipment) o).name();
    }
}
