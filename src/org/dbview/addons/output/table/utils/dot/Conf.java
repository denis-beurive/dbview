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

package org.dbview.addons.output.table.utils.dot;

/**
 * This class defines constants used to configure the DOT representation.
 * @remark If you need to customize the image that represents the database, you should change the values defined in this class.
 *
 * @author Denis Beurive
 */
public class Conf
{
    /**
     * Background color for the tables.
     */
    static final public String TABLE_BG_COLOR           = "lightgrey";

    /**
     * Color of the grid's edges, inside a table.
     */
    static final public String TABLE_GRID_COLOR         = "white";

    /**
     * Color of the edges for the grids that contain relations' information.
     */
    static final public String RELATION_FG_COLOR        = "black";

    /**
     * Background color of the edges for the grids that contain relations' information.
     */
    static final public String RELATION_BG_COLOR        = "beige";

    /**
     * Color of the arrows that represents "soft" links (soft foreign keys).
     */
    static final public String SOFT_EDGE_COLOR          = "green";

    /**
     * Color of the arrows that represents "hard" links (hard foreign keys).
     */
    static final public String HARD_EDGE_COLOR          = "red";

    /**
     * Color of the arrows that represents mixed (soft + hard) links (soft foreign keys).
     */
    static final public String SOFT_AND_HARD_EDGE_COLOR = "blue";
}
