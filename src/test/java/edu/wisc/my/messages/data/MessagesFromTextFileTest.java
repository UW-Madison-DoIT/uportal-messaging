package edu.wisc.my.messages.data;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.IOException;

import org.junit.Assert;
import org.junit.Test;

import org.springframework.mock.env.MockEnvironment;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.core.io.DefaultResourceLoader;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;

public class MessagesFromTextFileTest {
  
  /**
   * Test that the data in the incoming text file is actually valid.
   */
  @Test
  public void dataIsValid() {

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
      Assert.fail("Invalid incoming data in MessagesFromTextFile");
    }
  } 
  
  /**
   * Test that an exception encountered in preparing to read the text file results in a throw from
   * the reader.
   */
  @Test(expected = RuntimeException.class)
  public void exceptionPropagates() {
    MessagesFromTextFile messageReader = new MessagesFromTextFile();

    Environment env = mock(Environment.class);
    when(env.getProperty("message.source")).thenThrow(new RuntimeException());
    messageReader.setEnv(env);

    ResourceLoader loader = mock(ResourceLoader.class);
    messageReader.setResourceLoader(loader);

    messageReader.allMessages();
  }

  /**
   * Test that an IOException encountered in reading the text file results in a throw from the
   * reader.
   */
  @Test(expected = RuntimeException.class)
  public void ioExceptionPropagates() throws IOException {
    MessagesFromTextFile messageReader = new MessagesFromTextFile();

    Environment env = mock(Environment.class);
    when(env.getProperty("message.source")).thenReturn("some-particular-message-file.json");
    messageReader.setEnv(env);

    Resource resource = mock(Resource.class);
    when(resource.getInputStream()).thenThrow(IOException.class);

    ResourceLoader loader = mock(ResourceLoader.class);
    when(loader.getResource("some-particular-message-file.json")).thenReturn(resource);
    messageReader.setResourceLoader(loader);

    messageReader.allMessages();
  }

}
