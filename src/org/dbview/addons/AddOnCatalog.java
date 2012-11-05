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

package org.dbview.addons;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.dbview.conf.Conf;
import org.dbview.utils.JavaVm;

/**
 * @class AddOnCatalog
 * The class is a base class for all add-on catalogues.
 * <ul>
 *    <li>An add-on is represented by a package.</li>
 *    <li>An add-on exports adaptors that are interfaces to the add-on's environment.</li>
 *    <li>An add-on's adaptors are located in the add-on's package.</li>
 *    <li>Add-ons are regrouped into JAR files.</li>
 *    <li>A catalogue lists all add-ons located in a given JAR file.</li>
 * </ul>
 * 
 * @author Denis Beurive
 */
public class AddOnCatalog
{
    /**
     * This hash table lists all add-ons declared in the given resource. Each
     * add-on is associated to its adaptors.
     * <ul>
     *    <li>Key: Name of the add-on.</li>
     *    <li>Value: See class AddDon. It defines the following data:
     *        <ul>
     *            <li>The name of the package in which the add-on is declared.</li>
     *            <li>The list of adaptors declared for the add-on.</li>
     *        </ul>
     *    </li>
     * </ul>
     */
    private Hashtable<String, AddOn> __addons = null;

    /**
     * This array contains the expected list of adaptors' names.
     * This array is used to check that all add-ons present all the mandatory adaptors.
     */
    private ArrayList<String> __adaptors_list = null;

    /**
     * Create a catalogue of add-ons.
     *
     * @param in_resource_name
     *            Name of the resource that contains the add-ons (this is a JAR).
     * @param in_adaptors_list
     *            List of adaptors that must be part of the add-on's package.
     * @param in_pattern
     *            Regular expression that is used to recognize an add-on.
     * @throws Exception
     */
    public AddOnCatalog(String in_resource_name,
            ArrayList<String> in_adaptors_list,
            Pattern in_pattern) throws Exception
    {
        this.__addons        = new Hashtable<String, AddOn>();
        this.__adaptors_list = in_adaptors_list;

        // Load the JAR file that contains all adapters.
        ArrayList<String> jar_content = JavaVm.listJar(Conf.get(in_resource_name));

        // Locate targets.
        // System.out.println(in_resource_name + " > " + jar_content.size());
        Iterator<String> i = jar_content.iterator();
        while (i.hasNext())
        {
            String entry = i.next().replaceAll("/|\\\\", ".");

            Matcher m_adaptor = in_pattern.matcher(entry);

            // Did we find an adaptor?
            if (m_adaptor.matches())
            {
                AddOn target = null;
                String target_package = m_adaptor.group(1);
                String target_name = m_adaptor.group(2);
                String target_adaptor = m_adaptor.group(3);

                if (! this.__adaptors_list.contains(target_adaptor)) { continue; }

                if (!this.__addons.containsKey(target_name))
                {
                    target = new AddOn();
                    target.package_name = target_package;
                    this.__addons.put(target_name, target);
                }
                else
                {
                    target = this.__addons.get(target_name);
                }

                target.adaptors_list.add(target_adaptor);
            }

            // The entry is not related to targets... skip it.
        }

        // Check that the target has all the mandatory adaptors.
        Enumeration<String> targets = this.__addons.keys();
        while (targets.hasMoreElements())
        {
            String target_name = targets.nextElement();
            if (!this.__isOk())
            {
                throw new Exception("Unexpected error. The add-on \"" + target_name + "\" is missing an adaptor! This error should not happen unless you modify the software.");
            }
        }

        // At this point, the catalog is built.
    }

    /**
     * The method produces a string that represents the catalogue.
     * @note This method is used to debug the application.
     * @return The method returns a string that represents the catalogue.
     */
    @Override
    public String toString()
    {
        try
        {
            String r = "";
            Enumeration<String> targets = this.getTargets();

            while (targets.hasMoreElements())
            {
                String target_name = targets.nextElement();
                AddOn target = this.__addons.get(target_name);
                String package_name = target.package_name;

                r = r.concat("[Target] " + target_name + System.getProperty("line.separator"));
                r = r.concat("\tPackage: " + package_name + System.getProperty("line.separator"));

                for (int i = 0; i < target.adaptors_list.size(); i++)
                {
                    String adaptator_name = target.adaptors_list.get(i);
                    r = r.concat("\t         - " + adaptator_name + System.getProperty("line.separator"));
                }
            }

            return r;
        }
        catch (Exception e)
        {
            return "Could not generate textual representation of the catalogue: " + e.getMessage();
        }
    }

    /**
     * This method returns the list of all add-ons found in the catalog.
     * @return This method returns the list of all add-ons found in the catalog.
     * @throws Exception
     */
    public Enumeration<String> getTargets() throws Exception
    {
        Enumeration<String> e = this.__addons.keys();
        ArrayList<String> r   = new ArrayList<String>();

        while (e.hasMoreElements()) { r.add(e.nextElement()); }
        return Collections.enumeration(r);
    }

    /**
     * Check the list of detected adaptors, for all add-ons in the catalogue.
     * @return If the list of detected adaptors is valid, then the function
     *         returns the value TRUE. Otherwise the function returns the value
     *         fALSE.
     */
    private Boolean __isOk()
    {
        for (String adaptor : this.__adaptors_list)
        {
            if (-1 == this.__adaptors_list.indexOf(adaptor))
            {
                return Boolean.FALSE;
            }
        }
        return Boolean.TRUE;
    }

    /**
     * This method returns the name of the package that contains adaptors'
     * implementations for a given add-on (that is: the add-on's package).
     *
     * @param in_add_on_name
     *            Name of the add-on.
     * @return This method returns the name of the package that contains
     *         adaptors' implementations for the given add-on.
     * @throws Exception
     */
    public String getPackage(String in_add_on_name) throws AddOnCatalogException
    {
        if (!this.__addons.containsKey(in_add_on_name))
        {
            throw new AddOnCatalogException("You try to get the name of a package (out of the catalog) for an add-on (\"" + in_add_on_name + "\") that does not exist!");
        }
        return this.__addons.get(in_add_on_name).package_name;
    }

    /**
     * This method returns an instance of a given adaptor's, for a given add-on.
     *
     * @param in_target_name
     *            Name of the add-on to which the adaptor belongs.
     *            Please note that this name may contain dashes.
     *            This string will be converted into "Lower Camel Case".
     * @param in_adaptor_name
     *            Name of the adaptor to create.
     * @return The method returns a new instance of the required adaptor.
     * @throws AddOnCatalogException
     */
    public Object getAdaptor(String in_target_name,
                             String in_adaptor_name) throws AddOnCatalogException
    {
        // Convert into Lower Camel Case ("a-bc-de" -> "aBcDe").
        String real_name = AddOnCatalog.fromCli(in_target_name);

        AddOn target = this.__addons.get(real_name);
        if (null == target)
        {
            throw new AddOnCatalogException("The requested add-on (\"" + in_target_name + "\") does not exist.");
        }
        String package_name = target.package_name;
        String class_name = package_name + "." + in_adaptor_name;

        try
        {
            // System.out.println("Name = " + class_name);
            return Class.forName(class_name).newInstance();
        }
        catch (ExceptionInInitializerError e)
        {
            throw new AddOnCatalogException("Unexpected error. This error should not happen, unless you modify the software (ExceptionInInitializerError). " + e.getClass().getName() + ": " + e.getMessage());
        }
        catch (ClassNotFoundException e)
        {
            throw new AddOnCatalogException("Unexpected error. This error should not happen, unless you modify the software (ClassNotFoundException). " + e.getClass().getName() + ": " + e.getMessage());
        }
        catch (LinkageError e)
        {
            throw new AddOnCatalogException("Unexpected error. This error should not happen, unless you modify the software (LinkageError). " + e.getClass().getName() + ": " + e.getMessage());
        }
        catch (IllegalAccessException e)
        {
            throw new AddOnCatalogException("Unexpected error. This error should not happen, unless you modify the software (IllegalAccessException). " + e.getClass().getName() + ": " + e.getMessage());
        }
        catch (InstantiationException e)
        {
            throw new AddOnCatalogException("Unexpected error. This error should not happen, unless you modify the software (InstantiationException). " + e.getClass().getName() + ": " + e.getMessage());
        }
        catch (SecurityException e)
        {
            throw new AddOnCatalogException("Unexpected error. This error should not happen, unless you modify the software (SecurityException). " + e.getClass().getName() + ": " + e.getMessage());
        }
    }

    /**
     * This method converts an add-on name, given from the command line interface, into its internal representation.
     * @param in_cli_name Name of the add-on, as given by the command line.
     * @return The method returns the internal name of the add-on.
     */

    public static String fromCli(String in_cli_name)
    {
        return org.dbview.utils.Strings.dashToLowerCamelCase(in_cli_name);
    }

    /**
     * This method converts an add-on name into its CLI name.
     * @param in_real_name "Internal" name of the add-on.
     * @return The method returns the CLI name of the add-on.
     */
    public static String toCli(String in_real_name)
    {
        return org.dbview.utils.Strings.lowerCamelCaseToDash(in_real_name);
    }
}
