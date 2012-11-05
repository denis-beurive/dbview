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

import java.util.Calendar;
import java.text.SimpleDateFormat;

/**
 * This class contains some dates' utilities.
 * @author Denis BEURIVE
 */
public class Date
{
    /**
     * Format for dates.
     */
    private static String DATE_FORMAT = "yyyy-MM-dd HH:mm:ss";

    /**
     * Set the format of dates.
     * @param in_format Dates' format.
     */
    public static void setDateFormat(String in_format)
    {
        Date.DATE_FORMAT = in_format;
    }

    /**
     * Return the current date and time.
     * @return The method returns the current date and time.
     */
    public static String now()
    {
      Calendar cal = Calendar.getInstance();
      SimpleDateFormat sdf = new SimpleDateFormat(DATE_FORMAT);
      return sdf.format(cal.getTime());
    }
}
