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
import org.dbview.addons.output.table.utils.dot.*;
import org.dbview.db.structure.Database;
import org.dbview.db.structure.Field;
import org.dbview.db.structure.Table;
import org.dbview.utils.dot.*;
import org.dbview.utils.dot.label.*;

/**
 * This class implements the export to DOT, with an intermediate level of details.
 * @see AbstractDatabaseExporter
 * @author Denis Beurive
 */
public class DotMedium extends AbstractDatabaseExporter
{
    /**
     * Create the exporter.
     * @param in_tables List of tables to export.
     * @param in_db Handle to the database.
     */
    public DotMedium(ArrayList<Table> in_tables, Database in_db)
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
            table_node.setLabel(this.__createMediumTableLabel(table, digraph.getRankdir()));

            SubGraph sub_graph = new SubGraph(table.getName());
            sub_graph.setStyle("filled");
            sub_graph.setBackgroundColor(Conf.TABLE_BG_COLOR);
            sub_graph.setNodeStyle("filled");
            sub_graph.setNodeColor(Conf.TABLE_GRID_COLOR);
            sub_graph.setLabel(table.getName());
            sub_graph.addNode(table_node);

            digraph.addSubGraph(sub_graph);
        }

        // ------------------------------------------------------------------
        // Add the joins between tables.
        // ------------------------------------------------------------------

        Integer looper = 0;
        for (Table dependant_table : this._tables)
        {
            for (Table reference_table : this._db.getReferenceTables(dependant_table))
            {
                // WARNING: If we work on a sub list of the total list of tables (zoom level is activated), then some target's tables may not be printed!
                if (! this._tables.contains(reference_table)) { continue; }

                String edge_color = this._relationColor(this._db.getTableToTableRelationType(dependant_table, reference_table));

                if (0 != dependant_table.getName().compareTo(reference_table.getName()))
                {
                    // Create the edge from the reference table to the target table.
                    Edge join = new Edge();
                    join.setFrom(dependant_table.getName());
                    join.setTo(reference_table.getName());
                    join.setPenwidth("2");
                    join.setColor(edge_color);
                    join.setLtail(SubGraph.generateName(dependant_table.getName()));
                    join.setLhead(SubGraph.generateName(reference_table.getName()));
                    digraph.addEdge(join);
                }
                else
                {
                    String middle_name = "l" + looper.toString();
                    looper += 1;

                    Node middle = new Node(middle_name);
                    middle.setShape("point");
                    digraph.addNode(middle);

                    Edge join1 = new Edge();
                    join1.setArrowhead("none");
                    join1.setFrom(dependant_table.getName());
                    join1.setTo(middle_name);
                    join1.setPenwidth("2");
                    join1.setColor(edge_color);
                    join1.setLtail(SubGraph.generateName(dependant_table.getName()));
                    digraph.addEdge(join1);

                    Edge join2 = new Edge();
                    join2.setFrom(middle_name);
                    join2.setTo(reference_table.getName());
                    join2.setPenwidth("2");
                    join2.setColor(edge_color);
                    join2.setLhead(SubGraph.generateName(reference_table.getName()));
                    digraph.addEdge(join2);
                }
            }
        }

        return digraph.toString();
    }

    /**
     * This method creates the &quot;medium&quot; label for a given table. The label is a
     * grid that contains 5 columns, and as many row as the number of fields in
     * the table.
     *
     * @param in_table
     *            The table.
     * @param in_rankdir
     *            Initial orientation of a record node. Values can be:
     *            <ul>
     *               <li>TB (Top to bottom)</li>
     *               <li>LR (Left to right)</li>
     *               <li>RL 'Right to left)</li>
     *            </ul>
     * <p>See GRAPHVIZ' documentation for "rankdir": http://www.graphviz.org/doc/info/shapes.html</p>
     * @return The method returns a string that represents the label.
     */
    private String __createMediumTableLabel(Table in_table, String in_rankdir)
    {
        GridByRow label = new GridByRow();
        label.setRankdir(in_rankdir);

        // Create rows: {name}.
        for (Field field : in_table.getFields())
        {
            ArrayList<String> line = new ArrayList<String>();

            // Add the name of the field.
            line.add(field.getName());

            // Add the line to the label.
            label.add(line);
        }

        return label.toString();
    }
}
