package edu.wisc.my.messages.exception;

/**
 * Message representing that access to a requested message is forbidden.
 * Sub-classes represent specific reasons why access might be forbidden.
 */
public class ForbiddenMessageException
  extends Exception {

  public ForbiddenMessageException(String message) {
    super(message);
  }
}
