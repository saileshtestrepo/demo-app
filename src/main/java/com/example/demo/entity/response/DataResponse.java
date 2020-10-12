package com.example.demo.entity.response;

import lombok.*;

import java.util.List;

@Getter
@Setter
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class DataResponse {
  private Info info;
  private Data data;
  private List<String> errors;
}
