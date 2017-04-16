package org.bohdi.protobuf.inspector;


import com.google.protobuf.Message;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;


public class IdeaTest {

    @Test
    public void test_filter() {
        List<Object> objects = new ArrayList<Object>();
        objects.add("Hello");
        objects.add(14);
        objects.add("Goodbye");
        objects.add(new Object());



    }

    private <T> List<T> filter(Class<T> clazz, List<Object> objects) {
        List<T> found = new ArrayList<T>();

        for (Object object : objects) {
            if (clazz.isInstance(object))
                found.add((T) object);
        }
        return found;
    }
}
