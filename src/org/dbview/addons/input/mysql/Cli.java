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

package org.dbview.addons.input.mysql;

import java.util.ArrayList;
import org.dbview.adapter.AbstractCli;
import org.dbview.adapter.CliException;
import org.dbview.adapter.CliParameter;
import org.dbview.adapter.CliParameterTypes;
import org.dbview.addons.input.utils.mysql.CliOptions;
import org.dbview.addons.input.utils.mysql.XML;
import org.dbview.utils.args4j.*;
import org.jdom.Element;
import org.kohsuke.args4j.CmdLineException;
import org.kohsuke.args4j.CmdLineParser;

/**
 * This class is responsible for extracting values from the command line, for the MySql add-on.
 *
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
     * @remark The returned container is mainly used to detect unexpected arguments in the command line.
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
        CmdLineParser parser = new CmdLineParser(this.__options);

        try
        {
            parser.parseArgument(this.__options.extractArgvForThisSet(in_vargs));

        }
        catch (CmdLineException e)
        {
            throw new CliException(e.getMessage());
        }

        // Extract values.
        String host       = this.__options.getHost();
        String login      = this.__options.getUser();
        String password   = this.__options.getPassword();
        String db_name    = this.__options.getDbNanme();
        String fk_matcher = this.__options.getSoftForeignKeyDetector();
        Integer port      = this.__options.getPort();

        // Set default values.
        if (in_set_default)
        {
            host = null == host ? "localhost" : host;
            login = null == login ? "root" : login;
            port = null == port ? 3306 : port;
            password = null == password ? "" : password;
            fk_matcher = null == fk_matcher ? "" : fk_matcher;
        }

        // Sanity check.
        if (in_strict)
        {
            if (null == db_name)
            {
                throw new CliException("The mandatory command line option \"--dbname\" is missing.");
            }
        }

        // Create the XML element.
        if (null != host)
        {
            conf.add(new Element(XML.HOST).addContent(host));
        }
        if (null != port)
        {
            conf.add(new Element(XML.PORT).addContent(port.toString()));
        }
        if (null != login)
        {
            conf.add(new Element(XML.LOGIN).addContent(login));
        }
        if (null != password)
        {
            conf.add(new Element(XML.PASSWORD).addContent(password));
        }
        if (null != db_name)
        {
            conf.add(new Element(XML.DBNAME).addContent(db_name));
        }
        if (null != fk_matcher)
        {
            conf.add(new Element(XML.FKMATCHER).addContent(fk_matcher));
        }

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
        p1.parameter = CliOptions.HOST;
        p1.type = CliParameterTypes.STRING;
        p1.mandatory = Boolean.FALSE;
        p1.description = "Host name, or IP address of the MySql server (default: localhost).";

        CliParameter p2 = new CliParameter();
        p2.parameter = CliOptions.USER;
        p2.type = CliParameterTypes.STRING;
        p2.mandatory = Boolean.FALSE;
        p2.description = "User's name used to log to the MySql server (default: root).";

        CliParameter p3 = new CliParameter();
        p3.parameter = CliOptions.PASSWORD;
        p3.type = CliParameterTypes.STRING;
        p3.mandatory = Boolean.FALSE;
        p3.description = "User's password (default: no password).";

        CliParameter p4 = new CliParameter();
        p4.parameter = CliOptions.DBNAME;
        p4.type = CliParameterTypes.STRING;
        p4.mandatory = Boolean.TRUE;
        p4.description = "Name of the database to use.";

        CliParameter p5 = new CliParameter();
        p5.parameter = CliOptions.FKMATCHER;
        p5.type = CliParameterTypes.STRING;
        p5.mandatory = Boolean.FALSE;
        p5.description = "Name of the matcher used to find foreign keys if the database engine does not support foreing key constraint (Typically�: MyIsam). Please not that you can use this feature even if you use a database engine that supports foreing key constraint. For example: you are using InnoDb database engine wthout declaring foreign key constraints, but your database� schema presents implicit joins (default: do not use foreign key matcher).";
        p5.resource_name = "fkMatchers";

        CliParameter p6 = new CliParameter();
        p6.parameter = CliOptions.PORT;
        p6.type = CliParameterTypes.INTEGER;
        p6.mandatory = Boolean.FALSE;
        p6.description = "TCP port number used by the MySql server (default: 3306)";

        params.add(p1);
        params.add(p2);
        params.add(p3);
        params.add(p4);
        params.add(p5);
        params.add(p6);
        return params;
    }
}
