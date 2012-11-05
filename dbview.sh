#!/bin/bash

SOURCE="${BASH_SOURCE[0]}"
while [ -h "$SOURCE" ] ; do SOURCE="$(readlink "$SOURCE")"; done
PWD="$( cd -P "$( dirname "$SOURCE" )" && pwd )"

# -----------------------------------------------------------------------
# Third part JARSs
# -----------------------------------------------------------------------

JDOM=$PWD/lib/jdom-1.1.2.jar
JGRAPTH=$PWD/lib/jgrapht-jdk1.6.jar
MYSQL=$PWD/lib/mysql-connector-java-5.1.18-bin.jar
JARGS=$PWD/lib/args4j-2.0.19.jar
JAXEN=$PWD/lib/jaxen-1.1.3.jar

# -----------------------------------------------------------------------
# Application's JAR
# -----------------------------------------------------------------------

DBVIEW=$PWD/lib/dbview.jar

# -----------------------------------------------------------------------
# Resources' JARs
# WARNING: These values are defined in the property file.
# __CONFPROP__
# -----------------------------------------------------------------------

INPUT_ADAPTORS=$PWD/extensions/databaseAdaptors.jar
OUTPUT_ADAPTORS=$PWD/extensions/exporters.jar
FK_MATCHERS=$PWD/extensions/softForeignKeyDetectors.jar

# -----------------------------------------------------------------------
# Resources' pathes.
# -----------------------------------------------------------------------

EXTENSIONS_DIR=$PWD/extensions
CONF_DIR=$PWD/conf

# -----------------------------------------------------------------------
# Run the application with the correct class path.
# -----------------------------------------------------------------------

java -Dprogram.name="dbview.sh" -classpath "$PWD":"$JDOM":"$JAXEN":"$JGRAPTH":"$MYSQL":"$JARGS":"$DBVIEW":"$INPUT_ADAPTORS":"$OUTPUT_ADAPTORS":"$FK_MATCHERS":"$EXTENSIONS_DIR":"$CONF_DIR" org.dbview.runtime.Dbview "$@"

