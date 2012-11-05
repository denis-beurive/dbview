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

package org.dbview.adapter;

import java.util.ArrayList;
import org.jdom.Element;
import org.dbview.utils.args4j.*;

/**
 * @class AbstractCli
 *
 * This class is a base class for all command line interface (CLI) adaptors.
 * A command line interface adaptor parses the command line and returns an XML document.
 * The returned XML document is provided to all adaptors that need configuration
 * Please note that the returned XML representation is independent from the user interface (CLI, GUI...) used to get the configuration.
 *
 * @author Denis BEURIVE
 */
public abstract class AbstractCli
{
    /**
     * Return the <i>command line options container</i>.
     * @remark See Args4j. The command line options are declared in a class. We call this class the <i>command line options container</i> 
     * @note The returned container is mainly used to detect unexpected arguments in the command line.
     * @return The method returns the command line options container.
     */
    public abstract OptionContainer getOptionsContainer();

    /**
     * This method returns the description of the command line expected by this adaptor.
     * @remark This method is only used when the user requires to print the help.
     * @return The method returns an array of command line parameter's
     *         descriptions.
     */
    public abstract ArrayList<CliParameter> getOptions();

    /**
     * This method returns an XML document that represents the command line.
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
     * @return The method returns the extracted information. The returned
     *         structure is a XML element identified by the tag "data". Example:
     *         <code>
     *         &lt;data&gt;&lt;login&gt;...&lt;/login&gt;&lt;password&gt;...&lt;/password&gt;&lt;/data&gt;
     *         </code>
     */
    public Element getConf(String[] in_vargs,
                           Boolean in_strict,
                           Boolean in_set_default) throws CliException
    {
        ArrayList<Element> values  = null;
        Element conf = new Element("data");

        try
        {
            // We get the configuration.
            values = this._getConf_(in_vargs, in_strict, in_set_default);
        }
        catch (CliException e)
        {
            throw new CliException("The target's configuration is not valid: " + e.getMessage());
        }

        // We wrap the configuration into a CML container.
        for (int i = 0; i < values.size(); i++)
        {
            conf.addContent(values.get(i));
        }

        // DEBUG
        // System.out.println("getConf done");
        return conf;
    }

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
    protected abstract ArrayList<Element> _getConf_(String[] in_vargs,
                                                    Boolean in_strict,
                                                    Boolean in_set_default) throws CliException;


}
