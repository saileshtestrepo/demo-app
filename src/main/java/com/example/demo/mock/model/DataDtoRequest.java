package com.example.demo.mock.model;

import lombok.*;

@Getter
@Setter
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class DataDtoRequest {
  private String id;
  private String data;
}
