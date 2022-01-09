package org.bohdi.protobuf.inspector;


public interface PiPredicate<MessageT> {
    // ProtobufInspector is only use for recording success/fail.

    boolean test(ProtobufInspector<MessageT> pi, AuditTrail auditTrail, MessageT message);
}
