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

package org.dbview.resources;

/**
 * This class defines the interface for a "soft foreign key" detector.
 * Please note that a soft foreign key is defined by the field's name only.
 * There is no constraint declared in the database.
 * This kind of foreign key is used with database systems that don't support foreign keys (ex: MyIsam).
 * @author Denis Beurive
 */
public abstract class AbstractSotfForeignKeyDetector
{
    /**
     * This method tests whether a field, identified by its name, is a soft foreign key or not.
     * @param in_field_name Name of the field to test.
     * @return If the field is a (soft) foreign key, then the method returns the value TRUE.
     *         Otherwise, it returns the value FALSE.
     */
    public abstract Boolean isFk(String in_field_name);

    /**
     * If the field is a soft foreign key, then the method returns the name of the "reference" table.
     * @param in_field_name Name of the field.
     * @return If the field is a (soft) foreign key, then the method returns the name of the "reference" table.
     *         Otherwise, the method returns the value null.
     */
    public abstract String referenceTable(String in_field_name);

    /**
     * If the field is a (soft) foreign key, then the method returns the name of the "reference" field (within the "reference" table).
     * @param in_field_name Name of the field.
     * @return If the field is a (soft) foreign key, then the method returns the name of the "reference" field.
     *         Otherwise, the method returns the value null.
     */
    public abstract String referenceField(String in_field_name);

    /**
     * This method returns a description of the detector.
     * @return The method returns a description of the detector.
     */
    public abstract String description();
}
