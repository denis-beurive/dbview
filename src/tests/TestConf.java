package tests;

import org.junit.Before;
import org.junit.Test;
import org.dbview.conf.Conf;

/**
 * This class tests the mechanism used to load the application's configuration (from the properties file). 
 * @author Denis Beurive
 */
public class TestConf
{
    private static final String __CONF1 = "sotfForeignKeyDetectors";
    private static final String __CONF2 = "inputAddons";
    
    
    @Before
    public void setUp()
    {
        System.out.println("");
        System.out.println("------------------------------------------------------------");
        System.out.println("Testing Conf.");
        System.out.println("------------------------------------------------------------");
        System.out.println("");
    }    
    
    /**
     * This method loads the configuration.
     */
    @Test public void load()
    {
        System.out.println("Load the configuration... ");
        try {
                Conf.init();
                System.out.println(Conf.get(TestConf.__CONF1));
                System.out.println(Conf.get(TestConf.__CONF2));
            }
        catch(Exception e)
        {
            System.out.println("ERROR: " + e.getMessage());
        }
        System.out.println("DONE");
    }
}
