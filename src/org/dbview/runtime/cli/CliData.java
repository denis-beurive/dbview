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

package org.dbview.runtime.cli;
import java.util.Hashtable;

/**
 * <p>This class contains the data extracted from the command line.</p>
 * <p>The interpretation of this data structure depends on the type of request.</p>
 * @author Denis Beurive
 */
public class CliData
{
    /**
     * This attribute represents the type of request.
     * @remark See class CliParser.
     *         Request types are represented by constants. The constants' names begin with the string "CS_" (Command Selector).
     */
	public int type = org.dbview.runtime.cli.CliActionSelector.CLI_UNKNOWN;

	/**
	 * This attribute contains the data extracted from the command line.
	 * The attribute's value can be "null" if the request does not require data.
	 * <ul>
	 *     <li>Key: The name of a command line option.</li>
	 *     <li>Value: The value of the command line option.</li>
	 * </ul>
	 */
	public Hashtable<String, Object> data = null;

	/**
	 * This attribute contains an error message.
	 */
	public String message = null;

	/**
	 * Constructor.
	 * @param in_type This argument represents the type of request (depends on the command selector).
	 * @param in_data This argument contains the data extracted from the command line.
	 */
	public CliData(int in_type, Hashtable<String, Object> in_data)
	{
	    this.type = in_type;
	    this.data = in_data;
	}

	/**
	 * Constructor.
	 * @param in_type This argument represents the type of request.
	 * @param in_error This argument represents an error message.
	 */
	public CliData(int in_type, String in_error)
	{
	    this.type    = in_type;
	    this.message = in_error;
	}

	/**
	 * Constructor.
	 * @param in_type This argument represents the type of request.
	 */
	public CliData(int in_type)
	{
	    this.type = in_type;
	}
}
