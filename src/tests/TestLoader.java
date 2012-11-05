package tests;

import org.jdom.Element;
import org.junit.Before;
import org.junit.Test;
import org.dbview.addons.input.mysql.*;
import org.dbview.addons.output.table.dotLight.*;
import org.dbview.db.structure.*;
import org.dbview.input_addons.*;

public class TestLoader
{
	// Constants for tests.
	private static final String CONFIGURATION_HOST     = new String("localhost");
	private static final String CONFIGURATION_LOGIN    = new String("root");
	private static final String CONFIGURATION_PASSWORD = new String("root"); // W:'' M:root
	private static final String CONFIGURATION_DBNAME   = new String("mydb");
	private static final String CONFIGURATION_FK       = new String("");
	private static final int    CONFIGURATION_PORT     = 8889; // W:3308 M:8889
	
    @Before public void setUp()
    {
        System.out.println("");
        System.out.println("------------------------------------------------------------");
        System.out.println("Testing DB loading and exporting.");
        System.out.println("------------------------------------------------------------");
        System.out.println("");
    }
    
    @Test public void testMe()
    {
    	// Create a profile for the database used for test.
    	
        Element data = new Element("data");
        data.addContent(new Element(org.dbview.addons.input.utils.mysql.XML.HOST).addContent(TestLoader.CONFIGURATION_HOST));
        data.addContent(new Element(org.dbview.addons.input.utils.mysql.XML.LOGIN).addContent(TestLoader.CONFIGURATION_LOGIN));
        data.addContent(new Element(org.dbview.addons.input.utils.mysql.XML.PASSWORD).addContent(TestLoader.CONFIGURATION_PASSWORD));
        data.addContent(new Element(org.dbview.addons.input.utils.mysql.XML.DBNAME).addContent(TestLoader.CONFIGURATION_DBNAME));
        data.addContent(new Element(org.dbview.addons.input.utils.mysql.XML.PORT).addContent(new Integer(TestLoader.CONFIGURATION_PORT).toString()));
        data.addContent(new Element(org.dbview.addons.input.utils.mysql.XML.FKMATCHER).addContent(TestLoader.CONFIGURATION_FK));
        @SuppressWarnings("unused")
        Configuration conf = new Configuration();
        
        System.out.println("Test loading...");
        try
        {   
            System.out.println("Loading");
        	InputCatalog input = new InputCatalog();
        	AbstractLoader loader = (AbstractLoader)input.getAdaptor("mysql", "Loader");
        	Database d = loader.load(data);
        	
        	System.out.println("Exporting");
        	Exporter ex    = new Exporter();
        	Element config = new Element("conf");
        	ex.export(d, config, d.getTables());
        }
        catch (Exception e)
        {
        	System.out.println("ERROR: " + e.getMessage());
        }
        System.out.println("DONE");
    }
    
}
