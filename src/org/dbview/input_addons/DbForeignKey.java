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

/**
 * <p>This class represents a foreign key.</p>
 * <p>Please note that there are two kinds of foreign key:</p>
 * <ul>
 *     <li>Hard foreign key: This kind of key is materialized by a constraint.</li>
 *     <li>Soft foreign key: This kind of key is only materialized by its name.</li>
 * </ul>
 * <p>A foreign key is defined by a &quot;reference field&quot; and a &quot;dependent field&quot;.</p>
 *
 * @author Denis Beurive
 */
public class DbForeignKey
{
    /**
     * Name of the "reference" table.
     */
	public String table_name = null;

	/**
	 * Name of the "reference" field, within the "reference" table.
	 */
	public String field_name = null;

	/**
	 * Name of the "reference" table.
	 */
	public String reference_table_name = null;

	/**
	 * Name of the "reference" field, within the "target" table.
	 */
	public String reference_field_name = null;

	/**
	 * Return the full name of the field.
	 * @return The méthod the full name of the field.
	 */
	public String getFullName() { return this.table_name + "." + this.field_name; }
	
	/**
	 * Create a foreign key.
	 * @param in_table_name Name of the "dependent" table.
	 * @param in_field_name Name of the "dependent" field, within the "reference" table.
	 * @param in_reference_table_name Name of the "reference" table.
	 * @param in_reference_field_name Name of the "reference" field, within the "reference" table.
	 */
	public DbForeignKey(String in_table_name, String in_field_name, String in_reference_table_name, String in_reference_field_name)
	{
		this.table_name        = in_table_name;
		this.field_name        = in_field_name;
		this.reference_field_name = in_reference_field_name;
		this.reference_table_name = in_reference_table_name;
	}
}
