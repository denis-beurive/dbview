package tests;

import org.junit.Before;
import org.junit.Test;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.DriverManager;
import java.sql.SQLException;

public class TestJdbc
{
    @Before
    public void setUp()
    {
        System.out.println("");
        System.out.println("------------------------------------------------------------");
        System.out.println("Testing JDBC.");
        System.out.println("------------------------------------------------------------");
        System.out.println("");
    }
    
    @Test public void testMe()
    {
        try
        {
            // WARNING !!!!!!
            // The MySql installed with MAMP listens to a non standard MySql TCP port.
            
        		Connection conn = null;
    	
            // Class.forName("com.mysql.jdbc.Driver");
            try
            {
            		// MAMP (Mac OS)
            		System.out.println("Try connecting using MAMP default condfiguration.");
            		conn = DriverManager.getConnection("jdbc:mysql://localhost:8889/mydb?user=root&password=root");
            		System.out.println("MAMP default: OK");
            	}
            catch (SQLException e)
            {
                	// WAMP (Windows)
                	System.out.println("Try connecting using WAMP default condfiguration.");
                	conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/mydb?user=root");
                	System.out.println("WAMP default: OK");
            }
            
            System.out.println("OK");
            try
            {
                // MAMP (Mac OS)
                System.out.println("Try getting meta data");
                DatabaseMetaData dbm = conn.getMetaData();
                
                // A "catalog" is a "database".
                System.out.println("Databases:");
                ResultSet catalogs = dbm.getCatalogs(); 
                while (catalogs.next())
                {
                    System.out.println("\t> " + catalogs.getString(1));
                }
                
                
                
                System.out.println("Table for database \"mydb\":");
                ResultSet tables = dbm.getTables("mydb", "%", "%", null);
                
                System.out.println("Column count: " + tables.getMetaData().getColumnCount());
                System.out.println("List of columns:");
                for (int i=1; i<=tables.getMetaData().getColumnCount(); i++)
                {
                    System.out.println("\t> " + tables.getMetaData().getColumnName(i));
                }
                
                System.out.println("List of tables:");
                while (tables.next())
                {
                    String table_name = tables.getString(3);
                    System.out.println("\t> " + table_name);
                    
                    // Get the primary keys.
                    ResultSet pks = dbm.getPrimaryKeys("mydb", "%", table_name);
                    while (pks.next())
                    {
                        System.out.println("\t\tPK:> " + pks.getString(4));
                    }
                    
                    // Get all Columns.
                    ResultSet keys = dbm.getColumns("mydb", "%", table_name, "%");
                    while (keys.next())
                    {
                        System.out.println("\t\tKeys:> " + keys.getString(4) + " NULL? " + keys.getString(18));
                    }
                    
                    ResultSet fks = dbm.getImportedKeys("mydb", "%", table_name);
                    while (fks.next())
                    {
                        System.out.println("\t\tFK:> " + fks.getString(7) + "." + fks.getString(8) + " => " + fks.getString(3) + "." + fks.getString(4));
                    }
                    
                    // Get indexes
                    ResultSet idxs = dbm.getIndexInfo("mydb", "%", table_name, false, false);
                    while (idxs.next())
                    {
                        System.out.println("\t\tIndex:> " + idxs.getString(9) + " [" + idxs.getString("INDEX_NAME") + "] " + " Non unique? " + (idxs.getBoolean("NON_UNIQUE") ? "YES" : "NO"));
                    }
                    
                }
                
                
                
                
                catalogs.close();
                System.out.println("OK");
            }
            catch (SQLException e)
            {
                System.out.println("ERROR:" + e.getMessage());
            }          
            
        }
    catch(SQLException e)
    {
        System.out.println("SQLException: " + e.getMessage());
        System.out.println("SQLState: "     + e.getSQLState());
        System.out.println("VendorError: "  + e.getErrorCode());
    }
    catch(Exception x)
    {
        System.out.println("Can not load JDBC class!");
    }
    }
    
}
