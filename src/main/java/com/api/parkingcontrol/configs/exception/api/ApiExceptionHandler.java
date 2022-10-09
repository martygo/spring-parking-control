package com.api.parkingcontrol.configs.exception.api;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

import com.api.parkingcontrol.configs.exception.domain.DomainException;
import com.api.parkingcontrol.configs.exception.domain.EntityNotFoundException;

import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindException;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@ControllerAdvice
public class ApiExceptionHandler extends ResponseEntityExceptionHandler {

  private final MessageSource messageSource;

  @Override
  protected ResponseEntity<Object> handleMethodArgumentNotValid(
      MethodArgumentNotValidException exception,
      HttpHeaders headers,
      HttpStatus status,
      WebRequest request) {
    var problem = new Problem();

    problem.setStatus(status.value());
    problem.setDataHora(OffsetDateTime.now());
    problem.setTitulo("Um ou mais campos estão inválidos. Faça o preenchimento correto e tente novamente.");
    problem.setCampos(mapBindExceptionToCampos(exception));

    return handleExceptionInternal(exception, problem, headers, status, request);
  }

  @ExceptionHandler(DomainException.class)
  public ResponseEntity<Object> handleNegocio(
      DomainException exception,
      WebRequest request) {
    var status = HttpStatus.BAD_REQUEST;
    var problem = new Problem();

    problem.setStatus(status.value());
    problem.setDataHora(OffsetDateTime.now());
    problem.setTitulo(exception.getMessage());

    return handleExceptionInternal(exception, problem, new HttpHeaders(), status, request);
  }

  @ExceptionHandler(EntityNotFoundException.class)
  public ResponseEntity<Object> handleEntidadeNaoEncontradaException(
      EntityNotFoundException exception,
      WebRequest request) {
    var status = HttpStatus.NOT_FOUND;
    var problem = new Problem();

    problem.setStatus(status.value());
    problem.setDataHora(OffsetDateTime.now());
    problem.setTitulo(exception.getMessage());

    return handleExceptionInternal(exception, problem, new HttpHeaders(), status, request);
  }

  private List<Problem.Campo> mapBindExceptionToCampos(BindException exception) {
    Function<ObjectError, Problem.Campo> mapper = error -> new Problem.Campo(
        ((FieldError) error).getField(),
        messageSource.getMessage(error, LocaleContextHolder.getLocale()));

    return exception.getBindingResult().getAllErrors()
        .stream()
        .map(mapper)
        .collect(Collectors.toList());
  }
}
