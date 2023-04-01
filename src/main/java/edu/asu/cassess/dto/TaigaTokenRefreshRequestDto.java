package edu.asu.cassess.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TaigaTokenRefreshRequestDto {
  private String refresh;
}
