package fr.unice.smart_campus.middleware.model.sensor;

/**
 * This class represents the mapping between SensorValueType and classes in Java
 */
public enum SensorValueType {
    FLOAT(Float.class), SHORT(Short.class), LONG(Long.class),
    CHAR(Character.class), BYTE(Byte.class), INTEGER(Integer.class),
    DOUBLE(Double.class), STRING(String.class), BOOLEAN(Boolean.class);

    private Class<?> classType;

    /**
     *
     * @param c a Java class
     */
    SensorValueType(Class c) {
        classType = c;
    }

    public Class<?> getClassType(){
        return classType;
    }
}
