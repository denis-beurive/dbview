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

package org.dbview.addons.output.table.dotMedium;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Iterator;
import org.dbview.addons.output.table.utils.dot.*;
import org.dbview.db.structure.Database;
import org.dbview.db.structure.Table;
import org.dbview.output_addons.AbstractExporter;
import org.jdom.Element;

/**
 * @class Exporter
 * This class implements the adapter that generates the DOT specification file for the add-on that produces medium DOT (GraphViz) representations.
 * @author Denis BEURIVE
 */
public class Exporter extends AbstractExporter
{
    /**
     * This method produces a detailed DOT representation of the database.
     * @param in_db Database to export.
     * @param in_cli_conf Configuration parameters for the exporter.
     * @param in_tables List of tables to export.
     *        You may specify all the tables of the database, or a subset of tables.
     * @return The exporter returns a string. This string is the DOT's representation of the given set of tables.
     * @throws Exception
     */
    public Object export(Database in_db, Element in_cli_conf, ArrayList<Table> in_tables) throws Exception
    {
        // Extract options.
        String layout = org.dbview.utils.Jdom.getTextOrCdata(in_cli_conf.getChild(XML.LAYOUT)); // "TB" or "LR"
        String path   = org.dbview.utils.Jdom.getTextOrCdata(in_cli_conf.getChild(XML.PATH));

        // Get the list of table to zoom at.
        org.dbview.databaseExporters.dot.DotMedium e = new org.dbview.databaseExporters.dot.DotMedium(in_tables, in_db);
        Hashtable<String, Object> options = new Hashtable<String, Object>();
        options.put("layout", layout);
        String dot = e.export(options); // This is a string.

        if (path.length() > 0)
        {
            FileWriter fstream = new FileWriter(path, Boolean.TRUE);
            BufferedWriter out = new BufferedWriter(fstream);
            out.write(dot);
            out.write(System.getProperty("line.separator"));
            out.close();
        }
        else
        {
            System.out.println(dot);
        }

        return null;
    }
}
