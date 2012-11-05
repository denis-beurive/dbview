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


package org.dbview.addons.output.table.dotMedium;

import java.util.ArrayList;
import org.jdom.Element;
import org.dbview.adapter.AbstractCli;
import org.dbview.adapter.CliException;
import org.dbview.adapter.CliParameter;
import org.dbview.adapter.CliParameterTypes;
import org.dbview.addons.output.table.utils.dot.CliOptions;
import org.dbview.addons.output.table.utils.dot.XML;
import org.dbview.utils.args4j.*;
import org.kohsuke.args4j.CmdLineException;
import org.kohsuke.args4j.CmdLineParser;

/**
 * @class Cli
 * This class implements the CLI adapter for the add-on that produces medium DOT (GraphViz) representations.
 * @author Denis BEURIVE
 */
public class Cli extends AbstractCli
{
    /**
     * This attribute represents the <i>command line options' container</i> used to parse the command line that applies to the add-on.
     * @remark See Args4j. The command line options are declared in a class. We call this class the <i>command line options container</i>
     */
    private CliOptions __options = new CliOptions();

    /**
     * Return the command line options container.
     *
     * @note The returned container is mainly used to detect unexpected arguments in the command line.
     * @return The method returns the command line options container.
     */
    public OptionContainer getOptionsContainer() { return this.__options; }

    /**
     * This method returns the information extracted from the command line.
     *
     * @param in_vargs
     *            The command line arguments.
     * @param in_strict
     *            This parameter indicates whether the CLI parser should check
     *            the presence of mandatory options, or not.
     *            <ul>
     *              <li>TRUE: Check the presence of mandatory options.</li>
     *              <li>FALSE: Does not check the presence of mandatory options.</li>
     *            </ul>
     * @param in_set_default
     *            This parameter indicates whether the method should set default
     *            values or not.
     *            <ul>
     *              <li>TRUE: Set default values. When adding a new profile, you should activate the assignment of default values.</li>
     *              <li>FALSE: Does not set default values. When updating a profile, you should deactivate the assignment of default values.</li>
     *            </ul>
     * @return The method returns the information extracted from the command
     *         line. The configuration is represented by a list of
     *         XML elements. Each element of the returned list will be included
     *         in the XML representation of the configuration.
     */
    protected ArrayList<Element> _getConf_(String[] in_vargs,
                                           Boolean in_strict,
                                           Boolean in_set_default) throws CliException
    {
        ArrayList<Element> conf = new ArrayList<Element>();
        CmdLineParser parser    = new CmdLineParser(this.__options);

        try
        {
            parser.parseArgument(this.__options.extractArgvForThisSet(in_vargs));
        }
        catch (CmdLineException e)
        {
            throw new CliException(e.getMessage());
        }

        // Add the layout.
        try
        {
            conf.add(new Element(XML.LAYOUT).addContent(this.__options.getLayout() == org.dbview.addons.output.table.utils.dot.CliOptions.LAYOUT_HORIZONTAL ? "LR" : "TB"));
        }
        catch (Exception e)
        {
            throw new CliException(e.getMessage());
        }

        // Add the output path.
        conf.add(new Element(XML.PATH).addContent(null == this.__options.getPath() ? "" : this.__options.getPath()));

        return conf;
    }

    /**
     * This method returns the description of the command line expected by this adaptor.
     *
     * @return The method returns an array of command line parameter's
     *         descriptions.
     */
    public ArrayList<CliParameter> getOptions()
    {
        ArrayList<CliParameter> params = new ArrayList<CliParameter>();

        CliParameter p1 = new CliParameter();
        p1.parameter    = CliOptions.LAYOUT;
        p1.type         = CliParameterTypes.STRING;
        p1.mandatory    = Boolean.FALSE;
        p1.description  = "The graph's layout. Values can be: \"horizontal\" (or \"h\"), \"vertical\" (or \"v\").";
        params.add(p1);

        CliParameter p2 = new CliParameter();
        p2.parameter    = CliOptions.PATH;
        p2.type         = CliParameterTypes.STRING;
        p2.mandatory    = Boolean.FALSE;
        p2.description  = "Path to the output file (default is STDOUT).";
        params.add(p2);

        return params;
    }
}