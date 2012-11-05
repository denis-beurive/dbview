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

import java.util.*;
import org.dbview.utils.*;

/**
 * This class represents sub graphs.
 * @author Denis Beurive
 */
public class SubGraph
{
    /**
     * Name of the sub graph.
     */
    private String __name             = null;

    /**
     * Style of the sub graph.
     */
    private String __style            = null;

    /**
     * Background color for the sub graph.
     */
    private String __background_color = null;

    /**
     * Sub graph's label.
     */
    private String __label            = null;

    /**
     * Style of all nodes within the sub graph.
     */
    private String __node_style       = null;

    /**
     * Background color of all nodes within the sub graph.
     */
    private String __node_color       = null;

    /**
     * List of nodes within the sub graph.
     */
    private ArrayList<Node> __nodes   = new ArrayList<Node>();

    /**
     * The list of edges.
     */
    private ArrayList<Edge> __edges   = new ArrayList<Edge>();
    
    /**
     * Add an edge to the subgraph.
     * @param in_edge Edge to add.
     */
    public void addEdge(Edge in_edge) { this.__edges.add(in_edge); }
    
    /**
     * Create a unique, <b>and valid</b> (no spaces...), name for the sub graph.
     * @param in_name Original name.
     * @return The method returns a unique name.
     * @throws Exception
     */
    public static String generateName(String in_name)
    {
        return "cluster_" + Strings.SHAsum(in_name);
    }
    
    /**
     * Create a sub graph.
     * @param in_name Friendly name of the sub graph.
     * @note Please note that spaces are replaced by "_".
     */
    public SubGraph(String in_name)
    {
        this.__name = SubGraph.generateName(in_name);
    }

    /**
     * Set the style of the sub graph.
     * @param in_style Name of the style to apply.
     */
    public void setStyle(String in_style) { this.__style = in_style; }

    /**
     * Set the background color of the sub graph.
     * @param in_color The color that must be applied to the background.
     */
    public void setBackgroundColor(String in_color)	{ this.__background_color = in_color; }

    /**
     * Set the label of the sub graph (this is the title).
     * @param in_label The label to set.
     */
    public void setLabel(String in_label) { this.__label = in_label; }

    /**
     * Set the style of all nodes within the sub graph.
     * @param in_style Name of the style to apply to all nodes.
     */
    public void setNodeStyle(String in_style) { this.__node_style = in_style; }

    /**
     * Set the background color of all nodes within the sub graph.
     * @param in_color Background color of all nodes within the sub graph.
     */
    public void setNodeColor(String in_color) { this.__node_color = in_color; }

    /**
     * Add a node to the sub graph.
     * @param in_node Node to add to the sub graph.
     */
    public void addNode(Node in_node) { this.__nodes.add(in_node); }

    /*
     * The method returns the DOT's name of a sub graph, given its friendly name.
     * @param in_name Friendly name of the sub graph.
     * @return The method returns the DOT's name of the sub graph.
     */
    // public static String getName(String in_name) { return "cluster_" + in_name; }

    /**
     * Return a string that represents this graph.
     * @return The method returns a string that represents this graph.
     */
    public String toString()
    {
    	ArrayList<String> lines = new ArrayList<String>();
    	lines.add("subgraph " + this.__name);
    	lines.add("{");

    	// Global configuration for the sub graph.
    	if (null != this.__style)			 { lines.add("\t" + "style=" + this.__style + ";"); }
    	if (null != this.__background_color) { lines.add("\t" + "color=" + this.__background_color + ";"); }

    	// Set the title.
    	if (null != this.__label) { lines.add("\t" + "label=\"" + this.__label + "\";"); }

    	// Add the configuration for the nodes.
    	if ((null != this.__node_style) || (null != this.__node_color))
    	{
    		ArrayList<String> params = new ArrayList<String>();
    		if (null != this.__node_style) { params.add("style=" + this.__node_style); }
    		if (null != this.__node_color) { params.add("color=" + this.__node_color); }
    		lines.add("\t" + "node [" + org.dbview.utils.Strings.join(params, ", ") + "];");
    	}

    	// Add the nodes.
    	for (Node node: this.__nodes) { lines.add(org.dbview.utils.Strings.indent("\t", node.toString())); }

        // Add all edges.
        for (Edge edge: this.__edges) { lines.add(org.dbview.utils.Strings.indent("\t", edge.toString())); }
    	
    	lines.add("}");
    	return org.dbview.utils.Strings.joinWithNewLines(lines);
    }
}
