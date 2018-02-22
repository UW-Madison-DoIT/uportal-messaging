package edu.wisc.my.messages.time;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.function.Predicate;
import org.apache.commons.lang3.StringUtils;

/**
 * Predicate that is true for Strings that represent dates and date-times that are after the
 * LocalDateTime given at construction. False for null, empty, and blank inputs. Throws
 * RuntimeException on otherwise unparseable input.
 */
public class IsoDateTimeStringAfterPredicate
  implements Predicate<String> {

  LocalDateTime when;

  public IsoDateTimeStringAfterPredicate(LocalDateTime afterWhen) {
    this.when = afterWhen;
  }

  @Override
  public boolean test(String s) {

    if (StringUtils.isBlank(s)) {
      return false;
    }

    if (s.contains("T")) {
      return LocalDateTime.parse(s).isAfter(when);
    } else {
      return LocalDate.parse(s).isAfter(when.toLocalDate());
    }
  }
}
