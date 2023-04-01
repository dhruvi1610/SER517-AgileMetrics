package edu.asu.cassess.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class TaigaTokenRefreshResponseDto {

  @JsonProperty("auth_token")
  private String authToken;

  @JsonProperty("refresh")
  private String refreshToken;
}
