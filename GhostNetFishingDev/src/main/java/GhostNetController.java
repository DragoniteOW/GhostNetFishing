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
    
    // swap to new entry page
    public String erfassen()
    {
    	// we dont want any previously data to be used
    	currentNet = null;
    	initMeldendePerson();
    	
    	return "erfassen.xhtml?faces-redirect=true";
    }
    
    // get formatted "title" string of a GhostNet, like a description
    public static String getTitle(GhostNet net)
    {
    	String title = "Nr. ";
    	title += net.getNr();
    	title += " - Latitude: ";
    	title += net.getStandort().getLatitude();
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
    
    // initialize and swap to edit page
    public String edit(GhostNet net)
    {
    	currentNet = new GhostNet(net.getNr(), net.getStandort(), net.getGröße(), net.getStatus(), net.getMeldendePerson(), net.getBergendePerson());
    	initBergendePerson();
    	initMeldendePerson();
    	return "edit.xhtml?faces-redirect=true";
    }
    
    // initialize or use already set Person
    public void initMeldendePerson()
    {
    	if(currentNet == null || currentNet.getMeldendePerson() == null)
    		this.meldendePerson = new Person("", "", "");
    	else
    		this.meldendePerson = currentNet.getMeldendePerson();
    }
    
    // initialize or use already set Person
    public void initBergendePerson()
    {
    	if(currentNet.getBergendePerson() == null)
    		this.bergendePerson = new Person("", "", "");
    	else
    		this.bergendePerson = currentNet.getBergendePerson();
    }
    
    // swap back to homepage
    public String übersicht()
    {
    	return "index.xhtml?faces-redirect=true";
    }
    
    // swap to Google Maps overview
    public String mapView()
    {
    	return "mapview.xhtml?faces-redirect=true";
    }
    
    // initialize new GhostNet and return its index
    public int createNewGhostNet() 
    {
    	GhostNet n = new GhostNet();
    	
    	int newNr = (int)ghostNetDAO.getGhostNetCount() + 1;
    	n.setNr(newNr);
    	this.newGhostNet = n;
    	return newNr;
    }
    
    // check validity of any given GhostNet
    public static boolean isValidGhostNet(GhostNet n)
    {
    	return (n.getNr() != 0 && n.getStandort() != null && n.getStatus() != null && n.getGröße() != 0);
    	
    }
    
    // validator for UI Input: phone number
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
    
    // validator for UI Input: GPS Location in Decimal Degrees
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
    
    // validator for UI Input: Name (check for disallowed signs)
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
    
    // when a GhostNet is GEBORGEN, it may no longer be edited. This checks
    // that for any given GhostNet
    public boolean isDone(GhostNet n)
    {
    	return (n.getStatus() == Status.GEBORGEN);
    }
    
    // saves a new GhostNet entry, accessing the DAO
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
            
            //verhindert spätere Befüllung der Formulare
            this.meldendePerson = null;
            this.newGhostNet = null;
    		return "index.xhtml?faces-redirect=true";
    	}
    	else
    		return "";
    }
    
    // saves our edited GhostNet and sets the appropriate Personen
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
    
    // returns a formatted string of a Person
    public String getDisplayNameBergend(Person p)
	{
		if(p == null)
		{
			return "";
		}
		
		return p.getFirstName() + " " + p.getLastName() + ", " + p.getTelNr();
	}
    
    // returns a formatted string of a Person
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
    
    // getters & setters

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
	
	public GhostNet getNewGhostNet() {
    	return newGhostNet;
    }
	
	public void setNewGhostNet(GhostNet n) {
		newGhostNet = n;
	}
    
    public GhostNet getCurrentNet() {
    	return currentNet;
    }
    
    public void setCurrentNet(GhostNet n) {
    	currentNet = n;
    }
}
