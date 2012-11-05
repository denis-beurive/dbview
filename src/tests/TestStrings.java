package tests;

import org.dbview.utils.Strings;
import org.junit.Before;
import org.junit.Test;

public class TestStrings
{
    @Before
    public void setUp()
    {
        System.out.println("");
        System.out.println("------------------------------------------------------------");
        System.out.println("Testing Strings.");
        System.out.println("------------------------------------------------------------");
        System.out.println("");
    }
    
    @Test public void testDashTolowerCamelCase()
    {
        String t1 = "-ab-c-d";  // -> "AbCD"
        String t2 = "a-bc-d-";  // -> "aBcD-"
        String t3 = "acbd";     // -> "abcd"
        String t4 = "a-b-c-d";  // -> "aBCD"
        
        System.out.println(Strings.dashToLowerCamelCase(t1));
        System.out.println(Strings.dashToLowerCamelCase(t2));
        System.out.println(Strings.dashToLowerCamelCase(t3));
        System.out.println(Strings.dashToLowerCamelCase(t4));
    }
    
    @Test public void testLowerCamelCaseToDash()
    {
        String t1 = "AbCD";  // -> "-ab-c-d"
        String t2 = "aBcD";  // -> "a-bc-d
        String t3 = "acbd";  // -> "abcd"
        String t4 = "aBCD";  // -> "a-b-c-d"
        
        System.out.println(Strings.lowerCamelCaseToDash(t1));
        System.out.println(Strings.lowerCamelCaseToDash(t2));
        System.out.println(Strings.lowerCamelCaseToDash(t3));
        System.out.println(Strings.lowerCamelCaseToDash(t4));
    }
    
    @Test public void testFirstToUpper()
    {
        String t1 = "AbCD";  // -> "AbCD"
        String t2 = "aBcD";  // -> "aBcD"
        String t3 = "1cbd";  // -> "1cbd"
        String t4 = "aBCD";  // -> "aBCD"
        String t5 = "a";     // -> "A"
        String t6 = "aB";    // -> "AB"
        
        System.out.println(Strings.firstToUppercase(t1));
        System.out.println(Strings.firstToUppercase(t2));
        System.out.println(Strings.firstToUppercase(t3));
        System.out.println(Strings.firstToUppercase(t4));
        System.out.println(Strings.firstToUppercase(t5));
        System.out.println(Strings.firstToUppercase(t6));
    }
    
    @Test public void testFirstToLower()
    {
        String t1 = "AbCD";  // -> "abCD"
        String t2 = "aBcD";  // -> "aBcD"
        String t3 = "1cbd";  // -> "1cbd"
        String t4 = "aBCD";  // -> "aBCD"
        
        System.out.println(Strings.firstTolowercase(t1));
        System.out.println(Strings.firstTolowercase(t2));
        System.out.println(Strings.firstTolowercase(t3));
        System.out.println(Strings.firstTolowercase(t4));
    }
    
    @Test public void testDashToUpperCamelCase()
    {
        String t1 = "-ab-c-d";  // -> "AbCD"
        String t2 = "a-bc-d-";  // -> "ABcD-"
        String t3 = "acbd";     // -> "Abcd"
        String t4 = "a-b-c-d";  // -> "ABCD"
        
        System.out.println(Strings.dashToUpperCamelCase(t1));
        System.out.println(Strings.dashToUpperCamelCase(t2));
        System.out.println(Strings.dashToUpperCamelCase(t3));
        System.out.println(Strings.dashToUpperCamelCase(t4));
    }
    
    @Test public void testUpperCamelCaseToDash()
    {
        String t1 = "AbCD";  // -> "ab-c-d"
        String t2 = "ABcD";  // -> "a-bc-d
        String t3 = "Acbd";  // -> "abcd"
        String t4 = "ABCD";  // -> "a-b-c-d"
        
        System.out.println(Strings.upperCamelCaseToDash(t1));
        System.out.println(Strings.upperCamelCaseToDash(t2));
        System.out.println(Strings.upperCamelCaseToDash(t3));
        System.out.println(Strings.upperCamelCaseToDash(t4));
    }
    
}
