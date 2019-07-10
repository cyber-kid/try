package com.gson.model;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.text.DateFormat;

public abstract class AbstractTest {
  public Gson getGson() {
    return new GsonBuilder()
            .setDateFormat(DateFormat.FULL, DateFormat.FULL)
            .setPrettyPrinting()
            .create();
  }
}
