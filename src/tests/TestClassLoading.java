package tests;


import org.junit.Before;
import org.junit.Test;
import org.dbview.resources.AbstractSotfForeignKeyDetector;

/**
 * This class explores the JAVA class loading process.
 * @author Denis Beurive
 */
public class TestClassLoading
{
    private static final String __CLASS = "org.dbview.resources.softforeignkeydetectors.FkTargetTableName"; 
    
    @Before
    public void setUp()
    {
        System.out.println("");
        System.out.println("------------------------------------------------------------");
        System.out.println("Testing class loading.");
        System.out.println("------------------------------------------------------------");
        System.out.println("");
    }    
    
    /**
     * Print the CLASSPATH.
     */
    @Test public void showClassPath()
    {
        System.out.println("CLASSPATH");
        String pathes[] = System.getProperty("java.class.path").split(":");
        for (int i=0; i<pathes.length; i++) { System.out.println("\t> " + pathes[i]); }
        System.out.println("DONE");
    }
    
    /**
     * Create an instance of a class (from JAR or ".class".).
     * @throws ClassNotFoundException
     * @throws IllegalAccessException
     * @throws InstantiationException
     */
    @Test public void loadFkExtension() throws ClassNotFoundException, IllegalAccessException, InstantiationException
    {
    	System.out.println("Loading foreign key pattern matcher " + TestClassLoading.__CLASS + "...");
    	try
    	{
    	    @SuppressWarnings("unused")
	        AbstractSotfForeignKeyDetector p = (AbstractSotfForeignKeyDetector) Class.forName(TestClassLoading.__CLASS).newInstance();
    	}
    	catch (Exception e)
    	{
    		System.out.println("ERROR: " + e.getMessage());
    	}
    	System.out.println("DONE");
    }
}
