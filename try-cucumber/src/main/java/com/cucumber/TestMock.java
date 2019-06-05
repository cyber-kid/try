package com.cucumber;

class TestMock {
  static String isItFriday(String today) {
    return "Friday".equals(today) ? "Yeap" : "Nope";
  }
}
