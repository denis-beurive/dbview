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
package org.dbview.utils.args4j;

import java.lang.reflect.Field;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.ArrayList;

import org.kohsuke.args4j.Option;

/**
 * This class is a parent class for all command line options' containers.
 * @remark See Args4j. The command line options are declared in a class. We call this class the <i>command line options container</i>
 *
 * @author Denis BEURIVE
 */
public class OptionContainer
{
    /**
     * Each time an option is parsed, then the associated command line arguments are removed from the list of command line argument.
     * This attribute contains the remaining arguments in the command line.
     */
    private ArrayList<String> __shrinked_argv = new ArrayList<String>();

    /**
     * This method returns the list of command line option strings ("-host", "-user", ...).
     * For each command line option string, the list contains a flag that indicates whether the option takes an argument or not.
     * @return The method returns a hash.
     *         <ul>
     *              <li>Key:   The command option string.</li>
     *              <li>Value: A boolean value that indicates whether the option takes an argument or not.</li>
     *         </ul>
     */
    public Hashtable<String, Boolean> options()
    {
        Hashtable<String, Boolean> options = new Hashtable<String, Boolean>();
        Field[] fields = this.getClass().getDeclaredFields();
        for (Field field: fields)
        {
            Option o = null;
            o = field.getAnnotation(Option.class);
            if (null == o) { continue; }

            String   option_class_name = field.getType().getName();
            String[] option_aliases    = o.aliases();
            String   option_name       = o.name();
            Boolean  take_args         = 0 == option_class_name.compareTo("boolean") ? Boolean.FALSE : Boolean.TRUE;

            options.put(option_name, take_args);

            // System.out.println("OPT> " + option_name);

            for (String alias: option_aliases) { options.put(alias, take_args); }
        }
        return options;
    }

    /**
     * This method is mainly used for debugging.
     * @return The method returns a string that represents the container's state.
     */
    @Override
    public String toString()
    {
        String result = "";

        Hashtable<String, Boolean> o = this.options();
        Enumeration<String> keys     = o.keys();

        while (keys.hasMoreElements())
        {
            String key  = keys.nextElement();
            Boolean arg = o.get(key);
            result += key + " => " + (arg ? "TRUE" : "FALSE") + System.getProperty("line.separator");
        }

        return result;
    }

    /**
     * This method extracts the command line arguments that apply to <i>these</i> options, from a given list of arguments.
     * @remark This method is used to implement a 2 stages command line parsing. Sometimes, it is necessary to extract a part of information from the command line in order to parse the all arguments.
     * @param in_argv List of arguments.
     * @return The method returns the list of command line arguments that applies to this options' container.
     */
    public String[] extractArgvForThisSet(String[] in_argv)
    {
        // System.out.println("> Given: " + org.dbview.utils.Strings.join(Arrays.asList(in_argv), ", "));

        ArrayList<String> result           = new ArrayList<String>();
        Hashtable<String, Boolean> options = this.options();
        int removed                        = 0;

        this.__shrinked_argv = new ArrayList<String>();
        for (String arg: in_argv) { this.__shrinked_argv.add(arg); }

        // System.out.println(">>> " + this.__shrinked_argv.size());
        // Enumeration<String> e = options.keys();
        // while (e.hasMoreElements()) { System.out.println("HASH(" + e.nextElement() + ")"); }

        for (int i=0; i<in_argv.length; i++)
        {
            String arg = in_argv[i];
            // System.out.println("CLI(" + arg + ")");

            if (options.containsKey(arg))
            {
                result.add(arg);
                this.__shrinked_argv.remove(i - removed);
                removed++;
                if (! options.get(arg))        { continue; }
                if (i == (in_argv.length - 1)) { break; }
                i += 1;
                result.add(in_argv[i]);
                this.__shrinked_argv.remove(i - removed);
                removed++;
            }
        }

        // System.out.println("> Left: " + org.dbview.utils.Strings.join(this.__shrinked_argv, ", "));
        return result.toArray(new String[result.size()]);
    }

    /**
     * This method returns the list of command line arguments that remains after the command line parsing.
     * @return The method returns the list of command line arguments that remains after the command line parsing.
     */
    public String[] shrink()
    {
        return this.__shrinked_argv.toArray(new String[this.__shrinked_argv.size()]);
    }
}
