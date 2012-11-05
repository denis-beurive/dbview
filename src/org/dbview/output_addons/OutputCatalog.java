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

package org.dbview.output_addons;

import java.util.ArrayList;
import java.util.regex.Pattern;

/**
 * This class loads all the resources that contain output adapters' implementations and creates a catalog of available add-ons.
 * @remark Right now, there is only one <i>resource</i> that contains input adaptors. This is a JAR file, create by the ANT script.
 * @author Denis BEURIVE
 */
public class OutputCatalog extends org.dbview.addons.AddOnCatalog
{
    /**
     * Name of the <i>property</i> that references the JAR file that contains all the adaptors to list.
     * @remark This property is defined in the configuration file of the software.
     *         The software's configuration file is a "property file" (see file "resources.properties").
     */
    private static final String ADAPTOR_RESOURCE_NAME = "outputTableAddons"; // __CONFPROP__

    /**
     * Regular expression that is used to recognize an adaptor, for an add-on specialized in the rendering of tables.
     * @remark There is a relation between the following line and the ANT script (build.xml)
     *         <code>&lt;property name=&quot;dir.pkg.output.table.addons&quot; value=&quot;org/dbview/addons/output/table&quot;/&gt;</code>
     *         <p>example: <code>toto.titi.tata.addons.output_type.addon_name.adaptor_name.class</code></p>
     *          The important point is: "addons.output.<type of the output>.<name of the add-on>.<name of the adaptor>.class".
     * @warning Make sure that the regular expression has 3 pairs of brackets.
     *          <ul>
     *              <li>First pair of bracket: the full name of the package that represents the add-on.</li>
     *              <li>Second pair: the name of the add-on.</li>
     *              <li>Third pair: The name of the adaptor.</li>
     *          </ul>
     */
    private static final Pattern ADD_ON_PATTERN = Pattern.compile("^(.+\\.addons\\.output\\.[^\\.]+\\.([^\\.]+))\\.([^\\.]+)\\.class$"); // __BUILD_CONF__

    /**
     * This array contains the expected list of adaptors' names.
     * @remark If you plan to add a new adapter, then you must declare the new adaptor in the list below.
     */
    private static final ArrayList<String> ADAPTORS = new ArrayList<String>() {{ add("Cli"); add("Description"); add("Exporter"); }};

    /**
     * Constructor.
     * @throws Exception
     */
    public OutputCatalog() throws Exception
    {
        super(OutputCatalog.ADAPTOR_RESOURCE_NAME, OutputCatalog.ADAPTORS, OutputCatalog.ADD_ON_PATTERN);
    }
}
