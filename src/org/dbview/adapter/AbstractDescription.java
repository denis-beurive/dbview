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

package org.dbview.adapter;

/**
 * @class AbstractDescription
 * This class contains data that describes an add-on.
 * @author Denis BEURIVE
 */
abstract public class AbstractDescription
{
    /**
     * This method returns a short description of the target.
     * @return The method returns a short description of the target.
     */
    public abstract String brief();

    /**
     * This method returns a long description of the target.
     * @return The method returns a long description of the target.
     */
    public abstract String detailed();
}
