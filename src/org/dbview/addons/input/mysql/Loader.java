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
import org.dbview.input_addons.*;
import org.dbview.resources.*;
import org.jdom.*;

/**
 * @class Loader
 * This class implements the loader adapter for the Mysql add-on.
 * @remark The loader is used to gain information from the input data source.
 * @author Denis Beurive
 */
public class Loader extends AbstractLoader
{
    /**
     * This method returns the URL used to open a connection to the database.
     * @param in_data XML element that contains the data required to open the connection.
     * @return The method returns the URL used to open a connection to the database.
     * @throws Exception
     */
    protected String _getUrl_(Element in_data) throws Exception
    {
        // System.out.println("Call _getUrl_");
        // System.out.println(in_data.getName());

        if (0 != in_data.getName().compareTo("data"))
        { throw new Exception("Invalid data for connection: " + org.dbview.utils.Jdom.print(in_data)); }

        String host      = org.dbview.utils.Jdom.getTextOrCdata(in_data.getChild(XML.HOST));
        String port      = org.dbview.utils.Jdom.getTextOrCdata(in_data.getChild(XML.PORT));
        String login     = org.dbview.utils.Jdom.getTextOrCdata(in_data.getChild(XML.LOGIN));
        String password  = org.dbview.utils.Jdom.getTextOrCdata(in_data.getChild(XML.PASSWORD));
        String db_name   = org.dbview.utils.Jdom.getTextOrCdata(in_data.getChild(XML.DBNAME));
        String URL       = "jdbc:mysql://" + host + ":" + port + "/" + db_name + "?user=" + login;

        if (0 < password.length()) { URL += "&password=" + password; }

        // System.out.println(URL);

        return URL;
    }

    /**
     * This method returns an instance of a soft foreign key detector (if it is defined in the profile).
     * @param in_profile Profile of the input target.
     * @return The method returns an instance of a soft foreign key detector, or null if no detector is defined.
     */
    protected AbstractSotfForeignKeyDetector _getSoftForeignKeyDetector_(Element in_profile) throws Exception
    {
        if (0 != in_profile.getName().compareTo("data"))
        { throw new Exception("Invalid data for soft key detector instantiation: " + org.dbview.utils.Jdom.print(in_profile)); }

        // System.out.println(org.dbview.utils.Jdom.print(in_profile));
        
        String matcher = org.dbview.utils.Jdom.getTextOrCdata(in_profile.getChild(XML.FKMATCHER));
        if (org.dbview.utils.Strings.isEmpty(matcher)) { return null; }

        // System.out.println("(" + matcher + ")");

        SotfForeignKeyDetectorCatalog catalog = new SotfForeignKeyDetectorCatalog();
        AbstractSotfForeignKeyDetector d = catalog.getFkMatcherByCliName(matcher);

        if (null == d)
        { throw new Exception("Invalid soft key detector " + matcher + " (this detector does not exist!)."); }

        return d;
    }
}
