package org.asuki.common.callback;

import static java.lang.System.out;

public class MessageCallback {
    @Message(type = MessageType.NORMAL)
    public void handleNormalMessage(String message) {
        out.println("Message: " + message);
    }

    @Message(type = MessageType.URGENT)
    public void handleUrgentMessage(String message) {
        out.println("Message: " + message);
    }
}
