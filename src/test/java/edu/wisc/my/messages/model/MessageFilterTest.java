package edu.wisc.my.messages.model;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.HashSet;
import java.util.Set;
import org.junit.Test;

public class MessageFilterTest {

  @Test
  public void matchesWhenUserIsInAMatchingGroup() {
    MessageFilter filter = new MessageFilter();
    filter.addGroupsItem("matchingGroup");
    filter.addGroupsItem("someOtherGroup");

    final User user = new User();
    Set<String> groups = new HashSet<>();
    groups.add("matchingGroup");
    groups.add("unrelatedGroup");
    user.setGroups(groups);

    assertTrue(filter.test(user));
  }

  @Test
  public void doesNotMatchWhenUserIsInNoMatchingGroup() {
    MessageFilter filter = new MessageFilter();
    filter.addGroupsItem("someGroup");
    filter.addGroupsItem("someOtherGroup");

    final User user = new User();
    Set<String> groups = new HashSet<>();
    groups.add("notMatchingGroup");
    groups.add("unrelatedGroup");
    user.setGroups(groups);

    assertFalse(filter.test(user));
  }

}
