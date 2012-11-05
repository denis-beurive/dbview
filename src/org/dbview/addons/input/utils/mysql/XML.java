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

package org.dbview.addons.input.utils.mysql;

/**
 * @class XML
 *
 * This class defines the names of the elements that form the XML representation of a Mysql configuration.
 * @remark The XML document generated by the class "Cli" is given to the class "Configuration".
 *         The two classes must share the same naming convention...
 * @author Denis BEURIVE
 */
public class XML
{
    /** Name of the XML tag that contains the name of the host that runs the MySql server. */
    public static final String HOST         = "host";

    /** Name of the XML tag that contains the port number used by the MySql server. */
    public static final String PORT         = "port";

    /** Name of the XML tag that contains the login used to connect to the MySql server. */
    public static final String LOGIN        = "login";

    /** Name of the XML tag that contains the password associated to the login used to connect to the MySql server. */
    public static final String PASSWORD     = "password";

    /** Name of the XML tag that contains the name of the database. */
    public static final String DBNAME       = "dbname";

    /** Name of the XML tag that contains the name of the soft foreign key detector. */
    public static final String FKMATCHER    = "fkmatcher";
}
