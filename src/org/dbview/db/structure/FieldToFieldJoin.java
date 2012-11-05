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

/**
 * <p>This class represents a join (between two fields). A join is defined by a foreign key (defined in the "dependent" table).</p>
 * <p>Please note that we define two kinds of foreign keys :</p>
 * <ul>
 *      <li>Soft foreign keys. A hard foreign key is associated to a database constraint.</li>
 *      <li>Hard foreign keys. A soft foreign key is defined only by its name (this implies the existence of naming conventions). Example : "fk_customer_id" is a foreign key. It joins the field "fk_customer_id" to the field "id" of the table "customer".</li>
 * </ul>
 * <p>The join connects a "dependent" field - which is the foreign key - (in a dependent table), to a "reference" field (in a reference table).</p>
 * <p>A join contains 2 cardinalities :</p>
 * <ul> 
 *      <li>One cardinality represents the number of "reference" fields potentially associated to one "dependent" field.</li>
 *      <li>The other cardinality represents the number of "dependent" fields potentially associated to one "reference" fields.</li>
 * </ul>
 * <p>Cardinalities are represented by a character.</p>
 * <ul>
 *      <li>?: Zero or one.</li>
 *      <li>*: Zero or more.</li>
 *      <li>+: At least one (1,2,3...).</li>
 *      <li>1: One.</li>
 * </ul>
 * 
 * @remark Please note that these conventions are inspired by the regular expressions. 
 * @author Denis Beurive
 */
public class FieldToFieldJoin
{
	/**
	 * Minimum occurrence of the field at the origin - in the dependent table - of the relation (values can be "0" or "1").
	 * Values:
	 * <ul>
	 *     <li>Database.MIN_0</li>
	 *     <li>Database.MIN_1</li>
	 * </ul>
	 */
    public int src_min;

    /**
     * Maximum occurrence of the field at the origin - in the dependent table - of the relation (values can be "1" or "N").
     * Value:
     * <ul>
     *      <li>Database.MAX_1</li>
     *      <li>Database.MAX_N</li>
     * </ul>
     */
    public int src_max;

    /**
     * Minimum occurrence of the field at the end - in the reference table - of the relation (values can be "0" or "1").
     * Value:
     * <ul>
     *      <li>Database.MAX_1</li>
     *      <li>Database.MAX_N</li>
     * </ul>
     */
    public int dst_min;

    /**
     * Maximum occurrence of the field at the end - in the reference table - of the relation (values can be "1" or "N").
     * Value:
     * <ul>
     *      <li>Database.MAX_1</li>
     *      <li>Database.MAX_N</li>
     * </ul>
     */
    public int dst_max;

    /**
     * Field at the origin of the relation (in the dependent table).
     * @remark To get the field's table: src_field.getTable().
     */
    public Field src_field;

    /**
     * Field at the destination of the relation (in the reference table).
     * @remark To get the field's table: dst_field.getTable().
     */
    public Field dst_field;

    /**
     * This attribute represents the type of join. It can be:
     * <ul>
     *      <li>Database.UNDEFINED_LINK</li>
     *      <li>Database.SOFT_LINK</li>
     *      <li>Database.HARD_LINK</li>
     * </ul>
     */
    public int type = Database.UNDEFINED_LINK;

    /**
     * This method returns a textual representation of the type of the join.
     * @return The method returns a textual representation of the type of the join.
     * @throws Exception
     */
    public String type() throws Exception
    {
        switch (this.type)
        {
            case Database.UNDEFINED_LINK:       return "undefined";
            case Database.HARD_LINK:            return "hard";
            case Database.SOFT_LINK:            return "soft";
            default: throw new Exception("You try to print a relation which type is not set!");
        }
    }

    /**
     * This method returns a textual representation of the cardinality from the "dependent field" to the "reference field".
     * @return The method returns a textual representation of the cardinality.
     */
    public String cardinality_from_dependent_to_reference()
    {
        // @A est associé à minA..maxB @B
        return this.__getSymbol(this.src_min, this.dst_max);
    }

    /**
     * This method returns a textual representation of the cardinality from the "reference field" to the "dependent field".
     * @return The method returns a textual representation of the cardinality.
     */
    public String cardinality_from_reference_to_dependent()
    {
        // @B est associé à 0..maxA @A
        return this.__getSymbol(Database.MIN_0, this.src_max);
    }

    /**
     * <p>This method returns the symbol that represents a cadinality.</p>
     * <p>Cardinalities are represented by a character.</p>
     * <ul>
     *      <li>?: Zero or one.</li>
     *      <li>*: Zero or more.</li>
     *      <li>+: At least one (1,2,3...).</li>
     *      <li>1: One.</li>
     * </ul>
     * @param in_min Minimum occurrence of the field.
     * @param in_max Maximtm occurrence of the field.
     * @remark Please note that these conventions are inspired by the regular expressions.
     * @return This method returns the symbol that represents a cadinality.
     */
    private String __getSymbol(int in_min, int in_max)
    {
        if ((Database.MIN_0 == in_min) && (Database.MAX_1 == in_max)) { return "?"; }
        if ((Database.MIN_0 == in_min) && (Database.MAX_N == in_max)) { return "*"; }
        if ((Database.MIN_1 == in_min) && (Database.MAX_1 == in_max)) { return "1"; }
        if ((Database.MIN_1 == in_min) && (Database.MAX_N == in_max)) { return "+"; }
        return "!!!";
    }
}
