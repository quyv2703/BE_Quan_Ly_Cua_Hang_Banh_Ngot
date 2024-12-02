package com.henrytran1803.BEBakeManage.nofication.model;

import lombok.Data;

@Data
public class NotificationMessage {
    private String type;
    private String message;
    private MessageSeverity severity;
    private Integer duration;

    public enum MessageSeverity {
        SUCCESS, ERROR, WARNING, INFO
    }
}