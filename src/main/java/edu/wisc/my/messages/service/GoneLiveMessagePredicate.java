package edu.wisc.my.messages.service;

import edu.wisc.my.messages.model.Message;
import edu.wisc.my.messages.time.IsoDateTimeStringAfterPredicate;
import java.time.LocalDateTime;
import java.util.function.Predicate;
import org.apache.commons.lang3.Validate;

/**
 * Predicate that is true for messages that have gone live, either because they have no goLiveDate
 * or no filter that might contain a goLiveDate or because their goLiveDate is before the date of
 * comparison as given in the constructor. Messages with unparseable go live dates have not gone
 * live.
 */
public class GoneLiveMessagePredicate
  implements Predicate<Message> {

  LocalDateTime when;

  public GoneLiveMessagePredicate(LocalDateTime asOfWhen) {
    this.when = asOfWhen;
  }


  @Override
  public boolean test(Message message) {
    Validate.notNull(message);

    IsoDateTimeStringAfterPredicate afterWhen = new IsoDateTimeStringAfterPredicate(when);

    try {
      return ((null == message.getFilter())
        || (!afterWhen.test(message.getFilter().getGoLiveDate())));
    } catch (Exception e) {
      return false;
    }

  }
}
