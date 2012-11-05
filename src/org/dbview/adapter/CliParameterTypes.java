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
 * class CliParameterTypes This class contains types' indicators for command
 * line options.
 *
 * @author Denis BEURIVE
 */
public class CliParameterTypes
{
    /** The parameter's type is not defined. */
    public static final int UNDEFINED = -1;
    /** The parameter is a string. */
    public static final int STRING    = 0;
    /** The parameter is an integer. */
    public static final int INTEGER   = 1;
    /** The parameter is a boolean. */
    public static final int BOOLEAN   = 2;
    /** The parameter is a double. */
    public static final int DOUBLE    = 3;
    /** The parameter is a long. */
    public static final int LONG      = 4;

    /**
     * This method generates a textual representation of a given parameter's type.
     * @param in_constant Parametor's type.
     * @return The method returns a textual representation of the given parameter's type.
     * @throws Exception
     */
    public static String toString(int in_constant) throws Exception
    {
        if (CliParameterTypes.UNDEFINED == in_constant)
        {
            return "undefined";
        }
        if (CliParameterTypes.STRING == in_constant)
        {
            return "string";
        }
        if (CliParameterTypes.INTEGER == in_constant)
        {
            return "integer";
        }
        if (CliParameterTypes.BOOLEAN == in_constant)
        {
            return "boolean";
        }
        if (CliParameterTypes.DOUBLE == in_constant)
        {
            return "double";
        }
        if (CliParameterTypes.LONG == in_constant)
        {
            return "long";
        }
        throw new Exception("Unexpected type of CLI parameter: " + in_constant);
    }
}
