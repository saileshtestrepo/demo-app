package com.example.demo.exceptions;

import lombok.AllArgsConstructor;

import java.util.List;

@AllArgsConstructor
public class DataException extends Exception {

  private final String message;

  public DataException(final List<String> messages) {
    this.message = messages.toString();
  }

  @Override
  public String getMessage() {
    return message;
  }
}
