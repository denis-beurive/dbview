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
 * This class represents a node.
 * @author Denis Beurive
 */
public class Node
{
    /**
     * Name of the node.
     */
    private String __name     = null;

    /**
     * Shape of the node.
     * @see http://www.graphviz.org/doc/info/attrs.html
     */
    private String __shape    = null;

    /**
     * Style of the node.
     * @see http://www.graphviz.org/doc/info/attrs.html
     */
    private String __style    = null;

    /**
     * Node's label.
     * @see http://www.graphviz.org/doc/info/attrs.html
     */
    private String __label    = null;

    /**
     * Node's foreground color.
     * @see http://www.graphviz.org/doc/info/attrs.html
     */
    private String __color    = null;

    /**
     * Node's background color.
     * @see http://www.graphviz.org/doc/info/attrs.html
     */
    private String __bg_color = null;

    /**
     * Create a unique, <b>and valid</b> (no spaces...), name for the node.
     * @param in_name Original name.
     * @return The method returns a unique name.
     * @throws Exception
     */
    public static String generateName(String in_name)
    {
        return "node_" + Strings.SHAsum(in_name);
    }
    
    /**
     * Create a node.
     * @param in_name Name of the node to create.
     */
    public Node(String in_name)
    { this.__name = Node.generateName(in_name); }

    /**
     * Set the node's shape.
     * @param in_shape The node's shape.
     */
    public void setShape(String in_shape) { this.__shape = in_shape; }

    /**
     * Set the node's style.
     * @param in_style The style to set.
     */
    public void setStyle(String in_style) { this.__style = in_style; }

    /**
     * Set the node's label.
     * @param in_label The label to set.
     */
    public void setLabel(String in_label) { this.__label = in_label; }

    /**
     * Set the node's color.
     * @param in_color The color to set.
     */
    public void setColor(String in_color) { this.__color = in_color; }

    /**
     * Set the background color of the node.
     * @param in_color Background color of the node.
     * @note This setting may not have any effect, depending on the node's style. See the GraphViz documentation.
     */
    public void setFillColor(String in_color)   { this.__bg_color = in_color; }

    /**
     * The method returns the DOT's representation of the node.
	 * @return The method returns the DOT's representation of the node.
     */
    public String toString()
    {
        	ArrayList<String> words = new ArrayList<String>();
        	if (null != this.__shape)      { words.add("shape=" + this.__shape); }
        	if (null != this.__style)      { words.add("style=\"" + this.__style + "\""); }
        	if (null != this.__label)      { words.add("label=\"" + this.__label + "\""); }
        	if (null != this.__color)      { words.add("color=\"" + this.__color + "\""); }
        	if (null != this.__bg_color)   { words.add("fillcolor=\"" + this.__bg_color + "\""); }
        	return this.__name + " [" + org.dbview.utils.Strings.join(words, " ") + "];";
    }
}
