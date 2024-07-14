import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;

@Entity
public class GPSLocation
{
	private double altitude;
	private double longitude;
	
	@Id
	@GeneratedValue
	private int nr;
	
	public GPSLocation()
	{
		altitude = 0;
		longitude = 0;
	}
	
	public GPSLocation(int nr, double alt, double lng)
	{
		this.altitude = alt;
		this.setLongitude(lng);
	}
	
	public int getNr() {
		return nr;
	}
	
	public void setNr(int nr) {
		this.nr = nr;
	}

	public double getAltitude() {
		return altitude;
	}

	public void setAltitude(double altitude) {
		this.altitude = altitude;
	}

	public double getLongitude() {
		return longitude;
	}

	public void setLongitude(double longitude) {
		this.longitude = longitude;
	}
	
	public Boolean isValid()
	{
		if(altitude < -90 || altitude > 90)
			return false;
		if(longitude <-180 || longitude > 180)
			return false;
		
		return true;
	}
}