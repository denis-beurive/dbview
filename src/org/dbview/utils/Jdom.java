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

import java.util.List;

import org.jdom.*;
import org.jdom.filter.*;
import org.jdom.output.XMLOutputter;

/**
 * This class implements utilities used to manipulate XML through JDOM.
 * @author Denis Beurive
 */
public class Jdom
{
    /**
     * Filter for TEXT.
     * @see JDOM documentation.
     */
    private static Filter __filter_text = new ContentFilter(ContentFilter.TEXT);

    /**
     * Filter for CDATA.
     * @see JDOM documentation.
     */
    private static Filter __filter_cdata = new ContentFilter(ContentFilter.CDATA);

    /**
     * This method extracts the content of a given element.
     * It assumes that it is a CDTA or a TEXT.
     * @param in_element Element
     * @return The method may return:
     *         o A string that represents the content of the element.
     *         o null, if the element is empty or if the specified element is null.
     */
    public static String getTextOrCdata(Element in_element) throws Exception
    {
        List<Content> c = null;

        if (null == in_element) { return null; }

        // First, we try to get a content which is CDATA. This may fail for two reasons :
        //    o The content may not be a CDATA.
        //    o The content may be "empty".
        c = (List<Content>)in_element.getContent(Jdom.__filter_cdata);
        if (1 == c.size()) { return ((CDATA)c.get(0)).getText(); }
        if (1 <  c.size()) { throw new Exception("Invalid XML element: " + in_element.toString()); }

        // This is not a CDATA.. So it should be a TEXT.
        c = (List<Content>)in_element.getContent(Jdom.__filter_text);
        if (0 == c.size()) { return null; }
        if (1 <  c.size()) { throw new Exception("Invalid XML element: " + in_element.toString()); }
        return ((Text)c.get(0)).getTextTrim();
    }

    /**
     * Prints a XML element.
     * @param in_element XML element to print.
     * @return The method returns a string that represents the XML element.
     * @throws Exception
     */
    public static String print(Element in_element) throws Exception
    {
        XMLOutputter outputter = new XMLOutputter();
        outputter.setFormat(org.jdom.output.Format.getPrettyFormat());
        return outputter.outputString(in_element);
    }

    /**
     * Prints a XML document.
     * @param in_doc XML document to print.
     * @return The method returns a string that represents the XML document.
     */
    public static String print(Document in_doc) throws Exception
    {
        XMLOutputter outputter = new XMLOutputter();
        outputter.setFormat(org.jdom.output.Format.getPrettyFormat());
        return outputter.outputString(in_doc);
    }
}
