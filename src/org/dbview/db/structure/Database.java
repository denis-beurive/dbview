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

package org.dbview.db.structure;

import org.dbview.utils.Strings;

import java.util.Hashtable;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Collections;
import org.jgrapht.*;
import org.jgrapht.graph.*;
import java.lang.Math;




/**
 * This class represents a database.
 * @author Denis Beurive
 */
public class Database
{
    /**
     * This value is used by the method that generates unique names that don't represent tables.
     */
    private static Integer __n = 0;
    
    /**
     * This constant represents a minimum cardinality of 0 (for join).
     */
    public static final int MIN_0 = 0;

    /**
     * This constant represents a minimum cardinality of 1 (for join).
     */
    public static final int MIN_1 = 1;

    /**
     * This constant represents a maximum cardinality of 1 (for join).
     */
    public static final int MAX_1 = 1;

    /**
     * This constant represents a maximum cardinality of N (for join).
     */
    public static final int MAX_N = -1;

    /**
     * This constant represents an unknown type of relation.
     * @remark Please note that this value is used for initialization only.
     *         Once the database is loaded, no link should present this value.
     */
    public static final int UNDEFINED_LINK     = -1;

    /**
     * This constant represents a "soft relation".
     * @remark A soft relation contains only soft joins.
     *         A soft join is defined by foreign keys' names only. You can find this type of joins in MyIsam (MySql) tables, for example.
     */
    public static final int SOFT_LINK          = 0;

    /**
     * This constant represents a "mixed relation".
     * This type of relation is a mix between soft and hard joins.
     * @remark A soft relation contains only soft joins.
     *         A soft join is defined by foreign keys' names only. You can find this type of joins in MyIsam (MySql) tables, for example.
     * @remark A hard join is defined by the table's constraint.
     */
    public static final int SOFT_AND_HARD_LINK = 1;

    /**
     * This constant represents a "hard relation".
     * @remark A hard relation contains only hard joins.
     *         This type of join is defined by a table's constraints only.
     */
    public static final int HARD_LINK          = 2;

    /**
     * This hash table lists all database's tables.
     * <ul>
     *      <li>Hash's key:   The name of the table.</li>
     *      <li>Hash's value: The table which name is defined as key.</li>
     * </ul>
     */
    private Hashtable<String, Table> __tables = new Hashtable<String, Table>();

    // TDON
    /**
     * This hash contains all relations between tables.
     * <ul>
     *      <li>Hash's key:   The "dependent" table.</li>
     *      <li>Hash's value: A hash that lists all relation from the (dependent) table used as key. Relations are grouped by "reference" tables.
     *                 <ul>
     *                      <li>Hash's key:   The "reference" table.</li>
     *                      <li>Hash's value: Relations' "meta data" container. See class MetaData.</li>
     *                 </ul>
     *      </li>
     * </ul>
     */
    private Hashtable<Table, Hashtable<Table, TableToTableRelation>> __relations = null;

    /**
     * Name of the database.
     */
    private String __name = null;

    /**
     * Create a database.
     * @param in_name Name of the database.
     */
    public Database(String in_name)
    {
        this.__name = in_name;
    }

    /**
     * Return the name of the database.
     * @return The method returns the name of the database.
     */
    public String getName()
    {
        return this.__name;
    }

    /**
     * Return a table.
     * @param in_name Nane of the table to return.
     * @return The method returns a table.
     *         If the table does not exist in the database, then the method returns the value null.
     */
    public Table getTable(String in_name)
    {
        if (! this.__tables.containsKey(in_name)) { /* System.out.println("Key not found!!!"); */ }
        else { /* System.out.println(in_name + "  > Key found!!!"); */ }
        return this.__tables.get(in_name);
    }

    /**
     * Return the list of all tables in the database.
     * @return The method returns the list of all tables in the database.
     */
    public ArrayList<Table> getTables()
    {
        return Collections.list(this.__tables.elements());
    }

    /**
     * Add a table to the database.
     * @param in_table Table to add.
     * @throws org.dbview.db.structure.DatabaseException
     * @remark This method is accessible for package's members only.
     *         Adding a table to a database is done when a table is created.
     */
    void addTable(Table in_table) throws org.dbview.db.structure.DatabaseException
    {
        String table_name = in_table.getName();
        if (null != this.__tables.get(table_name))
        { throw new org.dbview.db.structure.DatabaseException("Table \"" + table_name + "\" already exists!"); }
        this.__tables.put(table_name, in_table);
    }

    /**
     * This method generates the list of all relations for the current database.
     * @throws Exception
     */
    public void generateRelations() throws Exception
    {
        this.__relations = new Hashtable<Table, Hashtable<Table, TableToTableRelation>>();
        for (Table table: this.getTables())
        { this.__relations.put(table, this.__getRelationsFromTable(table)); }
    }

    // TDON
    /**
     * This method returns the list of "reference" tables in contact with a given "dependent" table.
     * @param in_dependent_table The "dependent" table.
     * @return the method returns the list of "reference" tables in contact with the given "dependent" table.
     */
    public ArrayList<Table> getReferenceTables(Table in_dependent_table) throws Exception
    {
        if (null == this.__relations) { this.generateRelations(); }

        Hashtable<Table, TableToTableRelation> tbls = this.__relations.get(in_dependent_table);
        if (null == tbls) { throw new Exception("You try to get the list of reference tables from a reference table that does not exist!"); }

        Enumeration<Table> tables = tbls.keys();
        ArrayList<Table> references = new ArrayList<Table>();
        while (tables.hasMoreElements()) { references.add(tables.nextElement()); }
        return references;
    }

    // TDON
    /**
     * This method returns the list of "reference" tables in contact with a given "dependent" table, identified by its name.
     * @param in_dependent_table_name Name of the "dependent" table.
     * @return This method returns the list of "reference" tables in contact with a given "dependent" table, identified by its name.
     */
    public ArrayList<Table> getReferenceTables(String in_dependent_table_name) throws Exception
    {
        return this.getReferenceTables(this.getTable(in_dependent_table_name));
    }

    // TDON
    /**
     * This method returns the list of "dependent" tables in contact with a given "reference" table.
     * @param in_reference_table The "reference" table.
     * @return the method returns the list of "dependent" tables in contact with a given "reference" table.
     */
    public ArrayList<Table> getDependentTables(Table in_reference_table) throws Exception
    {
        if (null == this.__relations) { this.generateRelations(); }

        Hashtable<Table, ArrayList<FieldToFieldJoin>> relations = this.__getRelationsToTable(in_reference_table);

        return Collections.list(relations.keys());
    }

    // TDON
    /**
     * This method returns the list of "dependent" tables in contact with a given "reference" table, identified by its name.
     * @param in_reference_table_name The "reference" table_name.
     * @return This method returns the list of "dependent" tables in contact with a given "reference" table, identified by its name.
     */
    public ArrayList<Table> getDependentTables(String in_reference_table_name) throws Exception
    {
        return this.getDependentTables(this.getTable(in_reference_table_name));
    }

    // TDON
    /**
     * This method returns the list of <i>relations</i> from a given "dependent" table to a given "reference" table.
     * @param in_dependent_table The "dependent" table.
     * @param in_reference_table The "reference" table.
     * @return The method returns the list of relations from the given "dependent" table to the given "reference" table.
     * @remark A <i>relation</i> is a global link between two tables, while a <i>join</i> is a link between two fields.
     */
    public ArrayList<FieldToFieldJoin> getRelationsFromReferenceToTarget(Table in_dependent_table, Table in_reference_table) throws Exception
    {
        if (null == this.__relations) { this.generateRelations(); }

        // System.out.println(in_dependent_table.getName() + " => " + in_reference_table.getName());
        Hashtable<Table, TableToTableRelation> ref_relations = this.__relations.get(in_dependent_table);
        if (null == ref_relations) { throw new Exception("You try to get the list of reference tables from a reference table that does not exist!"); }

        // We check that we found something.
        if (! ref_relations.containsKey(in_reference_table)) { return new ArrayList<FieldToFieldJoin>(); }
        ArrayList<FieldToFieldJoin> r = ref_relations.get(in_reference_table).relations;
        if (null == r) { return new ArrayList<FieldToFieldJoin>(); }
    	return r;
    }

    // TDON
    /**
     * This method returns the type of <i>relation</i> between to given tables.
     * @param in_dependent_table The "dependent" table.
     * @param in_reference_table The "reference" table.
     * @return The method returns the type of relation between the given dependent table and the given reference table.
     *         It can be:
     *         <ul>
     *            <li>SOFT_AND_HARD_LINK</li>
     *            <li>SOFT_LINK</li>
     *            <li>HARD_LINK</li>
     *         </ul>
     */
    public int getTableToTableRelationType(Table in_dependent_table, Table in_reference_table) throws Exception
    {
        Hashtable<Table, TableToTableRelation> ref_relations = this.__relations.get(in_dependent_table);
        if (null == ref_relations) { throw new Exception("You try to get the type of relation from a reference table that does not exist!"); }

        TableToTableRelation meta = ref_relations.get(in_reference_table);
        if (null == meta) { throw new Exception("You try to get the type of relation to a reference table that does not exist!"); }

        if (meta.type == Database.UNDEFINED_LINK) { throw new Exception("The type of relation from a dependent table to a reference table is not defined! This situation should not happen!"); }

        return meta.type;
    }

    // TDON
    /**
     * This method returns the list of <i>relations</i> from a given "dependent" table (identified by its name) to a given "reference" table (identified by its name).
     * @param in_dependent_table_name Name of the dependent table.
     * @param in_reference_table_name Name of the reference table.
     * @return The method returns the list of relations from then given dependent table to the given reference table.
     */
    public ArrayList<FieldToFieldJoin> getRelationsFromReferenceToTarget(String in_dependent_table_name, String in_reference_table_name) throws Exception
    {
        return this.getRelationsFromReferenceToTarget(this.getTable(in_dependent_table_name), this.getTable(in_reference_table_name));
    }

    // TDON
    /**
     * This method returns the neighborhood of a given table.
     * @param in_table The table.
     * @return The method returns the list of tables in the neighborhood of the given table.
     * @warning The returned list does not contain the given table.
     */
    public ArrayList<Table> getNeighborhood(Table in_table) throws Exception
    {
        ArrayList<Table> references = this.getReferenceTables(in_table);
        ArrayList<Table> dependents = this.getDependentTables(in_table);

        for(Table table: dependents)
        {
            if (! references.contains(table)) { references.add(table); }
        }

        int n = references.indexOf(in_table);
        if (-1 != n) { references.remove(n); }

        return references;
    }

    /**
     * This method returns the neighborhood of a given table, identified by its name.
     * @param in_table_name The name of the table.
     * @return The method returns the list of tables in the neighborhood of the given table, identified by its name.
     * @warning The returned list does not contain the given table.
     */
    public ArrayList<Table> getNeighborhood(String in_table_name) throws Exception
    {
        Table t = this.getTable(in_table_name);
        if (null == t) { throw new Exception("You try to get the neighborhood of a table (" + in_table_name + ") that does not exist!"); }
        return this.getNeighborhood(t);
    }

    /**
     * This method returns the neighborhood of a given list of tables.
     * The size of the neighbourhood area is defined by an integer (the zoom level).
     * @param in_tables The list of tables.
     * @param in_level Zoom level (starts at 0).
     * @return The method returns the neighborhood of the given list of tables.
     * @throws Exception
     */
    public ArrayList<Table> zoomAroundByTables(ArrayList<Table> in_tables, Integer in_level) throws Exception
    {
        // WARNING: This is "shallow copy"... But it is necessary!
        @SuppressWarnings("unchecked")
        ArrayList<Table> to_zoom  = (ArrayList<Table>)in_tables.clone();
        ArrayList<Table> tables   = new ArrayList<Table>();
        ArrayList<Table> zoomed   = new ArrayList<Table>();
        Integer level             = in_level;

        // for (Table table: to_zoom)
        // { System.out.println("0> " + table.getName()); }

        // System.out.println("> " + in_level);
        for (int l=0; l<=in_level; l++)
        {
            ArrayList<Table> new_zoom = new ArrayList<Table>();

            for (Table zoom: to_zoom)
            {
                // Did we already zoom on this table?
                if (zoomed.contains(zoom)) { continue; }
                if (! this.__tables.containsValue(zoom)) { throw new Exception("You try to zoom around a table that does not exist! Table: " + zoom.toString()); }

                // The neighborhood of a give table contains the given table.
                if (! tables.contains(zoom)) { tables.add(zoom); }

                // Get the neighborhood of the current table.
                if (level > 0)
                {
                    ArrayList<Table> neighborhood = this.getNeighborhood(zoom);

                    // System.out.println("neighborhood for " + zoom.getName());
                    // for (Table table: neighborhood)
                    // { System.out.println("1> " + table.getName()); }
                    // System.out.println();

                    for (Table table: neighborhood)
                    {
                        if (! tables.contains(table)) { tables.add(table); }
                        // We may have to zoom around this table later.
                        if (! new_zoom.contains(table)) { new_zoom.add(table); }
                    }

                }

                // OK, zoomed of the current table.
                zoomed.add(zoom);


            }
            level--;

            // WARNING: This is "shallow copy"... But it is necessary!
            to_zoom = (ArrayList<Table>)new_zoom.clone();


        }

        // for (Table table: tables)
        // { System.out.println("> " + table.getName()); }

        return tables;
    }

    /**
     * This method returns the neighborhood of a given list of tables, identified by their names.
     * The size of the neighbourhood area is defined by an integer (the zoom level).
     * @param in_table_names The list of tables' name.
     * @param in_level Zoom level (starts at 0).
     * @return The method returns the neighborhood of the given list of tables, identified by their names.
     * @throws Exception
     */
    public ArrayList<Table> zoomAroundByNames(ArrayList<String> in_table_names, Integer in_level) throws Exception
    {
        // System.out.println("zoomAroundByNames");
        ArrayList<Table> tables = new ArrayList<Table>();
        for (String table_name: in_table_names)
        {
            // System.out.println(table_name);
            Table t = this.getTable(table_name);
            if (null == t) { throw new Exception("You try to zoom around a table (" + table_name + ") that does not exist!"); }
            tables.add(t);
        }
        return this.zoomAroundByTables(tables, in_level);
    }

    // TDON
    /**
     * This method returns the relations from a given "dependent" table (to a list of "reference" table(s)).
     * @param in_dependent_table Table for which you want to get the list of relations.
     * @return The method returns a hash table that contains "table to table" relation(s).
     *         <ul>
     *              <li>Keys: A "reference" table.</li>
     *              <li>Values: The "table to table" relation between the given "dependent" table and its associated "reference" tables.</li>
     *         </ul>
     * @throws Exception
     */
    private Hashtable<Table, TableToTableRelation> __getRelationsFromTable(Table in_dependent_table) throws Exception
    {
        if (null == this.__relations) { this.generateRelations(); }
    	    Hashtable<Table, TableToTableRelation> relations = new Hashtable<Table, TableToTableRelation>();

        // Get the list of foreign keys.
        for (Field field: in_dependent_table.getForeignKeys())
        {
            FieldToFieldJoin r = new FieldToFieldJoin();
            Field target           = field.getReference();
            Table reference        = target.getTable();

            r.src_field = field;
            r.dst_field = field.getReference();
            r.src_min   = field.isNull() 	      ? Database.MIN_0 : Database.MIN_1;
            r.src_max   = field.isUnique()	      ? Database.MAX_1 : Database.MAX_N;
            r.dst_min   = target.isNull()          ? Database.MIN_0 : Database.MIN_1;
            r.dst_max   = target.isUnique()        ? Database.MAX_1 : Database.MAX_N;

            r.type      = field.isHardForeignKey() ? Database.HARD_LINK : Database.SOFT_LINK;

            if (! relations.containsKey(reference)) { relations.put(reference, new TableToTableRelation()); }
            relations.get(reference).relations.add(r);
            this.__updateMetaRelationType(relations.get(reference), r.type);
        }

        return relations;
    }

    /**
     * This method is used by the method "__getRelationsFromTable()" only.
     * @param in_meta "Table to table" relation to update.
     * @param new_relation_type Type of the new "field to field" relation found in the "table to table" relation.
     */
    private void __updateMetaRelationType(TableToTableRelation in_meta, int new_relation_type)
    {
        if (Database.UNDEFINED_LINK == in_meta.type) { in_meta.type = new_relation_type; return; };
        if (Database.SOFT_AND_HARD_LINK == in_meta.type) { return; }
        if (new_relation_type != in_meta.type) { in_meta.type = Database.SOFT_AND_HARD_LINK; }
    }

    // TDON
    /**
     * This method returns the list of <i>joins</i> (between fields) to a given "reference" table.
     * @param in_reference_table Table for which you want to get the list of <i>joins</i> to.
     * @return The method returns a hash table that contains the list of <i>joins</i> to the given (reference) table.
     *         <ul>
     *              <li>Keys : "Dependent" tables.</li>
     *              <li>Values : A list of joins.</li>
     *         </ul>
     */
    private Hashtable<Table, ArrayList<FieldToFieldJoin>> __getRelationsToTable(Table in_reference_table) throws Exception
    {
        if (null == this.__relations) { this.generateRelations(); }
        Hashtable<Table, ArrayList<FieldToFieldJoin>> relations = new Hashtable<Table, ArrayList<FieldToFieldJoin>>();

        for (Table dependent_table: this.getTables())
        {
            ArrayList<FieldToFieldJoin> rels = this.getRelationsFromReferenceToTarget(dependent_table, in_reference_table);
            if (rels.size() > 0) { relations.put(dependent_table, rels); }
        }

        // System.out.println("__getRelationsToTable() done ok");
        return relations;
    }

    // TDON
    /**
     * This method returns the JGRAPHT representation of a given set of tables.
     * @param in_tables Set of tables.
     *        If the parameter's value is null, then the model is built using all the database's tables.
     * @return The method returns the JGRAPHT representation of the given set of tables (or the complete set of tables, if the specified set is null).
     * @see http://jgrapht.org/
     * @throws Exception
     */
    public UndirectedGraph<Table, JgraphtTableToTableEdge> jgrapht(ArrayList<Table> in_tables) throws Exception
    {
        ArrayList<Table> tables = null == in_tables ? this.getTables() : in_tables;
        UndirectedGraph<Table, JgraphtTableToTableEdge> g = new Pseudograph<Table, JgraphtTableToTableEdge>(new ClassBasedEdgeFactory<Table, JgraphtTableToTableEdge>(JgraphtTableToTableEdge.class));

        // Create all nodes.
        for (Table table: tables) { g.addVertex(table); }

        // Create all edges.
        for (Table table : tables)
        {
            for (Table reference: this.getReferenceTables(table.getName()))
            {
                if (! g.addEdge(table, reference, new JgraphtTableToTableEdge()))
                {
                    System.out.println("The edge already exists!");
                }
            }
        }

        return g;
    }

    /**
     * This method produces a textual representation of the database.
     * @return The method produces a textual representation of the database.
     * @remark This method is used for debugging.
     */
    public String toString()
    {
        ArrayList<String> result = new ArrayList<String>();
        Enumeration<Table> e = this.__tables.elements();

        result.add(this.__name);
        while (e.hasMoreElements())
        {
            Table t = e.nextElement();
            result.add(Strings.indent("\t", t.toString()));
        }

        return Strings.joinWithNewLines(result);
    }
    
    /**
     * This method returns an identifier that does not represent a table's name.
     * Each call to this method returns a unique identifier.
     * @return The method returns an identifier that does not represent a table's name.
     * @throws Exception
     */
    public String getNonTableTag() throws Exception
    {
        
        int lower    = 111;
        int higher   = 999999;
        String name  = null;

        while (Boolean.TRUE)
        {
            Integer random  = (int)(Math.random() * (higher-lower)) + lower;
            name = "T" + org.dbview.utils.Strings.SHAsum(random.toString().getBytes()) + "_" + Database.__n.toString();
            Database.__n += 1;
            if (null == this.getTable(name)) { break; }
        }
        return name;
    }

}
