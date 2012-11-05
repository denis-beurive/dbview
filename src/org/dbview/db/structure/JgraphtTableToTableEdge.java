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

package org.dbview.db.structure;

import org.jgrapht.graph.DefaultEdge;

/**
 * This class implements a specific type of edge for the library JgraphT.
 * @see http://www.jgrapht.org/
 * @author Denis Beurive
 */
public class JgraphtTableToTableEdge extends DefaultEdge
{
    /**
     * This value represents the version of the current class.
     * @remark Please note that this serial number is only important when the class is serialized, which is not the case here.
     *         It is defined in order to avoid waning messages during compilation.
     */
    private static final long serialVersionUID = 2L;

    /**
     * Build a new edge from a given table to a given table.
     * @remark Please note that this constructor can take any number of argument.
     *         You can add all properties you need in the edge.
     *         Edges are created explicitly:
     *         g.addEdge(table, target, new JgraphtEdge<Table>(...))
     */
    public JgraphtTableToTableEdge()
    {
        super();
    }

    /**
     * Return the starting point of the edge. This is a table.
     * @return The method returns the starting point of the edge.
     */
    public Table getSource() { return (Table)super.getSource(); }

    /**
     * Return the ending point of the edge. This is a table.
     * @return The method returns the ending point of the edge.
     */
    public Table getTarget() { return (Table)super.getTarget(); }

    /**
     * Return a textual representation of the edge.
     * @return The method returns a textual representation of the edge.
     */
    public String toString()
    {
        return this.getSource().getName() + " - " + this.getTarget().getName();
    }

}
