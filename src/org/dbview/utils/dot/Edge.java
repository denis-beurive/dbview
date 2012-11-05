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

/**
 * This class represents an edge.
 * @author Denis Beurive
 */
public class Edge
{
    /**
     * Beginning of the edge.
     */
	private String __from       = null;

	/**
	 * End of the edge.
	 */
	private String __to         = null;

	/**
     * See GRAPHVIZ' documentation for "ltail".
     * @see http://www.graphviz.org/doc/info/attrs.html
	 */
	private String __ltail		= null;

    /**
     * See GRAPHVIZ' documentation for "lhead".
     * @see http://www.graphviz.org/doc/info/attrs.html
     */
	private String __lhead		= null;

	/**
	 * Color of the edge.
	 */
	private String __color		= null;

	/**
	 * width of the edge.
	 */
	private String __penwidth	= null;

    /**
     * See GRAPHVIZ' documentation for "arrowtail".
     * @see http://www.graphviz.org/doc/info/attrs.html
     */
	private String __arrowtail  = null;

    /**
     * See GRAPHVIZ' documentation for "arrowhead".
     * @see http://www.graphviz.org/doc/info/attrs.html
     */
	private String __arrowhead  = null;
	
	/**
	 * Is the edge visible?
	 */
	private Boolean __edge_visible = Boolean.TRUE;

	/**
	 * Set the edge invisible.
	 */
	public void setInvisible()
	{
	    this.__edge_visible = Boolean.FALSE;
	}
	
	/**
	 * This method sets the position of the edge's tail.
	 * @param in_value Name of the element that the edge's tail is attached to.
	 */
	public void setLtail(String in_value) { this.__ltail = in_value; }

	/**
	 * This method sets the position of the edge's head.
	 * @param in_value Name of the element that the edge's head is attached to.
	 */
	public void setLhead(String in_value) { this.__lhead = in_value; }

	/**
	 * This method sets the edge's color.
	 * @param in_value the color to set.
	 */
	public void setColor(String in_value) { this.__color = in_value; }

	/**
	 * This method sets the width of the line used to draw the edge.
	 * @param in_value The width to set.
	 */
	public void setPenwidth(String in_value) { this.__penwidth = in_value; }

	/**
	 * Set the origin of the edge. Please note that the edge may not originate from the element, if you set the edge's tail.
	 * @param in_value Name of the element that represents the origin of the edge.
	 */
	public void setFrom(String in_value)
	{
	    this.__from = Node.generateName(in_value);
	}

	/**
	 * Name of the element that represents the edge's destination. Please note that the edge may not end to the element, if you set the edge's head.
	 * @param in_value Name of the element that represents the edge's destination.
	 */
	public void setTo(String in_value)
	{
	    this.__to = Node.generateName(in_value);
	}

	/**
	 * This method defines the appearance of the head of the edge.
	 * @param in_value Head's appearance.
	 */
	public void setArrowhead(String in_value) { this.__arrowhead	= in_value; }

	/**
     * This method defines the appearance of the tail of the edge.
     * @param in_value Tail's appearance.
     */
    public void setArrowtail(String in_value) { this.__arrowtail    = in_value; }

	/**
	 * This method returns a DOT's representation of the edge.
	 * @return The method returns a DOT's representation of the edge.
	 */
	public String toString()
	{
		if ((null == this.__from) || (null == this.__to)) { return "WARNING: You try to create a DOT edge without head or tail !!!"; }

		String edge = this.__from + " -> " + this.__to;
		ArrayList<String> words = new ArrayList<String>();
		Boolean params = Boolean.FALSE;

		if (null != this.__ltail)		{ words.add("ltail="	 + this.__ltail);		params = Boolean.TRUE; }
		if (null != this.__lhead)		{ words.add("lhead="	 + this.__lhead);		params = Boolean.TRUE; }
		if (null != this.__color)		{ words.add("color="	 + this.__color);		params = Boolean.TRUE; }
		if (null != this.__penwidth)	{ words.add("penwidth="  + this.__penwidth);	params = Boolean.TRUE; }
		if (null != this.__arrowhead)	{ words.add("arrowhead=" + this.__arrowhead);	params = Boolean.TRUE; }
		if (null != this.__arrowtail)   { words.add("arrowtail=" + this.__arrowtail);   params = Boolean.TRUE; }
		if (! this.__edge_visible)      { words.add("style=\"invis\"");                 params = Boolean.TRUE; }

		if (params) { edge = edge + " [" + org.dbview.utils.Strings.join(words, " ") + "]"; }
		return edge + ";";
	}
}
