package edu.wisc.my.messages.data;

import com.fasterxml.jackson.databind.ObjectMapper;
import edu.wisc.my.messages.model.Message;
import edu.wisc.my.messages.model.MessageArray;
import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.List;
import org.apache.commons.io.IOUtils;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Repository;

@Repository
public class MessagesFromTextFile {


  private ResourceLoader resourceLoader;
  private Environment env;

  @Autowired
  public void setResourceLoader(ResourceLoader resourceLoader) {
    this.resourceLoader = resourceLoader;
  }

  @Autowired
  public void setEnv(Environment env) {
    this.env = env;
  }

  public List<Message> allMessages() {

    try {
      Resource resource = resourceLoader.getResource(env.getProperty("message.source"));
      InputStream is = resource.getInputStream();
      String jsonTxt = IOUtils.toString(is, "UTF-8");
      JSONObject json = new JSONObject(jsonTxt);

      ObjectMapper mapper = new ObjectMapper();

      MessageArray messageArray = mapper.readValue(json.toString(), MessageArray.class);

      return Arrays.asList(messageArray.getMessages());

    } catch (IOException exception) {
      throw new RuntimeException("Unable to load messages from text file.", exception);
    }


  }

}
