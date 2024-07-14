import java.io.Serializable;

import java.util.regex.Pattern;
import java.util.regex.Matcher;
import jakarta.faces.application.FacesMessage;
import jakarta.faces.component.UIComponent;
import jakarta.faces.context.FacesContext;
import jakarta.enterprise.context.SessionScoped;
import jakarta.faces.validator.ValidatorException;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import jakarta.persistence.EntityTransaction;

@Named
@SessionScoped
public class GhostNetController implements Serializable
{
    private GhostNet newGhostNet;
    
    private GhostNet currentNet;
    
    private Person meldendePerson;
    
    private Person bergendePerson;

    
    @Inject
    GhostNetDAO ghostNetDAO;
    
    @Inject
    MapController mapController;

    
    public GhostNet getNewGhostNet() {
    	return newGhostNet;
    }
    
    public GhostNet getCurrentNet()
    {
    	return currentNet;
    }
    
    public String erfassen()
    {
    	return "erfassen.xhtml?faces-redirect=true";
    }
    
    public static String getTitle(GhostNet net)
    {
    	String title = "Nr. ";
    	title += net.getNr();
    	title += " - Latitude: ";
    	title += net.getStandort().getAltitude();
    	title += " Longitude: ";
    	title += net.getStandort().getLongitude();
    	title += " - Status: ";
    	title += net.getStatus().toString();
    	title += " - Gemeldet von: ";
    	if(net.getMeldendePerson() != null)
    		title += net.getMeldendePerson().getFirstName() + " " + net.getMeldendePerson().getLastName();
    	else
    		title += "anonym";
    	if(net.getBergendePerson() != null)
    	{
    		title += " - Bergung durch: ";
    		title += net.getBergendePerson().getFirstName() + " " + net.getBergendePerson().getLastName() + ", Tel: " + net.getBergendePerson().getTelNr();
    	}
    	return title;
    }
    
    public String edit(GhostNet net)
    {
    	currentNet = new GhostNet(net.getNr(), net.getStandort(), net.getGröße(), net.getStatus(), net.getMeldendePerson(), net.getBergendePerson());
    	initBergendePerson();
    	initMeldendePerson();
    	return "edit.xhtml?faces-redirect=true";
    }
    
    public void initMeldendePerson()
    {
    	if(currentNet == null || currentNet.getMeldendePerson() == null)
    		this.meldendePerson = new Person("", "", "");
    	else
    		this.meldendePerson = currentNet.getMeldendePerson();
    }
    
    public void initBergendePerson()
    {
    	if(currentNet.getBergendePerson() == null)
    		this.bergendePerson = new Person("", "", "");
    	else
    		this.bergendePerson = currentNet.getBergendePerson();
    }
    
    public String übersicht()
    {
    	return "index.xhtml?faces-redirect=true";
    }
    
    public String mapView()
    {
    	return "mapview.xhtml?faces-redirect=true";
    }

    public void removeGhostNet() {
       //to do
    }
    
    public int createNewGhostNet() 
    {
    	GhostNet n = new GhostNet();
    	initMeldendePerson();
    	int newNr = (int)ghostNetDAO.getGhostNetCount() + 1;
    	n.setNr(newNr);
    	this.newGhostNet = n;
    	return newNr;
    }
    
    public static boolean isValidGhostNet(GhostNet n)
    {
    	return (n.getNr() != 0 && n.getStandort() != null && n.getStatus() != null && n.getGröße() != 0);
    	
    }
    
    public void validatePhoneNumber(FacesContext context, UIComponent component,
            Object value) throws ValidatorException {
    	
    	Pattern pattern = Pattern.compile("^(\\+{0,1}\\d\\d{1,3} ?)?\\d{3,15}$");
        Matcher matcher = pattern.matcher(value.toString());
        
        if(!matcher.find())
        {
            FacesMessage msg = 
                new FacesMessage("Fehlerhafte Tel. Nr.", 
                        "Fehlerhafte Tel. Nr.");
            msg.setSeverity(FacesMessage.SEVERITY_ERROR);
            throw new ValidatorException(msg);
        }

    }
    
    public void validateLocation(FacesContext context, UIComponent component,
            Object value) throws ValidatorException {
    	
    	GPSLocation location = (GPSLocation)value;
        
        if(!location.isValid())
        {
            FacesMessage msg = 
                new FacesMessage("Fehlerhafter Standort, außerhalb des erlaubten Bereichs", 
                        "Fehlerhafter Standort, außerhalb des erlaubten Bereichs");
            msg.setSeverity(FacesMessage.SEVERITY_ERROR);
            throw new ValidatorException(msg);
        }

    }
    
    public void validateName(FacesContext context, UIComponent component,
    		Object value) throws ValidatorException {
    	
    	Pattern pattern = Pattern.compile("[^()\\[\\]{}*&^%$#@!\\d]+");
    	Matcher matcher = pattern.matcher(value.toString());
        
        if(!matcher.matches())
        {
            FacesMessage msg = 
                new FacesMessage("Fehlerhafter Name", 
                        "Fehlerhafter Name, bitte nutzen Sie keine Sonderzeichen");
            msg.setSeverity(FacesMessage.SEVERITY_ERROR);
            throw new ValidatorException(msg);
        }
    }
    
    
    
    public boolean isDone(GhostNet n)
    {
    	return (n.getStatus() == Status.GEBORGEN);
    }
    
    public String saveNewGhostNet()
    {
    	if(isValidGhostNet(newGhostNet))
    	{
    		if(meldendePerson.isValid())
    			newGhostNet.setMeldendePerson(meldendePerson);
    		
    		System.err.println("Saving new GhostNet Nr. " + newGhostNet.getNr());
            EntityTransaction t = ghostNetDAO.getAndBeginTransaction();
            ghostNetDAO.persist(newGhostNet);
            t.commit();
            this.meldendePerson = null;
    		return "index.xhtml?faces-redirect=true";
    	}
    	else
    		return "";
    }
    
    public String saveCurrentGhostNet()
    {
    	if(isValidGhostNet(currentNet))
    	{
    		GhostNet n = ghostNetDAO.getGhostNetAtIndex(currentNet.getNr() -1);
    		
    		n.setStatus(currentNet.getStatus());
    		if((n.getStatus() == Status.BERGUNG_BEVORSTEHEND || n.getStatus() == Status.GEBORGEN) && bergendePerson.isValid())
    			n.setBergendePerson(bergendePerson);
    		else
    			n.setBergendePerson(null);
    		
    		if(n.getStatus() == Status.VERSCHOLLEN)
    			n.setMeldendePerson(meldendePerson);
    		
    		if(n.getStatus() == Status.GEMELDET)
    		{
    			if(meldendePerson.isValid())
    				n.setMeldendePerson(meldendePerson);
    			else
    				n.setMeldendePerson(null);
    		}
    		
    		System.err.println("Saving current GhostNet Nr. " + n.getNr());
            EntityTransaction t = ghostNetDAO.getAndBeginTransaction();
            ghostNetDAO.persist(n);
            t.commit();
            
    		return "index.xhtml?faces-redirect=true";
    	}
    	else
    		return "";
    }
    
    public String getDisplayNameBergend(Person p)
	{
		if(p == null)
		{
			return "";
		}
		
		return p.getFirstName() + " " + p.getLastName() + ", " + p.getTelNr();
	}
    
    public String getDisplayNameMeldend(Person p)
	{
		if(p == null || !p.isValid())
		{
			return "anonym";
		}
		
		String s = "";
		
		if(p.getFirstName() != null && !p.getFirstName().isBlank())
			s += (p.getFirstName() + " ");
		if(p.getLastName() != null && !p.getLastName().isBlank())
			s += p.getLastName();
		if(p.getTelNr() != null && !p.getTelNr().isBlank())
			s += (", " + p.getTelNr());
		
		return s;
	}
    
    public String updateGhostNet()
    {
    	if(isValidGhostNet(newGhostNet))
    	{
    		System.err.println("Updating and saving GhostNet Nr. " + newGhostNet.getNr());
            EntityTransaction t = ghostNetDAO.getAndBeginTransaction();
            ghostNetDAO.merge(newGhostNet);
            t.commit();
    		return "index.xhtml?faces-redirect=true";
    	}
    	else
    		return "";
    }

	public Person getMeldendePerson() {
		return meldendePerson;
	}

	public void setMeldendePerson(Person meldendePerson) {
		this.meldendePerson = meldendePerson;
	}

	public Person getBergendePerson() {
		return bergendePerson;
	}

	public void setBergendePerson(Person bergendePerson) {
		this.bergendePerson = bergendePerson;
	}
}
