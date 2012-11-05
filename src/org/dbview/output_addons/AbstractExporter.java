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

package org.dbview.output_addons;

import java.util.ArrayList;
import org.jdom.Element;
import org.dbview.db.structure.*;

/**
 * This class represents the exporter adaptor for output add-ons.
 * @author Denis BEURIVE
 */
abstract public class AbstractExporter
{
    /**
     * This method generates a representation of the database, from the internal representation.
     * @param in_db Database to export.
     * @param in_cli_conf Configuration parameters for the exporter.
     * @param in_tables List of tables to export.
     * @return The exporter returns an object. The nature of the object can be anything.
     * @throws Exception
     */
	public abstract Object export(Database in_db, Element in_cli_conf, ArrayList<Table> in_tables) throws Exception;
}

