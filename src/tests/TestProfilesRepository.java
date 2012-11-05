package tests;

import static org.junit.Assert.*;


import org.junit.Before;
import org.junit.Test;
import org.dbview.addons.input.utils.mysql.XML;
import org.dbview.input_addons.AbstractConfiguration;
import org.dbview.input_addons.InputCatalog;
import org.dbview.input_addons.ProfilesRepository;
import org.jdom.Element;
import org.jdom.JDOMException;
import java.util.ArrayList;

/**
 * This class tests the profiles' directory management.
 * @author Denis BEURIVE
 */
public class TestProfilesRepository
{
    // Constants for tests.
    private static final String CONFIGURATION_NAME     = new String("installation");
    private static final String CONFIGURATION_HOST     = new String("labdev");
    private static final String CONFIGURATION_LOGIN    = new String("root");
    private static final String CONFIGURATION_PASSWORD = new String("toto");
    private static final String CONFIGURATION_DBNAME   = new String("MyDb");
    private static final String CONFIGURATION_TARGET   = new String("mysql");
    private static final String CONFIGURATION_FK       = new String("Pattern1");
    private static final int    CONFIGURATION_PORT     = 1097;
    
    @Before
    public void setUp()
    {
        System.out.println("");
        System.out.println("------------------------------------------------------------");
        System.out.println("Testing the profiles' repository.");
        System.out.println("------------------------------------------------------------");
        System.out.println("");
        
        try {
            System.out.println("Initialize the profiles' repository...");
            ProfilesRepository.init();
            System.out.println(ProfilesRepository.print());
            System.out.println();
        }
        catch (Exception e)
        {
            System.out.println("ERROR: " + e.getMessage());
        }
        System.out.println("DONE");
    }
    
    /**
     * Test repository's initialization.
     */
    @Test public void testRepository() 
    {
        try
        {
        	Element configuration = null;
        	Element data          = null;
            AbstractConfiguration profile = ProfilesRepository.getProfileInstance("testMySql");
            assertTrue(null == profile);
            
            System.out.print("Creating a profile...");
            
            // Create a profile.
            configuration = new Element("configuration");
            data = new Element("data");
            data.addContent(new Element(XML.HOST).addContent(TestProfilesRepository.CONFIGURATION_HOST));
            data.addContent(new Element(XML.LOGIN).addContent(TestProfilesRepository.CONFIGURATION_LOGIN));
            data.addContent(new Element(XML.PASSWORD).addContent(TestProfilesRepository.CONFIGURATION_PASSWORD));
            data.addContent(new Element(XML.DBNAME).addContent(TestProfilesRepository.CONFIGURATION_DBNAME));
            data.addContent(new Element(XML.PORT).addContent(new Integer(TestProfilesRepository.CONFIGURATION_PORT).toString()));
            data.addContent(new Element(XML.FKMATCHER).addContent(TestProfilesRepository.CONFIGURATION_FK));
            configuration.setAttribute("name", TestProfilesRepository.CONFIGURATION_NAME);
            configuration.setAttribute("target", TestProfilesRepository.CONFIGURATION_TARGET);
            configuration.addContent(data);

            InputCatalog catalog = new InputCatalog();
            profile = (AbstractConfiguration)catalog.getAdaptor(TestProfilesRepository.CONFIGURATION_TARGET, "Configuration");
            profile.fromXml(configuration, null, null);
            System.out.println(" DONE");
            
            // Add the new profile.
            System.out.print("Add the new profile to the repository...");
            ProfilesRepository.add(profile);
            System.out.println(" DONE");
            System.out.println(ProfilesRepository.print());
            
            Thread.sleep(1000);
            
            // Update the pofile.
            System.out.print("Update the profile...");
            configuration = new Element("configuration");
            data = new Element("data");
            data.addContent(new Element(XML.HOST).addContent(TestProfilesRepository.CONFIGURATION_HOST));
            data.addContent(new Element(XML.LOGIN).addContent(TestProfilesRepository.CONFIGURATION_LOGIN));
            data.addContent(new Element(XML.PASSWORD).addContent("The new password"));
            data.addContent(new Element(XML.DBNAME).addContent(TestProfilesRepository.CONFIGURATION_DBNAME));
            data.addContent(new Element(XML.PORT).addContent(new Integer(TestProfilesRepository.CONFIGURATION_PORT).toString()));
            data.addContent(new Element(XML.FKMATCHER).addContent(TestProfilesRepository.CONFIGURATION_FK));
            configuration.setAttribute("name", TestProfilesRepository.CONFIGURATION_NAME);
            configuration.setAttribute("target", TestProfilesRepository.CONFIGURATION_TARGET);
            configuration.addContent(data);

            profile.fromXml(configuration, null, null);
            ProfilesRepository.update(profile);
            System.out.println(" DONE");
            
            System.out.println(ProfilesRepository.print());
            
            ArrayList<String> list = ProfilesRepository.getProfilesNames();
            for (int i=0; i<list.size(); i++) { System.out.println(">> " + list.get(i)); }
        }
        catch (JDOMException e)
        {
            System.out.println("ERROR: " + e.getMessage());
        }
        catch (Exception e)
        {
            System.out.println("ERROR: " + e.getMessage());
        }
        
    }
}
