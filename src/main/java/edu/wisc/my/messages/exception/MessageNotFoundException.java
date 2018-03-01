package edu.wisc.my.messages.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Represents case where requested message was not found.
 */
@ResponseStatus(value = HttpStatus.NOT_FOUND, reason = "Did not find requested message.")
public class MessageNotFoundException
  extends Exception {

}
