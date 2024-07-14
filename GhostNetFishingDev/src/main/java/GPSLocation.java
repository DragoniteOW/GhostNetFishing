import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;

@Entity
public class GPSLocation
{
	// one easy format of GPS Locations is "Decimal Degrees" or "DD", which consists of 2 numbers
	// latitude and longitude. We use double as a datatype to allow for better accuracy, which may 
	// be needed on deep sea operations
	private double latitude;
	private double longitude;
	
	@Id
	@GeneratedValue
	private int nr;
	
	public GPSLocation()
	{
		latitude = 0;
		longitude = 0;
	}
	
	public GPSLocation(int nr, double alt, double lng)
	{
		this.latitude = alt;
		this.setLongitude(lng);
	}
	
	public int getNr() {
		return nr;
	}
	
	public void setNr(int nr) {
		this.nr = nr;
	}

	public double getLatitude() {
		return latitude;
	}

	public void setLatitude(double latitude) {
		this.latitude = latitude;
	}

	public double getLongitude() {
		return longitude;
	}

	public void setLongitude(double longitude) {
		this.longitude = longitude;
	}
	
	// Decimal Degrees may only be between -90 and 90 in latitude, and -180 to 180 in longitude
	public Boolean isValid()
	{
		if(latitude < -90 || latitude > 90)
			return false;
		if(longitude <-180 || longitude > 180)
			return false;
		
		return true;
	}
}