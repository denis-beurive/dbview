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

import java.util.Arrays;
import java.util.List;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.Formatter;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * This class contains tools used to manipulate strings.
 * @author Denis Beurive
 */
public class Strings
{
    /**
     * New line separator.
     */
	private static String __newLine = System.getProperty("line.separator");

	/**
	 * This method joins all elements of a given list using new lines as separator.
	 * @param in_list List to join.
	 * @return The method returns a string.
	 */
	public static String joinWithNewLines(List<String> in_list)
	{
		if (in_list.size() > 0)
		{
			int index = 0;
			String result = "";
			for (index=0; index<in_list.size()-1; index++) { result =  result.concat(in_list.get(index) + Strings.__newLine); }
			result = result.concat(in_list.get(index));
			return result;
		}
		return "";
	}

	/**
	 * This method joins all elements of a given list using a given separator.
	 * @param in_list List to join.
	 * @param in_separator Separator to use.
	 * @return The method returns a string.
	 */
	public static String join(List<String> in_list, String in_separator)
	{
		if (in_list.size() > 0)
		{
			int index = 0;
			String result = "";
			for (index=0; index<in_list.size()-1; index++) { result =  result.concat(in_list.get(index) + in_separator); }
			result = result.concat(in_list.get(index));
			return result;
		}
		return "";
	}

	/**
	 * This method adds a given prefix at the beginning of all lines in a given text.
	 * @param in_prefix String to add at the beginning of all lines.
	 * @param in_text Text to indent.
	 * @return The method returns the indented text.
	 */
	public static String indent(String in_prefix, String in_text)
	{
	    ArrayList<String> r = new ArrayList<String>();
	    String lines[] = in_text.split(Strings.__newLine);
	    for (int i=0; i<lines.length; i++) { r.add(in_prefix.concat(lines[i])); }
	    return Strings.joinWithNewLines(r);
	}

	/**
	 * This method returns the size of the longest string in a list of strings.
	 * @param in_list List of strings.
	 * @return The method returns the size of the longest string in a list of strings.
	 */
	public static int longest(ArrayList<String> in_list)
	{
		int max = 0;
		for (int i=0; i<in_list.size(); i++) { max = in_list.get(i).length() > max ? in_list.get(i).length() : max; }
		return max;
	}

	/**
	 * This method adds a given padding to the end of a given string.
	 * @param in_string The string that will be padded.
	 * @param in_length Length of the string at the end of the operation.
	 * @param in_padding Padding to add.
	 * @return The method returns the padded string.
	 */
	public static String paddingRight(String in_string, int in_length, Character in_padding)
	{
	    String new_string = in_string;
	    int lenght        = new_string.length();

	    for (int j=0; j<in_length-lenght; j++) { new_string += in_padding.toString(); }
	    return new_string;
	}

	/**
	 * This method adds spaces to the end of all elements of a given array.
	 * @param in_list Array of elements.
	 * @param in_length Minimum length for all elements, at the end of the operation.
	 */
	public static void paddingRight(ArrayList<String> in_list, int in_length)
	{
		for (int i=0; i<in_list.size(); i++)
		{
			String	new_string = in_list.get(i);
			int	 	length = new_string.length();

			for (int j=0; j<in_length-length; j++) { new_string += " "; }
			in_list.set(i, new_string);
		}
	}

	/**
	 * This method adds a given padding to the end of all elements of a given array.
	 * @param in_list Array of elements.
	 * @param in_length Minimum length for all elements, at the end of the operation.
	 * @param in_padding Padding to add.
	 */
	public static void paddingRight(ArrayList<String> in_list, int in_length, Character in_padding)
	{
		for (int i=0; i<in_list.size(); i++)
		{
			String	new_string = in_list.get(i);
			int	 	length = new_string.length();

			for (int j=0; j<in_length-length; j++) { new_string += in_padding.toString(); }
			in_list.set(i, new_string);
		}
	}

	/**
	 * This method adds spaces to the end of all elements of a given array of strings.
	 * At the end of the operation, all elements of the array have the same length.
	 * The length of all elements is equal to the length of the largest element plus a given gap.
	 * @param in_list Array of elements.
	 * @param in_gap Size of the gap.
	 */
	public static void margingRight(ArrayList<String> in_list, int in_gap)
	{
		int max = Strings.longest(in_list);
		Strings.paddingRight(in_list, max+in_gap);
	}

	/**
	 * This method converts a string into an array list of strings.
	 * @param in_source String to convert.
	 * @return The method returns the array list of strings.
	 */
	public static ArrayList<String> stringToArrayList(String in_source)
	{
	    ArrayList<String> list_of_chars = new ArrayList<String>();
	    for (char c: in_source.toCharArray()) { list_of_chars.add(Character.toString(c)); }
	    return list_of_chars;
	}

	/**
	 * This method returns a string that is the concatenation of an array list of strings.
	 * @param in_array Array list of strings to convert.
	 * @return The method returns the string that represents the concatenation of the given array.
	 */
	public static String arrayListToString(ArrayList<String> in_array)
	{
	    String res = new String();
	    for (String s: in_array) { res = res.concat(s); }
	    return res;
	}

	/**
	 * This method converts a string that contains dashes into "Lower Camel Case".
	 * Example: "a-bc-de" becomes "aBcDe".
	 * @param in_source String to convert.
	 * @return The method converts the given string into lower Camel case.
	 */
	public static String dashToLowerCamelCase(String in_source)
	{
	    ArrayList<String> list_of_chars = Strings.stringToArrayList(in_source);

	    while (true)
	    {
	        int dash_index = list_of_chars.indexOf("-");
	        if (-1 == dash_index) { break; }
	        if ((dash_index + 1) == list_of_chars.size()) { break; }
	        list_of_chars.set(dash_index+1, list_of_chars.get(dash_index+1).toUpperCase());
	        list_of_chars.remove(dash_index);
	    }

	    return Strings.arrayListToString(list_of_chars);
	}

    /**
     * This method converts a string that contains dashes into "Upper Camel Case".
     * Example: "a-bc-de" becomes "ABcDe".
     * @param in_source String to convert.
     * @return The method converts the given string into upper Camel case.
     */
    public static String dashToUpperCamelCase(String in_source)
    {
        return Strings.firstToUppercase(Strings.dashToLowerCamelCase(in_source));
    }

	/**
	 * This method returns the dashed representation of a given "Lower Camel Case" string.
	 * Example: "aBcDe" -> "a-bc-de"
	 * @param in_source "Lower Camel Case" to convert.
	 * @return The method returns the dashed representation of the given "Lower Camel Case" string.
	 */
	public static String lowerCamelCaseToDash(String in_source)
	{
	    Pattern p = Pattern.compile("^[a-zA-Z]$");
	    ArrayList<String> list_of_chars = Strings.stringToArrayList(in_source);
	    ArrayList<String> dash          = new ArrayList<String>();

        for (int i = 0; i < list_of_chars.size(); i++)
        {
            String c = list_of_chars.get(i);
            Matcher matcher = p.matcher(c);

            if (! matcher.matches()) { dash.add(c); continue; }

            if (0 == c.toUpperCase().compareTo(c))
            { dash.add("-" + c.toLowerCase()); }
            else { dash.add(c); }
        }

        return Strings.arrayListToString(dash);
	}

    /**
     * This method returns the dashed representation of a given "Upper Camel Case" string.
     * Example: "ABcDe" -> "a-bc-de"
     * @param in_source "Lower Camel Case" to convert.
     * @return The method returns the dashed representation of the given "Upper Camel Case" string.
     */
    public static String upperCamelCaseToDash(String in_source)
    {
        return Strings.lowerCamelCaseToDash(Strings.firstTolowercase(in_source));
    }

	/**
	 * This method converts the first character of a given string into upper case.
	 * @param in_string String to convert.
	 * @return The method returns the converted string.
	 */
	public static String firstToUppercase(String in_string)
	{
	    if (0 == in_string.length()) { return ""; }
	    if (1 == in_string.length()) { return in_string.substring(0, 1).toUpperCase(); }
	    return in_string.substring(0, 1).toUpperCase() + in_string.substring(1);
	}

	/**
     * This method converts the first character of a given string into lower case.
     * @param in_string String to convert.
     * @return The method returns the converted string.
     */
    public static String firstTolowercase(String in_string)
    {
        if (0 == in_string.length()) { return ""; }
        if (1 == in_string.length()) { return in_string.substring(0, 1).toLowerCase(); }
        return in_string.substring(0, 1).toLowerCase() + in_string.substring(1);
    }

    /**
     * Test if a string is empty or null.
     * @param in_string String to test.
     * @return The method returns TRUE if the string is empty or null.
     *         Otherwise, it returns FALSE.
     */
    public static Boolean isEmpty(String in_string)
    {
        if (null == in_string) { return Boolean.TRUE; }
        if (0 == in_string.length()) { return Boolean.TRUE; }
        return Boolean.FALSE;
    }
    
    /**
     * Convert an array of bytes into a string that represents a hexadecimal value.
     * @param in_hash Array of bytes.
     * @return The method returns a string of hexadecimal characters that represents the given array of bytes.
     */
    private static String byteArray2Hex(final byte[] in_hash)
    {
        Formatter formatter = new Formatter();
        for (byte b : in_hash) { formatter.format("%02x", b); }
        return formatter.toString();
    }

    /**
     * Compute the SHA of a given array of bytes.
     * The returned value is a string of hexadecimal characters.
     * @param in_convertme Array of bytes to process.
     * @return The method returns a string that represents the SHA of the given array of bytes.
     * @note We ignore exception "NoSuchAlgorithmException" (since the Algorithm "SHA-1" exists)
     */
    public static String SHAsum(byte[] in_convertme)
    {
        try {
                MessageDigest md = MessageDigest.getInstance("SHA-1");
                return byteArray2Hex(md.digest(in_convertme));
            }
        catch (Exception e) { }
        return "SHA-1 Algorithm does not exist!";
    }
    
    /**
     * Compute the SHA of a given string.
     * The returned value is a string of hexadecimal characters.
     * @param in_convertme String to process.
     * @return The method returns a string that represents the SHA of the given string.
     */
    public static String SHAsum(String in_convertme)
    {
        return Strings.SHAsum(in_convertme.getBytes());
    }
}
