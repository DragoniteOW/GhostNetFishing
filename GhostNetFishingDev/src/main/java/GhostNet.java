import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Named;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;

@Named
@ApplicationScoped
@Entity
public class GhostNet
{
	@Id
	private int nr;
	
	@OneToOne(fetch=FetchType.EAGER, cascade=CascadeType.ALL)
	private GPSLocation standort;
	
	private int größe;
	
	@ManyToOne(fetch= FetchType.EAGER, cascade=CascadeType.ALL)
	private Person bergendePerson = null;
	
	@ManyToOne(fetch= FetchType.EAGER, cascade=CascadeType.ALL)
	private Person meldendePerson = null;
	
	private Status status;
	
	// Konstruktoren
	
	public GhostNet()
	{
		this.größe = 0;
		this.status = Status.GEMELDET;
	}
	
	public GhostNet(int nr, GPSLocation coords, int größe, Status status)
	{
		this.nr = nr;
		this.standort = coords;
		this.setGröße(größe);
		this.status = status;
	}
	
	public GhostNet(int nr, GPSLocation coords, int größe, Status status, Person person)
	{
		this(nr, coords, größe, status);
		this.meldendePerson = person;
	}
	
	public GhostNet(int nr, GPSLocation coords, int größe, Status status, Person person, Person bergendeP)
	{
		this(nr, coords, größe, status, person);
		this.bergendePerson = bergendeP;
	}
	
	// status checks
	
	public boolean needsBergendePerson()
	{
		return (this.status == Status.BERGUNG_BEVORSTEHEND || this.status == Status.GEBORGEN);
	}
	
	public boolean isVerschollen()
	{
		return 	this.status == Status.VERSCHOLLEN;
	}
	
	public boolean isGemeldet()
	{
		return this.status == Status.GEMELDET;
	}
	
	// getters & setters
	
	public int getNr() {
		return nr;
	}
	public void setNr(int nr) {
		this.nr = nr;
	}
	public GPSLocation getStandort() {
		return standort;
	}
	public void setStandort(GPSLocation coordinates) {
		this.standort = coordinates;
	}
	public Person getBergendePerson() {
		return bergendePerson;
	}
	public void setBergendePerson(Person bergendePerson) {
		this.bergendePerson = bergendePerson;
	}

	public Status getStatus() {
		return status;
	}

	public void setStatus(Status status) {
		this.status = status;
	}

	public int getGröße() {
		return größe;
	}

	public void setGröße(int größe) {
		this.größe = größe;
	}

	public Person getMeldendePerson() {
		return meldendePerson;
	}

	public void setMeldendePerson(Person meldendePerson) {
		this.meldendePerson = meldendePerson;
	}
}