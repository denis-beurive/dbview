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

package org.dbview.db.structure;

/**
 * This class represents a dead foreign key.
 * @author Denis Beurive
 */

public class DeadForeignKey
{
    /**
     * Name of the table that holds the field.
     */
    private String __table = null;
    
    /**
     * Name of the field.
     */
    private String __field = null;
    
    /**
     * Create a dead foreign key.
     * @param in_table_name Name of the table.
     * @param in_field_name Name of the field.
     */
    public DeadForeignKey(String in_table_name, String in_field_name)
    {
        this.__table = in_table_name;
        this.__field = in_field_name;
    }
    
    /**
     * Return the name of the table.
     * @return The method returns the name of the table.
     */
    public String getTable() { return this.__table; }
    
    /**
     * Return the name of the field.
     * @return The method returns the name of the field.
     */
    public String getField() { return this.__field; }

    /**
     * Return the full name of the field.
     * @return The method returns the full name of the field.
     */
    public String getFullName() { return this.__table + "." + this.__field; }
}
