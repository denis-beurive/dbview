package tests;

import org.dbview.addons.AddOnCatalogException;
import org.dbview.addons.input.utils.mysql.XML;
import org.dbview.input_addons.*;
import org.jdom.*;
import org.jdom.output.XMLOutputter;
import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;

public class TestConfiguration
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
  
  private Element __configuration_light      = null;
  private Element __configuration_full       = null;
  private AbstractConfiguration __conf_full  = null;
  private AbstractConfiguration __conf_light = null;

  @Before
  public void setUp()
  {
      System.out.println("");
      System.out.println("------------------------------------------------------------");
      System.out.println("Testing configuration.");
      System.out.println("------------------------------------------------------------");
      System.out.println("");
      
      // Build a full configuration.
      System.out.print("Create full XML... ");
      this.__configuration_full = new Element("configuration");
      Element data = new Element("data");
      data.addContent(new Element(XML.HOST).addContent(TestConfiguration.CONFIGURATION_HOST));
      data.addContent(new Element(XML.LOGIN).addContent(TestConfiguration.CONFIGURATION_LOGIN));
      data.addContent(new Element(XML.PASSWORD).addContent(TestConfiguration.CONFIGURATION_PASSWORD));
      data.addContent(new Element(XML.DBNAME).addContent(TestConfiguration.CONFIGURATION_DBNAME));
      data.addContent(new Element(XML.PORT).addContent(new Integer(TestConfiguration.CONFIGURATION_PORT).toString()));
      data.addContent(new Element(XML.FKMATCHER).addContent(TestConfiguration.CONFIGURATION_FK));
      this.__configuration_full.setAttribute("name", TestConfiguration.CONFIGURATION_NAME);
      this.__configuration_full.setAttribute("target", TestConfiguration.CONFIGURATION_TARGET);
      this.__configuration_full.addContent(data);
      System.out.println("DONE");

      // Build a light configuration.
      System.out.print("Create light XML... ");
      this.__configuration_light = new Element("data");
      this.__configuration_light.addContent(new Element(XML.HOST).addContent(TestConfiguration.CONFIGURATION_HOST));
      this.__configuration_light.addContent(new Element(XML.LOGIN).addContent(TestConfiguration.CONFIGURATION_LOGIN));
      this.__configuration_light.addContent(new Element(XML.PASSWORD).addContent(TestConfiguration.CONFIGURATION_PASSWORD));
      this.__configuration_light.addContent(new Element(XML.DBNAME).addContent(TestConfiguration.CONFIGURATION_DBNAME));
      this.__configuration_light.addContent(new Element(XML.PORT).addContent(new Integer(TestConfiguration.CONFIGURATION_PORT).toString()));
      this.__configuration_light.addContent(new Element(XML.FKMATCHER).addContent(TestConfiguration.CONFIGURATION_FK));
      System.out.println("DONE");
      
      // Initialise the configuration with the newly created XML element.
      try
      {
          if (Boolean.FALSE)
          {
        	  System.out.println("FULL configuration source");
        	  System.out.println();
        	  System.out.println(org.dbview.utils.Jdom.print(this.__configuration_full));
        	  System.out.println();
        	  System.out.println("LIGHT configuration source");
        	  System.out.println();
        	  System.out.println(org.dbview.utils.Jdom.print(this.__configuration_light));
        	  System.out.println();
          }
    	  
    	  InputCatalog catalog = new InputCatalog();
    	  
    	  System.out.print("Initialize the full configuration... ");
	      this.__conf_full = (AbstractConfiguration)catalog.getAdaptor(TestConfiguration.CONFIGURATION_TARGET, "Configuration");
	      this.__conf_full.fromXml(this.__configuration_full, null, null);
	      System.out.println("DONE");
	      
    	  System.out.print("Initialize the light configuration... ");
	      this.__conf_light = (AbstractConfiguration)catalog.getAdaptor(TestConfiguration.CONFIGURATION_TARGET, "Configuration");
	      this.__conf_light.fromXml(this.__configuration_light, TestConfiguration.CONFIGURATION_NAME, TestConfiguration.CONFIGURATION_TARGET);
	      System.out.println("DONE");
	      
          if (Boolean.FALSE)
          {
    	      System.out.println("FULL configuration initialized");
    	      System.out.println();
    	      System.out.println(org.dbview.utils.Jdom.print(this.__conf_full.toXml()));
    	      System.out.println();
    	      System.out.println("LIGHT configuration source");
    	      System.out.println();
    	      System.out.println(org.dbview.utils.Jdom.print(this.__conf_light.toXml()));
    	      System.out.println();
          } 
      }
      catch (AddOnCatalogException e)
      {
    	  System.out.println("ERROR: " + e.getMessage());
	  }
      catch (ConfigurationException e)
      {
          System.out.println("ERROR: " + e.getMessage());
          assertTrue(0==1);
      }
      catch (Exception e)
      {
    	  System.out.println("ERROR: " + e.getMessage());
	  }
  }
  
  @Test
  public void testFromFullXml()
  {
      try {
    	  	  Element data          = null;
              Element configuration = null;
              String  host          = null;
              String  port          = null;
              String  login         = null;
              String  password      = null;
              String  db_name       = null;
              String  fk_matcher    = null;
              
              // Double check the full configuration...
              
              System.out.print("Make sure that the full configuration is well saved... ");
              configuration = this.__conf_full.toXml();
              data = configuration.getChild("data"); // may be null
              
              if (Boolean.FALSE)
              {
            	  System.out.println("FULL configuration");
            	  System.out.println();
            	  System.out.println(org.dbview.utils.Jdom.print(configuration));
            	  System.out.println();
              }
              
              if (null == data) { throw new Exception("The variable \"data\" is null!"); }

              if (Boolean.FALSE)
              {
            	  System.out.println("FULL data");
            	  System.out.println();
            	  System.out.println(org.dbview.utils.Jdom.print(data));
            	  System.out.println();
              }

              host       = org.dbview.utils.Jdom.getTextOrCdata(data.getChild(XML.HOST));
              port       = org.dbview.utils.Jdom.getTextOrCdata(data.getChild(XML.PORT));
              login      = org.dbview.utils.Jdom.getTextOrCdata(data.getChild(XML.LOGIN));
              password   = org.dbview.utils.Jdom.getTextOrCdata(data.getChild(XML.PASSWORD));
              db_name    = org.dbview.utils.Jdom.getTextOrCdata(data.getChild(XML.DBNAME));
              fk_matcher = org.dbview.utils.Jdom.getTextOrCdata(data.getChild(XML.FKMATCHER));
              
              assertTrue(host.compareTo(TestConfiguration.CONFIGURATION_HOST) == 0);
              assertTrue(Integer.parseInt(port) == TestConfiguration.CONFIGURATION_PORT);
              assertTrue(login.compareTo(TestConfiguration.CONFIGURATION_LOGIN) == 0);
              assertTrue(password.compareTo(TestConfiguration.CONFIGURATION_PASSWORD) == 0);
              assertTrue(db_name.compareTo(TestConfiguration.CONFIGURATION_DBNAME) == 0);
              assertTrue(fk_matcher.compareTo(TestConfiguration.CONFIGURATION_FK) == 0);
              
              System.out.println("DONE");

              // Double check the light configuration...
              
              if (Boolean.FALSE)
              {
            	  System.out.println("LIGHT configuration");
            	  System.out.println();
            	  System.out.println(org.dbview.utils.Jdom.print(this.__configuration_light));
            	  System.out.println();
              }
              
              System.out.print("Make sure that the light configuration is well saved... ");
              configuration = this.__conf_light.toXml();
              data = configuration.getChild("data"); // may be null
              
              host       = org.dbview.utils.Jdom.getTextOrCdata(data.getChild(XML.HOST));
              port       = org.dbview.utils.Jdom.getTextOrCdata(data.getChild(XML.PORT));
              login      = org.dbview.utils.Jdom.getTextOrCdata(data.getChild(XML.LOGIN));
              password   = org.dbview.utils.Jdom.getTextOrCdata(data.getChild(XML.PASSWORD));
              db_name    = org.dbview.utils.Jdom.getTextOrCdata(data.getChild(XML.DBNAME));
              fk_matcher = org.dbview.utils.Jdom.getTextOrCdata(data.getChild(XML.FKMATCHER));
              
              assertTrue(host.compareTo(TestConfiguration.CONFIGURATION_HOST) == 0);
              assertTrue(Integer.parseInt(port) == TestConfiguration.CONFIGURATION_PORT);
              assertTrue(login.compareTo(TestConfiguration.CONFIGURATION_LOGIN) == 0);
              assertTrue(password.compareTo(TestConfiguration.CONFIGURATION_PASSWORD) == 0);
              assertTrue(db_name.compareTo(TestConfiguration.CONFIGURATION_DBNAME) == 0);
              assertTrue(fk_matcher.compareTo(TestConfiguration.CONFIGURATION_FK) == 0);
              System.out.println("DONE");
          }
      catch (AddOnCatalogException e)
      {
    	  System.out.println("ERROR: " + e.getMessage());
	  }
      catch (ConfigurationException e)
      {
          System.out.println("ERROR: " + e.getMessage());
          assertTrue(0==1);
      }
      catch (Exception e)
      {
    	  System.out.println("ERROR: " + e.getMessage() + " " + e.getClass());
	  }
  }

  @Test
  public void testToXml()
  {
     System.out.println("Converting configuration into XML... ");
     
     try {
	         XMLOutputter outputter = new XMLOutputter();     
	         outputter.setFormat(org.jdom.output.Format.getPrettyFormat());     
	         Element xml = this.__conf_full.toXml();
	         String s = outputter.outputString(xml);
	         System.out.println(s);     

	         this.__conf_full.fromXml(xml, null, null);
	         System.out.println("DONE");
         }
     catch (ConfigurationException e)
     {
         System.out.println("ERROR: " + e.getMessage());
         // assertTrue(0==1);
     }
     catch (Exception e)
     {
         System.out.println("ERROR: " + e.getMessage());
         assertTrue(0==1);
     }
  }
}
