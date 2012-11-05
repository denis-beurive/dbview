package tests;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.Iterator;

import org.junit.Before;
import org.junit.Test;
import org.dbview.db.structure.*;

public class TestStructure
{
    Database database = null;
    Table table = null;
    Table targetTable = null;
    
    @Before public void setUp()
    {
        System.out.println("");
        System.out.println("------------------------------------------------------------");
        System.out.println("Testing Database structure.");
        System.out.println("------------------------------------------------------------");
        System.out.println("");
        
        try {
                this.database       = new Database("myDb");
                this.table          = new Table(this.database, "myTable");
                this.targetTable    = new Table(this.database,"targetTable");
                
                // May throw exception.
                Field pk = new Field(this.table, "pk");
                Field ui = new Field(this.table, "ui");
                Field mi = new Field(this.table, "mi");
                Field fk = new Field(this.table, "fk");
                @SuppressWarnings("unused")
                Field st = new Field(this.table, "st");
    
                // Don't throw exception.
                pk.isAPrimaryKey();
                ui.isAUniqueIndex();
                mi.isAMultipleIndex();
                fk.isAforeignKey(new Field(this.targetTable, "targetField"));
                fk.isAUniqueIndex();
            }
        catch (org.dbview.db.structure.TableException e)
        {
            System.out.println("ERROR: " + e.getMessage());
            assertTrue(1==0);
        }
        catch (org.dbview.db.structure.DatabaseException e)
        {
            System.out.println("ERROR: " + e.getMessage());
            assertTrue(1==0);            
        }
        catch (org.dbview.db.structure.FieldException e)
        {
            System.out.println("ERROR: " + e.getMessage());
            assertTrue(1==0);            
        }
    }
    
    @Test public void testDatabase()
    {
        System.out.println("This is the table:");
        System.out.println(this.table.toString());
        Field f = this.table.getField("pk");
        System.out.println("Primary key is: " + f.getName());
        System.out.println("Printing the database:");
        System.out.println(this.database.toString());
        
        
        assertTrue(this.table.getField("pk").isPrimaryKey());
        assertTrue(this.table.getField("fk").isForeignKey());
        assertTrue(this.table.getField("ui").isUniqueIndex());
        assertTrue(this.table.getField("mi").isMultipleIndex());
        
        assertTrue(this.table.getField("ui").isIndex());
        assertTrue(this.table.getField("mi").isIndex());
        assertTrue(this.table.getField("pk").isKey());
        assertTrue(this.table.getField("fk").isKey());
        
        assertFalse(this.table.getField("st").isKey());
        assertFalse(this.table.getField("st").isIndex());
        assertFalse(this.table.getField("st").isPrimaryKey());
        assertFalse(this.table.getField("st").isForeignKey());
        
        assertTrue(this.table.getField("pk").getName().compareTo("pk") == 0);
        assertTrue(this.table.getField("pk").getTableName().compareTo("myTable") == 0);
        
        assertTrue(this.targetTable.getField("targetField").getFkToMe().getFullName().compareTo("myTable.fk") == 0);
        
        assertTrue(this.table.getDatabaseName().compareTo("myDb") == 0);
        assertTrue(this.targetTable.getDatabaseName().compareTo("myDb") == 0);
        
        try {
                assertTrue(this.table.getField("fk").getReference().getName().compareTo("targetField") == 0);
                assertTrue(this.table.getField("fk").getReference().getTableName().compareTo("targetTable") == 0);
                
                System.out.println(">>> Printing");
                ArrayList<Field> list = this.table.getFields();
                Iterator<Field> i = list.iterator();
                while (i.hasNext())
                {
                    Field fd = i.next();
                    System.out.println("\t" + fd.toString());
                }                
            }
        catch (Exception e)
        {
            System.out.println("ERROR: " + e.getMessage());
            assertTrue(1==0);
        }
        
    }
}
