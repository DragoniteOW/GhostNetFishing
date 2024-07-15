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
            new GhostNet(1, new GPSLocation(1.8, 3.7), 20, Status.GEMELDET),
            new GhostNet(2, new GPSLocation(9.049357, -28.568964), 4, Status.BERGUNG_BEVORSTEHEND, new Person("Peter", "Lustig", "+49 123456789"), new Person("Peter", "Lustig", "+49 123456789")),
            new GhostNet(3, new GPSLocation(-20.453, 0.01), 3, Status.GEBORGEN, new Person("Karl", "Heinz", "+8155577005"), new Person("Christopher", "Schwiewager", "+17612345678")),
            new GhostNet(4, new GPSLocation(-8.57238947, -9.1231), 75, Status.GEMELDET, new Person("Hans", "Imglück", "06955555555"))
             });

    public SheaSepherd()
    {
        
    }
}