package org.bohdi.protobuf.inspector;


import com.google.protobuf.Message;

public interface Function<T> {//} extends Message> {
    Object op(T t);
}
