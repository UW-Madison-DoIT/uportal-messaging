package edu.wisc.my.messages.exception;

import edu.wisc.my.messages.model.Message;
import edu.wisc.my.messages.model.User;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.FORBIDDEN, reason = "Requesting user not in audience for requested message")
public class UserNotInMessageAudienceException extends ForbiddenMessageException {

  private final Message messageNotForUser;
  private final User userNotInMessageAudience;

  public UserNotInMessageAudienceException(Message message, User user) {
    super("User not within audience of requested message");
    this.messageNotForUser = message;
    this.userNotInMessageAudience = user;
  }
}
