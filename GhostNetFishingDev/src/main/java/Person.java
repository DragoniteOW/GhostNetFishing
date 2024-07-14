import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Named;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;

@Entity
@Named
@ApplicationScoped
public class Person 
{
	@Id
	@GeneratedValue
	private int nr;
	
	private String firstName;
	private String lastName;
	private String telNr;
	
	public Person()
	{
		
	}
	
	public Person(String firstname, String lastname)
	{
		this.firstName = firstname;
		this.lastName = lastname;
	}
	
	public Person(String firstname, String lastname, String telnr)
	{
		this(firstname, lastname);
		this.telNr = telnr;
	}

	public int getNr() {
		return nr;
	}

	public void setNr(int nr) {
		this.nr = nr;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public String getTelNr() {
		return telNr;
	}

	public void setTelNr(String telNr) {
		this.telNr = telNr;
	}
	
	public boolean isValid()
	{
		if(firstName == null || lastName == null)
			return false;
		return !(firstName.isBlank() && lastName.isBlank());
	}
	
	
	
	
}