package edu.wisc.my.messages.time;

import static org.junit.Assert.*;

import java.time.LocalDateTime;
import org.junit.Test;

public class IsoDateTimeStringBeforePredicateTest {

  private IsoDateTimeStringBeforePredicate beforeNowPredicate =
    new IsoDateTimeStringBeforePredicate(LocalDateTime.now());

  @Test
  public void nullEvaluatesFalse() {
    assertFalse("Null shouldn't be considered before now.", beforeNowPredicate.test(null));
  }

  @Test
  public void emptyStringEvaluatesFalse() {
    assertFalse("Empty string shouldn't be considered before now.", beforeNowPredicate.test(""));
  }

  @Test
  public void whitespaceStringEvaluatesFalse() {
    assertFalse("Whitespace shouldn't be considered before now.",
      beforeNowPredicate.test("\t   \t"));
  }

  @Test(expected = RuntimeException.class)
  public void stringThatIsNotADateThrows() {
    beforeNowPredicate.test("Garbage");
  }

  @Test
  public void uwWasIncorporatedBeforeNow() {
    assertTrue("July 26 1848 should be before now.",
      beforeNowPredicate.test("1848-07-26"));
  }

  /**
   * Test that even if the time of incorporation is specified to the second, it's still before now.
   */
  @Test
  public void uwWasIncorporatedBeforeNowWhenTimeSpecific() {
    assertTrue("July 26 1848 around 1p should be before now.",
      beforeNowPredicate.test("1848-07-26T13:01:04"));
  }

  @Test
  public void uwTercentenialIsNotBeforeNow() {
    assertFalse("July 26 2148 should not be before now.",
      beforeNowPredicate.test("2148-07-26"));
  }

  /**
   * Test that even if the time of the tercentenial is specified to the second, it's still not
   * before now.
   */
  @Test
  public void uwTercentenialIsNotBeforeNowWhenTimeSpecific() {
    assertFalse("July 26 2148 around 9a should not be before now.",
      beforeNowPredicate.test("2148-07-26T09:05:32"));
  }
}