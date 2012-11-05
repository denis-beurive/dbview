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
package org.dbview.utils;

/**
 * This class implements utilities used to interact with the operating system.
 * @author Denis Beurive
 */
public class Os
{
    /**
     * This method returns the name of the current operating system.
     * @return The method returns the name of the current operating system.
     */
	public static String getOsName()
	{
		if (System.getProperty("os.name").toLowerCase().indexOf("windows") > -1)
		{ return "windows"; }
		
		if (System.getProperty("os.name").toLowerCase().indexOf("linux") > -1)
		{ return "linux"; }
		    
		if (System.getProperty("os.name").toLowerCase().indexOf("mac") > -1)
		{ return "mac"; }
		 
		return "unknown";
	}
	

}
