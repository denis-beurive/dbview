package tests;

import org.junit.Before;
import org.junit.Test;
import org.dbview.addons.AddOnCatalogException;
import org.dbview.input_addons.InputCatalog;
import org.dbview.output_addons.OutputCatalog;
import java.util.Enumeration;

/**
 * This class implements unitary tests for the input adaptors' catalogue.
 * @author Denis Beurive
 */
public class TestCatalog
{
	InputCatalog in_catalog   = null;
	OutputCatalog out_catalog = null;
	
    @Before
    public void setUp()
    {
        System.out.println("");
        System.out.println("------------------------------------------------------------");
        System.out.println("Testing the input and output adaptor catalogs.");
        System.out.println("------------------------------------------------------------");
        System.out.println("");
        
        try {
	            System.out.println("Create an input catalog...");
	            this.in_catalog = new InputCatalog();
	            System.out.println("Create an output catalog...");
	            this.out_catalog = new OutputCatalog();
        	}
	    catch (Exception e)
	    {
	        System.out.println("ERROR: " + e.getMessage());
	    }
    }
    
    /**
     * Test catalogue's initialization.
     */
    @Test public void printCatalog()
    {
        try {
                System.out.println("Print the input catalog...");
                System.out.println(this.in_catalog.toString());
                System.out.println("Print the output catalog...");
                System.out.println(this.out_catalog.toString());
            }
        catch (Exception e)
        {
            System.out.println("ERROR: " + e.getMessage());
        }
        System.out.println("DONE");
    }
    
    /**
     * Test data extraction.
     */
    @Test public void getData()
    {
        try {
        		Enumeration<String> e = null;
        	
            	System.out.println("Get the list of targets from the input catalog...");
            	e = this.in_catalog.getTargets();
            	while (e.hasMoreElements()) { System.out.println(">>> " + e.nextElement()); }
            	System.out.println("Package for target \"mySql\": " + this.in_catalog.getPackage("mysql"));
            	
            	System.out.println("");
            	
            	System.out.println("Get the list of targets from the output catalog...");
            	e = this.out_catalog.getTargets();
            	String test_exporter = null;
            	while (e.hasMoreElements())
            	{
            	    test_exporter = e.nextElement();
            	    System.out.println(">>> " + test_exporter);
            	}
            	
            	System.out.println("Package for target \"" + test_exporter + "\": " + this.out_catalog.getPackage(test_exporter));
        	}
        catch (AddOnCatalogException e)
	    {
	        System.out.println("ERROR: " + e.getMessage());
	    }
	    catch (Exception e)
	    {
	        System.out.println("ERROR: " + e.getMessage());
	    }
	    System.out.println("DONE");
    }
    
    /**
     * Test the instances' retrieval.
     */
    @Test public void getInstances()
    {
        try {
        		System.out.println("Get instances from the input catalog...");
        		this.in_catalog.getAdaptor("mysql", "Cli");
        		this.in_catalog.getAdaptor("mysql", "Configuration");
        		this.in_catalog.getAdaptor("mysql", "Description");
        		this.in_catalog.getAdaptor("mysql", "Loader");
        		
        		System.out.println("Get instances from the output catalog...");
        		this.out_catalog.getAdaptor("dotFull", "Cli");
        		this.out_catalog.getAdaptor("dotFull", "Description");
    		}
        catch (AddOnCatalogException e)
	    {
	        System.out.println("ERROR: " + e.getMessage());
	    }
	    catch (Exception e)
	    {
	        System.out.println("ERROR: " + e.getMessage());
	    }
	    System.out.println("DONE");
    }

}
