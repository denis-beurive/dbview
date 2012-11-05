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

package org.dbview.addons.input.utils.mysql;

import org.dbview.utils.args4j.*;
import org.kohsuke.args4j.Option;

/**
 * @class CliOptions
 *
 * This class defines the <i>command line options container</i> associated to the MySql odd-on.
 * @remark See Args4j. The command line options are declared in a class. We call this class the <i>command line options container</i>
 * @author Denis Beurive
 */
public class CliOptions extends OptionContainer
{
    /** Command line option that represents the name of the name of the host that runs the MySql server. */
    final static public String HOST        = "-host";

    /** Command line option that represents the name of the MySql user. */
    final static public String USER        = "-user";

    /** Command line option that represents the password used to connect to the MySql server. */
    final static public String PASSWORD    = "-password";

    /** Command line option that represents the name of the database. */
    final static public String DBNAME      = "-dbname";

    /** Command line option that represents the name of the soft foreign key detector to use. */
    final static public String FKMATCHER   = "-fkmatcher";

    /** Command line option that represents the port's number used by the MySql server. */
    final static public String PORT        = "-port";

    /** Args4J option's container for the name of the host that run the MySql server. */
    @Option(name=CliOptions.HOST, handler=SpecialStringOptionHandler.class)
    private String  __dbhost      = null;

    /** Args4J option's container for the name of the MySql user. */
    @Option(name=CliOptions.USER, handler=SpecialStringOptionHandler.class)
    private String  __dbuser      = null;

    /** Args4J option's container for the password used to connect to the MySql server. */
    @Option(name=CliOptions.PASSWORD, handler=SpecialStringOptionHandler.class)
    private String  __dbpassword  = null;

    /** Args4J option's container for the name of the database. */
    @Option(name=CliOptions.DBNAME, handler=SpecialStringOptionHandler.class)
    private String  __dbname      = null;

    /** Args4J option's container for the soft foreign key detector. */
    @Option(name=CliOptions.FKMATCHER, handler=SpecialStringOptionHandler.class)
    private String  __db_soft_foreign_key_detector = null;

    /** Args4J option's container for the port's number used by the MySql server.*/
    @Option(name=CliOptions.PORT)
    private Integer __dbport      = null;

    /** Return the name of the host that run the MySql server.
     *  @return Return the name of the host that run the MySql server.
     */
    public String getHost()      { return this.__dbhost; }

    /**
     * Return the name of the name of the MySql's user.
     * @return Return the name of the name of the MySql's user.
     */
    public String getUser()      { return this.__dbuser; }

    /**
     * Return the password used to connect to the MySql server.
     * @return Return the password used to connect to the MySql server.
     */
    public String getPassword()  { return this.__dbpassword; }

    /**
     * Return the name of the database.
     * @return Return the name of the database.
     */
    public String getDbNanme()   { return this.__dbname; }

    /**
     * Return the name of the soft foreign the detector.
     * @return Return the name of the soft foreign the detector.
     */
    public String getSoftForeignKeyDetector() { return this.__db_soft_foreign_key_detector; }

    /**
     * Return the port's number used by the MySql server.
     * @return Return the port's number used by the MySql server.
     */
    public Integer getPort()     { return this.__dbport; }
}
