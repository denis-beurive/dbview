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

import org.jdom.Element;
import org.jdom.output.XMLOutputter;
import java.util.ArrayList;

/**
 * This class implements the database's configuration adaptor.
 * @author Denis Beurive
 */
abstract public class AbstractConfiguration
{
    /**
     * Name of the profile associated to this configuration.
     */
    private String __profile_name  = null;

    /**
     * The name of the add-on to which this adaptor is associated.
     */
    private String __addon = null;

    /**
     * Construct a configuration.
     */
    public AbstractConfiguration()
    {
        this.__addon = this._getAddOnName_();
    }

    /**
     * This method returns the name of the add-on associated to this adaptor.
     *
     * @return The method returns the name of the add-on associated to this adaptor.
     */
    public String getAddOnName()
    {
        return this.__addon;
    }

    /**
     * <p>This method initializes a configuration based on a given XML document.
     *    The structure of this (XML) document depends on the values of parameters "in_addon_name" and "in_profile_name".
     *    If these parameters are <b>NOT</b> given, then the XML document must be:</p> 
     *
     * <pre>
     *      &lt;configuration name=&quot;...&quot; target=&quot;...&quot;&gt;
     *         &lt;data&gt;
     *         ...
     *         &lt;/data&gt;
     *      &lt;/configuration&gt;
     * </pre>
     * 
     * <p>
     * Otherwise:
     * </p>
     *
     * <pre>
     *      &lt;data&gt;
     *      ...
     *      &lt;/data&gt;
     * </pre>
     *
     * @param in_xml
     *            XML document.
     * @param in_addon_name
     *            Name of the add-on to which this configuration is associated.
     *            This argument's value may be null.
     * @param in_profile_name
     *            Name of the profile. This argument's value may be null.
     * @throws ConfigurationException
     */
    public void fromXml(Element in_xml,
                        String in_addon_name,
                        String in_profile_name) throws ConfigurationException, Exception
    {
        if ((null == in_profile_name) ^ (null == in_addon_name))
        {
            throw new Exception("Unexpected error: this error should not happen unless you modify the code of the software. The parameters \"in_addon_name\" and \"in_profile_name\" must be defined, or undefined, simultaneously.");
        }

        // Get the configuration's name.
        if (null == in_profile_name)
        {
            this.__profile_name = in_xml.getAttributeValue("name");
            if (null == this.__profile_name)
            {
                throw new ConfigurationException("XML representation of the configuration has no name!");
            }
        }
        else
        {
            this.__profile_name = in_profile_name;
        }

        // Get the configuration's type.
        if (null == in_addon_name)
        {
            this.__addon = in_xml.getAttributeValue("target");
            if (null == this.__addon)
            {
                throw new ConfigurationException("XML representation of the configuration has no type!");
            }
        }
        else
        {
            this.__addon = in_addon_name;
        }

        // Extract data from the generic configuration's holder.
        Element specific = null;
        if (null == in_profile_name)
        {
            specific = in_xml.getChild("data");
        }
        else
        {
            specific = in_xml;
        }
        if (null == specific)
        {
            throw new ConfigurationException("XML representation of the configuration has no \"specific\" data container!");
        }

        try
        {
            this._fromXml_(specific);
        }
        catch (ConfigurationException e)
        {
            throw new ConfigurationException("The XML document provided from the CLI adaptor seems to be invalid. This error should not happen unless you modify the software. Did you call the method \"AbstractCli::getGetCont()\" with \"in_set_default=FALSE?\" " + e.getMessage());
        }
    }

    /**
     * <p>This method creates a XML document that represents a configuration.</p>
     * <p>The structure of the returned document is:</p>
     *
     * <pre>
     *      &lt;configuration name=&quot;...&quot; target=&quot;...&quot;&gt;
     *          &lt;data&gt;...&lt;/data&gt;
     *      &lt;/configuration&gt;
     * </pre>
     *
     * @return The method returns a XML element.
     */
    public Element toXml()
    {
        // Create the configuration.
        Element configuration = new Element("configuration");
        configuration.setAttribute("name", this.__profile_name);
        configuration.setAttribute("target", this.getAddOnName());

        // Add the generic and the specialized configuration.
        configuration.addContent(this.getData());

        return configuration;
    }

    /**
     * <p>This method returns the data part of the configuration.</p>
     * <p>The returned document is:</p>
     *
     * <pre>
     *      &lt;data&gt;...&lt;/data&gt;
     * </pre>
     *
     * @return The method returns the data part of the configuration.
     */
    public Element getData()
    {
        // Ask the generic configuration's holder to export the list of specific
        // configuration's data.
        ArrayList<Element> specific_data = this._toXml_();

        // Build the specific configuration.
        Element generic = new Element("data");
        for (int index = 0; index < specific_data.size(); index++)
        {
            generic.addContent(specific_data.get(index));
        }

        return generic;
    }

    /**
     * This method generates a literal representation of the XML document.
     *
     * @return The method returns a literal representation of the XML document.
     */
    public String printToXml()
    {
        XMLOutputter outputter = new XMLOutputter();
        outputter.setFormat(org.jdom.output.Format.getPrettyFormat());
        return outputter.outputString(this.toXml());
    }

    /**
     * This method returns a list of XML elements. These XML elements represent
     * the configuration for the specific add-on to which the configuration is
     * associated. These XML elements will be included in the configuration.
     *
     * @return The method returns a list of XML elements.
     * @note This method is used with the repository.
     */
    protected abstract ArrayList<Element> _toXml_();

    /**
     * This method initializes the configuration for the specific add-on to
     * which the configuration is associated. The initialization is done using
     * the content of a given XML element.
     *
     * @param in_xml
     *            Document XML.
     * @throws ConfigurationException
     * @note This method is used with the catalogue.
     */
    protected abstract void _fromXml_(Element in_xml) throws ConfigurationException;

    /**
     * This method returns the name of the specific add-on to
     * which the configuration is associated.
     *
     * @return The method returns the name of the specific add-on
     *         to which the configuration is associated.
     */
    protected abstract String _getAddOnName_();

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
    public abstract ArrayList<String[]> toStrings();

}
