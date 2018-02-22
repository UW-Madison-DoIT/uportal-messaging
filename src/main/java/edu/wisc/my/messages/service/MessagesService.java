package edu.wisc.my.messages.service;

import edu.wisc.my.messages.data.MessagesFromTextFile;
import edu.wisc.my.messages.model.Message;
import edu.wisc.my.messages.model.User;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;
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
}
