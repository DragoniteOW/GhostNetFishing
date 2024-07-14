import jakarta.faces.component.UIComponent;
import jakarta.faces.context.FacesContext;
import jakarta.faces.convert.Converter;
import jakarta.faces.convert.FacesConverter;

import java.util.ArrayList;
import java.util.List;

@FacesConverter("StatusConverter")
public class StatusConverter implements Converter<Object> {

    @Override
    public Object getAsObject(FacesContext facesContext, UIComponent uiComponent, String s) {
        if(s == "Gemeldet")
        	return Status.GEMELDET;
        if(s == "Bergung bevorstehend")
        	return Status.BERGUNG_BEVORSTEHEND;
        if(s == "Geborgen")
        	return Status.GEBORGEN;
        if(s == "Verschollen")
        	return Status.VERSCHOLLEN;
        //else
        return null;
    }

    
    @Override
    public String getAsString(FacesContext facesContext, UIComponent uiComponent, Object o) {
        String compare = o.toString();
        if (compare == Status.GEMELDET.toString())
        	return "Gemeldet";
        if (compare == Status.BERGUNG_BEVORSTEHEND.toString())
        	return "Bergung bevorstehend";
        if (compare == Status.GEBORGEN.toString())
        	return"Geborgen";
        if (compare == Status.VERSCHOLLEN.toString())
        	return "Verschollen";
        //else
        return "Unbekannt";
    }
}