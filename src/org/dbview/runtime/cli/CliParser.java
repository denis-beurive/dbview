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

import java.util.Arrays;
import java.util.Hashtable;
import org.jdom.JDOMException;
import java.util.ArrayList;
import java.lang.reflect.Method;
import org.dbview.adapter.*;
import org.dbview.addons.AddOnCatalogException;
import org.dbview.addons.input.utils.mysql.CliOptions;
import org.dbview.input_addons.*;
import org.dbview.output_addons.*;
import org.dbview.utils.args4j.CommaSeparatedStripedListOptionHandler;
import org.dbview.utils.args4j.OptionContainer;
import org.dbview.utils.args4j.SpecialStringOptionHandler;
import org.jdom.Element;
import org.kohsuke.args4j.*;


/**
 * This class handles the process of parsing the command line, which is not a trivial action.
 *
 * @author Denis BEURIVE
 */
public class CliParser
{
    /**
     * Command line.
     */
    private static String[]      __vargs               = null;

    /**
     * This is the catalogue of all available input add-ons.
     * This object is used to instantiate input add-ons.
     */
    private static InputCatalog  __catalog             = null;

    /**
     * This is a command line option.
     * It represents an input add-on (used to load a specific database).
     */
    public static final String   CLI_INPUT_ADDON      = "db-adaptor";

    /**
     * This is a command line option.
     * It represents an output add-on.
     * The output add-on defines the output's format.
     */
    public static final String   CLI_OUTPUT_ADDON     = "exporter";

    /**
     * This is a command line option.
     * It represents the name of a profile.
     */
    public static final String   CLI_PROFILE           = "profile";

    /**
     * This is a command line option.
     * It represents the two extremities of a path.
     */
    public static final String   CLI_BETWEEN          = "between";

    /**
     * This is a command line option.
     * It represents a list of tables to zoom in.
     */
    public static final String   CLI_ZOOM          = "zoom";

    /**
     * This is a command line option.
     * If a list of tables to zoom in is defined, then this value represents a zoom level.
     */
    public static final String   CLI_ZOOM_LEVEL          = "zoom-level";

    /**
     * This is a command line option.
     * This option is used when the user wants to calculate the path(s) between two tables.
     * it represents the maximum number of paths to show.
     */
    public static final String   CLI_LIMIT             = "limit";

    /**
     * This is a command selector.
     * The user requests the print of the general help message.
     */
    public static final String   CS_HELP               = "help";

    /**
     * This is a command selector.
     * The user requests the list of all available input add-ons.
     */
    public static final String   CS_LIST_INPUT         = "list-db-adaptors";

    /**
     * This is a command selector.
     * VThe user requests the list of all available output add-ons for tables.
     */
    public static final String   CS_LIST_OUTPUT_TABLE        = "list-table-exporters";

    /**
     * This is a command selector.
     * The user requests the print of the help message for a specific input add-on.
     */
    public static final String   CS_INPUT_ADDON_HELP  = "help-db-adaptor";

    /**
     * This is a command selector.
     * The user requests the print of the help message for a specific output add-on.
     */
    public static final String   CS_OUTPUT_ADDON_HELP = "help-table-exporter";

    /**
     * This is a command selector.
     * The user wants to add a new profile to the profiles' repository.
     */
    public static final String   CS_PROFILE_ADD        = "profile-add";

    /**
     * This is a command selector.
     * The user wants to remove a profile from the profiles' repository.
     */
    public static final String   CS_PROFILE_DELETE     = "profile-delete";

    /**
     * This is a command selector.
     * The user wants to print a given profile.
     */
    public static final String   CS_PROFILE_SHOW       = "profile-show";

    /**
     * This is a command selector.
     * The user wants to update a given profile.
     */
    public static final String   CS_PROFILE_UPDATE     = "profile-update";

    /**
     * This is a command selector.
     * The user wants to print all profiles in the profiles' repository.
     */
    public static final String   CS_PROFILE_LIST       = "profile-list";

    /**
     * This is a command selector.
     * The user wants to export a database.
     */
    public static final String   CS_EXPORT             = "export";

    /**
     * This is a command selector.
     * The user wants to print the list of all available soft foreign key detectors.
     */
    public static final String   CS_LIST_SFK_DETECTOR    = "list-soft-key-detectors";

    /**
     * Initialize the class.
     *
     * @throws Exception
     */
    private static void __init() throws Exception
    {
        if (null == CliParser.__catalog)
            CliParser.__catalog = new InputCatalog();
    }

    /**
     * This method prints the software' usage.
     * @return This method returns a string that represents the help message.
     */
    public static String help()
    {
        ArrayList<String> lines = new ArrayList<String>();

        String program_name = System.getProperty("program.name");
        program_name = null == program_name ? "DEBUG MESSAGE: Set the program's name in the launcher!" : program_name;

        lines.add(program_name + ":");
        lines.add(CliParser.CS_HELP);
        lines.add("");
        lines.add(CliParser.CS_LIST_INPUT);
        lines.add("");
        lines.add(CliParser.CS_LIST_OUTPUT_TABLE);
        lines.add("");
        lines.add(CliParser.CS_LIST_SFK_DETECTOR);
        lines.add("");
        lines.add(CliParser.CS_INPUT_ADDON_HELP      + " <name of the add-on to which you need help>");
        lines.add("");
        lines.add(CliParser.CS_OUTPUT_ADDON_HELP     + " <name of the add-on to which you need help>");
        lines.add("");
        lines.add(CliParser.CS_PROFILE_LIST);
        lines.add("");
        lines.add(CliParser.CS_PROFILE_DELETE        + " <name of the profile to delete>");
        lines.add("");
        lines.add(CliParser.CS_PROFILE_SHOW          + " <name of the profile to show>");
        lines.add("");
        lines.add(CliParser.CS_PROFILE_ADD           + " <name of the profile to add> "
                                                     + "-" + CliParser.CLI_INPUT_ADDON + " <name of the database adaptor> /profile's configuration/");
        lines.add("");
        lines.add(CliParser.CS_PROFILE_UPDATE        + " <name of the profile to update> /One, or more, profile's parameter(s)/");
        lines.add("");
        lines.add(CliParser.CS_EXPORT                + " -" + CliParser.CLI_OUTPUT_ADDON + " <name of the exporter> /exporter's configuration/"
                                                     + " [-" + CliParser.CLI_BETWEEN + " <name of a table>,<name of a table> [-"  + CliParser.CLI_LIMIT + " <maximum number of pathes to calculate>]"
                                                     + " | -" + CliParser.CLI_ZOOM + " <comma separated list of tables> [-" + CliParser.CLI_ZOOM_LEVEL + " <value>]]"
                                                     + " (-" + CliParser.CLI_PROFILE + " <profile's name> | -" + CliParser.CLI_INPUT_ADDON + " <name of the adaptor> /adaptor's configuration/)");
        lines.add("");
        return org.dbview.utils.Strings.joinWithNewLines(lines);
    }

    /**
     * Parse the command line. Please not that this method will try all the
     * specialized command line parsers.
     *
     * @note Specialized parsers are private methods that start with the string
     *       of characters "__try", followed by an uppercase letter. Specialized
     *       command line parsers take no argument, at return an instance of
     *       CliData.
     * @param in_vargs
     *            Array of strings that represent the command line.
     * @return The parser returns an instance of CliData.
     * @throws Exception
     */
    public static CliData parse(String[] in_vargs)
    {
        CliData data = null;
        CliParser.__vargs = in_vargs;

        if (0 == CliParser.__vargs.length)
        {
            return new CliData(CliActionSelector.CLI_HELP);
        }

        Method[] methods = CliParser.class.getDeclaredMethods();

        for (int i = 0; i < methods.length; i++)
        {
            if (!methods[i].getName().matches("^__try[A-Z].+$"))
            {
                continue;
            }
            try
            {
                // System.out.println(methods[i].getName());
                data = (CliData) methods[i].invoke(null, new Object[0]);
            }
            catch (Exception e)
            {
                System.out.println("Unexpected error: " + e.getMessage() + " " + e.getClass().getName());
            }
            if (null != data)
            {
                return data;
            }
        }

        return new CliData(CliActionSelector.CLI_UNKNOWN);
    }

    /**
     * Test if the user asks for help.
     *
     * @return <ul>
     *              <li>If the user asks for help, then the method returns an instance of CliData. The value of the member "type" of the returned object is
     *                  CliConstantes.CLI_HELP.</li>
     *              <li>Otherwise, the method returns the value null.</li>
     *         </ul>
     */
    @SuppressWarnings("unused")
    private static CliData __tryHelp() throws Exception
    {
        if (0 == CliParser.__vargs[0].compareTo(CliParser.CS_HELP))
        {
            String message = CliParser.__checkCliEmpty();
            if (null != message) { return new CliData(CliActionSelector.CLI_HELP, message); }
            return new CliData(CliActionSelector.CLI_HELP);
        }
        return null;
    }

    /**
     * Test if the user wants to print the help for a specific input add-on.
     *
     * @return <ul>
     *              <li>If the user asks for help, then the method returns an instance of CliData.
     *                  The value of the member "type" of the returned object is CliConstantes.CLI_INPUT_HELP.</li>
     *              <li>Otherwise, the method returns the value null.</li>
     *         </ul>
     * @throws Exception
     */
    @SuppressWarnings("unused")
    private static CliData __tryInputHelp() throws Exception
    {
        if (0 != CliParser.__vargs[0].compareTo(CliParser.CS_INPUT_ADDON_HELP)) { return null; }
        if (2 != CliParser.__vargs.length) { return new CliData(CliActionSelector.CLI_INPUT_HELP, "In order to print the usage for a specific input add-on, you must specify the name of the requested add-on. "); }

        // Check the argument's value.
        String target_name = CliParser.__vargs[1];
        if (target_name.startsWith("-")) { return new CliData(CliActionSelector.CLI_INPUT_HELP, "Arguments must not start with a dash (-). If an argument starts with a dash, then you should escape it. For example, \"-foo\" becomes \"\\-foo\"."); }
        target_name = target_name.replaceFirst("^\\\\", "");

        Hashtable<String, Object> data = new Hashtable<String, Object>();
        data.put("name", target_name);
        return new CliData(CliActionSelector.CLI_INPUT_HELP, data);
    }

    /**
     * Test if the user wants to print the help for a specific output add-on.
     *
     * @return <ul>
     *              <li>If the user asks for help, then the method returns an instance of CliData.
     *                  The value of the member "type" of the returned object is CliConstantes.CLI_OUTPUT_HELP.</li>
     *              <li>Otherwise, the method returns the value null.</li>
     *         </ul>
     * @throws Exception
     */
    @SuppressWarnings("unused")
    private static CliData __tryOutputHelp() throws Exception
    {
        if (0 != CliParser.__vargs[0].compareTo(CliParser.CS_OUTPUT_ADDON_HELP)) { return null; }
        if (2 != CliParser.__vargs.length) { return new CliData(CliActionSelector.CLI_OUTPUT_HELP, "In order to print the usage for a specific output add-on, you must specify the name of the requested add-on."); }

        // Check the argument's value.
        String target_name = CliParser.__vargs[1];
        if (target_name.startsWith("-"))
        { return new CliData(CliActionSelector.CLI_OUTPUT_HELP, "Arguments must not start with a dash (-). If an argument starts with a dash, then you should escape it. For example, \"-foo\" becomes \"\\-foo\"."); }
        target_name = target_name.replaceFirst("^\\\\", "");

        Hashtable<String, Object> data = new Hashtable<String, Object>();
        data.put("name", target_name);
        return new CliData(CliActionSelector.CLI_OUTPUT_HELP, data);
    }

    /**
     * Test if the user asks for the list of available input add-ons.
     *
     * @return <ul>
     *              <li>If the user asks for the list of available input add-ons, then the method returns an instance of CliData.
     *                  The value of the member "type" of the returned object is CliConstantes.CLI_LIST_INPUT_TARGETS.</li>
     *              <li>Otherwise, the method returns the value null.</li>
     *         </ul>
     */
    @SuppressWarnings("unused")
    private static CliData __tryTargetInput()
    {
        if (0 != CliParser.__vargs[0].compareTo(CliParser.CS_LIST_INPUT)) return null;
        String message = CliParser.__checkCliEmpty();
        if (null != message) return new CliData(CliActionSelector.CLI_LIST_INPUT_ADDONS, message);
        return new CliData(CliActionSelector.CLI_LIST_INPUT_ADDONS);
    }

    /**
     * Test if the user asks for the list of available soft foreign key matchers.
     * @return <ul>
     *              <li>If the user asks for the list of available soft foreign key matchers, then the method returns an instance of CliData.
     *                  The value of the member "type" of the returned object is CliConstantes.CLI_LIST_FK_MATCHER.</li>
     *              <li> Otherwise, the method returns the value null.</li>
     *         </ul>
     */
    @SuppressWarnings("unused")
    private static CliData __tryListFkMatcher()
    {
        if (0 != CliParser.__vargs[0].compareTo(CliParser.CS_LIST_SFK_DETECTOR)) return null;
        String message = CliParser.__checkCliEmpty();
        if (null != message) return new CliData(CliActionSelector.CLI_LIST_SFK_DETECTOR, message);
        return new CliData(CliActionSelector.CLI_LIST_SFK_DETECTOR);
    }

    /**
     * Test if the user asks for the list of available output add-ons for tables.
     *
     * @return <ul>
     *              <li>If the user asks for the list of available output add-ons, then the method returns an instance of CliData.
     *                  The value of the member "type" of the returned object is CliConstantes.CLI_LIST_OUTPUT_TARGETS.</li>
     *              <li>Otherwise, the method returns the value null.</li>
     *         </ul>
     */
    @SuppressWarnings("unused")
    private static CliData __tryAddOnOutputTable()
    {
        if (0 != CliParser.__vargs[0].compareTo(CliParser.CS_LIST_OUTPUT_TABLE)) return null;
        String message = CliParser.__checkCliEmpty();
        if (null != message) return new CliData(CliActionSelector.CLI_HELP, message);
        return new CliData(CliActionSelector.CLI_LIST_OUTPUT_TABLE_ADDONS);
    }

    /**
     * Test if the user wants to add a new profile to the profiles' repository.
     *
     * @return <ul>
     *              <li>If the user wants to add a new profile, then the method returns an instance of CliData.
     *                  <ul>
     *                      <li>The value of the member "type" of the returned object is CliConstantes.CLI_PROFILE_ADD.</li>
     *                      <li>The member "data" of the returned object contains the following keys:
     *                          <ul>
     *                              <li>"name": The name of the profile to add.</li>
     *                              <li>"input-target": The add-on's name.</li>
     *                              <li>"input-target-configuration": the profile's configuration (XML element).</li>
     *                          </ul>
     *                      </li>
     *                  </ul>
     *              </li>
     *              <li>Otherwise, the method returns the value null.</li>
     *         </ul>
     */
    @SuppressWarnings("unused")
    private static CliData __tryProfileAdd()
    {
        /**
         * This class contains the CLI options needed to add a new profile into the profiles' repository.
         * @author Denis Beurive
         */
        class CliOptionsProfileAdd extends OptionContainer
        {
            @Option(name = "-" + CliParser.CLI_INPUT_ADDON, handler = SpecialStringOptionHandler.class)
            private String __target = null;

            public String getTarget() { return this.__target; }
        }

        if (0 != CliParser.__vargs[0].compareTo(CliParser.CS_PROFILE_ADD)) return null;

        String profile_name            = null;
        String profile_target          = null;
        CliOptionsProfileAdd options   = new CliOptionsProfileAdd();
        OptionContainer options_target = null;

        // CliParser.__debug(CliParser.__vargs);

        // ------------------------------------------------------------------------
        // First, get the name of the new profile.
        // ------------------------------------------------------------------------

        if (CliParser.__vargs.length < 2)
        { return new CliData(CliActionSelector.CLI_PROFILE_ADD, "To add a new profile, you must specify the profile's name."); }
        profile_name = CliParser.__vargs[1];

        // Check the argument's value.
        if (profile_name.startsWith("-"))
        {
            return new CliData(CliActionSelector.CLI_PROFILE_ADD, "Arguments must not start with a dash (-). If an argument starts with a dash, then you should escape it. For example, \"-foo\" becomes \"\\-foo\".");
        }
        profile_name = profile_name.replaceFirst("^\\\\", "");

        // ------------------------------------------------------------------------
        // Then, find out the target.
        // ------------------------------------------------------------------------

        try
        {
            CmdLineParser parser = new CmdLineParser(options);
            parser.parseArgument(options.extractArgvForThisSet(CliParser.__vargs));

            // System.out.println("-");
            // CliParser.__debug(options.extractArgvForThisSet(CliParser.__vargs));
            // System.out.println("-");

            profile_target = options.getTarget();

            // Detect the lack of add-on's name.
            if (null == profile_target)
            return new CliData(CliActionSelector.CLI_PROFILE_ADD, "To add a new profile, you must specify the profile's add-on (-" + CliParser.CLI_INPUT_ADDON + " <add-on's name>).");
        }
        catch (CmdLineException e)
        {
            // Please note that this bloc should never be executed since we use
            // string options (which accepts any kind of characters' sequence).
            return new CliData(CliActionSelector.CLI_PROFILE_ADD, "It seems that you want to add a new profile. But something os wrong with the way you specify your request: " + e.getMessage());
        }

        // ------------------------------------------------------------------------
        // Now that we know the profile's add-on, we parse the command line
        // arguments for this specific add-on.
        // ------------------------------------------------------------------------

        try
        {
            CliParser.__init();
            Hashtable<String, Object> data = new Hashtable<String, Object>();
            Element configuration = null;

            // Get a CLI adapter for the requested add-on.
            AbstractCli cli = (AbstractCli) CliParser.__catalog.getAdaptor(profile_target, "Cli");

            options_target = cli.getOptionsContainer();

            // Parse the command line and extract the new profile's
            // configuration.
            configuration = cli.getConf(CliParser.__vargs, Boolean.TRUE, Boolean.TRUE);

            // Check for unexpected arguments.
            ArrayList<OptionContainer> olist = new ArrayList<OptionContainer>();
            olist.add(options);
            olist.add(options_target);
            ArrayList<String> shrinked_list = CliParser.__shrinkArgv(olist);
            if (0 != shrinked_list.size())
            { return new CliData(CliActionSelector.CLI_PROFILE_ADD, "Unexpected atgument(s): " + org.dbview.utils.Strings.join(shrinked_list, ", ") + "."); }

            // OK
            data.put("name", profile_name);
            data.put("input-target", profile_target);
            data.put("input-target-configuration", configuration);
            return new CliData(CliActionSelector.CLI_PROFILE_ADD, data);
        }
        catch (AddOnCatalogException e)
        {
            return new CliData(CliActionSelector.CLI_PROFILE_ADD, "An error occurred while processing the target's name \"" + profile_target + "\". The given name (" + profile_target + ") is probably not recognized. " + e.getMessage());
        }
        catch (Exception e)
        {
            return new CliData(CliActionSelector.CLI_PROFILE_ADD, e.getMessage());
        }
    }

    /**
     * Test if the user wants to delete a profile.
     *
     * @return <ul>
     *              <li>If the user wants to delete a profile, then the method returns an instance of CliData.
     *                  <ul>
     *                      <li>The value of the member "type" of the returned object is CliConstantes.CLI_PROFILE_REMOVE.</li>
     *                      <li>The member "data" of the returned object contains the following keys:
     *                          <ul>
     *                              <li>"name": The name of the profile to add.</li>
     *                          </ul>
     *                      </li>
     *                  </ul>
     *              </li>
     *              <li>Otherwise, the method returns the value null.</li>
     *         </ul>
     */
    @SuppressWarnings("unused")
    private static CliData __tryProfileDelete()
    {
        if (0 != CliParser.__vargs[0].compareTo(CliParser.CS_PROFILE_DELETE)) return null;
        if (2 != CliParser.__vargs.length) return new CliData(CliActionSelector.CLI_PROFILE_REMOVE, "In order to delete a profile, you must specify the name of the profile to delete.");

        String profile_name = CliParser.__vargs[1];

        // Check the argument's value.
        if (profile_name.startsWith("-")) { return new CliData(CliActionSelector.CLI_PROFILE_REMOVE, "Arguments must not start with a dash (-). If an argument starts with a dash, then you should escape it. For example, \"-foo\" becomes \"\\-foo\"."); }
        profile_name = profile_name.replaceFirst("^\\\\", "");

        Hashtable<String, Object> data = new Hashtable<String, Object>();
        data.put("name", profile_name);
        return new CliData(CliActionSelector.CLI_PROFILE_REMOVE, data);
    }

    /**
     * Test if the user wants to print a profile.
     *
     * @return <ul>
     *            <li>If the user wants to print a profile, then the method returns an instance of CliData.
     *                <ul>
     *                    <li>The value of the member "type" of the returned object is CliConstantes.CLI_PROFILE_SHOW.</li>
     *                    <li>The member "data" of the returned object contains the following keys:
     *                        <ul>
     *                           <li>"name": The name of the profile to add.</li>
     *                        </ul>
     *                    </li>
     *                </ul>
     *            </li>
     *            <li>Otherwise, the method returns the value null.</li>
     *         </ul>
     */
    @SuppressWarnings("unused")
    private static CliData __tryProfileShow()
    {
        if (0 != CliParser.__vargs[0].compareTo(CliParser.CS_PROFILE_SHOW)) return null;
        if (2 != CliParser.__vargs.length) return new CliData(CliActionSelector.CLI_PROFILE_SHOW, "In order to diplay a profile, you must specify the name of the profile to display.");

        String profile_name = CliParser.__vargs[1];

        // Check the argument's value.
        if (profile_name.startsWith("-")) { return new CliData(CliActionSelector.CLI_PROFILE_SHOW, "Arguments must not start with a dash (-). If an argument starts with a dash, then you should escape it. For example, \"-foo\" becomes \"\\-foo\"."); }
        profile_name = profile_name.replaceFirst("^\\\\", "");

        Hashtable<String, Object> data = new Hashtable<String, Object>();
        data.put("name", profile_name);
        return new CliData(CliActionSelector.CLI_PROFILE_SHOW, data);
    }

    /**
     * Test if the user wants to update a given profile.
     *
     * @return <ul>
     *             <li>If the user wants to update a profile, then the method returns an instance of CliData.
     *                 <ul>
     *                     <li>The value of the member "type" of the returned object is CliConstantes.CLI_PROFILE_UPDATE.</li>
     *                     <li>The member "data" of the returned object contains the following keys:
     *                         <ul>
     *                             <li>"name": The name of the profile to add.</li>
     *                             <li>"target"; The add-on's name.</li>
     *                             <li>"new_configuration": the new profile's configuration (XML element).</li>
     *                             <li>"old_configuration": the old profile's configuration (XML element).</li>
     *                         </ul>
     *                      </li>
     *                 </ul>
     *             </li>
     *             <li>Otherwise, the method returns the value null.</li>
     *         </ul>
     */
    @SuppressWarnings("unused")
    private static CliData __tryProfileUpdate()
    {
        if (0 != CliParser.__vargs[0].compareTo(CliParser.CS_PROFILE_UPDATE)) { return null; }

        String profile_target = null;
        String profile_name   = null;

        // ------------------------------------------------------------------------
        // First, extract the name of the profile to update.
        // ------------------------------------------------------------------------

        if (CliParser.__vargs.length < 2) { return new CliData(CliActionSelector.CLI_PROFILE_UPDATE, "To update a rofile, you must specify the profile's name."); }
        profile_name = CliParser.__vargs[1];

        // Check the argument's value.
        if (profile_name.startsWith("-")) { return new CliData(CliActionSelector.CLI_PROFILE_UPDATE, "Arguments must not start with a dash (-). If an argument starts with a dash, then you should escape it. For example, \"-foo\" becomes \"\\-foo\"."); }
        profile_name = profile_name.replaceFirst("^\\\\", "");

        // ------------------------------------------------------------------------
        // Now get the profile's add-on.
        // ------------------------------------------------------------------------

        AbstractConfiguration profile_conf = null;
        try
        {
            profile_conf = ProfilesRepository.getProfileInstance(profile_name);
            if (null == profile_conf) { return new CliData(CliActionSelector.CLI_PROFILE_UPDATE, "The profile's name you specified (" + profile_name + ") can not be found in the profiles' repository."); }
            profile_target = profile_conf.getAddOnName();
        }
        catch (Exception e)
        {
            return new CliData(CliActionSelector.CLI_PROFILE_UPDATE, "Can not get the current profile's data from the profiles' repository. The profiles' repository may be screwed up! " + e.getMessage());
        }

        // ------------------------------------------------------------------------
        // Now that we know the profile's add-on, we find out the list of
        // command
        // line arguments for this add-on.
        // ------------------------------------------------------------------------

        try
        {
            CliParser.__init();
            Element configuration = null;

            // Get a CLI adapter for the add-on.
            AbstractCli cli = (AbstractCli) CliParser.__catalog.getAdaptor(profile_target, "Cli");

            // Parse the command line and extract the new profile's
            // configuration.
            // Parsing, in this case, is not strict.
            configuration = cli.getConf(CliParser.__vargs, Boolean.FALSE, Boolean.FALSE);

            // Check for unexpected arguments.
            OptionContainer options_target = cli.getOptionsContainer();
            ArrayList<OptionContainer> olist = new ArrayList<OptionContainer>();
            olist.add(options_target);
            ArrayList<String> shrinked_list = CliParser.__shrinkArgv(olist);
            if (0 != shrinked_list.size())
            { return new CliData(CliActionSelector.CLI_PROFILE_UPDATE, "Unexpected atgument(s): " + org.dbview.utils.Strings.join(shrinked_list, ", ") + "."); }

            // OK
            Hashtable<String, Object> data = new Hashtable<String, Object>();
            data.put("name", profile_name);
            data.put("target", profile_target);
            data.put("new_configuration", configuration);
            data.put("old_configuration", profile_conf.toXml());
            return new CliData(CliActionSelector.CLI_PROFILE_UPDATE, data);
        }
        catch (AddOnCatalogException e)
        {
            return new CliData(CliActionSelector.CLI_PROFILE_UPDATE, "An error occurred while processing the profile's which name \"" + profile_name + "\". The profile's add-on (" + profile_target + ") is probably not recognized anymore!. " + e.getMessage());
        }
        catch (Exception e)
        {
            return new CliData(CliActionSelector.CLI_PROFILE_UPDATE, e.getMessage());
        }
    }

    /**
     * This method tests if the user wants to list all the profiles stored in
     * the repository.
     *
     * @return <ul>
     *              <li>If the user wants to list all the profiles, then the method returns an instance of CliData.
     *                  <ul>
     *                      <li>The value of the member "type" of the returned object is CliConstantes.CLI_PROFILE_LIST.</li>
     *                  </ul>
     *              </li>
     *              <li>Otherwise, the method returns the value null.</li>
     *         </ul>
     */
    @SuppressWarnings("unused")
    private static CliData __tryProfileList()
    {
        if (0 != CliParser.__vargs[0].compareTo(CliParser.CS_PROFILE_LIST)) return null;
        return new CliData(CliActionSelector.CLI_PROFILE_LIST);
    }

    /**
     * This method tests if the user wants to process a given database.
     *
     * @return <ul>
     *            <li>If the user wants to process a given database, then the method returns an instance of CliData.
     *                <ul>
     *                    <li>The value of the member "type" of the returned object is CliConstantes.CLI_EXPORT.</li>
     *                </ul>
     *            </li>
     *            <li>Otherwise, the method returns thevalue null</li>
     *         </ul>
     */
    @SuppressWarnings("unused")
    private static CliData __tryExport()
    {
        /**
         * This class contains the required configuration used to parse the options that represents the list of tables to zoom in.
         * @author Denis BEURIVE
         */
        class ZoomOptions extends OptionContainer
        {
            /**
             * List of tables to zoom at.
             */
            @Option(name= "-" + CliParser.CLI_ZOOM, handler=CommaSeparatedStripedListOptionHandler.class)
            private String __zoom = null;

            /**
             * Zoom lebel.
             */
            @Option(name= "-" + CliParser.CLI_ZOOM_LEVEL)
            private Integer __zoom_level = null;

            /**
             * This method returns the list of tables to zoom in.
             * @return The method returns the list of tables to zoom in.
             */
            public ArrayList<String> getZoom()
            {
                if (null == this.__zoom) { return null; }

                String[] values = this.__zoom.split(",");
                ArrayList<String> result = new ArrayList<String>();
                for (String s: values) { result.add(s); }
                return result;
            }

            /**
             * This method returns the zoom level to apply (to a list of tables).
             * @return The method returns the zoom level to apply (to a list of tables).
             */
            public Integer getZoomLevel()
            {
                return this.__zoom_level;
            }
        }

        /**
         * This class represents the required configuration used to parse the options that defined a path.
         * @author Denis Beurive
         */
        class PathOptions extends OptionContainer
        {
            /**
             * This option represents the two extremities of the path.
             */
            @Option(name= "-" + CliParser.CLI_BETWEEN, handler=CommaSeparatedStripedListOptionHandler.class)
            private String __between = null;

            /**
             * This option represents the maximum number of path to calculate.
             */
            @Option(name= "-" + CliParser.CLI_LIMIT)
            private Integer __limit = null;

            /**
             * Return the two extremities of the path to calculate.
             * @return Return the two extremities of the path to calculate.
             */
            public ArrayList<String> getBetween()
            {
                if (null == this.__between) { return null; }

                String[] values = this.__between.split(",");
                ArrayList<String> result = new ArrayList<String>();
                for (String s: values) { result.add(s); }
                return result;
            }

            /**
             * Return the maximum number of paths to calculate.
             * @return Return the maximum number of paths to calculate.
             */
            public Integer getLimit()
            { return this.__limit; }
        }

        if (0 != CliParser.__vargs[0].compareTo(CliParser.CS_EXPORT)) return null;

        String profile_name                                 = null; // May be null, if the connection's profile is included within the command line.
        String input_addon_name                             = null; // May be null (if a profile's name is specified).
        String output_addon_name                            = null;
        Element xml_input_configuration                     = null;
        Element xml_output_configuration                    = null;
        ArrayList<String> zoom_list                         = null;
        Integer zoom_level                                  = null;
        ArrayList<String> between                           = null;
        String path_from                                    = null;
        String path_to                                      = null;
        Integer path_limit                                  = null;
        Hashtable<String, Object> data                      = new Hashtable<String, Object>();
        CliOptionsIOTargets options_targets                 = new CliOptionsIOTargets();
        CliOptionsProfileName options_profile_name          = new CliOptionsProfileName();
        OptionContainer options_input_cli                   = new OptionContainer();
        OptionContainer options_output_cli                  = new OptionContainer();

        // ------------------------------------------------------------------------
        // First, extract the name of the input and output add-ons.
        // SET input_target_name:
        // output_target_name: Necessarily set.
        // ------------------------------------------------------------------------

        try
        {
            CmdLineParser parser = new CmdLineParser(options_targets);
            parser.parseArgument(options_targets.extractArgvForThisSet(CliParser.__vargs));

            input_addon_name  = options_targets.getInputTarget();
            output_addon_name = options_targets.getOutputTarget();

            // Make sure that the mandatory option is set.
            if (null == output_addon_name) { return new CliData(CliActionSelector.CLI_EXPORT, "In order to process a database, you must, at least, indicate the name of the output add-on's adaptor (-" + CliParser.CLI_OUTPUT_ADDON + " <add-on's name>). And, optionally, you may specify the name of an input add-on (-" + CliParser.CLI_INPUT_ADDON + " <add-on's name>)."); }
        }
        catch (CmdLineException e)
        {
            // Please note that this bloc should never be executed since we use
            // string options (which accepts any kind of characters' sequence).
            return new CliData(CliActionSelector.CLI_EXPORT, "It seems that you want to export a database. But something is wrong with the way you specify your request: " + e.getMessage());
        }

        // ------------------------------------------------------------------------
        // Get the input add-on's configuration from a profile or from the
        // command line.
        // ------------------------------------------------------------------------

        if (null == input_addon_name)
        {
            // No input add-on name is specified. I assume that a profile's name is given.
            try
            {
                CmdLineParser parser = new CmdLineParser(options_profile_name);
                parser.parseArgument(options_profile_name.extractArgvForThisSet(CliParser.__vargs));

                profile_name = options_profile_name.getProfileName();

                if (null == profile_name)
                return new CliData(CliActionSelector.CLI_EXPORT, "In order to process a given database, you must specify a profile's name (-" + CliParser.CLI_PROFILE + " <name of the profile>), or a set of input adaptor configurations's parameters (-" + CliParser.CLI_INPUT_ADDON + " <...> ...). ");

                // Get the configuration for the requested profile's name.
                ProfilesRepository.init();
                AbstractConfiguration conf = ProfilesRepository.getProfileInstance(profile_name);
                if (null == conf)
                {
                    return new CliData(CliActionSelector.CLI_EXPORT, "The profile's name you specified (" + profile_name + ") can not be found in the profiles' repository.");
                }

                xml_input_configuration = conf.toXml().getChild("data");
                if (null == xml_input_configuration)
                {
                    return new CliData(CliActionSelector.CLI_EXPORT, "The profile's you specified (" + profile_name + ") is not valid. It does not contain any \"data\" tag!");
                }

                input_addon_name = conf.getAddOnName();
            }
            catch (CmdLineException e)
            {
                // Please note that this bloc should never be executed since we
                // use string options (which accepts any kind of characters'
                // sequence).
                return new CliData(CliActionSelector.CLI_EXPORT, "It seems that you want to export a database. But something is wrong with the way you specify your request: " + e.getMessage());
            }
            catch (JDOMException e)
            {
                return new CliData(CliActionSelector.CLI_EXPORT, "It seems that you want to export a database. But it seems that your profiles' repository is screwed up: " + e.getMessage());
            }
            catch (Exception e)
            {
                return new CliData(CliActionSelector.CLI_EXPORT, "It seems that you want to export a database. An unexpected error occured: " + e.getMessage());
            }
        }
        else
        {
            // A add-on name is given. I assume that an input add-on
            // configuration is given through the command line.
            try
            {
                CliParser.__init();

                // Get a CLI adapter for the add-on.
                AbstractCli cli = (AbstractCli) CliParser.__catalog.getAdaptor(input_addon_name, "Cli");

                // Get the options' container, that will be used later (to look for unexpected arguments).
                options_input_cli = cli.getOptionsContainer();

                // Parse the command line and extract the new profile's
                // configuration.
                xml_input_configuration = cli.getConf(CliParser.__vargs, Boolean.TRUE, Boolean.TRUE);
            }
            catch (AddOnCatalogException e)
            {
                return new CliData(CliActionSelector.CLI_EXPORT, "An error occurred while extracting input add-on's configuration from the command line. " + e.getMessage());
            }
            catch (Exception e)
            {
                return new CliData(CliActionSelector.CLI_EXPORT, e.getMessage());
            }
        }

        // ------------------------------------------------------------------------
        // Now, get the configuration for the output add-on.
        // ------------------------------------------------------------------------

        try
        {
            OutputCatalog out = new OutputCatalog();
            AbstractCli cli = (AbstractCli) out.getAdaptor(output_addon_name, "Cli");

            // Get the options' container, that will be used later (to look for unexpected arguments).
            options_output_cli = cli.getOptionsContainer();

            xml_output_configuration = cli.getConf(CliParser.__vargs, Boolean.TRUE, Boolean.TRUE);
            // System.out.println(org.dbview.utils.Jdom.print(output_configuration));
        }
        catch (CliException e)
        {
            return new CliData(CliActionSelector.CLI_EXPORT, "It seems that you want to export a database. But something is wrong with the way you specify the output add-on's adaptor configuration : " + e.getMessage());
        }
        catch (AddOnCatalogException e)
        {
            return new CliData(CliActionSelector.CLI_EXPORT, "It seems that you want to export a database. An error occurred while using the exporter \"" + output_addon_name + "\" : " + e.getMessage());
        }
        catch (Exception e)
        {
            return new CliData(CliActionSelector.CLI_EXPORT, e.getMessage());
        }

        // ------------------------------------------------------------------------
        // OK... Shall we zoom?
        // ------------------------------------------------------------------------

        ZoomOptions options_zoom  = new ZoomOptions();
        CmdLineParser zoom_parser = new CmdLineParser(options_zoom);

        try
        {
            zoom_parser.parseArgument(options_zoom.extractArgvForThisSet(CliParser.__vargs));
        }
        catch (CmdLineException e)
        {
            return new CliData(CliActionSelector.CLI_EXPORT, e.getMessage());
        }

        zoom_list  = options_zoom.getZoom();
        zoom_level = options_zoom.getZoomLevel();

        if (null == zoom_list)
        if (null != zoom_level)
        { return new CliData(CliActionSelector.CLI_EXPORT, "Option -" + CliParser.CLI_ZOOM_LEVEL + " can not be used alone. You must specify a list of tables to zoom in."); }

        // ------------------------------------------------------------------------
        // OK... Shall calculate a path?
        // ------------------------------------------------------------------------

        PathOptions options_path  = new PathOptions();
        CmdLineParser path_parser = new CmdLineParser(options_path);

        try
        {
            path_parser.parseArgument(options_path.extractArgvForThisSet(CliParser.__vargs));
        }
        catch (CmdLineException e)
        {
            return new CliData(CliActionSelector.CLI_EXPORT, e.getMessage());
        }

        between    = options_path.getBetween();
        path_limit = options_path.getLimit();

        if (null == between)
        if (null != path_limit)
        { return new CliData(CliActionSelector.CLI_EXPORT, "Option -" + CliParser.CLI_LIMIT + " can not be used alone. You must specify a path to calculate."); }

        if (null != between)
        if (between.size() != 2)
        { return new CliData(CliActionSelector.CLI_EXPORT, "Option -" + CliParser.CLI_BETWEEN + " defines two table (<name of a table>,<name of a table>)."); }

        // ------------------------------------------------------------------------
        // Make sure that the "zoom" option and the "path" option are not defined
        // simultaneously.
        // ------------------------------------------------------------------------

        if ((null != zoom_list) && (null != between))
        { return new CliData(CliActionSelector.CLI_EXPORT, "You can not define simultaneously a list of tables to zoom in and a path to calculate."); }

        // Check for unexpected arguments.
        ArrayList<OptionContainer> olist = new ArrayList<OptionContainer>();
        olist.add(options_targets);
        olist.add(options_profile_name);
        olist.add(options_input_cli);
        olist.add(options_output_cli);
        olist.add(options_path);
        olist.add(options_zoom);

        ArrayList<String> shrinked_list = CliParser.__shrinkArgv(olist);
        if (0 != shrinked_list.size())
        { return new CliData(CliActionSelector.CLI_EXPORT, "Unexpected atgument(s): " + org.dbview.utils.Strings.join(shrinked_list, ", ") + "."); }

        // ------------------------------------------------------------------------
        // OK, we have everything. Let's return.
        // ------------------------------------------------------------------------

        if (null != between)
        {
            data.put("path-between", between);
            data.put("path-limit",   null == path_limit ? 1 : path_limit);
        }

        if (null != zoom_list)
        {
            data.put("zoom-list",  zoom_list);
            data.put("zoom-level", null == zoom_level ? 0 : zoom_level);
        }

        data.put("input-target-name",    input_addon_name);          // String
        data.put("output-target-name",   output_addon_name);         // String
        data.put("input-configuration",  xml_input_configuration);   // Element
        data.put("output-configuration", xml_output_configuration);  // Element

        // DEBUG
        // System.out.println("__tryExport done");

        return new CliData(CliActionSelector.CLI_EXPORT, data);
    }

    /**
     * This method makes sure that the command line does not contain any command line parameter after the command selector.
     *
     * @return <ul>
     *             <li>If the command line does not contain any command line parameter after the command selector, then the method returns the value null.</li>
     *             <li>Otherwise, the method returns a string that represents the error message.</li>
     *         </ul>
     * @throws Exception
     */
    private static String __checkCliEmpty()
    {
        if (1 < CliParser.__vargs.length)
        {
            ArrayList<String> m = new ArrayList<String>();
            for (int i = 1; i < CliParser.__vargs.length; i++)
            {
                m.add(CliParser.__vargs[i]);
            }
            return "Unexpected command line arguments (" + org.dbview.utils.Strings.join(m, ", ") + ").";
        }
        return null;
    }

    /**
     * This method extracts the following argument from the command line:
     * <ul>
     *      <li>The command selector.</li>
     *      <li>The non option argument that may follow the command selector (until the first option is reached)</li>
     *      <li>The option's arguments given by the list of given options' containers.</li>
     * </ul>
     * @param in_options The list of options' containers.
     * @return The method returns an array of command line arguments.
     */
    private static ArrayList<String> __shrinkArgv(ArrayList<OptionContainer> in_options)
    {
        ArrayList<String> res = new ArrayList<String>(Arrays.asList(CliParser.__vargs));

        // First, remove all non option's argument at the beginning of the command line.
        // System.out.println(res.size());
        for (String arg: CliParser.__vargs)
        {
            // System.out.println("\t> " + arg);
            if (arg.startsWith("-")) { break; }
            res.remove(0);
        }
        // System.out.println(res.size());

        // Then, remove all *required* options.
        for (OptionContainer option: in_options)
        {
            option.extractArgvForThisSet(res.toArray(new String[res.size()]));

            // String[] os = option.shrink();
            // System.out.println("> " + os.length);

            res = new ArrayList<String>( Arrays.asList(option.shrink()));

        }

        return res;
    }

    /**
     * Print the command line.
     * @param in_argv The command line.
     * @remark This method is used for debug only.
     */
    @SuppressWarnings("unused")
    private static void __debug(String in_argv[])
    {
        for (String arg: in_argv)
        {
            System.out.println("\t> " + arg);
        }
    }
}

// ---------------------------------------------------------------------------------
// The following classes define generic option's classes.
// These options may be used by several parsing routines.
//----------------------------------------------------------------------------------

/**
 * This class contains the CLI option used to extract a profile's name from the command line.
 * @remark This class is designed to be used with the library Args4j.
 * @remark This class is not public.
 * @author Denis Beurive
 */
class CliOptionsProfileName extends OptionContainer
{
    /** Name of the profile. */
    @Option(name = "-" + CliParser.CLI_PROFILE, handler = SpecialStringOptionHandler.class)
    private String __name = null;

    /**
     * Returns the name of the profile.
     * @return Returns the name of the profile.
     */
    public String getProfileName()
    {
        return this.__name;
    }
}

/**
 * This class contains the CLI options needed to proceed to an export.
 * It defines options for an input and an output add-on.
 * @remark This class is designed to be used with the library Args4j.
 * @remark This class is not public.
 * @author Denis Beurive
 */
class CliOptionsIOTargets extends OptionContainer
{
    /** Name of the input add-on. */
    @Option(name = "-" + CliParser.CLI_INPUT_ADDON, handler = SpecialStringOptionHandler.class)
    private String __input_target  = null;

    /** Name of the output add-on. */
    @Option(name = "-" + CliParser.CLI_OUTPUT_ADDON, handler = SpecialStringOptionHandler.class)
    private String __output_target = null;

    /**
     * Returns the name of the input add-on.
     * @return Returns the name of the input add-on.
     */
    public String getInputTarget() { return this.__input_target; }

    /**
     * Returns the name of the output add-on.
     * @return Returns the name of the output add-on.
     */
    public String getOutputTarget() { return this.__output_target; }
}





