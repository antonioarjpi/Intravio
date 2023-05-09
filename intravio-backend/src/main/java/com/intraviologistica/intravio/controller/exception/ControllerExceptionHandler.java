package com.intraviologistica.intravio.controller.exception;


import com.fasterxml.jackson.databind.exc.InvalidFormatException;
import com.fasterxml.jackson.databind.exc.UnrecognizedPropertyException;
import com.intraviologistica.intravio.service.exceptions.AuthenticateErrorException;
import com.intraviologistica.intravio.service.exceptions.FileNotFoundException;
import com.intraviologistica.intravio.service.exceptions.ResourceNotFoundException;
import com.intraviologistica.intravio.service.exceptions.RuleOfBusinessException;
import jakarta.servlet.http.HttpServletRequest;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.hibernate.NonUniqueResultException;
import org.hibernate.exception.ConstraintViolationException;
import org.hibernate.id.IdentifierGenerationException;
import org.postgresql.util.PSQLException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.FieldError;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.servlet.NoHandlerFoundException;

import java.time.OffsetDateTime;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@ControllerAdvice
public class ControllerExceptionHandler {

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<StandardError> resourceNotFoundException(ResourceNotFoundException e,
                                                                   HttpServletRequest request) {

        StandardError error = new StandardError(OffsetDateTime.now(), HttpStatus.NOT_FOUND.value(),
                "Resource Not FoundException", e.getMessage(), request.getRequestURI());

        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
    }

    @ExceptionHandler(RuleOfBusinessException.class)
    public ResponseEntity<StandardError> ruleOfBusinessException(RuleOfBusinessException e, HttpServletRequest request) {

        StandardError error = new StandardError(OffsetDateTime.now(), HttpStatus.BAD_REQUEST.value(),
                "Rule Of Business", e.getMessage(), request.getRequestURI());

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
    }

    @ExceptionHandler(FileNotFoundException.class)
    public ResponseEntity<StandardError> fileNotFoundException(FileNotFoundException e, HttpServletRequest request) {

        StandardError error = new StandardError(OffsetDateTime.now(), HttpStatus.NOT_FOUND.value(),
                "FileNotFoundException.", "Arquivo não localizado", request.getRequestURI());

        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
    }

    @ExceptionHandler(AuthenticateErrorException.class)
    public ResponseEntity<StandardError> authentication(AuthenticateErrorException e, HttpServletRequest request) {

        StandardError error = new StandardError(OffsetDateTime.now(), HttpStatus.UNAUTHORIZED.value(),
                "AuthenticateErrorException", e.getMessage(), request.getRequestURI());

        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(error);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<StandardError> validation(MethodArgumentNotValidException e, HttpServletRequest request) {

        ValidationError error = new ValidationError(OffsetDateTime.now(), HttpStatus.UNPROCESSABLE_ENTITY.value(),
                "Validation Error", e.getFieldError().getDefaultMessage(), request.getRequestURI());

        for (FieldError x : e.getBindingResult().getFieldErrors()) {
            error.addError(x.getField(), x.getDefaultMessage());
        }

        return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY).body(error);
    }

    @ExceptionHandler(NonUniqueResultException.class)
    public ResponseEntity<StandardError> nonUniqueResultException(NonUniqueResultException e, HttpServletRequest request) {

        StandardError error = new StandardError(OffsetDateTime.now(), HttpStatus.BAD_REQUEST.value(), "Non Unique Result",
                "Erro: A consulta retornou mais de um resultado quando esperava-se apenas um. Verifique os critérios " +
                        "de busca e tente novamente com critérios mais específicos ou use outra forma de filtragem. ",
                request.getRequestURI());

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
    }

    @ExceptionHandler(IdentifierGenerationException.class)
    public ResponseEntity<StandardError> identifierGenerationException(IdentifierGenerationException e, HttpServletRequest request) {

        StandardError error = new StandardError(OffsetDateTime.now(), HttpStatus.BAD_REQUEST.value(), "Identifier Generation",
                "Houve um problema ao gerar um identificador único para o registro. Por favor, revise os dados e tente " +
                        "novamente ou entre em contato com suporte técnico", request.getRequestURI());

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
    }

    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<StandardError> methodArgumentTypeMismatch(MethodArgumentTypeMismatchException e, HttpServletRequest request) {

        StandardError error = new StandardError(OffsetDateTime.now(), HttpStatus.METHOD_NOT_ALLOWED.value(),
                "Method Argument Type Mismatch", "O valor informado para a requisição é inválido. Por favor, verifique se" +
                " os valores informados estão corretos e tente novamente", request.getRequestURI());

        return ResponseEntity.status(HttpStatus.METHOD_NOT_ALLOWED).body(error);
    }

    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    public ResponseEntity<StandardError> httpRequestMethodNotSupportedException(HttpRequestMethodNotSupportedException e,
                                                                                HttpServletRequest request) {

        StandardError error = new StandardError(OffsetDateTime.now(), HttpStatus.METHOD_NOT_ALLOWED.value(),
                "Method Argument Type Mismatch", "O valor informado para a requisição é inválido. Por favor, " +
                "verifique se os valores informados estão corretos e tente novamente", request.getRequestURI());

        return ResponseEntity.status(HttpStatus.METHOD_NOT_ALLOWED).body(error);
    }

    @ExceptionHandler(NoHandlerFoundException.class)
    public ResponseEntity<StandardError> noHandlerFoundException(NoHandlerFoundException e, HttpServletRequest request) {

        StandardError error = new StandardError(OffsetDateTime.now(), HttpStatus.NOT_FOUND.value(), "No Handler Found",
                "Não foi possível encontrar a página que você está procurando. Verifique se a URL está correta ou " +
                        "tente novamente mais tarde.", request.getRequestURI());

        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<StandardError> handleDataIntegrityViolationException(DataIntegrityViolationException ex,
                                                                               HttpServletRequest request) {

        StandardError error = new StandardError(OffsetDateTime.now(), HttpStatus.BAD_REQUEST.value(),
                "Data Integrity Violation", "Erro na violação de dados", request.getRequestURI());

        Throwable cause = ExceptionUtils.getRootCause(ex);
        if (cause instanceof PSQLException) {
            PSQLException psqlException = (PSQLException) cause;
            String message = psqlException.getMessage();
            if (message.contains("violates unique constraint")) {
                String[] fieldName = extractFieldName(message);
                error.setMessage("O campo '" + fieldName[0] + "' com o valor (" + fieldName[1] + ") já existe. Por favor, " +
                        "insira um valor diferente ou edite o registro existente.");
                return ResponseEntity.badRequest().body(error);
            } else if (message.contains("violates foreign key constraint")) {
                String[] token = message.toUpperCase(Locale.ROOT).split("\"");
                error.setMessage("Não foi possível atualizar/excluir o registro em: " + token[1] +
                        ". Parece que existe uma dependência com: " + token[5] + ". Por favor, verifique os dados que estão " +
                        "sendo modificados ou excluídos e tente novamente.");
                return ResponseEntity.badRequest().body(error);
            }
        } else if (cause instanceof ConstraintViolationException) {
            ConstraintViolationException cve = (ConstraintViolationException) cause;
            String message = cve.getMessage();
            String[] fieldName = extractFieldName(message);
            error.setMessage("Campo '" + fieldName[0] + "' (" + fieldName[1] + ") possui valor inválido");
            return ResponseEntity.badRequest().body(error);
        }

        error.setMessage(ex.getMessage());

        return ResponseEntity.badRequest().body(error);
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<StandardError> handleHttpMessageNotReadableException(HttpMessageNotReadableException e, HttpServletRequest request) {
        String fieldName = null;
        String invalidValue = null;

        if (e.getCause() instanceof InvalidFormatException) {
            InvalidFormatException ex = (InvalidFormatException) e.getCause();
            fieldName = ex.getPath().get(0).getFieldName();
            invalidValue = ex.getValue().toString();
        } else if (e.getCause() instanceof UnrecognizedPropertyException) {
            UnrecognizedPropertyException ex = (UnrecognizedPropertyException) e.getCause();
            fieldName = ex.getPropertyName();
            invalidValue = ex.getOriginalMessage();
        }

        StandardError error = new StandardError(OffsetDateTime.now(), HttpStatus.BAD_REQUEST.value(), "HttpMessageNotReadableException",
                "O campo '" + fieldName + "' possui valor inválido: " + invalidValue, request.getRequestURI());

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
    }

    private String[] extractFieldName(String message) {
        Pattern pattern = Pattern.compile("\\((.*?)\\)");
        Matcher matcher = pattern.matcher(message);

        String constraint = "";
        String value = "";

        while (matcher.find()) {
            if (constraint.isEmpty()) {
                constraint = matcher.group(1);
            } else {
                value = matcher.group(1);
                break;
            }
        }

        return new String[]{constraint, value};
    }
}
