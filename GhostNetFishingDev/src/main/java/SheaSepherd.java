import java.util.Arrays;
import java.util.List;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Named;

@Named
@ApplicationScoped
public class SheaSepherd
{
    // our list to initialize the program, for testing purposes, if it's still empty
    static final List<GhostNet> initGhostNets = Arrays.asList(new GhostNet[]{
            new GhostNet(1, new GPSLocation(0, 1,2), 20, Status.GEMELDET),
            new GhostNet(2, new GPSLocation(1, 30.48,69.420), 4, Status.BERGUNG_BEVORSTEHEND, new Person("Peter", "Lustig", "+49 123456789"), new Person("Peter", "Lustig", "+49 123456789"))
             });

    public SheaSepherd()
    {
        
    }
}