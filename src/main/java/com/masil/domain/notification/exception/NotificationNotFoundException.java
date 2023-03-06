package com.masil.domain.notification.exception;

import com.masil.global.error.exception.EntityNotFoundException;
import com.masil.global.error.exception.ErrorCode;

public class NotificationNotFoundException extends EntityNotFoundException {
    public NotificationNotFoundException() {
        super(ErrorCode.NOTIFICATION_NOT_FOUND);
    }
}
