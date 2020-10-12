package com.example.demo.entity.response;

import lombok.*;

@Getter
@Setter
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class Info {
  private String correlationId;
  private String timestamp;
}
