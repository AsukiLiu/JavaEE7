package org.asuki.common.callback;

public interface MessageService {
    void registerMessageCallback(MessageCallback callback);

    void sendMessage(String message, MessageType messageType) throws Exception;
}
