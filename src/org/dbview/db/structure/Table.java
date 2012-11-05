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

package org.dbview.db.structure;

import org.dbview.db.structure.Database;
import org.dbview.db.structure.Field;
import org.dbview.utils.Strings;
import org.dbview.input_addons.DbIndex;

import java.util.Enumeration;
import java.util.Hashtable;
import java.util.ArrayList;
import java.util.Iterator;

/**
 * This class represents a table.
 * @author Denis Beurive
 * @remark Adding a field to a table is done when a field is created.
 */
public class Table
{
    /**
     * The hash table lists all table's fields.
     * <ul>
     *      <li>Key: Name of the field.</li>
     *      <li>Value: The field.</li>
     * </ul>
     */
    private Hashtable<String, Field> __fields = new Hashtable<String, Field>();

    /**
     * The array contains the list of all fields listed in the order of declaration.
     */
    private ArrayList<String> __orderedFields = new ArrayList<String>();

    /** Name of the table. */
    private String __name = null;

    /** Database that contains this table. */
    private Database __db = null;
    
    /** List of all indexes for this table. */
    private Hashtable<String, DbIndex> __indexes = new Hashtable<String, DbIndex>();
    
    /** List of all indexes that are composed of one field. */
    private Hashtable<String, DbIndex> __simple_indexes = new Hashtable<String, DbIndex>();
    
    /** List of all indexes that are composed of more than one field. */
    private Hashtable<String, DbIndex> __composite_indexes = new Hashtable<String, DbIndex>();

    /** Number of indexes that are composed of more than one field. */
    private Integer __simple_indexes_count = -1;
    
    /** Number of indexes that are composed of one field. */
    private Integer __composite_indexes_count = -1;
    
    /** 
     * Set the list of indexes.
     * @param in_list The list of indexes to set.
     */
    public void setIndexes(Hashtable<String, DbIndex> in_list)
    {
        this.__indexes = in_list;
    }
    
    /**
     * This method returns the list of indexes that are defined by only one field.
     * @return The method returns the list of indexes that are defined by only one field.
     */
    public Hashtable<String, DbIndex> getSimpleIndexes()
    {
        if (-1 != this.__simple_indexes_count) { return this.__simple_indexes; }
        
        this.__simple_indexes_count = 0;
        for (Enumeration<String> e = this.__indexes.keys(); e.hasMoreElements(); )
        {
            String  index_name = e.nextElement();
            DbIndex index      = this.__indexes.get(index_name);
            if (index.fieldCount() > 1) { continue; }
            this.__simple_indexes.put(index_name, index);
            this.__simple_indexes_count++;
        }
        
        return this.__simple_indexes;
    }
    
    /**
     * This method returns the list of indexes that are defined by more than one field.
     * @return The method returns the list of indexes that are defined by more than one field.
     */
    public Hashtable<String, DbIndex> getCompositeIndexes()
    {
        if (-1 != this.__composite_indexes_count) { return this.__composite_indexes; }
        
        this.__composite_indexes_count = 0;
        for (Enumeration<String> e = this.__indexes.keys(); e.hasMoreElements(); )
        {
            String  index_name = e.nextElement();
            DbIndex index      = this.__indexes.get(index_name);
            if (1 == index.fieldCount()) { continue; }
            this.__composite_indexes.put(index_name, index);
            this.__composite_indexes_count++;
        }
        
        return this.__composite_indexes;
    }
    
    /**
     * This method returns the number of indexes composed by only one index.
     * @return The method returns the number of indexes composed by only one index.
     */
    public Integer compositeIndexCount()
    {
        if (-1 == this.__composite_indexes_count) { this.getCompositeIndexes(); }
        return this.__composite_indexes_count;
    }

    /**
     * This method returns the number of indexes composed by more than one index.
     * @return The method returns the number of indexes composed by more than one index.
     */
    public Integer simpleIndexCount()
    {
        if (-1 == this.__simple_indexes_count) { this.getSimpleIndexes(); }
        return this.__simple_indexes_count;
    }
    
    /**
     * This method returns the name of the table.
     * @return The method returns the name of the table.
     */
    public String getName()
    {
        return this.__name;
    }

    /**
     * Create a table.
     * @param in_db Database that contains the newly created table.
     * @param in_name Name of the table.
     * @throws org.dbview.db.structure.DatabaseException
     */
    public Table(Database in_db, String in_name) throws org.dbview.db.structure.DatabaseException
    {
        this.__name = in_name;
        this.__db   = in_db;
        in_db.addTable(this);
    }

    /**
     * Add a field to the table.
     * @param in_field Field to add.
     * @throws org.dbview.db.structure.TableException
     * @remark This method is accessible for package's members only.
     *         Adding a field to a table is done when a field is created.
     */
    void addField(Field in_field) throws org.dbview.db.structure.TableException
    {

    	String field_name = in_field.getName();
        if (null != this.__fields.get(field_name))
        { throw new org.dbview.db.structure.TableException("Table " + this.__name + ": Field " + field_name + " already added!"); }
        this.__fields.put(in_field.getName(), in_field);
        __orderedFields.add(field_name);
    }

    /**
     * Returns a field.
     * @param in_name Name of the field to return
     * @return If the field, referenced by its name, exists, then the method returns it.
     *         Otherwise, the method returns the value null.
     */
    public Field getField(String in_name)
    {
        return this.__fields.get(in_name);
    }

    /**
     * Return the list of all fields in the table.
     * The returned list is ordered. The order is the order of the table's definition.
     * @return The method returns the list of all fields in the table.
     */
    public ArrayList<Field> getFields()
    {
    	ArrayList<Field> result = new ArrayList<Field>();
    	Iterator<String> i = this.__orderedFields.iterator();
    	while (i.hasNext()) { result.add(this.__fields.get(i.next())); }
    	return result;
    }

    /**
     * Return the database that contains this table.
     * @return The method returns the database that contains this table.
     */
    public Database getDatabase()
    {
        return this.__db;
    }

    /**
     * Return the name of the database that contains this table.
     * @return The method returns the name of the database that contains this table.
     */
    public String getDatabaseName()
    {
        return this.__db.getName();
    }

    /**
     * Return the list of foreign keys.
     * @return The method returns the list of foreign keys.
     */
    public ArrayList<Field> getForeignKeys()
    {
        ArrayList<Field> fks = new ArrayList<Field>();
        for (Field field: this.getFields()) { if (field.isForeignKey()) { fks.add(field); } }
        return fks;
    }

    /**
     * Produce a textual representation of the table.
     * @return The method returns a string that represents a textual representation of the table.
     */
    public String toString()
    {
        ArrayList<String> result = new ArrayList<String>();
        ArrayList<Field>  fields = this.getFields();

        result.add(this.getName());
        Iterator<Field> i = fields.iterator();
        while (i.hasNext())
        {
            Field f = i.next();
            result.add("\t" + f.toString());
        }

        return Strings.joinWithNewLines(result);
    }
}
