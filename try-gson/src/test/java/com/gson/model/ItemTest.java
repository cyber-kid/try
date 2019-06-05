package com.swagger.model;

import lombok.extern.slf4j.Slf4j;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

@Slf4j
public class ItemTest extends AbstractTest{
  @Test
  public void listTest() {
    List<Item> items = new ArrayList<>();

    items.add(new Item("1", "param1"));
    items.add(new Item("2", "param2"));
    items.add(new Item("3", "param3"));

    String json = getGson().toJson(items);

    log.info("Result is: " + json);
  }
}
