package edu.wisc.my.messages.service;

import edu.wisc.my.messages.model.Message;
import edu.wisc.my.messages.model.MessageFilter;
import edu.wisc.my.messages.time.IsoDateTimeStringBeforePredicate;
import java.time.LocalDateTime;
import java.util.function.Predicate;
import org.apache.commons.lang3.Validate;

/**
 * Predicate that is true for messages that are expired (or cannot be verified as not expired due to
 * bogus expiration data.) False otherwise. False for messages with no filter (and thus no
 * expiration date). Throws RuntimeException on evaluating null message.
 */
public class ExpiredMessagePredicate
  implements Predicate<Message> {

  private LocalDateTime when;

  public ExpiredMessagePredicate(LocalDateTime asOfWhen) {
    this.when = asOfWhen;
  }

  @Override
  public boolean test(Message message) {
    Validate.notNull(message);

    IsoDateTimeStringBeforePredicate beforeWhen =
      new IsoDateTimeStringBeforePredicate(when);

    try {
      MessageFilter filter = message.getFilter();
      return (null != filter
        && beforeWhen.test(filter.getExpireDate()));
    } catch (Exception e) {
      return true;
    }

  }
}
