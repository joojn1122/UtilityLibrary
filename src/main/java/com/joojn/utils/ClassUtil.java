package com.joojn.utils;

public class ClassUtil {

    // Consumer = void(T)
    // Supplier = T()
    // Function = R(T)
    // BiFunction = R(T, U)
    // Predicate = boolean(T)
    // BiPredicate = boolean(T, U)

    public interface ClassWalker {
        boolean walk(Class<?> clazz);
    }

    public static void walk(
            boolean searchAll,
            Class<?> topClass,
            ClassWalker walker
    )
    {
        if(!searchAll)
        {
            walker.walk(topClass);
            return;
        }

        Class<?> currentClass = topClass;
        do
        {
            if(walker.walk(currentClass)) break;
        }
        while((currentClass = currentClass.getSuperclass()) != Object.class);

        // don't forget about Object class
        walker.walk(Object.class);
    }

}