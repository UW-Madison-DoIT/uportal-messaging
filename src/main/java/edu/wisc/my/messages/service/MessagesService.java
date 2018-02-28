package edu.wisc.my.messages.service;

import edu.wisc.my.messages.data.MessagesFromTextFile;
import edu.wisc.my.messages.model.Message;
import edu.wisc.my.messages.model.User;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import org.apache.commons.lang3.Validate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class MessagesService {

  protected final Logger logger = LoggerFactory.getLogger(getClass());

  private MessagesFromTextFile messageSource;

  public List<Message> allMessages() {
    return messageSource.allMessages();
  }

  public List<Message> filteredMessages(User user) {

    Predicate<Message> neitherPrematureNorExpired =
      new ExpiredMessagePredicate(LocalDateTime.now()).negate()
        .and(new GoneLiveMessagePredicate(LocalDateTime.now()));

    // retain those messages with suitable dates and audiences
    Predicate<Message> retainMessage =
      neitherPrematureNorExpired.and(new AudienceFilterMessagePredicate(user));

    List<Message> validMessages = new ArrayList<>();

    validMessages.addAll(messageSource.allMessages());
    validMessages.removeIf(retainMessage.negate()); // remove the messages we're not retaining

    return validMessages;
  }

  @Autowired
  public void setMessageSource(MessagesFromTextFile messageSource) {
    this.messageSource = messageSource;
  }

  /**
   * Get the message with a given ID, or null if no such message
   *
   * @return Message matching ID, or null if none.
   */
  public Message messageById(String idToMatch) {
    logger.trace("messageById(\"{}\")", idToMatch);
    Validate.notNull(idToMatch);

    Predicate<Message> messageMatchesRequestedId = new MessageIdPredicate(idToMatch);

    List<Message> allMessages = allMessages();

    List<Message> matchingMessages = allMessages.stream().filter(messageMatchesRequestedId)
      .collect(Collectors.toList());

    if (matchingMessages.isEmpty()) {
      logger.debug("Found no message for id [{}]", idToMatch);
      return null;
    } else if (matchingMessages.size() == 1) {
      Message foundMessage = matchingMessages.get(0);
      logger.trace("Found message [{}].", foundMessage);
      return foundMessage;
    } else {
      logger.error("Multiple messages have id [{}]. Messages data corruption?", idToMatch);
      throw new IllegalStateException("Multiple messages matched id [" + idToMatch
        + "], which should have been a unique ID matching at most one message.");
    }
  }
}
