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

package org.dbview.utils.dot;

import java.lang.Float;
import java.util.*;

/**
 * This class represents a directed graph.
 * @author Denis Beurive
 */
public class DiGraph
{
    /**
     * <p>The initial orientation of a record node depends on the rankdir attribute. If this attribute is TB (the default) or TB, corresponding to vertical layouts, the top-level fields in a record are displayed horizontally. If, however, this attribute is LR or RL, corresponding to horizontal layouts, the top-level fields are displayed vertically.</p>
     * <p>See GRAPHVIZ' documentation for "rankdir".</p>
     * @see http://www.graphviz.org/doc/info/shapes.html
     */
    private String              __rankdir = "LR";

    /**
     * See GRAPHVIZ' documentation for "nodesep".
     * @see http://www.graphviz.org/doc/info/attrs.html
     */
	private Float	             __nodesep     = null;

    /**
     * See GRAPHVIZ' documentation for "ranksep".
     * @see http://www.graphviz.org/doc/info/attrs.html
     */
	private Float	             __ranksep     = null;

	/**
     * See GRAPHVIZ' documentation for "compound".
     * @see http://www.graphviz.org/doc/info/attrs.html
	 */
	private Boolean 		         __compound    = Boolean.FALSE;

	/**
	 * The list of sub graphs.
	 */
	private ArrayList<SubGraph>  __subgraphs   = new ArrayList<SubGraph>();

	/**
	 * The list of nodes.
	 */
	private ArrayList<Node>      __nodes       = new ArrayList<Node>();

	/**
	 * The list of edges.
	 */
	private ArrayList<Edge>      __edges       = new ArrayList<Edge>();

	/**
	 * Set the initial orientation of a record node.
	 * @param in_direction This string defines the direction. Values can be:
	 *        <ul>
	 *             <li>TB (Top to bottom)</li>
	 *             <li>LR (Left to right)</li>
	 *             <li>RL 'Right to left)</li>
	 *        </ul>
	 */
	public void setRankdir(String in_direction) { this.__rankdir = in_direction; }

	/**
	 * This method returns the graph's layout.
	 * @return The method returns the graph's layout. Values can be:
     *         <ul>
     *             <li>TB (Top to bottom)</li>
     *             <li>LR (Left to right)</li>
     *             <li>RL 'Right to left)</li>
     *         </ul>
	 */
	public String getRankdir() { return this.__rankdir; }

	/**
	 * This method sets the minimum distance between two nodes.
	 * @param in_value Minimum distance to set.
	 * @see GraphViz documentation.
	 */
	public void setNodesep(Float in_value) { this.__nodesep = in_value; }

	/**
	 * This method sets the minimum distance between two graph's ranks.
	 * @param in_value Minimum distance to set.
	 * @see GraphViz documentation.
	 */
	public void setRankset(Float in_value) { this.__ranksep = in_value; }

	/**
	 * This method set the compound's flag.
	 * @param in_value TRUE or FALSE.
	 * @see GraphViz documentation.
	 */
	public void setCompound(Boolean in_value) { this.__compound = in_value; }

	/**
	 * Add a sub graph to the graph.
	 * @param in_subgraph Sub graph to add.
	 */
	public void addSubGraph(SubGraph in_subgraph) { this.__subgraphs.add(in_subgraph); }

	/**
	 * Add an edge to the graph.
	 * @param in_edge Edge to add.
	 */
	public void addEdge(Edge in_edge) { this.__edges.add(in_edge); }

	/**
	 * Add a node to the graph.
	 * @param in_node Node to add.
	 */
	public void addNode(Node in_node) { this.__nodes.add(in_node); }

	/**
	 * This method returns a string that represents the directed graph.
	 * @return The method returns a string that represents the directed graph.
	 */
	public String toString()
	{
		ArrayList<String> lines = new ArrayList<String>();

		lines.add("digraph G");
		lines.add("{");

		// Add the global configuration for the graph.
		lines.add("\trankdir=" + this.__rankdir + ";");

		if (this.__compound)        { lines.add("\t" + "compound=true;"); }
		if (null != this.__nodesep) { lines.add("\t" + "nodesep=" + this.__nodesep + ";"); }
		if (null != this.__ranksep) { lines.add("\t" + "ranksep=" + this.__ranksep + ";"); }
		lines.add("");

		// Add all sub graphs.
		for (SubGraph subgraph: this.__subgraphs) { lines.add(org.dbview.utils.Strings.indent("\t", subgraph.toString())); lines.add(""); }

		// Add all nodes.
		for (Node node: this.__nodes) { lines.add(org.dbview.utils.Strings.indent("\t", node.toString())); lines.add(""); }

		// Add all edges.
		for (Edge edge: this.__edges) { lines.add(org.dbview.utils.Strings.indent("\t", edge.toString())); lines.add(""); }

		lines.add("}");
		return org.dbview.utils.Strings.joinWithNewLines(lines);
	}
}
