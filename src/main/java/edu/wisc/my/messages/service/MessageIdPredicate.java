package edu.wisc.my.messages.service;

import edu.wisc.my.messages.model.Message;
import java.util.Objects;
import java.util.function.Predicate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Predicate that is true where the tested message has a (potentially null) ID matching that
 * (potentially null) ID given at construction.
 */
public class MessageIdPredicate
  implements Predicate<Message> {

  private final String idToMatch;
  protected Logger logger = LoggerFactory.getLogger(getClass());

  /**
   * Instantiate a MessageIdPredicate that tests for a specific ID.
   *
   * @param idToMatch potentially null message identifier to look for
   */
  public MessageIdPredicate(String idToMatch) {
    this.idToMatch = idToMatch;
  }

  @Override
  public boolean test(Message message) {
    boolean result = (null != message
      && Objects.equals(this.idToMatch, message.getId()));
    logger.trace("{} result testing for id {} in message {}", result, idToMatch, message);
    return result;
  }
}
