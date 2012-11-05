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

import java.util.ArrayList;
import org.dbview.db.structure.*;

/**
 * This class represents an index.
 * @author Denis Beurive
 */
public class DbIndex
{
	/**
	 * Name of the table that contains the index.
	 */
    public String table_name = null;

    /**
     * Name of the index.
     */
    public String index_name = null;
    
    /**
     * List of fields that are part of the index.
     */
    public ArrayList<Field> fields = new ArrayList<Field>(); 

	/**
	 * This field indicates whether the index is unique or not.
	 */
	public Boolean unique = Boolean.FALSE;

	/**
	 * Create an index.
	 * @param in_table_name Name of the table.
	 * @param in_unique This argument indicates whether the index is unique or not.
	 *        <ul>
	 *             <li>TRUE: The index is unique.</li>
	 *             <li>FALSE: The index is not unique.</li>
	 *        </ul>
	 * @param in_index_name Name of the index.
	 */
	public DbIndex(String in_table_name, Boolean in_unique, String in_index_name)
	{
		this.table_name = in_table_name;
		this.index_name = in_index_name;
		this.unique     = in_unique;
	}

	/**
	 * This method tests whether the index is unique or not.
	 * @return If the index is unique, then the method returns the value TRUE.
	 *         Otherwise, it returns the value FALSE.
	 */
	public Boolean isUnique() { return this.unique; }
	
	/**
	 * This method returns the name of the index.
	 * @return This method returns the name of the index.
	 */
	public String getIndexName() { return this.index_name; }
	
	/**
	 * This method adds a field to the list of fields that compose the index.
	 * @param in_field Field to add.
	 */
	public void addField(Field in_field) { this.fields.add(in_field); }
	
	/**
	 * This method tests if a given field, represented by its name, is part of the index.
	 * @param in_field Field to add.
	 * @return 
	 */
	public Boolean containsField(Field in_field) { return this.fields.contains(in_field); }
	
	/**
	 * This method returns the number of fields that compose the index.
	 * @return The method returns the number of fields that compose the index.
	 */
	public Integer fieldCount() { return this.fields.size(); }

	/**
	 * This method returns a field given its position in the list of fields that composes the index.
	 * @param in_pos Position of the field to extract.
	 * @return The method returns the field at the given position.
	 * @throws Exception
	 */
	public Field getField(Integer in_pos) throws Exception
	{
	    if (in_pos >= this.fields.size()) { throw new Exception("You try to extract a field from an index at an invalid position (" + in_pos.toString()  + ")"); }
	    return this.fields.get(in_pos);
	}
}
