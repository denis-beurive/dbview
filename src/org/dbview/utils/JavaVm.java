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

package org.dbview.utils;

import java.io.File;
import java.io.FileInputStream;
import java.net.URL;
import java.util.jar.JarEntry;
import java.util.jar.JarInputStream;
import java.lang.Exception;
import java.util.ArrayList;

/**
 * This class implements utilities used to interact with the JAVA run-time environment.
 * @author Denis Beurive
 */
public class JavaVm
{
    /**
     * This method lists the content of a JAR file.
     * @param in_name Name of the JAR file.
     * @return The method returns the list of entries found in the JAR file.
     * @throws Exception
     */
    static public ArrayList<String> listJar(String in_name) throws Exception
    {
        ArrayList<String> list = new ArrayList<String>(); 
        
        URL jar_url = JavaVm.locateResource(in_name);
        if (null == jar_url) { throw new Exception("The JAR file \"" + in_name + "\" could not be found!"); }
        
        JarInputStream ji = new JarInputStream(new FileInputStream(new File(jar_url.toURI())));
        
        while(Boolean.TRUE)
        {
            JarEntry entry = ji.getNextJarEntry();
            if (null == entry) { break; }
            list.add(entry.getName());
        }
        
        return list;
    }
    
    /**
     * This method tries to locate a resource, given its name.
     * @param in_name Name of the resource.
     * @return If the method locates the resource, then it returns the resource's URL.
     *         Otherwise, it returns the value null.
     */
    public static URL locateResource(String in_name)
    {
        return JavaVm.class.getClassLoader().getResource(in_name);
    }
}
