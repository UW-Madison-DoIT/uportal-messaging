package edu.wisc.my.messages.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Message representing that access to a requested message is forbidden. Sub-classes represent
 * specific reasons why access might be forbidden.
 */
@ResponseStatus(value = HttpStatus.FORBIDDEN, reason = "Access denied to requested message.")
public class ForbiddenMessageException
  extends Exception {

  public ForbiddenMessageException(String message) {
    super(message);
  }
}
