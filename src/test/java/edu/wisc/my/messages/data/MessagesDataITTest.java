package edu.wisc.my.messages.data;

import org.junit.Test;
import org.springframework.core.io.DefaultResourceLoader;
import org.springframework.core.io.ResourceLoader;
import org.springframework.mock.env.MockEnvironment;

public class MessagesDataITTest {

    /**
    * Test that the data in the incoming text file is actually valid.
    */
    @Test
    public void dataIsValid() throws Exception {

        MockEnvironment mockEnv = new MockEnvironment();

        // ToDo: It would be very cool is this value could automaticall stay in sync
        //  with the actual value in the application.properties file.
        mockEnv.setProperty("message.source", "classpath:messages.json");
        ResourceLoader defaultLoader = new DefaultResourceLoader();
        MessagesFromTextFile messageReader = new MessagesFromTextFile();
        messageReader.setEnv(mockEnv);
        messageReader.setResourceLoader(defaultLoader);
        messageReader.allMessages();
    }
}
