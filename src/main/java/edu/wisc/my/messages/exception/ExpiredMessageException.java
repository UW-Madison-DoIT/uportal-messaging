package edu.wisc.my.messages.exception;

import edu.wisc.my.messages.model.Message;
import java.time.LocalDateTime;

public class ExpiredMessageException
  extends ForbiddenMessageException {

  private final Message expiredMessage;

  /**
   * Time of consideration. The frame of reference for considering the message expired.
   * Typically, this is "now".
   */
  private final LocalDateTime asOfWhen;

  public ExpiredMessageException(Message messageWithRequestedId, LocalDateTime asOfWhen) {
    super("Message with id [" + messageWithRequestedId.getId() + "] is expired as of " + asOfWhen
      .toString());
    this.expiredMessage = messageWithRequestedId;
    this.asOfWhen = asOfWhen;
  }
}
