import java.io.Serializable;

import org.primefaces.event.map.OverlaySelectEvent;
import org.primefaces.model.map.Marker;
import org.primefaces.model.map.DefaultMapModel;
import org.primefaces.model.map.LatLng;
import org.primefaces.model.map.MapModel;

import jakarta.faces.application.FacesMessage;
import jakarta.faces.context.FacesContext;
import jakarta.faces.view.ViewScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;

@Named
@ViewScoped
public class MapController implements Serializable
{
	private final static MapModel mapModel = new DefaultMapModel();
	
	private Marker marker;
	
	@Inject
	GhostNetDAO ghostNetDAO;
	
	public MapController()
	{
		
	}
	
	public Marker getMarker()
	{
		return marker;
	}

    // adds Markers as an Overlay to our map model
    public MapModel initMapModel()
    {
    	for(GhostNet net: ghostNetDAO.getGhostNets())
    	{
    		Status status = net.getStatus();
    		if (status==Status.GEMELDET || status==Status.BERGUNG_BEVORSTEHEND)
    		{
    			LatLng latlng = new LatLng(net.getStandort().getLatitude(), net.getStandort().getLongitude());
    			mapModel.addOverlay(new Marker(latlng, GhostNetController.getTitle(net)));
    		}
    	}
    	return mapModel;
    }
	
    // handles the event that the user clicks on a map marker
	public void onMarkerSelect(OverlaySelectEvent event)
	{
	    	marker = (Marker) event.getOverlay();
	
	        FacesContext.getCurrentInstance().addMessage(null,
	                new FacesMessage(FacesMessage.SEVERITY_INFO,
	                        "GhostNet", marker.getTitle()));
	}

	public static MapModel getMapmodel() {
		return mapModel;
	}
}