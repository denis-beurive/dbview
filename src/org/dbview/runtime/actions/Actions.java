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

package org.dbview.runtime.actions;

import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;
import java.io.*;
import org.jdom.Element;
import org.dbview.adapter.AbstractCli;
import org.dbview.adapter.AbstractDescription;
import org.dbview.adapter.CliParameter;
import org.dbview.db.structure.*;
import org.dbview.input_addons.*;
import org.dbview.output_addons.*;
import org.dbview.resources.*;
import org.dbview.utils.Strings;
import org.jgrapht.GraphPath;
import org.jgrapht.UndirectedGraph;
import org.jgrapht.alg.KShortestPaths;

/**
 * This class implements all action defined by the command line interface.
 *
 * @author Denis Beurive
 */

public class Actions
{

    /**
     * This method lists all input targets.
     *
     * @throws Exception
     */
    public static void listInputTargets() throws Exception
    {
        InputCatalog catalog = new InputCatalog();
        Enumeration<String> e = catalog.getTargets();
        while (e.hasMoreElements())
        {
            System.out.println("\t" + e.nextElement());
        }
    }

    /**
     * This method lists all foreign key matchers.
     * @throws Exception
     */
    public static void listFkMatchers() throws Exception
    {
        SotfForeignKeyDetectorCatalog catalogue = new SotfForeignKeyDetectorCatalog();
        Enumeration<String> e = catalogue.getMatchers();
        while (e.hasMoreElements())
        {
            String matcher_name = e.nextElement();
            String desc = catalogue.getFkMatcherDescriptionByName(matcher_name);
            System.out.println("\t" + SotfForeignKeyDetectorCatalog.toCli(matcher_name));
            System.out.println(org.dbview.utils.Strings.indent("\t", desc));
            System.out.println();
        }
        System.out.println("Please note that, if you don't find the soft foreign key detector that you need, you can easily create a new one, and add it to the list. Only very basic knowledge in Java is required for this operation. Basically, you write one class, and you reccompile the software (usng the provided ANT specification). That's it. Look at the online documentation for an example.");

        System.out.println();
    }

    /**
     * This method prints the help for a specific input add-on.
     * @param in_args This hash table contains the parameters required for printing the help for a specific input add-on.
     *        Keys are:
     *        <ul>
     *          <li>name; Name of the input add-on.</li>
     *        </ul>
     *
     * @throws Exception
     */
    public static void helpInputAddOn(Hashtable<String, Object> in_args) throws Exception
    {
        String add_on_name = (String)in_args.get("name");

        InputCatalog catalog = new InputCatalog();
        AbstractDescription description = (AbstractDescription) catalog.getAdaptor(add_on_name, "Description");
        AbstractCli cli = (AbstractCli) catalog.getAdaptor(add_on_name, "Cli");

        System.out.println(description.brief());
        ArrayList<CliParameter> opt = cli.getOptions();
        System.out.println("Parameters:");
        for (CliParameter param : opt)
        {
            System.out.println("\t" + param.parameter + " (" + (param.mandatory ? "mandatory" : "optional") + ")");
            // System.out.println("\ttype:\t\t" +
            // CliParameterTypes.toString(param.type));
            // System.out.println("\tmandatory:\t" + (param.mandatory ? "TRUE" :
            // "FALSE"));
            // System.out.println("\tdescription:\t" + param.description);
            // System.out.println();
        }
    }

    /**
     * This method prints the help for a specific output target.
     * @param in_args This hash table contains the parameters required for printing the help for a specific output add-on.
     *        Keys are:
     *        <ul>
     *          <li>name: Name of the output add-on.
     *        </ul>
     *
     * @throws Exception
     */
    public static void helpOutputAddOn(Hashtable<String, Object> in_args) throws Exception
    {
        String add_on_name = (String)in_args.get("name");

        OutputCatalog catalog = new OutputCatalog();
        AbstractDescription description = (AbstractDescription) catalog.getAdaptor(add_on_name, "Description");
        AbstractCli cli = (AbstractCli) catalog.getAdaptor(add_on_name, "Cli");

        System.out.println(description.brief());
        ArrayList<CliParameter> opt = cli.getOptions();
        System.out.println("Parameters:");
        for (CliParameter param : opt)
        {
            System.out.println("\t" + param.parameter + " (" + (param.mandatory ? "mandatory" : "optional") + ")");
            // System.out.println("\ttype:\t\t" +
            // CliParameterTypes.toString(param.type));
            // System.out.println("\tmandatory:\t" + (param.mandatory ? "TRUE" :
            // "FALSE"));
            // System.out.println("\tdescription:\t" + param.description);
            // System.out.println();
        }
    }

    /**
     * This method lists all output targets.
     *
     * @throws Exception
     */
    public static void listOutputTargets() throws Exception
    {
        OutputCatalog catalog = new OutputCatalog();
        Enumeration<String> e = catalog.getTargets();
        while (e.hasMoreElements())
        {
            System.out.println("\t" + OutputCatalog.toCli(e.nextElement()));
        }
    }

    /**
     * This method adds a new profile to the repository.
     *
     * @param in_args This hash table contains the parameters required for updating a profile.
     *        Keys are:
     *        <ul>
     *          <li>name: Name of the new profile to add.</li>
     *          <li>input-target: Profile's target.</li>
     *          <li>input-target-configuration: XML document, generated by the CLI parser, that represents the profile.</li>
     *        </ul>
     *
     * @throws Exception
     */
    public static void addProfile(Hashtable<String, Object> in_args) throws Exception
    {
        String profile_name   = (String)in_args.get("name");
        String target_name    = (String)in_args.get("input-target");
        Element configuration = (Element)in_args.get("input-target-configuration");

        InputCatalog catalog = new InputCatalog();
        AbstractConfiguration profile = (AbstractConfiguration) catalog.getAdaptor(target_name, "Configuration");
        profile.fromXml(configuration, target_name, profile_name);
        // System.out.println(profile.printToXml());
        ProfilesRepository.add(profile);
    }

    /**
     * This method removes a profile from the repository.
     * @param in_args This hash table contains the parameters required for updating a profile.
     *        Keys are:
     *        <ul>
     *          <li>name: Name of the profile to remove.</li>
     *        </ul>

     * @throws Exception
     */
    // public static void removeProfile(String in_profile_name) throws Exception
    public static void removeProfile(Hashtable<String, Object> in_args) throws Exception
    {
        String profile_name = (String)in_args.get("name");
        ProfilesRepository.remove(profile_name);
    }

    /**
     * This method removes a profile from the repository.
     * @param in_args This hash table contains the parameters required for updating a profile.
     *        Keys are:
     *        <ul>
     *          <li>name: Name of the profile to upddate.</li>
     *          <li>target: Name of the profile's target.</li>
     *          <li>new_configuration: XML document, generated by the CLI parser, that represents the new profile.</li>
     *          <li>old_configuration: XML document, generated by the CLI parser, that represents the old profile.</li>
     *        </ul>
     *
     * @throws Exception
     */
    public static void updateProfile(Hashtable<String, Object> in_args) throws Exception

    {
        String profile_name       = (String)in_args.get("name");
        String target_name        = (String)in_args.get("target");
        Element new_configuration = (Element)in_args.get("new_configuration");
        Element old_configuration = (Element)in_args.get("old_configuration");

        // try { System.out.println("New profile:");
        // System.out.println(org.dbview.utils.Jdom.print(in_new_configuration));
        // System.out.println("Old profile:");
        // System.out.println(org.dbview.utils.Jdom.print(in_old_configuration));
        // } catch (Exception e) { }

        // Build the new version of the profile...
        Element old_data = old_configuration.getChild("data");
        @SuppressWarnings("unchecked")
        List<Element> params = (List<Element>)new_configuration.getChildren();
        for (int i = 0; i < params.size(); i++)
        {
            Element param = params.get(i);
            String name = param.getName();
            Element e = old_data.getChild(name);
            if (null == e)
            {
                throw new Exception("Unexpected error : the specified parameter \"" + name + "\" could not be found in the current configuration. Please note that this error should not happen unless you modify the software.");
            }
            try
            {
                e.setText(param.getText());
            }
            catch (Exception ex)
            {
                throw new Exception("Unexpected error : " + ex.getMessage());
            }
        }

        // try { System.out.println("Profile to inject into repository:");
        // System.out.println(org.dbview.utils.Jdom.print(in_old_configuration));
        // } catch (Exception e) { }

        // Update the repository with the new version of the profile.
        try
        {
            InputCatalog c = new InputCatalog();
            AbstractConfiguration new_conf = (AbstractConfiguration) c.getAdaptor(target_name, "Configuration");
            new_conf.fromXml(old_configuration, null, null);
            ProfilesRepository.update(new_conf);
        }
        catch (Exception ex)
        {
            throw new Exception("Unexpected error: " + ex.getMessage() + " " + ex.getClass().getName());
        }
    }

    /**
     * This method prints a given profile.
     * @param in_args This hash table contains the parameters required for showing a profile.
     *        Keys are:
     *        <ul>
     *          <li>name: Name of the profile to show.</li>
     *        </ul>
     * @throws Exception
     */
    public static void showProfile(Hashtable<String, Object> in_args) throws Exception

    {
        String name = (String)in_args.get("name");
        ProfilesRepository.init();
        AbstractConfiguration profile_configuration = ProfilesRepository.getProfileInstance(name);
        if (null == profile_configuration)
        {
            throw new Exception("The profile \"" + name + "\" does not exist.");
        }

        ArrayList<String[]> data = profile_configuration.toStrings();
        ArrayList<String> fields = new ArrayList<String>();
        for (int i = 0; i < data.size(); i++)
        {
            fields.add(data.get(i)[0]);
        }
        Strings.margingRight(fields, 2);
        for (int i = 0; i < data.size(); i++)
        {
            System.out.println("\t" + fields.get(i) + data.get(i)[1]);
        }
    }

    /**
     * This method prints the list of profile's names stored in the repository.
     *
     * @throws Exception
     */
    public static void listProfiles() throws Exception
    {
        ArrayList<String> profiles = null;

        ProfilesRepository.init();
        profiles = ProfilesRepository.getProfilesNames();
        for (int i = 0; i < profiles.size(); i++)
        {
            System.out.println(profiles.get(i));

            Hashtable<String, Object> params = new Hashtable<String, Object>();
            params.put("name", profiles.get(i));
            Actions.showProfile(params);
            System.out.println();
        }
    }

    /**
     * This method exports a given database to a given output format.
     * @param in_args his hash table contains the parameters for the export.
     *        Keys are:
     *        <ul>
     *          <li>path-between: If the user request the calculation of a path, this array contains the names of the tables at the extremities.</li>
     *          <li>path-limit: If the user request the calculation of a path, this integer represents the maximum number of paths.</li>
     *          <li>zoom-list: If the user request a zoom around tables, this array contains the list of tables to zoom in.</li>
     *          <li>zoom-level: If the user request a zoom around tables, this integer represents the neighbourhood area.</li>
     *          <li>input-target-name: Name of the add-on that is used to connect to a database.</li>
     *          <li>output-target-name: Name of the add-on used to export the database.</li>
     *          <li>input-configuration: Specific configuration for the input add-on.</li>
     *          <li>output-configuration: Specific configuration for the output add-on.</li>
     *        </ul>
     *
     * @throws Exception
     */

    public static void export(Hashtable<String, Object> in_args) throws Exception
    {
        @SuppressWarnings("unchecked")
        ArrayList<String> path_between  = in_args.containsKey("path-between") ? (ArrayList<String>)in_args.get("path-between") : null;
        Integer path_limit              = in_args.containsKey("path-limit")   ? (Integer)in_args.get("path-limit") : null;

        @SuppressWarnings("unchecked")
        ArrayList<String> zoom_list     = in_args.containsKey("zoom-list")  ? (ArrayList<String>)in_args.get("zoom-list") : null;
        Integer zoom_level              = in_args.containsKey("zoom-level") ? (Integer)in_args.get("zoom-level") : null;

        String  input_target            = (String)in_args.get("input-target-name");
        String  output_target           = (String)in_args.get("output-target-name");
        Element input_configuration     = (Element)in_args.get("input-configuration");
        Element in_output_confguration  = (Element)in_args.get("output-configuration");


        // Create the catalogues.
        InputCatalog in_catalog   = new InputCatalog();
        OutputCatalog out_catalog = new OutputCatalog();

        // First get the "loader" adapter for the requested input target.
        AbstractLoader loader = (AbstractLoader) in_catalog.getAdaptor(input_target, "Loader");

        // Then, get the "exporter" adaptor for the requested output target.
        AbstractExporter exporter = (AbstractExporter) out_catalog.getAdaptor(output_target, "Exporter");

        // System.out.println("OK");

        // Load the database.
        Database db = loader.load(input_configuration);
        // System.out.println("DB loaded!");
        
        // Get the list of tables to export.
        Object representation;
        if (null != zoom_list)
        {
            ArrayList<Table> tables = db.zoomAroundByNames(zoom_list, zoom_level);
            representation = exporter.export(db, in_output_confguration, tables);
        }
        else
        {
            if (null != path_between)
            {
                ArrayList<Object> lr   = new ArrayList<Object>();
                String from_table_name = path_between.get(0);
                String to_table_name   = path_between.get(1);

                // Make sure that the given table's names exist in the loaded database.
                if (null == db.getTable(from_table_name))
                { throw new Exception("The name of the table you specified as starting point of the path (\"" + from_table_name + "\") does not exist in the database."); }

                if (null == db.getTable(to_table_name))
                { throw new Exception("The name of the table you specified as ending point of the path (\"" + to_table_name + "\") does not exist in the database."); }

                // Get the JgraphT object that represents the database.
                UndirectedGraph<Table, JgraphtTableToTableEdge> g = db.jgrapht(null);

                // Calculate the shortest path(es) between the two specified tables.
                KShortestPaths<Table, JgraphtTableToTableEdge> pathesFinder = new KShortestPaths<Table, JgraphtTableToTableEdge>(g, db.getTable(from_table_name), path_limit);
                List <GraphPath<Table, JgraphtTableToTableEdge>> pathes = pathesFinder.getPaths(db.getTable(to_table_name));
                Iterator<GraphPath<Table, JgraphtTableToTableEdge>> pathesIterator = pathes.iterator();

                while (pathesIterator.hasNext())
                {
                    // Get the start and the end of the pathes (we already know)
                    GraphPath<Table, JgraphtTableToTableEdge> gr = pathesIterator.next();

                    // For memory:
                    // String StartVertex = gr.getStartVertex().getName();
                    // String EndVertex   = gr.getEndVertex().getName();

                    // Get the list of jumps.
                    List <JgraphtTableToTableEdge> edges = gr.getEdgeList();
                    Iterator<JgraphtTableToTableEdge> edgesItearator = edges.iterator();
                    ArrayList<Table> tables = new ArrayList<Table>();
                    while (edgesItearator.hasNext())
                    {
                        JgraphtTableToTableEdge edge = (JgraphtTableToTableEdge)edgesItearator.next();

                        Table src = edge.getSource();
                        Table dst = edge.getTarget();
                        if (! tables.contains(src)) { tables.add(src); }
                        if (! tables.contains(dst)) { tables.add(dst); }
                    }
                    lr.add((String)exporter.export(db, in_output_confguration, tables));
                }
            }
            else
            {
                ArrayList<Table> tables = db.getTables();
                representation = exporter.export(db, in_output_confguration, tables);
            }
        }

        // DEBUG
        // System.out.println("export() - exported...");
    }


}
