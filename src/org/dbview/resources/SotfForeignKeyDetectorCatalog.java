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

package org.dbview.resources;

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
 * This class implements the catalogue of soft foreign key detectors.
 * @author Denis Beurive
 */
public class SotfForeignKeyDetectorCatalog
{
    /**
     * Name of the <i>property</i> that references the JAR file that contains the implementation of all soft foreign key detectors.
     * @remark This property is defined in the configuration file of the software.
     *         The software's configuration file is a "property file" (see file "resources.properties").
     */
    private static final String __FJ_RESOURCE_NAME = "sotfForeignKeyDetectors"; // __CONFPROP__

    /**
     * This hash table lists all available soft foreign key detectors.
     * <ul>
     *     <li>Key: The name of the soft foreign key detector.</li>
     *     <li>Value: The name of the package that contains the soft foreign key detector.</li>
     * </ul>
     */
    private Hashtable<String, String> __matchers = null;

    /**
     * Create the catalogue of soft foreign key detectors.
     * @throws Exception
     */
    public SotfForeignKeyDetectorCatalog() throws SotfForeignKeyDetectorException
    {
        ArrayList<String> jar_content = null;
        this.__matchers = new Hashtable<String, String>();

        // WARNING !!!!!
        //
        // There is a relation between the following line and the ANT script (build.xml):
        // <property name="dir.pkg.fk" value="org/dbview/addons/input/resources/fk"/>
        //
        Pattern p_matchers = Pattern.compile("^(.+\\.resources\\.softforeignkeydetectors)\\.([^\\.]+)\\.class$"); // __BUILD_CONF__

        // Load the JAR file that contains all (soft) foreign key detectors.
        try
        {
            jar_content = JavaVm.listJar(Conf.get(SotfForeignKeyDetectorCatalog.__FJ_RESOURCE_NAME));
        }
        catch (Exception e)
        {
            throw new SotfForeignKeyDetectorException("An error occurred during the initialization of the catalogue: " + e.getClass().getName() + ": " + e.getMessage() );
        }

        // Locate detectors.
        Iterator<String> i = jar_content.iterator();
        while (i.hasNext())
        {
            String entry = i.next().replaceAll("/|\\\\", ".");
            // System.out.println("> " + entry);
            Matcher m_matcher = p_matchers.matcher(entry);

            if (m_matcher.matches())
            {
                // System.out.println("Matched!");
                String matcher_package = m_matcher.group(1);
                String matcher_name    = m_matcher.group(2);
                // System.out.println(matcher_package + " - " + matcher_name);

                // Make sure that the detector's name starts with an upper case letter.
                if (! matcher_name.matches("^[A-Z].*$")) { throw new SotfForeignKeyDetectorException("A (soft) foreign key matcher must start with an upper case letter. Invalid name: " + matcher_name + "."); }

                if (! this.__matchers.containsKey(matcher_name)) { this.__matchers.put(matcher_name, matcher_package); }
                else
                {
                    // This case should not happen, since a JAR contains distinct entries.
                    throw new SotfForeignKeyDetectorException("Unexpected error : The JAR that contains (soft) foreign key matchers presents duplicated entries.");
                }
            }
        }
    }

    /**
     * This method returns the list of all available soft foreign key detectors.
     * @return The method returns the list of all available soft foreign key detectors.
     * @throws Exception
     */
    public Enumeration<String> getMatchers()
    {
        Enumeration<String> e = this.__matchers.keys();
        ArrayList<String> r   = new ArrayList<String>();

        while (e.hasMoreElements()) { r.add(e.nextElement()); }

        return Collections.enumeration(r);
    }

    /**
     * The method produces a string that represents the catalogue.
     *
     * @return The method returns a string that represents the catalogue.
     */
    @Override
    public String toString()
    {
        try
        {
            String r = "";
            Enumeration<String> matchers = this.getMatchers();

            while (matchers.hasMoreElements())
            {
                String matcher_name = matchers.nextElement();
                String package_name = this.__matchers.get(matcher_name);

                r = r.concat("[Matcher] "   + matcher_name + System.getProperty("line.separator"));
                r = r.concat("\tCLI name: " + SotfForeignKeyDetectorCatalog.toCli(matcher_name)) + System.getProperty("line.separator");
                r = r.concat("\tPackage: "  + package_name + System.getProperty("line.separator"));
            }

            return r;
        }
        catch (Exception e)
        {
            return "Could not generate textual representation of the catalogue: " + e.getMessage();
        }
    }

    /**
     * This method returns an instance of a soft foreign key detector, identified by its <i>real</i> (as opposed to "CLI") name.
     * @param in_real_name The CLI name of the soft foreign key detector.
     * @return The method returns an instance of a soft foreign key detector, identified the given (real) name.
     * @throws SotfForeignKeyDetectorException
     */
    public AbstractSotfForeignKeyDetector getFkMatcherByName(String in_real_name) throws SotfForeignKeyDetectorException
    {
        String package_name = this.__matchers.get(in_real_name);

        if (null == package_name)
        { throw new SotfForeignKeyDetectorException("The soft foreign key detector \"" + in_real_name + "\" does not exist."); }

        String class_name   = package_name + "." + in_real_name;

        try
        {
            return (AbstractSotfForeignKeyDetector)Class.forName(class_name).newInstance();
        }
        catch (ExceptionInInitializerError e)
        {
            throw new SotfForeignKeyDetectorException("Unexpected error. This error should not happen, unless you modify the software. " + e.getClass().getName() + ": " + e.getMessage());
        }
        catch (ClassNotFoundException e)
        {
            throw new SotfForeignKeyDetectorException("Unexpected error. This error should not happen, unless you modify the software. " + e.getClass().getName() + ": " + e.getMessage());
        }
        catch (LinkageError e)
        {
            throw new SotfForeignKeyDetectorException("Unexpected error. This error should not happen, unless you modify the software. " + e.getClass().getName() + ": " + e.getMessage());
        }
        catch (IllegalAccessException e)
        {
            throw new SotfForeignKeyDetectorException("Unexpected error. This error should not happen, unless you modify the software. " + e.getClass().getName() + ": " + e.getMessage());
        }
        catch (InstantiationException e)
        {
            throw new SotfForeignKeyDetectorException("Unexpected error. This error should not happen, unless you modify the software. " + e.getClass().getName() + ": " + e.getMessage());
        }
        catch (SecurityException e)
        {
            throw new SotfForeignKeyDetectorException("Unexpected error. This error should not happen, unless you modify the software. " + e.getClass().getName() + ": " + e.getMessage());
        }
    }

    /**
     * This method returns an instance of a soft foreign key detector, identified by its <i>CLI</i> (Command Line Interface) name.
     * @param in_cli_name The CLI name of the soft foreign key matcher.
     * @return The method returns an instance of a soft foreign key matcher, identified the given CLI name.
     * @throws SotfForeignKeyDetectorException
     */
    public AbstractSotfForeignKeyDetector getFkMatcherByCliName(String in_cli_name) throws SotfForeignKeyDetectorException
    {
        String real_name = SotfForeignKeyDetectorCatalog.fromCli(in_cli_name);
        return this.getFkMatcherByName(real_name);
    }

    /**
     * This method returns the description of a given soft foreign key detector, given its real name.
     * @param in_name Name of the soft foreign key detector.
     * @return The method returns the description of a given soft foreign key detector.
     * @throws SotfForeignKeyDetectorException
     */
    public String getFkMatcherDescriptionByName(String in_name) throws SotfForeignKeyDetectorException
    {
        AbstractSotfForeignKeyDetector m = this.getFkMatcherByName(in_name);
        return m.description();
    }

    /**
     * This method returns the description of a given soft foreign key detector, given its CLI name.
     * @param in_cli_name CLI name of the soft foreign key detector.
     * @return The method returns the description of a given soft foreign key detector.
     * @throws SotfForeignKeyDetectorException
     */
    public String getFkMatcherDescriptionByCliName(String in_cli_name) throws SotfForeignKeyDetectorException
    {
        return this.getFkMatcherDescriptionByName(SotfForeignKeyDetectorCatalog.fromCli(in_cli_name));
    }

    /**
     * This method returns the real name of a soft foreign key detector, given the CLI name of the detector.
     * @param in_cli_name CLI name of the detector.
     * @return The method returns the real name of a soft foreign key detector.
     */
    public static String fromCli(String in_cli_name)
    {
        return org.dbview.utils.Strings.dashToUpperCamelCase(in_cli_name);
    }

    /**
     * This method returns the CLI name of a soft foreign key detector, given the real name of the detector.
     * @param in_real_name Ream name of the detector.
     * @return The method returns the CLI name of a soft foreign key detector.
     */
    public static String toCli(String in_real_name)
    {
        return org.dbview.utils.Strings.upperCamelCaseToDash(in_real_name);
    }
}
