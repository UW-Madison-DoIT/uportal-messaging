package edu.wisc.my.messages.service;

import edu.wisc.my.messages.model.Message;
import edu.wisc.my.messages.model.MessageFilter;
import edu.wisc.my.messages.model.User;
import java.util.function.Predicate;
import org.apache.commons.lang3.Validate;

/**
 * Predicate that is true for messages where all of the potentially none audience filters of which
 * return true for the user specified in the constructor, and false otherwise.
 */
public class AudienceFilterMessagePredicate
  implements Predicate<Message> {

  private final User user;

  public AudienceFilterMessagePredicate(User user) {
    this.user = user;
  }

  @Override
  public boolean test(Message message) {
    Validate.notNull(message);

    MessageFilter audienceFilter = message.getFilter();

    if (null == audienceFilter) {
      return true; // all of the zero audience filters passed
    }

    return audienceFilter.test(user);
  }
}
