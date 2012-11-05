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

/**
 * This class contains constants that describe actions' selectors.
 *
 * @author Denis Beurive
 */
public class CliActionSelector
{
    /**
     * Unknown action selector.
     */
    public static int CLI_UNKNOWN             = -1;

    /**
     * The user requests the print of the general help message.
     */
    public static int CLI_HELP                = 0;

    /**
     * The user requests the print of the help message for a specific input add-on.
     */
    public static int CLI_INPUT_HELP          = 1;

    /**
     * The user requests the print of the help message for a specific output add-on.
     */
    public static int CLI_OUTPUT_HELP         = 2;

    /**
     * The user requests the print of the list of all available input add-ons.
     */
    public static int CLI_LIST_INPUT_ADDONS  = 3;

    /**
     * The user requests the print of the list of all available output add-ons.
     */
    public static int CLI_LIST_OUTPUT_TABLE_ADDONS = 4;

    /**
     * The user wants to add a new profile to the profiles' repository.
     */
    public static int CLI_PROFILE_ADD         = 5;

    /**
     * The user wants to remove a profile from the profiles' repository.
     */
    public static int CLI_PROFILE_REMOVE      = 6;

    /**
     * The user wants to print a given profile.
     */
    public static int CLI_PROFILE_SHOW        = 7;

    /**
     * The user wants to update a given profile.
     */
    public static int CLI_PROFILE_UPDATE      = 8;

    /**
     * The user wants to print all profiles in the profiles' repository.
     */
    public static int CLI_PROFILE_LIST        = 9;

    /**
     * The user wants to export a database.
     */
    public static int CLI_EXPORT              = 10;

    /**
     * The user wants to print the list of all available soft foreign key detectors.
     */
    public static int CLI_LIST_SFK_DETECTOR   = 11;

    /**
     * The user wants to calculate the path(s) between two tables.
     */
    public static int CLI_PATH                = 12;
}
