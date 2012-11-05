package tests;

import org.junit.Before;
import org.junit.Test;
import java.lang.Exception;
import java.util.ArrayList;
import org.dbview.conf.Conf;
import org.dbview.utils.JavaVm;

/**
 * This class implements unitary tests for the JVM tools.
 * @author Denis Beurive
 */
public class TestJavaVm
{
	/**
	 * This string represents the name of the property associated to the JAR file that contains the targets' adaptor implementation.
	 * @note See the configuration file "conf/resources.properties".
	 */
	private static final String RESOURCE_NAME = "sotfForeignKeyDetectors";
	
    @Before
    public void setUp()
    {
        System.out.println("");
        System.out.println("------------------------------------------------------------");
        System.out.println("Testing JavaVm.");
        System.out.println("------------------------------------------------------------");
        System.out.println("");
    }   
    
    /**
     * The method tests the mechanism that loads the content of a JAR.
     */
    @Test public void listJar()
    {
        try
        {
        	String jar_name = Conf.get(RESOURCE_NAME);
            System.out.println("Listing the resource JAR " + jar_name);
            ArrayList<String> content = JavaVm.listJar(jar_name);
            for (int i=0; i<content.size(); i++) { System.out.println("\t" + content.get(i)); }
        }
        catch (Exception e) { System.out.println("ERROR: " + e.getMessage()); }

        System.out.println("DONE");
    }    
}
