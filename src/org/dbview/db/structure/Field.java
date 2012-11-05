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

import org.dbview.db.structure.Table;

/**
 * This class represents a table's field.
 * @author Denis Beurive
 */
public class Field
{
	/**
	 * This constant is used to say that a field is not an index.
	 */
    final static int NOT_INDEX      = 0;

    /**
     * This constant is used to say that a field is not a key.
     */
    final static int NOT_KEY        = 0;

    /**
     * This constant is used to say that a field is a multiple index.
     */
    final static int MULTIPLE_INDEX = 1;

    /**
     * This constant is used to say that a field is a unique index.
     */
    final static int UNIQUE_INDEX   = 2;

    /**
     * This constant is used to say that a field is a (hard or soft) foreign key.
     */
    final static int KEY_FOREIGN    = 1;

    /**
     * This constant is used to say that a field is a primary index.
     */
    final static int KEY_PRIMRY     = 2;
    
    /**
     * This constant is used to say that a field is a "dead" foreign key.
     */
    final static int KEY_DEAD_FOREIGN = 3;

    /**
     * This constant is used to say that a field can not be null.
     */
    final static int NOT_NULL       = 0;

    /**
     * This constant is used to say that a field can be null.
     */
    final static int CAN_BE_NULL    = 1;

    /** Name of the table. */
    private Table __table = null;

    /** Name of the field. */
    private String __name = null;
    
    /** Name of the index that uses this field. */
    private String __index_name = null;

    /** Is the field a key? */
    private int __key = Field.NOT_KEY;

    /** Is the field an index? **/
    private int __index = Field.NOT_INDEX;

    /** Can the field be null? **/
    private int __null = Field.NOT_NULL;

    /** If the field is a foreign key (type IFieldTypes.FOREIGN_KEY), then his member represents the field's "reference". */
    private Field __reference = null;

    /** If this field is a "reference" for a foreign key, then this field represents the foreign key. */
    private Field __fk = null;

    /** If the field is a foreign key, then this attribute tells whether the field is a "hard" (as opposed to "soft") foreign key or not.  */
    private Boolean __fk_hard = Boolean.TRUE;
    
    /** If the field is a "dead" foreign key (type IFieldTypes.DEAD_FOREIGN_KEY), then his member represents the "dead" field's "reference". */
    private DeadForeignKey __dead_reference = null;

    /**
     * Assuming that this field is a dead foreign key, then the method returns the dead foreign key's reference.
     * @return If this field is a dead foreign key, then the method returns its reference.
     */
    public DeadForeignKey getDeadForeignKey() throws org.dbview.db.structure.FieldException
    {
        if (! this.isDeadForeignKey()) { throw new FieldException("Field " + this.getFullName() + " is not a dead foreing key!"); }
        return this.__dead_reference;
    }
    
    /**
     * This method is used to specify that a foreign key is a soft one.
     * @remark A soft foreign key defines a soft join. A soft join is defined by foreign keys' names only. You can find this type of joins in MyIsam (MySql) tables, for example.
     */
    public void isASoftForeignKey()
    {
        this.__fk_hard = Boolean.FALSE;
    }

    /**
     * This method is used to specify that a foreign key is a hard one.
     * @remark A hard foreign key defines a hard join. A hard join is defined by the table's constraint.
     */
    public void isAHardForeignKey()
    {
        this.__fk_hard = Boolean.TRUE;
    }

    /**
     * Test if a foreign key is soft.
     * @return The method returns the value TRUE if the foreign key is soft.
     */
    public Boolean isSoftForeignKey()
    {
        return ! this.__fk_hard;
    }

    /**
     * Test if a foreign key is hard.
     * @return The method returns the value TRUE if the foreign key is hard.
     */
    public Boolean isHardForeignKey()
    {
        return this.__fk_hard;
    }

    /**
     * Set the name of the index that uses this field.
     * @param in_index_name Name of the index.
     */
    public void setIndexName(String in_index_name)
    {
        this.__index_name = in_index_name;
    }

    /**
     * Return the name of the index that uses this field.
     * @return The method returns the name of the index that uses this field.
     */
    public String getIndexName()
    {
        return this.__index_name;
    }
    
    /**
     * Create a field, and add the field to a table.
     * @param in_table Table that contains <i>this</i> field.
     * @param in_name Name of the field.
     * @throw org.dbview.db.structure.TableException
     */
    public Field(Table in_table, String in_name) throws org.dbview.db.structure.TableException
    {
        this.__name = in_name;
        this.__table = in_table;
        in_table.addField(this);
    }

    /**
     * Set the field "can be NULL".
     */
    public void isANull()
    {
        this.__null = Field.CAN_BE_NULL;
    }

    /**
     * Set the field a primary key.
     */
    public void isAPrimaryKey()
    {
        this.__key = Field.KEY_PRIMRY;
    }

    /**
     * Set the field a foreign key.
     * @param in_reference "Reference" field.
     */
    public void isAforeignKey(Field in_reference) throws org.dbview.db.structure.FieldException
    {
        // Debug utile:
        // if (this.__fk != null) System.out.println("      " +  this.getFullName() + ".__fk = " + this.__fk.getFullName() + " reference is " + in_reference.getFullName());
        
        // We make sure that the field has not already been defined as a foreign key.
        if (this.__key == Field.KEY_FOREIGN) // if (null != this.__fk) // BUG !!!
        { throw new FieldException("Field " + in_reference.getFullName() + " has already been assigned to a foreign key (" + in_reference.__fk.getFullName() + ")!"); }
        
        // We make sure that the foreign key does not point to its own table.
        // Note: A foreign key *CAN* point to its own table.
        // if (0 == in_target.getTableName().compareTo(this.getTableName()))
        // { throw new FieldException("Field " + this.__name + ": You try to set a foreign key the same table than the foreign key itself!"); }

        this.__key       = Field.KEY_FOREIGN;
        this.__reference = in_reference;

        in_reference.__fk = this;
    }
    
    /**
     * Set the field a "dead" foreign key.
     * @param in_reference_table Name of the reference table.
     * @param in_reference_field Name of the reference field.
     */
    public void isADeadforeignKey(String in_reference_table, String in_reference_field) throws org.dbview.db.structure.FieldException
    {
        // We make sure that the field has not already been defined as a foreign key.
        if (this.__key == Field.KEY_FOREIGN) 
        { throw new FieldException("Field " + in_reference_field + " has already been assigned to a foreign key!"); }
        
        // We make sure that the field has not already been defined as a dead foreign key.
        if (this.__key == Field.KEY_DEAD_FOREIGN)
        { throw new FieldException("Field " + in_reference_field + " has already been assigned to a dead foreign key!"); }
        
        this.__key            = Field.KEY_DEAD_FOREIGN;
        this.__dead_reference = new DeadForeignKey(in_reference_table, in_reference_field); 
    }

    /**
     * Set the field a unique index.
     */
    public void isAUniqueIndex()
    {
        this.__index = Field.UNIQUE_INDEX;
    }

    /**
     * Set the field a multiple index.
     */
    public void isAMultipleIndex()
    {
        this.__index = Field.MULTIPLE_INDEX;
    }

    /**
     * If the field is a "reference" for a foreign key, then the method returns the foreign key.
     * @return If the field is a "reference" for a foreign key, then the method returns the foreign key.
     *         Otherwise, the method returns the value null.
     */
    public Field getFkToMe()
    {
        return this.__fk;
    }

    /**
     * Return the name of the field.
     * @return The method returns the name of the field.
     */
    public String getName()
    {
        return this.__name;
    }

    /**
     * This method returns the full name of the field. The full name includes the table's name.
     * @return The method returns the full name of the field.
     */
    public String getFullName()
    {
        return this.__table.getName() + "." + this.__name;
    }

    /**
     * Return the field's "reference" (if the field is a foreign key).
     * @return The method returns the field's "reference".
     * @throws org.dbview.db.structure.FieldException
     */
    public Field getReference() throws org.dbview.db.structure.FieldException
    {
        if (Field.KEY_FOREIGN != this.__key)
        { throw new FieldException("The field \"" + this.__name + "\" is not a foreign key."); }
        return this.__reference;
    }

    /**
     * Return the table that contains <i>this</i> field.
     * @return The method returns the table that contains "this" field.
     */
    public Table getTable()
    {
        return this.__table;
    }

    /**
     * This method returns the name of the field's table.
     * @return The method returns the name of the field's table.
     */
    public String getTableName()
    {
        return this.__table.getName();
    }

    /**
     * Test if the field is a primary key.
     * @return The method returns the value TRUE if the field is a primary key.
     *         Otherwise the method returns the value FALSE.
     */
    public Boolean isPrimaryKey()
    {
        return Field.KEY_PRIMRY == this.__key;
    }

    /**
     * Test if the field is a foreign key.
     * @return The method returns the value TRUE if the field is a foreign key.
     *         Otherwise the method returns the value FALSE.
     */
    public Boolean isForeignKey()
    {
        return Field.KEY_FOREIGN == this.__key;
    }
    
    /**
     * Test if the field is a dead foreign key.
     * @return The method returns the value TRUE if the field is a dead foreign key.
     *         Otherwise the method returns the value FALSE.
     */
    public Boolean isDeadForeignKey()
    {
        return Field.KEY_DEAD_FOREIGN == this.__key;
    }

    /**
     * Test if the field is a key (primary or foreign).
     * @return The method returns the value TRUE if the field is a key.
     *         Otherwise the method returns the value FALSE.
     */
    public Boolean isKey()
    {
        return Field.KEY_FOREIGN == this.__key
               ||
               Field.KEY_PRIMRY == this.__key;
    }

    /**
     * Test if the field is a unique index.
     * @return The method returns the value TRUE if the field is a unique index.
     *         Otherwise the method returns the value FALSE.
     */
    public Boolean isUniqueIndex()
    {
        return Field.UNIQUE_INDEX == this.__index;
    }

    /**
     * Test if the field has a unique constraint.
     * @return The method returns the value TRUE if the field is a unique constraint.
     *         Otherwise the method returns the value FALSE.
     */
    public Boolean isUnique()
    {
        return this.isUniqueIndex();
    }

    /**
     * Test if the field is a multiple index.
     * @return The method returns the value TRUE if the field is a multiple index.
     *         Otherwise the method returns the value FALSE.
     */
    public Boolean isMultipleIndex()
    {
        return Field.MULTIPLE_INDEX == this.__index;
    }

    /**
     * Test if the field is an index.
     * @return The method returns the value TRUE if the field is an index.
     *         Otherwise the method returns the value FALSE.
     */
    public Boolean isIndex()
    {
        return Field.UNIQUE_INDEX == this.__index
               ||
               Field.MULTIPLE_INDEX == this.__index;
    }

    /**
     * Test if the field can be null.
     * @return The method returns the value TRUE if the field can be null.
     */
    public Boolean isNull()
    {
        return this.__null == Field.CAN_BE_NULL;
    }

    /**
     * Create a textual representation of the field.
	 * @return The method returns a textual representation of the field.
     */
    public String toString()
    {
        String prefix = "";
        String back   = "";

        if (null != this.__fk) { back = " <= " + this.__fk.getFullName(); }

    	if (this.isPrimaryKey())  { return "PK " + this.getFullName() + back; }

    	if (this.isUniqueIndex()) { prefix = "UI"; }
    	else if (this.isMultipleIndex()) { prefix = "MI"; }
    	else { prefix = "ST"; }

    	if (this.isForeignKey()) { return "FK(" + prefix + ") " + this.getFullName() + " => " + this.__reference.getFullName() + back; }
    	
    	if (this.isDeadForeignKey()) { return "DFK(" + prefix + ") " + this.getFullName() + " => " + this.__dead_reference.getFullName() + back; }

    	return prefix + " " + this.getFullName() + back;
    }
}
