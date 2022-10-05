package com.api.parkingcontrol.configs.exception.api;

import java.time.OffsetDateTime;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;
import static com.fasterxml.jackson.annotation.JsonInclude.Include.NON_NULL;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;

@JsonInclude(NON_NULL)
@Data
public class Problem {

  private Integer status;
  private OffsetDateTime dataHora;
  private String titulo;

  private List<Campo> campos;

  @AllArgsConstructor
  @Getter
  public static class Campo {

    private final String nome;
    private final String mensagem;
  }
}
