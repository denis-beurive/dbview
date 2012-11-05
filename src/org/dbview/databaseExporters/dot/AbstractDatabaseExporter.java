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

package org.dbview.databaseExporters.dot;

import java.util.*;
import org.dbview.db.structure.Database;
import org.dbview.db.structure.Table;

/**
 * This class defines the generic interface of a database exporter that relies on Grahviz.
 * A database exporter creates a representation of a given list of tables, within a given database.
 * @remark Please note that all DOT add-ons use this exporter.
 * @author Denis Beurive
 */
public abstract class AbstractDatabaseExporter
{
    /**
     * List of table to export. Please note that the property's value may be null.
     * If the property's value is null, then all tables of the database are exported.
     */
    protected ArrayList<Table> _tables = null;

    /**
     * Database to export.
     */
    protected Database _db = null;

    /**
     * Create the exporter.
     * @param in_tables List of tables to export. This parameter can be null.
     *        If the parameter's value is null, then all tables of the database are exported.
     * @param in_db Handle to the database.
     */
    public AbstractDatabaseExporter(ArrayList<Table> in_tables, Database in_db)
    {
        this._db = in_db;
        if (null == in_tables) { this._tables = in_db.getTables(); } 
        else { this._tables   = in_tables; }
    }

    /**
     * This method exports the given tables, from the given database.
     * @param in_options Options.
     * @return The method returns a string that represents the DOT representation.
     * @throws Exception
     */
    public abstract String export(Hashtable<String, Object> in_options) throws Exception;

    /**
     * This method returns the color to apply to a given edge, given the type of the table to table relation.
     * @param in_table_to_table_relation_type The type of the relation.
     * @return The method returns the color to apply to a given edge.
     * @throws Exception
     */
    protected String _relationColor(int in_table_to_table_relation_type) throws Exception
    {
        switch (in_table_to_table_relation_type)
        {
            case Database.HARD_LINK             : return org.dbview.addons.output.table.utils.dot.Conf.HARD_EDGE_COLOR;
            case Database.SOFT_LINK             : return org.dbview.addons.output.table.utils.dot.Conf.SOFT_EDGE_COLOR;
            case Database.SOFT_AND_HARD_LINK    : return org.dbview.addons.output.table.utils.dot.Conf.SOFT_AND_HARD_EDGE_COLOR;
        }
        throw new Exception("Unexpected table to table type: " + in_table_to_table_relation_type);
    }
}
