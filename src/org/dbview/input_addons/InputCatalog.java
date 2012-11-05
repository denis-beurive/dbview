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

package org.dbview.input_addons;

import java.util.ArrayList;
import java.util.regex.Pattern;

/**
 * This class loads all the <i>resources</i> that contain input adapters' implementations and creates a catalog of available add-ons.
 * @remark Right now, there is only one <i>resource</i> that contains input adaptors. This is a JAR file, create by the ANT script.
 * @author Denis BEURIVE
 */
public class InputCatalog extends org.dbview.addons.AddOnCatalog
{
    /**
     * Name of the <i>property</i> that references the JAR file that contains all the adaptors to list.
     * @remark This property is defined in the configuration file of the software.
     *         The software's configuration file is a "property file" (see file "resources.properties").
     */
    private static final String ADAPTOR_RESOURCE_NAME = "inputAddons"; // __CONFPROP__

    /**
     * Regular expression that is used to recognize an adaptor for an input add-on.
     * @remark There is a relation between the following line and the ANT script (build.xml)
     *         <code>&lt;property name=&quot;dir.pkg.input.addons&quot;  value=&quot;org/dbview/addons/input&quot;/&gt;</code>
     *         <p>example <code>toto.titi.tata.addons.input.addon_name.adaptor_name.class</code></p>
     *         <p>The important point is: "addons.input.<name of the add-on>.<name of the adaptor>.class".</p>
     * @warning Make sure that the regular expression has 3 pairs of brackets.
     *          <ul>
     *              <li>First pair of bracket: the full name of the package that represents the add-on.</li>
     *              <li>Second pair: the name of the add-on.</li>
     *              <li>Third pair: The name of the adaptor.</li>
     *          </ul>
     */
    private static final Pattern ADD_ON_PATTERN  = Pattern.compile("^(.+\\.addons\\.input\\.([^\\.]+))\\.([^\\.]+)\\.class$"); // __BUILD_CONF__

    /**
     * This array contains the expected list of adaptors' names.
     * @remark If you plan to add a new adapter, then you must declare the new adaptor in the list below.
     */
    private static final ArrayList<String> ADAPTORS = new ArrayList<String>() {{ add("Cli"); add("Configuration"); add("Description"); add("Loader"); }};

    /**
     * Constructor.
     * @throws Exception
     */
    public InputCatalog() throws Exception
    {
        super(InputCatalog.ADAPTOR_RESOURCE_NAME, InputCatalog.ADAPTORS, InputCatalog.ADD_ON_PATTERN);
    }
}
