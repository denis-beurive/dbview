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
import java.lang.Exception;

/**
 * @class CliException
 * This exception is thrown whenever an error occurred while using the command line interface.
 * @author Denis Beurive
 */
public class CliException extends Exception
{
    /**
     * This value represents the version of the current class.
     * @note Please note that this serial number is only important when the class is serialized, which is not the case here.
     *       It is defined in order to avoid waning messages during compilation.
     */
    private static final long serialVersionUID = 2L;

    /**
     * Constructor.
     * @param in_message Error message.
     */
    public CliException(String in_message)
    {
        super(in_message);
    }
}
