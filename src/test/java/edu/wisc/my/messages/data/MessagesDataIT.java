package edu.wisc.my.messages.data;

import org.junit.Assert;
import org.junit.Test;
import org.springframework.core.io.DefaultResourceLoader;
import org.springframework.core.io.ResourceLoader;
import org.springframework.mock.env.MockEnvironment;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MessagesDataIT {

    /**
   * Test that the data in the incoming text file is actually valid.
   */
  @Test
  public void dataIsValid() {
    
    final Logger logger = LoggerFactory.getLogger(getClass());

    MockEnvironment mockEnv = new MockEnvironment();
    
    // ToDo: It would be very cool is this value could automaticall stay in sync
    //  with the actual value in the application.properties file.
    mockEnv.setProperty("message.source", "classpath:messages.json");
    
    ResourceLoader defaultLoader = new DefaultResourceLoader();

    MessagesFromTextFile messageReader = new MessagesFromTextFile();
    messageReader.setEnv(mockEnv);
    messageReader.setResourceLoader(defaultLoader);
    try{
       messageReader.allMessages();
    } catch (Exception e) {
      logger.error("DATA INTEGRATION TEST FAILED " + e.getMessage());
      e.printStackTrace();  
      Assert.fail("Invalid incoming data in MessagesFromTextFile");
    }
  } 

}