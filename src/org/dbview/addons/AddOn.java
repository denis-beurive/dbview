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


package org.dbview.addons;

import java.util.ArrayList;

/**
 * @class AddOn
 * This class contains information about a given add-on.
 * @author Denis Beurive
 */
public class AddOn
{
	/**
	 * This attribute represents the name of the package in which the add-on is declared.
	 */
	public String package_name = null;

	/**
	 * This attribute represents the list of adaptors declared for the add-on.
	 */
	public ArrayList<String> adaptors_list = new ArrayList<String>();
}