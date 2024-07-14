import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Base64;
import java.util.Collection;
import java.util.GregorianCalendar;
import java.util.List;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;

@Named
@ApplicationScoped
public class SheaSepherd
{
    
    static final List<GhostNet> initGhostNets = Arrays.asList(new GhostNet[]{
            new GhostNet(1, new GPSLocation(0, 1,2), 20, Status.GEMELDET),
            new GhostNet(2, new GPSLocation(1, 30.48,69.420), 4, Status.BERGUNG_BEVORSTEHEND, new Person("Peter", "Lustig", "+49 123456789"), new Person("Peter", "Lustig", "+49 123456789"))
             });

    
    /**
     * Creates a new instance of our SheaSepherd app base
     */
    public SheaSepherd()
    {
        
    }
}