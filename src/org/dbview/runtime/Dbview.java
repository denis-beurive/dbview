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

package org.dbview.runtime;

import org.jdom.*;
import org.dbview.runtime.actions.*;
import org.dbview.runtime.cli.*;

/**
 * This class implements the main entry point of the application.
 * @author Denis Beurive
 */
public class Dbview
{
    /**
     * This is the main entry point.
     * @param args Command line arguments.
     */
	public static void main(String[] args)
	{
		try
		{
			// ---------------------------------------------------------------------------
			// Parse the command line.
			// ---------------------------------------------------------------------------

			CliData cli_data = CliParser.parse(args);
			if (CliActionSelector.CLI_UNKNOWN == cli_data.type) { Dbview.__unexpectedCli(); System.exit(1); }
			if (null != cli_data.message) { Dbview.__errorMessage(cli_data.message); System.exit(1); }

			// ---------------------------------------------------------------------------
			// Look what we have...
			// ---------------------------------------------------------------------------

            if (cli_data.type == CliActionSelector.CLI_HELP)                     { System.out.println(CliParser.help()); }
            if (cli_data.type == CliActionSelector.CLI_INPUT_HELP)               { Actions.helpInputAddOn(cli_data.data); }
            if (cli_data.type == CliActionSelector.CLI_OUTPUT_HELP)              { Actions.helpOutputAddOn(cli_data.data); }
            if (cli_data.type == CliActionSelector.CLI_LIST_INPUT_ADDONS)        { Actions.listInputTargets(); }
            if (cli_data.type == CliActionSelector.CLI_LIST_OUTPUT_TABLE_ADDONS) { Actions.listOutputTargets(); }
            if (cli_data.type == CliActionSelector.CLI_LIST_SFK_DETECTOR)        { Actions.listFkMatchers(); }
            if (cli_data.type == CliActionSelector.CLI_PROFILE_ADD)              { Actions.addProfile(cli_data.data); }
            if (cli_data.type == CliActionSelector.CLI_PROFILE_REMOVE)           { Actions.removeProfile(cli_data.data); }
            if (cli_data.type == CliActionSelector.CLI_PROFILE_SHOW)             { Actions.showProfile(cli_data.data); }
            if (cli_data.type == CliActionSelector.CLI_PROFILE_UPDATE)           { Actions.updateProfile(cli_data.data); }
            if (cli_data.type == CliActionSelector.CLI_PROFILE_LIST)             { Actions.listProfiles(); }
            if (cli_data.type == CliActionSelector.CLI_EXPORT)                   { Actions.export(cli_data.data); }

	        System.exit(0);
		}
		catch (Exception e)
		{
			System.out.println(e.getMessage());
			System.exit(1);
		}
	}

	/**
	 * Prints the help message (if the action's selector is unknown).
	 */
	private static void __unexpectedCli()
	{
		System.out.println("Unrecognizable command line's pattern.");
		System.out.println("Type \"" + System.getProperty("program.name") + " help\" for help." );
	}

	/**
	 * Prints an error message.
	 * @param in_message Error message to print.
	 */
	private static void __errorMessage(String in_message)
	{
		System.out.println(in_message);
	}
}
