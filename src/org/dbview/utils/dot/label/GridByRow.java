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
package org.dbview.utils.dot.label;

import java.util.ArrayList;

/**
 * The class represents a node's label that describes a grid.
 * The grid's content is provided by a set of rows.
 * @author Denis Beurive
 */
public class GridByRow  extends AbstractGrid
{
    /**
     * List of rows.
     */
    private ArrayList<ArrayList<String>> __lines   = new ArrayList<ArrayList<String>>();

    /**
     * This method adds a row to the grid.
     * @param in_line Row to add.
     */
    public void add(ArrayList<String> in_line) { this.__lines.add(in_line); }

    /**
     * This method returns a string that represents the grid.
     * @return The method returns a string that represents the grid.
     */
    public String toString()
    {
        if (0 == this._rankdir_.compareTo("TB"))
        {
            return this._vRows_(this.__lines);
        }

        return this._hRows_(this.__lines);
    }
}
