package com.example.demo.entity.request;

import lombok.*;

@Getter
@Setter
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class DataRequest {
  private String id;
  private String value;
}
