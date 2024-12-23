package it.fulminazzo.jbukkit;

import it.fulminazzo.fulmicollection.objects.Printable;
import it.fulminazzo.fulmicollection.objects.Refl;
import it.fulminazzo.fulmicollection.utils.ReflectionUtils;
import lombok.NoArgsConstructor;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.util.Objects;

/**
 * A class used to compare two objects field by field.
 */
@NoArgsConstructor
public class Equable extends Printable {

    /**
     * Simulates the comparison of the current object with <code>null</code>.
     *
     * @return true only if every field is "empty". More specifically, a field is empty only if: <ul>     <li>is a primitive type and {@link #checkPrimitive(Class, Object)} returns <code>true</code>;</li>     <li>is null;</li>     <li>is not null, has the method <code>boolean isEmpty</code> and when invoked it returns <code>true</code>.</li> </ul>
     */
    protected boolean compareNull() {
        return compareNull(this);
    }

    /**
     * Simulates the comparison of the given object with <code>null</code>.
     *
     * @param object the object
     * @return true only if every field is "empty". More specifically, a field is empty only if: <ul>     <li>is a primitive type and {@link #checkPrimitive(Class, Object)} returns <code>true</code>;</li>     <li>is null;</li>     <li>is not null, has the method <code>boolean isEmpty</code> and when invoked it returns <code>true</code>.</li> </ul>
     */
    public static boolean compareNull(final @NotNull Object object) {
        Refl<?> refl = new Refl<>(object);
        for (final Field field : refl.getNonStaticFields()) {
            // Do not compare fields in inner classes
            if (field.getName().matches("this\\$[0-9]+")) continue;
            final Class<?> type = field.getType();
            final Object obj = refl.getFieldObject(field);
            if (obj == null) continue;
            else if (type.isAssignableFrom(Class.class)) return false;
            else if (ReflectionUtils.isPrimitive(type))
                if (checkPrimitive(type, obj)) continue;
                else return false;
            else if (obj instanceof Equable)
                if (((Equable) obj).compareNull()) continue;
                else return false;
            else if (field.getType().isArray()) {
                for (int i = 0; i < Array.getLength(obj); i ++) {
                    Object arrayElement = Array.get(obj, i);
                    if (!compareNull(arrayElement)) return false;
                }
                continue;
            } else {
                Refl<?> objRefl = new Refl<>(obj);
                try {
                    if (((boolean) Objects.requireNonNull(objRefl.invokeMethod(boolean.class, "isEmpty"))))
                        continue;
                    else return false;
                } catch (IllegalArgumentException ignored) {}
            }
            return compareNull(obj);
        }
        return true;
    }

    /**
     * Verifies that the given object is of primitive type.
     * If it is, verifies that its value is 0.
     *
     * @param type   the type
     * @param object the object
     * @return true if it is
     */
    static boolean checkPrimitive(final @NotNull Class<?> type, final @NotNull Object object) {
        if (!ReflectionUtils.isPrimitive(type)) return false;
        if (type.equals(boolean.class)) return !((boolean) object);
        else if (type.equals(byte.class)) return ((byte) object) == 0;
        else if (type.equals(short.class)) return ((short) object) == 0;
        else if (type.equals(char.class)) return ((char) object) == 0;
        else if (type.equals(int.class)) return ((int) object) == 0;
        else if (type.equals(long.class)) return ((long) object) == 0L;
        else if (type.equals(float.class)) return ((float) object) == 0.0f;
        else if (type.equals(double.class)) return ((double) object) == 0.0d;
        return false;
    }

    /**
     * Checks if an object is equal to the current one.
     * If it is null, {@link #compareNull()} is used.
     *
     * @param object the object
     * @return true if they are equal
     */
    public boolean equalsNull(Object object) {
        if (object == null) return compareNull();
        return equals(this, object);
    }

    @Override
    public boolean equals(Object o) {
        return equals(this, o);
    }

    /**
     * Checks that the given objects are equal by checking their classes and their fields.
     *
     * @param o1 the first object
     * @param o2 the second object
     * @return true if they are equal
     */
    public static boolean equals(final @Nullable Object o1, final @Nullable Object o2) {
        if (o1 == null && o2 == null) return true;
        if (o1 == null || o2 == null) return false;
        return o1.getClass().equals(o2.getClass()) && ReflectionUtils.equalsFields(o1, o2);
    }

}
