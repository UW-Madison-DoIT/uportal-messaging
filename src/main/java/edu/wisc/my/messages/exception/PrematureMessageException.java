package edu.wisc.my.messages.exception;

import edu.wisc.my.messages.model.Message;
import java.time.LocalDateTime;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Thrown on un-privileged request for a message that has not yet gone live.
 */
@ResponseStatus(value = HttpStatus.FORBIDDEN, reason = "Requested message is premature.")
public class PrematureMessageException
  extends ForbiddenMessageException {

  private Message prematureMessage;
  private LocalDateTime asOfWhen;

  public PrematureMessageException(Message prematureMessage, LocalDateTime asOfWhen) {
    super("Message with id " +
      ((null == prematureMessage) ?
        "" :
        prematureMessage.getId() + " ")
      + "is premature as of " + asOfWhen);
    this.prematureMessage = prematureMessage;
    this.asOfWhen = asOfWhen;
  }
}
