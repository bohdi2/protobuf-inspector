package org.bohdi.protobuf.inspector;


public interface PiPredicate<T> {
    boolean test(ProtobufInspector<T> pi);
}
