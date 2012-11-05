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

package org.dbview.conf;

import java.util.Properties;
import java.io.FileReader;
import java.io.File;
import java.net.URL;
import java.util.Enumeration;
import java.util.Hashtable;

/**
 * This class contains the software's configuration.
 * @remark Please note that the configuration is provided by an external files that contains "properties".
 * @see http://docs.oracle.com/javase/1.4.2/docs/api/java/util/Properties.html
 * @author Denis BEURIVE
 */
public class Conf
{
    /**
     * This array contains the list of configuration <i>external</i> files to load.
     */
    private static final String[] CONF_FILES = { "resources.properties" };
    
    /**
     * This hash table contains the loaded configuration.
     */
    private static Hashtable<String, String> __conf = new Hashtable<String, String>();
    
    /**
     * This flag indicates whether the configuration has been loaded or not.
     */
    private static Boolean ininialized = Boolean.FALSE;
    
    /**
     * This method initializes the configuration.
     * This method loads all external configuration files and build the global configuration.
     * @throws Exception
     */
    public static void init() throws Exception
    {
    	if (Conf.ininialized) { return; }
    	
        for (String conf_file : Conf.CONF_FILES)
        {
            // Search for the configuration file
            URL conf_url = Conf.class.getClassLoader().getResource(conf_file);
            if (null == conf_url) { throw new Exception("Could not find my configuration file " + conf_file + "!"); }
            
            // Load configuration file
            Properties p = new Properties();
            p.load(new FileReader(new File(conf_url.toURI())));
            
            // Trim spaces
            Enumeration<Object> e = p.keys();
            while (Boolean.TRUE)
            {
                if (! e.hasMoreElements()) { break; }
                String key = ((String)e.nextElement()).trim();
                
                if (Conf.__conf.containsKey(key))
                { throw new Exception("The configuration's entry \"" + key + "\" is defined twice!"); }
                
                String value = ((String)p.get(key)).trim();
                Conf.__conf.put(key, value);
            }
        }
        
        Conf.ininialized = Boolean.TRUE;
    }
    
    /**
     * This method returns the value of a given property.
     * @param in_key Name of the property to get.
     * @return The method returns the value associated with the given property's name.
     * @throws Exception
     */
    public static String get(String in_key) throws Exception
    {
        if (! Conf.ininialized) { Conf.init(); }
        return Conf.__conf.get(in_key);
    }
}
