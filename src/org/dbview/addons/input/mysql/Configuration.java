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

import org.dbview.addons.input.utils.mysql.XML;
import org.dbview.input_addons.AbstractConfiguration;
import org.dbview.input_addons.ConfigurationException;
import org.jdom.*;

import java.util.ArrayList;

/**
 * @class Configuration
 * This class implements the configuration adapter for the Mysql add-on.
 * @author Denis BEURIVE
 */
public class Configuration extends AbstractConfiguration
{
    /**
     * Name of the host that runs the MySql server.
     * @author Denis BEURIVE
     */
    private String __host = null;

    /**
     * Port number used to connect to the MySql serveur.
     * @author Denis BEURIVE
     */
    private Integer __port = new Integer(3306);

    /**
     * Identifier used to access the database.
     * @author Denis BEURIVE
     */
    private String __login = null;

    /**
     * Password used to authenticate to the MySql server.
     * @author Denis BEURIVE
     */
    private String __password = null;

    /**
     * Name of the database.
     * @author Denis BEURIVE
     */
    private String __db_name = null;

    /**
     * This string represents the name of the class used to detect foreign keys from field's names.
     */
    private String __soft_foreign_key_detector = null;

    /**
     * Constructor.
     * @author Denis BEURIVE
     */
    public Configuration()
    {
        super();
    }

    /**
     * This method returns the name of the specific add-on to
     * which the configuration is associated.
     *
     * @return The method returns the name of the specific add-on
     *         to which the configuration is associated.
     */
    protected String _getAddOnName_()
    {
        return "mysql";
    }

    /**
     * This method returns a list of XML elements. These XML elements represent
     * the configuration for the specific add-on to which the configuration is
     * associated (in this case "mysql").
     * These XML elements will be included in the configuration.
     * @return The method returns a list of XML elements.
     */
    protected ArrayList<Element> _toXml_()
    {
        ArrayList<Element> data = new ArrayList<Element>();
        data.add(new Element(XML.HOST).addContent(this.__host));
        data.add(new Element(XML.PORT).addContent(this.__port.toString()));
        data.add(new Element(XML.LOGIN).addContent(this.__login));
        data.add(new Element(XML.PASSWORD).addContent(new CDATA(this.__password)));
        data.add(new Element(XML.DBNAME).addContent(this.__db_name));
        if (null != this.__soft_foreign_key_detector) { data.add(new Element(XML.FKMATCHER).addContent(this.__soft_foreign_key_detector)); }
        return data;
    }

    /**
     * This method initializes the configuration for the specific add-on to
     * which the configuration is associated. The initialization is done using
     * the content of a given XML element.
     *
     * @param in_xml
     *            Document XML (that represents a configuration).
     * @throws ConfigurationException
     * @note This method is used with the profile's repository.
     */
    protected void _fromXml_(Element in_xml) throws ConfigurationException
    {
        Element e;

        e = in_xml.getChild(XML.HOST);
        if (null == e) { throw new ConfigurationException("Missing \"host\"."); }
        try { this.__host = org.dbview.utils.Jdom.getTextOrCdata(e); } catch (Exception x) { throw new ConfigurationException(x.getMessage()); }
        if (null == this.__host) { throw new ConfigurationException("XML representation of the configuration is not valid. Something is messed up with the host definition!"); }

        e = in_xml.getChild(XML.PORT);
        if (null == e) { throw new ConfigurationException("Missing \"port\".");  }
        String s = null;
        try { s = org.dbview.utils.Jdom.getTextOrCdata(e); } catch (Exception x) { throw new ConfigurationException(x.getMessage() + " XML representation of the configuration is not valid. Something is messed up with the port definition!"); }
        this.__port = null == s ? null : Integer.parseInt(s);

        e = in_xml.getChild(XML.LOGIN);
        if (null == e) { throw new ConfigurationException("Missing \"login\"."); }
        try { this.__login = org.dbview.utils.Jdom.getTextOrCdata(e); } catch (Exception x) { throw new ConfigurationException(x.getMessage() + " XML representation of the configuration is not valid. Something is messed up with the login definition!"); }

        e = in_xml.getChild(XML.FKMATCHER);
        if (null != e) { try { this.__soft_foreign_key_detector = org.dbview.utils.Jdom.getTextOrCdata(e); } catch (Exception x) { throw new ConfigurationException(x.getMessage() + " XML representation of the configuration is not valid. Something is messed up with the foreign key matcher definition!"); } }

        e = in_xml.getChild(XML.PASSWORD);
        if (null == e) { throw new ConfigurationException("Missing \"password\"."); }
        try { this.__password = org.dbview.utils.Jdom.getTextOrCdata(e); } catch (Exception x) { throw new ConfigurationException(x.getMessage() + " XML representation of the configuration is not valid. Something is messed up with the password definition!"); }

        e = in_xml.getChild(XML.DBNAME);
        if (null == e) { throw new ConfigurationException("Missing \"db_name\"."); }
        try { this.__db_name = org.dbview.utils.Jdom.getTextOrCdata(e); } catch (Exception x) { throw new ConfigurationException(x.getMessage() + " XML representation of the configuration is not valid. Something is messed up with the database's name definition!"); }
    }

    /**
     * This method returns a structure that can be sued to produce a literal
     * representation of the configuration. The returned structure is an array.
     * Each element of the returned array is an array that contains 2 strings.
     * <ul>
     *    <li>The first element of the array is the name of a configuration
     *        parameter.</li>
     *    <li>The second element of the array is the value of a
     *        configuration parameter.</li>
     * </ul>
     *
     * @return The method returns a structure that can be used to produce a
     *         literal representation of the configuration.
     */
    public ArrayList<String[]> toStrings()
    {
        	ArrayList<String[]> list = new ArrayList<String[]>();

        	String[] host        = { "host",       this.__host};
        	String[] port        = { "port",       this.__port.toString()};
        	String[] login       = { "login",      this.__login};
        	String[] password    = { "password",   this.__password};
        	String[] db_name     = { "db name",    this.__db_name};
        	String[] fk_matcher  = { "fk matcher", this.__soft_foreign_key_detector};

        	list.add(host);
        	list.add(port);
        	list.add(login);
        	list.add(password);
        	list.add(db_name);
        	list.add(fk_matcher);

        	return list;
    }

}
