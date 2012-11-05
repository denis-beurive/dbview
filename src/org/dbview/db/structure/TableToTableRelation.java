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
import java.util.ArrayList;

/**
 * <p>This class represents a relation between two tables.</p>
 * <ul>
 *      <li>A "table to table" relation contains one or more joins.</li>
 *      <li>It also contains extra information.</li>
 * </ul>
 * <p>Graphically, a relation (between two tables) is represented by an arrow.</p>
 * <ul>
 *      <li>The start of the arrow is the "dependent" table (that defines the foreign keys).</li>
 *      <li>The arrow points to the "reference" table.</li>
 *      <li>A relation contains at least one join (between two fields).</li>
 * </ul>
 * @author Denis Beurive
 */
class TableToTableRelation
{
    public TableToTableRelation() { this.relations = new ArrayList<FieldToFieldJoin>(); }

    /**
     * List of joins.
     */
    public ArrayList<FieldToFieldJoin> relations = null;

    /**
     * The type of "table to table" relation. It can be:
     * <ul>
     *    <li>SOFT_AND_HARD_LINK</li>
     *    <li>SOFT_LINK</li>
     *    <li>HARD_LINK</li>
     * </ul>
     */
    public int type = Database.UNDEFINED_LINK;
}
