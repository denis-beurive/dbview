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
import java.util.Enumeration;
import java.util.Hashtable;
import org.dbview.addons.output.table.utils.dot.*;
import org.dbview.db.structure.Database;
import org.dbview.db.structure.Field;
import org.dbview.db.structure.FieldToFieldJoin;
import org.dbview.db.structure.Table;
import org.dbview.input_addons.DbIndex;
import org.dbview.utils.dot.*;
import org.dbview.utils.dot.label.*;

/**
 * This class implements the export to DOT, with a high level of details.
 * @see AbstractDatabaseExporter
 * @author Denis Beurive
 */
public class DotFull extends AbstractDatabaseExporter
{
    /**
     * Create the exporter.
     * @param in_tables List of tables to export.
     * @param in_db Handle to the database.
     */
    public DotFull(ArrayList<Table> in_tables, Database in_db)
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
     * @return The method returns a string that represents the Graphviz representation.
     * @throws Exception
     */
    public String export(Hashtable<String, Object> in_options) throws Exception
    {
        DiGraph digraph = new DiGraph();
        String layout   = "TB"; 
        if (null != in_options) { if (in_options.containsKey("layout")) { layout = (String)in_options.get("layout"); } }
        digraph.setRankdir(layout);

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
            SubGraph sub_graph = new SubGraph(table.getName());
            sub_graph.setStyle("filled");
            sub_graph.setBackgroundColor(Conf.TABLE_BG_COLOR);
            sub_graph.setNodeStyle("filled");
            sub_graph.setNodeColor(Conf.TABLE_GRID_COLOR);
            sub_graph.setLabel(table.getName());
            
            // Noeud pour la table.
            Node table_node = new Node(table.getName());
            table_node.setShape("record");
            table_node.setStyle("bold");
            table_node.setLabel(this.__createFullTableLabel(table, digraph.getRankdir()));
            
            // Noeud pour les indexes.
            if (table.compositeIndexCount() > 0)
            {
                String index_table_name = table.getDatabase().getNonTableTag();
                Node index_node = new Node(index_table_name);
                index_node.setShape("record");
                index_node.setStyle("bold");
                index_node.setLabel(this.__createIndexLabel(table, digraph.getRankdir()));
                sub_graph.addNode(index_node);

                // For the vertical layout only, we add an invisible arrow.
                if (0 == layout.compareTo("TB"))
                {
                    Edge e = new Edge();
                    e.setInvisible();
                    e.setFrom(table.getName());
                    e.setTo(index_table_name);
                    sub_graph.addEdge(e);
                }
            }
            
            sub_graph.addNode(table_node);
            digraph.addSubGraph(sub_graph);
        }

        // ------------------------------------------------------------------
        // Add the joins between tables.
        // ------------------------------------------------------------------

        for (Table dependent_table : this._tables)
        {
            for (Table reference_table : this._db.getReferenceTables(dependent_table))
            {
                // WARNING: If we work on a sub list of the total list of tables (zoom level is activated), then some "reference" tables may not be printed!
                if (! this._tables.contains(reference_table)) { continue; }

                ArrayList<FieldToFieldJoin> joins = this._db.getRelationsFromReferenceToTarget(dependent_table, reference_table);

                String node_name = dependent_table.getName() + "2" + reference_table.getName();
                Node jn = new Node(node_name);
                Edge src2node = new Edge();
                Edge node2dst = new Edge();

                // Create the node associated to the relation.
                jn.setLabel(this.__createJoinLabel(joins, digraph.getRankdir()));
                jn.setShape("record");
                jn.setStyle("filled");
                jn.setColor(Conf.RELATION_FG_COLOR);
                jn.setFillColor(Conf.RELATION_BG_COLOR);

                String edge_color = this._relationColor(this._db.getTableToTableRelationType(dependent_table, reference_table));

                // Create the edge from the source table to the relation's
                // node.
                src2node.setFrom(dependent_table.getName());
                src2node.setTo(node_name);
                src2node.setPenwidth("2");
                src2node.setLtail(SubGraph.generateName(dependent_table.getName()));
                src2node.setColor(edge_color);
                src2node.setArrowhead("none");

                // Create the edge from the relation's node to the
                // destination table.
                node2dst.setFrom(node_name);
                node2dst.setTo(reference_table.getName());
                node2dst.setPenwidth("2");
                node2dst.setLhead(SubGraph.generateName(reference_table.getName()));
                node2dst.setColor(edge_color);

                // Add nodes and edge to the graph.
                digraph.addNode(jn);
                digraph.addEdge(src2node);
                digraph.addEdge(node2dst);
            }
        }

        return digraph.toString();
    }

    /**
     * This method creates the &quot;full&quot; label for a given table. The label is a
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
    private String __createFullTableLabel(Table in_table, String in_rankdir)
    {
        GridByRow label = new GridByRow();
        label.setRankdir(in_rankdir);

        // Create rows: {key, null, index, name, link}.
        for (Field field : in_table.getFields())
        {
            ArrayList<String> line = new ArrayList<String>();

            // Is the field a primary or a foreign key?
            // System.out.println("> " + field.toString());
            
            if (field.isPrimaryKey())
            { line.add("PRI"); }
            else
            {
                if (field.isForeignKey()) { line.add("FK"); }
                else
                { 
                    if (field.isDeadForeignKey())
                    {
                        try { line.add("DFK: " + field.getDeadForeignKey().getFullName()); }
                        catch (Exception e) { line.add("Interal error!"); }
                    }
                    else { line.add("-"); }
                }
            }
            // Can the field be NULL?
            if (field.isNull()) { line.add("NULL"); }
            else { line.add("-"); }

            // Is the field an index?
            if (field.isMultipleIndex())
            { line.add("MUL"); }
            else if (field.isUniqueIndex())
            { line.add("UNI"); }
            else { line.add("-"); }

            // Add the name of the field.
            line.add(field.getName());

            // Is the field a starting point for a link?
            if (field.isForeignKey())
            {
                if (field.isHardForeignKey())
                { line.add("H"); }
                else
                { line.add("S"); }
            }
            else
            { line.add("-"); }

            // Add the line to the label.
            label.add(line);
        }

        return label.toString();
    }

    /**
     * This method creates the &quot;index&quot; label for a given table.
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
    private String __createIndexLabel(Table in_table, String in_rankdir) throws Exception
    {
        GridByRow label = new GridByRow();
        label.setRankdir(in_rankdir);
        
        Hashtable<String, DbIndex> indexes = in_table.getCompositeIndexes();
        for (Enumeration<String> e = indexes.keys(); e.hasMoreElements(); )
        {
            String  index_name = e.nextElement();
            DbIndex index      = indexes.get(index_name);
            Integer count      = index.fieldCount();
            
            String idx = index.isUnique() ? "UNI:\\l" : "MUL:\\l";
            for (int i=0; i<count; i++)
            {
                idx += " - " + index.getField(new Integer(i)).getName() + "\\l";
            }
            
            ArrayList<String> l = new ArrayList<String>();
            l.add(idx);
            label.add(l);
        }
        
        return label.toString();
    }
    
    /**
     * This method generates the label that applies to a given join between two
     * tables.
     *
     * @param in_relations
     *            List of relations included in the join.
     * @param in_rankdir
     *            Initial orientation of a record node. Values can be:
     *            <ul>
     *               <li>TB (Top to bottom)</li>
     *               <li>LR (Left to right)</li>
     *               <li>RL 'Right to left)</li>
     *            </ul>
     *            <p>See GRAPHVIZ' documentation for "rankdir": http://www.graphviz.org/doc/info/shapes.html</p>
     * @return The method returns the DOT's representation of the join.
     */
    private String __createJoinLabel(ArrayList<FieldToFieldJoin> in_relations, String in_rankdir)
    {
        GridByRow label = new GridByRow();
        label.setRankdir(in_rankdir);

        for (FieldToFieldJoin join : in_relations)
        {
            ArrayList<String> l = new ArrayList<String>();
            l.add(join.cardinality_from_reference_to_dependent());
            l.add(join.src_field.getName());
            l.add(join.dst_field.getName());
            l.add(join.cardinality_from_dependent_to_reference());
            label.add(l);
        }
        return label.toString();
    }

}
