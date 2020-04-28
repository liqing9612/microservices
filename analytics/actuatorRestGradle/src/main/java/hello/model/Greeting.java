package hello.model;

import io.swagger.annotations.ApiModel;

@ApiModel(value = "Greeting", description = "Greeting data model")
public class Greeting {

  private long id;
  private String content;

  public Greeting() {}

  public Greeting(long id, String content) {
    this.id = id;
    this.content = content;
  }

  public long getId() {
    return id;
  }

  public String getContent() {
    return content;
  }

  public void setId(long id) {
    this.id = id;
  }

  public void setContent(String content) {
    this.content = content;
  }
}
