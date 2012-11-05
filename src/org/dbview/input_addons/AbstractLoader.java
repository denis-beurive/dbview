/*
	DbView - Graph Visualization
    Copyright (C) 2012  Denis BEURIVE

    This program is free software: you can redistribute it and/or modify
    it under the terms of the GNU General Public License as published by
    the Free Software Foundation, either version 3 of the License, or
    (at your option) any later version.

    This program is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU General Public License for more details.

    You should have received a copy of the GNU General Public License
    along with this program.  If not, see <http://www.gnu.org/licenses/>.
*/

/**
 * @author Denis Beurive
 */

package org.dbview.input_addons;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.util.*;
import org.jdom.*;
import org.dbview.db.structure.*;
import org.dbview.resources.*;

/**
 * <p>This class implements the database loader adaptor.</p>
 * <p>Please note that the exploration of the database relies on JDBC type 4.</p>
 * <p>Child classes may override the methods, if a specific database needs specific procedures.</p>
 * @author Denis Beurive
 */
public abstract class AbstractLoader
{
    /**
     * Handler to the database's connection.
     */
	private Connection __connection = null;

	/**
	 * This attribute contains the "meta data" for the current database.
	 * @see JDBC for details.
	 */
	private DatabaseMetaData __db_meta = null;

	/**
	 * This method returns an instance of a soft foreign key detector (if it is defined in the profile).
	 * @param in_profile Profile used to open a connexion to the database through the input add-on.
	 * @return The method returns an instance of a soft foreign key detector, or null if no detector is defined.
	 */
	protected abstract AbstractSotfForeignKeyDetector _getSoftForeignKeyDetector_(Element in_profile) throws Exception;

	/**
	 * This method returns the URL used to open a connection to the database.
	 * @param in_data XML element that contains the data required to open the connection.
	 * @return The method returns the URL used to open a connection to the database.
	 * @throws Exception
	 */
	protected abstract String _getUrl_(Element in_data) throws Exception;

	/**
     * This method returns the list of all tables in the database.
     * @param in_connection Connection to the database.
     * @param in_db_meta Meta data associated to the database.
     * @return The method returns the list of all tables in the database.
     * @throws Exception
     */
	protected ArrayList<String> _tables(Connection in_connection, DatabaseMetaData in_db_meta) throws Exception
	{
	    ArrayList<String> tables  = new ArrayList<String>();
	    ResultSet tbl = in_db_meta.getTables(in_connection.getCatalog(), "%", "%", null);
	    while (tbl.next()) { tables.add(tbl.getString("TABLE_NAME")); }
	    return tables;
	}

	/**
     * This method returns the list of all indexes for a given table.
     * It also set the table's list of indexes.
     * @param in_connection Connection to the database.
     * @param in_db_meta Meta data associated to the database.
     * @param in_table The table.
     * @return The method returns the list of all indexes for the given table.
     * @throws Exception
     */
	protected Hashtable<String, DbIndex> _indexes(Connection in_connection, DatabaseMetaData in_db_meta, Table in_table) throws Exception
	{
	    String table_name = in_table.getName();
	    Hashtable<String, DbIndex> indexes = new Hashtable<String, DbIndex>();
	    
	    ResultSet idxs = in_db_meta.getIndexInfo(in_connection.getCatalog(), "%", table_name, false, false);
        while (idxs.next())
        {
            String  field_name = idxs.getString("COLUMN_NAME");
            Boolean unique     = idxs.getBoolean("NON_UNIQUE") ? Boolean.FALSE : Boolean.TRUE;
            String  index_name = idxs.getString("INDEX_NAME");
            
            // System.out.println(">>>>> " + index_name + " " + field_name);
            
            // Is the index already listed?
            if (indexes.containsKey(index_name))
            {
                // The index is already listed. We just add a new field to it.
                DbIndex index = indexes.get(index_name);
                index.addField(in_table.getField(field_name));
            }
            else
            {
                // The index is not already listed.
                DbIndex index = new DbIndex(table_name, unique, index_name);
                index.addField(in_table.getField(field_name));
                indexes.put(index_name, index);
            }
        }
        in_table.setIndexes(indexes);
	    return indexes;
	}

   /**
    * This method returns the primary key for a given table.
    * @param in_connection Connection to the database.
    * @param in_db_meta Meta data associated to the database.
    * @param in_table The table.
    * @return <ul>
    *           <li>The method returns the name of the primary key, if the table defines a primary key.</li>
    *           <li>If the method does not define any primary key, then the method returns the value null.</li>
    *         </ul>
    * @throws Exception
    */
	protected ArrayList<String> _primary(Connection in_connection, DatabaseMetaData in_db_meta, Table in_table) throws Exception
	{
	    ArrayList<String> fields = new ArrayList<String>();
	    ResultSet pks = in_db_meta.getPrimaryKeys(in_connection.getCatalog(), "%", in_table.getName());
	    while (pks.next()) { fields.add(pks.getString("COLUMN_NAME")); }
	    return fields;
	}

	/**
     * This method returns the list of all foreign keys for a given table.
     * @param in_connection Connection to the database.
     * @param in_db_meta Meta data associated to the database.
     * @param in_table The table.
     * @return The method returns the list of all foreign keys for the given table.
     *         Each element of the returned list is an array that contains 2 elements.
     *         <ul>
     *              <li>First element: The name of the foreign key, within the given table.</li>
     *              <li>Second element: The name of the field, within the target table.</li>
     *         </ul>
     * @throws Exception
     */
	protected ArrayList<DbForeignKey> _foreign(Connection in_connection, DatabaseMetaData in_db_meta, Table in_table) throws Exception
	{
	    String table_name = in_table.getName();
	    ArrayList<DbForeignKey> foreign_keys  = new ArrayList<DbForeignKey>();
	    ResultSet fks = in_db_meta.getImportedKeys(in_connection.getCatalog(), "%", table_name);

        while (fks.next())
        {
            String field_name        = fks.getString("FKCOLUMN_NAME");
            String target_field_name = fks.getString("PKCOLUMN_NAME");
            String target_table_name = fks.getString("PKTABLE_NAME");
            foreign_keys.add(new DbForeignKey(table_name, field_name, target_table_name, target_field_name));
        }

	    return foreign_keys;
	}

	/**
	 * This method returns the list of all fields for a given table.
	 * @param in_connection Connection to the database.
	 * @param in_db_meta Meta data associated to the database.
	 * @param in_table The table.
	 * @return The method returns the list of all fields for the given table.
	 * @throws Exception
	 */
	protected ArrayList<String> _fields(Connection in_connection, DatabaseMetaData in_db_meta, Table in_table) throws Exception
	{
	    ArrayList<String> fields = new ArrayList<String>();
	    ResultSet keys = in_db_meta.getColumns(in_connection.getCatalog(), "%", in_table.getName(), "%");
	    while (keys.next()) { fields.add(keys.getString("COLUMN_NAME")); }
	    return fields;
	}

	/**
	 * This method returns the list of fields that can be null for a given table.
     * @param in_connection Connection to the database.
     * @param in_db_meta Meta data associated to the database.
     * @param in_table The table.
	 * @return Thie method returns the list of fields that can be null.
	 * @throws Exception
	 */
	protected ArrayList<String> _null(Connection in_connection,DatabaseMetaData in_db_meta, Table in_table) throws Exception
	{
	    ArrayList<String> fields = new ArrayList<String>();
	    ResultSet keys = in_db_meta.getColumns(in_connection.getCatalog(), "%", in_table.getName(), "%");
	    while (keys.next())
	    {
	        if (0 == keys.getString("IS_NULLABLE").compareTo("YES"))
	        { fields.add(keys.getString("COLUMN_NAME")); }
	    }
	    return fields;
	}

	/**
	 * This method loads information from a given database and returns a model of the database.
	 * @param in_profile Profile associated to the database (<code>&lt;data&gt;...&lt;/data&gt;</code>).
	 * @return The method returns a model of the database.
	 * @throws Exception
	 */
	public Database load(Element in_profile) throws Exception
	{
		ArrayList<String> tables = null;
		Database database        = null;
		String URL               = null;

		// Get the URL used to open the connection to the database.
		URL = this._getUrl_(in_profile);
		// System.out.println(URL);
		
		// Connect to the database.
		this.__connection = DriverManager.getConnection(URL);

		// Get meta data.
		// The metadata includes information about the database's tables, its supported SQL grammar, its stored procedures, the capabilities of this connection, and so on. 
		this.__db_meta = this.__connection.getMetaData();

		// Create the database data structure.
		database = new Database(this.__connection.getCatalog());

		// Get the names off all tables.
		tables = this._tables(this.__connection, this.__db_meta);

		// For each table, get the list of primary keys and indexes.
		for (String table_name: tables)
		{
			// Create the table, within the database.
			Table table = new Table(database, table_name);

			// Add all fields in the table.
			for (String field_name: this._fields(this.__connection, this.__db_meta, table))
			{
			    @SuppressWarnings("unused")
			    Field field = new Field(table, field_name);
			}

			// Set the primary keys, if exists.
			ArrayList<String> primaries = this._primary(this.__connection, this.__db_meta, table);
			if (null != primaries)
			{
			    for (String primary: primaries)
			    { table.getField(primary).isAPrimaryKey(); }
			}

			// Set all fields' properties that must be set.
			// Indexes set : Indexes that are composed by one and only one field.
			this._indexes(this.__connection, this.__db_meta, table);
			Hashtable<String, DbIndex> unique_indexes = table.getSimpleIndexes();
			
			for (Enumeration<String> e = unique_indexes.keys(); e.hasMoreElements(); )
			{
			    String index_name = e.nextElement();
			    DbIndex index = unique_indexes.get(index_name);
			    String field_name = index.getField(0).getName();
                if (index.unique) { table.getField(field_name).isAUniqueIndex(); }
                else { table.getField(field_name).isAMultipleIndex(); }
			}
		}
		
		// For each table, get the list of hard foreign keys, and the list of null fields.
		for (String table_name: tables)
		{
		    Table table = database.getTable(table_name);
		    
	        // Set all foreign keys.
            for (DbForeignKey key: this._foreign(this.__connection, this.__db_meta, table))
            {
                // Get the "reference" table.
                // If the reference table is not listed, we create a dead join.
                Table reference_table = database.getTable(key.reference_table_name);
                
                if (null == reference_table)
                {
                    // This is a dead foreign key.
                    table.getField(key.field_name).isADeadforeignKey(key.reference_table_name, key.reference_field_name);
                    // System.out.println("Found a DFK: " + key.getFullName() + " => " +  key.reference_table_name);
                    continue;
                    // A foreign key may be in a distinct database.
                    // throw new Exception("Unexpected error : the database seems to be invalid. Found a foreign key (\"" + key.field_name + "\") witch reference's table (\"" + key.reference_table_name + "\") does not exist!");
                }
                
                // Get the "reference" field.
                // If the reference field is not listed, we create a dead join.
                Field reference_field = reference_table.getField(key.reference_field_name);
                if (null == reference_field)
                { 
                    // This is a dead foreign key.
                    table.getField(key.field_name).isADeadforeignKey(key.reference_table_name, key.reference_field_name);
                    // System.out.println("Found a DFK: " + key.getFullName() + " => " +  key.reference_table_name);
                    continue;
                    // A foreign key may be in a distinct database.
                    // throw new Exception("Unexpected error : the database seems to be invalid. Found a foreign key (\"" + key.field_name + "\") witch reference's field (\"" + key.reference_table_name + "." + key.reference_field_name + "\") does not exist!");
                }

                // Debug reès utile:
                // Field fk = table.getField(key.field_name);
                // System.out.println("   (FK) " +  key.getFullName() + " -> " + target_field.getFullName());
                // System.out.println("      table.getField(" + key.getFullName() + ").isAforeignKey(" + target_field.getFullName() + ")");;
                // System.out.println("      Is the reference key " + target_field.getFullName() + " a primary key? " + target_field.isPrimaryKey().toString());
                // System.out.println("      Is the foreign key " + fk.getFullName() + " a primary key? " + fk.isPrimaryKey().toString());
                // System.out.println("      Is the foreign key " + fk.getFullName() + " (already) a foreign key? " + fk.isForeignKey().toString());
                
                // System.out.println(table_name + "." + key.field_name + " is a primary key");
                // System.out.println("\t     > " + key.field_name);
                // System.out.println("\t     > " + table.getField(key.field_name));
                table.getField(key.field_name).isAforeignKey(reference_field);
                table.getField(key.field_name).isAHardForeignKey();
            }

            // System.out.println("FK setted");
            
            // Set all "nullable" fields.
            for (String nullable_field: this._null(this.__connection, this.__db_meta, table))
            {
                // System.out.println(nullable_field);
                Field field = table.getField(nullable_field);
                if (null == field) { throw new Exception("Unexpected error : the database seems to be invalid. Can not find field (\"" + table_name + "." + nullable_field + "\") ?"); }
                field.isANull();
            }
		}
		
		// Find for soft foreign keys.
		AbstractSotfForeignKeyDetector fkDetector = this._getSoftForeignKeyDetector_(in_profile);

		if (null != fkDetector)
		{
		    for (String table_name: tables)
		    {
		        Table table = database.getTable(table_name);
		        for (Field field: table.getFields())
		        {
		            String field_name = field.getName();
		            if (! fkDetector.isFk(field_name)) { continue; }

		            // A hard foreign key may look like a soft one!
		            if (field.isForeignKey()) { continue; }

		            String target_table_name = fkDetector.referenceTable(field_name);
		            String target_field_name = fkDetector.referenceField(field_name);

	                // Get the target table.
	                Table target_table = database.getTable(target_table_name);
	                if (null == target_table) { throw new Exception("Unexpected error : the database seems to be invalid. Found a SOFT foreign key (\"" + field_name + "\") witch target's table (\"" + target_table_name + "\") does not exist!"); }

	                // Get the target field.
	                Field target_field = target_table.getField(target_field_name);
	                if (null == target_field) { throw new Exception("Unexpected error : the database seems to be invalid. Found a SOFT foreign key (\"" + field_name + "\") witch target's field (\"" + target_table_name + "." + target_field_name + "\") does not exist!"); }

	                // Set the field a soft foreign key.
	                field.isAforeignKey(target_field);
	                field.isASoftForeignKey();
		        }
		    }
		}

		// Now, generate the list of relations.
		database.generateRelations();


		return database;
	}
}
