package sensor;

public enum SensorValueType {
    FLOAT(Float.class), SHORT(Short.class), LONG(Long.class),
    CHAR(Character.class), BYTE(Byte.class), INTEGER(Integer.class),
    DOUBLE(Double.class), STRING(String.class), BOOLEAN(Boolean.class);

    private Class<?> classType;

    SensorValueType(Class c) {
        classType = c;
    }

    public Class<?> getClassType(){
        return classType;
    }
}
