package tests;

import org.junit.Before;
import org.junit.Test;
import org.dbview.resources.*;

public class TestFkCatalog
{
    @Before
    public void setUp()
    {
        System.out.println("");
        System.out.println("------------------------------------------------------------");
        System.out.println("Testing FkCatalog.");
        System.out.println("------------------------------------------------------------");
        System.out.println("");
    }
    
    @Test public void tsst1()
    {
        try
        {
            System.out.println("Create the catalog...");
            SotfForeignKeyDetectorCatalog catalog = new SotfForeignKeyDetectorCatalog();
            System.out.println("OK");
            
            System.out.println("Print the catalog");
            System.out.println(catalog);
            
            System.out.println("Create a soft foreign key detector... that exists.");
            AbstractSotfForeignKeyDetector fk1 = catalog.getFkMatcherByCliName("TargetTableNameUsId");
            System.out.println(fk1.description());
            
            System.out.println("Create a soft foreign key detector... that does *NOT* exist (throw an exception).");
            AbstractSotfForeignKeyDetector fk2 = catalog.getFkMatcherByCliName("pattern1");
            System.out.println(fk2.description());
        }
        catch (Exception e)
        {
            System.out.println(e.getMessage());
        }
    }
}
