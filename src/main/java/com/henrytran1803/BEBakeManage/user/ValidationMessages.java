package com.henrytran1803.BEBakeManage.user;

public enum ValidationMessages {
    FIRST_NAME_NOT_BLANK("first.name.not.blank"),
    LAST_NAME_NOT_BLANK("last.name.not.blank"),
    EMAIL_INVALID_FORMAT("email.invalid.format"),
    EMAIL_NOT_BLANK("email.not.blank"),
    PASSWORD_NOT_BLANK("password.not.blank"),
    DATE_OF_BIRTH_NOT_NULL("date.of.birth.not.null"),
    ACTIVE_STATUS_NOT_NULL("active.status.not.null"),
    ROLES_NOT_NULL("roles.not.null");

    private final String key;

    ValidationMessages(String key) {
        this.key = key;
    }

    public String getKey() {
        return key;
    }
}
