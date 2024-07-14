import jakarta.faces.component.UIComponent;
import jakarta.faces.context.FacesContext;
import jakarta.faces.convert.Converter;
import jakarta.faces.convert.FacesConverter;

import java.util.ArrayList;
import java.util.List;

@FacesConverter("GPSConverter")
public class GPSConverter implements Converter<Object> {

    @Override
    public Object getAsObject(FacesContext facesContext, UIComponent uiComponent, String s) 
    {
    	if(s==null || s.isBlank() || !s.contains(","))
    		return null;
    	int p=0, q=0;
        List<Double> liste = new ArrayList<>();
        while (q<s.length() && (p=s.indexOf(",", q))>-1) {
            liste.add(Double.parseDouble(s.substring(q,p).trim()));
            q = p+1;
        }
        if (p+1<s.length()) liste.add(Double.parseDouble(s.substring(q, s.length()).trim()));
        return new GPSLocation(0, liste.get(0), liste.get(1));
    }

    
    @Override
    public String getAsString(FacesContext facesContext, UIComponent uiComponent, Object o) 
    {
        GPSLocation location = (GPSLocation)o;
        String s = String.valueOf(location.getLatitude()) + ", " + String.valueOf(location.getLongitude());
        return s;
    }
}