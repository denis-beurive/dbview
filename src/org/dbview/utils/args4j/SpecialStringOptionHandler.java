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

package org.dbview.utils.args4j;

import org.kohsuke.args4j.CmdLineException;
import org.kohsuke.args4j.CmdLineParser;
import org.kohsuke.args4j.OptionDef;
import org.kohsuke.args4j.spi.Parameters;
import org.kohsuke.args4j.spi.Setter;
import org.kohsuke.args4j.spi.OptionHandler;;

/**
 * This class implements a special kind of options' values.
 * Options' values of this kind can not start with a dash.
 * For example, the option's value "-toto" is forbidden.
 * This rule has been set, so that it is possible to differentiate between an option (that starts with a dash) and a value.
 * @author Denis BEURIVE
 * @see args4j documentation.
 */
public class SpecialStringOptionHandler extends OptionHandler<String>
{
    /**
     * Constructor.
     * @param parser Args4J command line parser.
     * @param option See documentation for Args4J.
     * @param setter See documentation for Args4J.
     */
    public SpecialStringOptionHandler(CmdLineParser parser, OptionDef option, Setter<? super String> setter)
    {
        super(parser, option, setter);
    }

    /**
     * This method sets the parsed argument.
     * @param in_parameters List of strings that represents the parameters for the option.
     * @warning Keep in mind that this method may set the value null.
     * @return See documentation for Args4J.
     */
    public int parseArguments(Parameters in_parameters) throws CmdLineException
    {
        String option_value = in_parameters.getParameter(0);

        if (null == option_value) { setter.addValue(null); return 1; }

        if (option_value.startsWith("-"))
        { throw new CmdLineException(this.owner, "Invalid option's value. Option values can not start with a dash. Dashes are used to specify options. If the option's value you need to specify starts with a dash, then you must escape it. Exemple \"-myValue\" becomes \"\\-myValue\"."); }
        option_value = option_value.replaceFirst("^\\\\", "");
        setter.addValue(option_value);
        return 1;
    }

    /**
     * See documentation for Args4J.
     * @return See documentation for Args4J.
     */
    public String getDefaultMetaVariable()
    {
        return "VAL";
    }
}
