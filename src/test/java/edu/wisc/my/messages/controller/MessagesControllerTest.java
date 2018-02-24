package edu.wisc.my.messages.controller;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import edu.wisc.my.messages.data.MessagesFromTextFile;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.io.DefaultResourceLoader;
import org.springframework.core.io.ResourceLoader;
import org.springframework.http.MediaType;
import org.springframework.mock.env.MockEnvironment;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
public class MessagesControllerTest {

  @Autowired
  private MockMvc mvc;

  @Test
  public void siteIsUp() throws Exception {
    mvc.perform(MockMvcRequestBuilders.get("/").accept(MediaType.APPLICATION_JSON))
      .andExpect(status().isOk())
      .andExpect(content().contentTypeCompatibleWith("application/json"))
      .andExpect(content().json("{\"status\":\"up\"}"));
  }

  /**
   * Test that the data in the incoming text file is actually valid.
   */
  @Test
  public void dataIsValid() {

    MockEnvironment mockEnv = new MockEnvironment();

    // ToDo: It would be very cool is this value could automatically stay in sync
    //  with the actual value in the application.properties file.
    mockEnv.setProperty("message.source", "classpath:messages.json");

    ResourceLoader defaultLoader = new DefaultResourceLoader();

    MessagesFromTextFile messageReader = new MessagesFromTextFile();
    messageReader.setEnv(mockEnv);
    messageReader.setResourceLoader(defaultLoader);

    messageReader.allMessages();
  }
}
