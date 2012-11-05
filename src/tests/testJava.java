package tests;

import org.junit.Test;

public class testJava
{
    class MyRawClass<V>
    {
        @SuppressWarnings("unused")
        private V __attribut = null;
        public MyRawClass(V in_value) { this.__attribut = in_value; }
    }
    

    
    @Test public void tsst1()
    {
        // Class c = MyRawClass.class;
        // Eclipse says: "Class is a raw type. References to generic type Class<T> should be paremeterized".
        // But the "javac" compiler does not warn about it.
        
        // Fix the Warning.
        @SuppressWarnings("unused")
        Class<?> c = MyRawClass.class;
    }
}
