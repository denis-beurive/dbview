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

package org.dbview.addons.output.table.utils.dot;

import org.dbview.runtime.cli.CliParser;
import org.dbview.utils.args4j.*;
import org.kohsuke.args4j.Option;
import java.util.*;

/**
 * This class defines command line options container for all DOT add-ons.
 * @remark See Args4j. The command line options are declared in a class. We call this class the <i>command line options container</i>
 * @author Denis Beurive
 */
public class CliOptions extends OptionContainer
{
    /**
     * This constant means that the users requests the horizontal layout.
     */
    final static public int LAYOUT_HORIZONTAL = 1;

    /**
     * This constant means that the users requests the vertical layout.
     */
    final static public int LAYOUT_VERTICAL = 2;

    /**
     * Command line parameter used to specify the layout. Possible values are:
     * <ul>
     *      <li>"horizontal" (or "h")</li>
     *      <li>"vertical" (or "v")</li>
     * </ul>
     */
    final static public String LAYOUT = "-layout";

    /**
     * This is a command line option.
     * It represents the path of the output file.
     */
    final static public String PATH = "-path";

    /**
     * Layout. Possible values are:
     * <ul>
     *      <li>"horizontal" (or "h")</li>
     *      <li>"vertical" (or "v")</li>
     * </ul>
     */
    @Option(name=CliOptions.LAYOUT, handler=SpecialStringOptionHandler.class)
    private String  __layout  = null;

    /**
     * This option represents the path to the output file.
     */
    @Option(name=CliOptions.PATH, handler=SpecialStringOptionHandler.class)
    private String __path = null;

    /**
     * This method returns the layout requested by the user.
     * @return The method returns the layout requested by the user. The returned value may be:
     *         <ul>
     *              <li>CliOptions.LAYOUT_HORIZONTAL</li>
     *              <li>CliOptions.LAYOUT_VERTICAL</li>
     *         </ul>
     * @throws Exception
     */
    public int getLayout() throws Exception
    {
        if (null == this.__layout)                                  { return  CliOptions.LAYOUT_HORIZONTAL; }
        if (0 == this.__layout.compareToIgnoreCase("horizontal"))   { return  CliOptions.LAYOUT_HORIZONTAL; }
        if (0 == this.__layout.compareToIgnoreCase("h"))            { return  CliOptions.LAYOUT_HORIZONTAL; }
        if (0 == this.__layout.compareToIgnoreCase("vertical"))     { return  CliOptions.LAYOUT_VERTICAL; }
        if (0 == this.__layout.compareToIgnoreCase("v"))            { return  CliOptions.LAYOUT_VERTICAL; }
        throw new Exception("Invalid layout \"" + this.__layout + "\"");
    }

    /**
     * This method returns the path to the output file.
     * @return This method returns the path to the output file.
     */
    public String getPath()
    { return this.__path; }
}
