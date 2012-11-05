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

import java.util.ArrayList;
import java.util.Hashtable;
import org.dbview.db.structure.Database;
import org.dbview.db.structure.Table;
import org.dbview.utils.dot.*;


/**
 * This class implements the export to DOT, with a low level of details.
 * @see AbstractDatabaseExporter
 * @author Denis Beurive
 */
public class DotLight extends AbstractDatabaseExporter
{
    /**
     * Create the exporter.
     * @param in_tables List of tables to export.
     * @param in_db Handle to the database.
     */
    public DotLight(ArrayList<Table> in_tables, Database in_db)
    {
        super(in_tables, in_db);
    }

    /**
     * This method exports the given tables, from the given database.
     * @param in_options Options.
     *        Options may be:
     *        <ul>
     *              <li>"layout"</li>
     *        </ul>
     * @return The method returns a string that represents the DOT representation.
     * @throws Exception
     */
    public String export(Hashtable<String, Object> in_options) throws Exception
    {
        DiGraph digraph = new DiGraph();
        if (null != in_options)
        {
            if (in_options.containsKey("layout"))
            digraph.setRankdir((String)in_options.get("layout"));
        }

        // ------------------------------------------------------------------
        // Configure the directed graph.
        // ------------------------------------------------------------------

        digraph.setNodesep(new Float(0.5));
        digraph.setRankset(new Float(0.7));
        digraph.setCompound(Boolean.TRUE);

        // ------------------------------------------------------------------
        // Add the table.
        // ------------------------------------------------------------------

        for (Table table : this._tables)
        {
            Node table_node = new Node(table.getName());
            table_node.setShape("record");
            table_node.setStyle("bold");
            table_node.setLabel(table.getName());
            digraph.addNode(table_node);
        }

        // ------------------------------------------------------------------
        // Add the joins between tables.
        // ------------------------------------------------------------------

        for (Table reference_table : this._tables)
        {
            for (Table target_table : this._db.getReferenceTables(reference_table))
            {
                // WARNING: If we work on a sub list of the total list of tables (zoom level is activated), then some target's tables may not be printed!
                if (! this._tables.contains(target_table)) { continue; }

                String edge_color = this._relationColor(this._db.getTableToTableRelationType(reference_table, target_table));
                Edge join = new Edge();

                // Create the edge from the source table to the relation's node.
                join.setFrom(reference_table.getName());
                join.setTo(target_table.getName());
                join.setPenwidth("2");
                join.setColor(edge_color);

                digraph.addEdge(join);
            }
        }

        return digraph.toString();
    }
}
