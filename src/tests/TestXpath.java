package tests;

import java.util.List;

import org.jdom.xpath.XPath;
import org.junit.Test;
import org.jdom.*;

public class TestXpath
{
    /**
     * Test catalogue's initialization.
     */
    @Test public void printCatalog()
    {
        System.out.println("");
        System.out.println("------------------------------------------------------------");
        System.out.println("Testing XPATH.");
        System.out.println("------------------------------------------------------------");
        System.out.println("");
        
        try {
                Element root = new Element("configuration");
                root.addContent("test xpath!");
                Document doc = new Document(root);
                
                System.out.print("Create an Xpath expression...");
                XPath xpa = XPath.newInstance("configuration");
                System.out.println("DONE");

                System.out.print("Execute XPath now...");
                @SuppressWarnings("unused")
                List<Element> list = (List<Element>)xpa.selectNodes(doc.getRootElement());
                System.out.println("DONE");

            }
        catch (Exception e)
        {
            System.out.println("ERROR: " + e.getMessage());
        }
        
    }
}
