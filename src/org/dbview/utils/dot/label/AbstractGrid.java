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

package org.dbview.utils.dot.label;

import java.util.ArrayList;

/**
 * This class is the base class for all classes that generates grids for node's labels.
 * @author Denis BEURIVE
 */
public abstract class AbstractGrid
{
    /**
     * This attribute represents the global orientation of the DOT's graph.
     * @see http://www.graphviz.org/doc/info/shapes.html
     */
    protected String _rankdir_ = "TB";

    /**
     * Set the initial orientation of a record node.
     * @param in_direction This string defines the direction. Values can be:
     *        <ul>
     *             <li>TB (Top to bottom)</li>
     *             <li>LR (Left to right)</li>
     *             <li>RL 'Right to left)</li>
     *        </ul>
     */
    public void setRankdir(String in_direction) { this._rankdir_ = in_direction; }

    /**
     * Create a grid by processing rows in a vertical layout.
     * @param in_data Rows.
     * @return The method returns the representation of the grid.
     */
    protected String _vRows_(ArrayList<ArrayList<String>> in_data)
    {
        if (0 == in_data.size()) { return ""; }

        // We assume that lines have the same sizes.
        int column_number = in_data.get(0).size();
        ArrayList<ArrayList<String>> columns = new ArrayList<ArrayList<String>>();

        for (int i=0; i<column_number; i++)
        {
            ArrayList<String> column = new ArrayList<String>();
            for (ArrayList<String> line: in_data) { column.add(line.get(i)); }
            columns.add(column);
        }

        ArrayList<String> tubes = new ArrayList<String>();
        for (ArrayList<String> column: columns) { tubes.add("{" + org.dbview.utils.Strings.join(column, "|") + "}"); }
        return org.dbview.utils.Strings.join(tubes, " | ");
    }

    /**
     * Create a grid by processing rows in a horizontal layout.
     * @param in_data Rows.
     * @return The method returns the representation of the grid.
     */
    protected String _hRows_(ArrayList<ArrayList<String>> in_data)
    {
        return "{" + this._vRows_(in_data) + "}";
    }

    /**
     * This method returns a string that represents the grid.
     * @return The method returns a string that represents the grid.
     */
    public abstract String toString();
}
