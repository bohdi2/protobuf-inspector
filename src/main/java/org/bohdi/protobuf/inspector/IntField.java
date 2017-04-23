package org.bohdi.protobuf.inspector;


import java.util.function.Function;


public class IntField<T> {//implements Expectation<T> {

    private final Function<T, Integer> function;
    private final int value;

    public IntField(Function<T, Integer> function, int value) {
        this.function = function;
        this.value = value;
    }

    public boolean test(ProtobufInspector<T> protobufInspector) {
        //protobufInspector.test(p->function.apply(protobufInspector) == value);
        return true;
        //System.err.println("Field.check() " + value + ", {" + TextFormat.shortDebugString((Message) message) + "}");
        //return protobufInspector.expect(value, function);
        //return protobufInspector.xxexpectField(path, value);
    }

    public boolean filter(ProtobufInspector<T> protobufInspector, T message) {
        return false;
        //System.err.println("Field.filter(protobuf, message)");
        //return protobufInspector.filter(value, function, message);
        //return protobufInspector.filterField(message, path, value);
    }

    //public String toString() {
    //    return String.format("Field(%s, %s", "comment", value);
    //}


}
