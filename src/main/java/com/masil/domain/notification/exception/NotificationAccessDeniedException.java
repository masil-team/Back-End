package com.masil.domain.notification.exception;

import com.masil.global.error.exception.ErrorCode;
import com.masil.global.error.exception.NoAuthenticationException;

public class NotificationAccessDeniedException extends NoAuthenticationException {
    public NotificationAccessDeniedException() {
        super(ErrorCode.NOTIFICATION_ACCESS_DENIED);
    }
}
