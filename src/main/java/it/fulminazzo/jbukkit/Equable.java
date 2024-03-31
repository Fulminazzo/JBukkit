package it.fulminazzo.jbukkit;

import it.fulminazzo.fulmicollection.objects.Printable;
import it.fulminazzo.fulmicollection.utils.ReflectionUtils;

public class Equable extends Printable {

    public boolean equals(Object o) {
        if (o instanceof Equable) return equals(this, o);
        return super.equals(o);
    }

    public static boolean equals(Object o1, Object o2) {
        return ReflectionUtils.equalsClass(o1, o2) && ReflectionUtils.equalsFields(o1, o2);
    }
}
