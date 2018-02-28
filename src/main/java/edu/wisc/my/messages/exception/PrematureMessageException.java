package edu.wisc.my.messages.exception;

import edu.wisc.my.messages.model.Message;
import java.time.LocalDateTime;

/**
 * Thrown on un-privileged request for a message that has not yet gone live.
 */
public class PrematureMessageException
  extends ForbiddenMessageException {

  private Message prematureMessage;
  private LocalDateTime asOfWhen;

  public PrematureMessageException(Message prematureMessage, LocalDateTime asOfWhen) {
    super("Message with id " + prematureMessage.getId() + " is premature as of "
      + asOfWhen.toString());
    this.prematureMessage = prematureMessage;
    this.asOfWhen = asOfWhen;
  }

}
