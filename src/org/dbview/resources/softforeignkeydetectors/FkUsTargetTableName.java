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

package org.dbview.resources.softforeignkeydetectors;

import org.dbview.resources.AbstractSotfForeignKeyDetector;
import java.util.*;

/**
 * This class implements a (soft) foreign key detector.
 * Foreign key looks like: fk_&lt;name of the target table&gt;
 * The target field is always "id".
 * @author Denis Beurive
 */
public class FkUsTargetTableName extends AbstractSotfForeignKeyDetector
{
    /**
     * This method tests whether a field, identified by its name, is a (soft) foreign key or not.
     * @param in_field_name Name of the field to test.
     * @return If the field is a (soft) foreign key, then the method returns the value TRUE.
     *         Otherwise, it returns the value FALSE.
     */
    public Boolean isFk(String in_field_name)
    {
        if (in_field_name.length() < 4) { return Boolean.FALSE; }
        if (in_field_name.startsWith("fk_")) { return Boolean.TRUE; }
        return Boolean.FALSE;
    }

    /**
     * If the field is a (soft) foreign key, then the method returns the name of the "reference" table.
     * @param in_field_name Name of the field.
     * @return If the field is a (soft) foreign key, then the method returns the name of the "reference" table.
     *         Otherwise, the method returns the value null.
     */
    public String referenceTable(String in_field_name)
    {
        if (! this.isFk(in_field_name)) { return null; }

        ArrayList<String> tokens = new ArrayList<String>(Arrays.asList(in_field_name.split("_")));
        tokens.remove(0);

        return org.dbview.utils.Strings.join((tokens), "_");
    }

    /**
     * If the field is a (soft) foreign key, then the method returns the name of the "reference" field.
     * @param in_field_name Name of the field.
     * @return If the field is a (soft) foreign key, then the method returns the name of the "reference" field.
     *         Otherwise, the method returns the value null.
     */
    public String referenceField(String in_field_name)
    {
        if (! this.isFk(in_field_name)) { return null; }
        return "id";
    }

    /**
     * This method returns a description of the matcher.
     * @return The method returns a description of the matcher.
     */
    public String description()
    {
        ArrayList<String> doc = new ArrayList<String>();
        doc.add("Pattern:      fk_{name of the target table}.");
        doc.add("Target field: Always \"id\".");
        doc.add("Example:      fk_product_category");
        	doc.add(	"              -> product_category.id");

        return org.dbview.utils.Strings.joinWithNewLines(doc);
    }
}
