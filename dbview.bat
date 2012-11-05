@ECHO OFF

REM Doc: http://www.robvanderwoude.com/parameters.php


set PWD=%~dp0

REM ----------------------------------------------------------------------------------
REM External JARs.
REM ----------------------------------------------------------------------------------

set JDOM=%PWD%lib\jdom-1.1.2.jar
set JGRAPTH=%PWD%lib\jgrapht-jdk1.6.jar
set MYSQL=%PWD%lib\mysql-connector-java-5.1.18-bin.jar
set JARGS=%PWD%lib\args4j-2.0.19.jar
set JAXEN=%PWD%lib\jaxen-1.1.3.jar

REM ----------------------------------------------------------------------------------
REM Application's JAR.
REM ----------------------------------------------------------------------------------

set DBVIEW=%PWD%lib\dbview.jar

REM ----------------------------------------------------------------------------------
REM Resources' JARs.
REM WARNING: These values are defined in the property file.
REM __CONFPROP__
REM ----------------------------------------------------------------------------------

set INPUT_ADAPTORS=%PWD%extensions\databaseAdaptors.jar
set OUTPUT_ADAPTORS=%PWD%extensions\exporters.jar
set FK_MATCHERS=%PWD%extensions\softForeignKeyDetectors.jar

REM ----------------------------------------------------------------------------------
REM Resources' pathes.
REM ----------------------------------------------------------------------------------

set EXTENSIONS_DIR=%PWD%extensions
set CONF_DIR=%PWD%conf

REM ----------------------------------------------------------------------------------
REM Run the application with the correct class path.
REM
REM WARNING!
REM
REM The paths that appear in the class' path must *NOT* end with the character "\"!
REM    WRONG: -classpath "%PWD%"
REM    OK:    -classpath "%PWD%."
REM ----------------------------------------------------------------------------------

java -Dprogram.name="dbview.bat" -classpath "%PWD%.";"%JDOM%";"%JAXEN%";"%JGRAPTH%";"%MYSQL%";"%JARGS%";"%DBVIEW%";"%INPUT_ADAPTORS%";"%OUTPUT_ADAPTORS%";"%FK_MATCHERS%";"%EXTENSIONS_DIR%";"%CONF_DIR%" org.dbview.runtime.Dbview %*


