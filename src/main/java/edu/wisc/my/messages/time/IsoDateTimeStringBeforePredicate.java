package edu.wisc.my.messages.time;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.function.Predicate;
import org.apache.commons.lang3.StringUtils;

/**
 * Predicate that is true if the String represents a date or date-time in ISO format that is before
 * the LocalDateTime specified at construction. False otherwise, including on null, empty, or blank
 * input. Throws RuntimeException on non-blank unparseable input.
 */
public class IsoDateTimeStringBeforePredicate
  implements Predicate<String> {

  LocalDateTime when;

  public IsoDateTimeStringBeforePredicate(LocalDateTime beforeWhen) {
    this.when = beforeWhen;
  }

  @Override
  public boolean test(String s) {
    if (StringUtils.isBlank(s)) {
      return false;
    }

    // the date string might be time-specified
    if (s.contains("T")) {
      return LocalDateTime.parse(s).isBefore(when);
    } else {
      return LocalDate.parse(s).isBefore(when.toLocalDate());
    }
  }

}
