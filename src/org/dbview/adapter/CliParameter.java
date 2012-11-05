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
 * @class CliParameter
 * This class represents a command line parameter.
 * @remark This class is only used when the user requires to print the help.
 * @author Denis BEURIVE
 */
public class CliParameter
{
    /** Name of the CLI parameter. */
    public String parameter = null;

    /** Is the CLI parameter mandatory. */
    public Boolean mandatory = Boolean.FALSE;

    /** Description of the CLI parameter. */
    public String description = null;

    /** Type of parameter. Value is a constant defined in the class CliParameterTypes. */
    public int type = CliParameterTypes.UNDEFINED;

    /**
     * Name of the resource that implements the feature behind the parameter.
     * <ul>
     *      <li>Resources are packaged into JAR files.</li>
     *      <li>The list of available resources is defined in the application's configuration file "resources.properties".</li>
     *      <li>Each resource is bound to a JAR file.</li>
     * </ul>
     * 
     * @remark Please not that this attribute does <b>NOT</b> represents a file name. It represents the entry, in the configuration file "resources.properties", that is bound to the JAR file.
     */
    public String resource_name = null;
}
