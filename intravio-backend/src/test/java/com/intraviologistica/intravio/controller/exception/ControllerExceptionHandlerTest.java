package com.intraviologistica.intravio.controller.exception;

import com.fasterxml.jackson.databind.exc.UnrecognizedPropertyException;
import com.intraviologistica.intravio.service.exceptions.AuthenticateErrorException;
import com.intraviologistica.intravio.service.exceptions.FileNotFoundException;
import com.intraviologistica.intravio.service.exceptions.ResourceNotFoundException;
import com.intraviologistica.intravio.service.exceptions.RuleOfBusinessException;
import jakarta.servlet.http.HttpServletRequest;
import org.hibernate.NonUniqueResultException;
import org.hibernate.id.IdentifierGenerationException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.core.MethodParameter;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.servlet.NoHandlerFoundException;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class ControllerExceptionHandlerTest {

    private ControllerExceptionHandler controllerExceptionHandler;

    @Mock
    private ResourceNotFoundException resourceNotFoundException;

    @Mock
    private RuleOfBusinessException ruleOfBusinessException;

    @Mock
    private FileNotFoundException fileNotFoundException;

    @Mock
    private AuthenticateErrorException authenticateErrorException;

    @Mock
    private NonUniqueResultException nonUniqueResultException;

    @Mock
    private IdentifierGenerationException identifierGenerationException;

    @Mock
    private MethodArgumentTypeMismatchException methodArgumentTypeMismatchException;

    @Mock
    private HttpRequestMethodNotSupportedException httpRequestMethodNotSupportedException;

    @Mock
    private NoHandlerFoundException noHandlerFoundException;

    @Mock
    private DataIntegrityViolationException dataIntegrityViolationException;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        controllerExceptionHandler = new ControllerExceptionHandler();
        resourceNotFoundException = mock(ResourceNotFoundException.class);
    }

    @Test
    public void testResourceNotFoundException() {
        String errorMessage = "Resource not found";
        when(resourceNotFoundException.getMessage()).thenReturn(errorMessage);

        MockHttpServletRequest request = new MockHttpServletRequest();
        ResponseEntity<StandardError> response = controllerExceptionHandler.resourceNotFoundException(
                resourceNotFoundException, request);

        StandardError standardError = new StandardError();
        standardError.setTimestamp(response.getBody().getTimestamp());
        standardError.setMessage(response.getBody().getMessage());
        standardError.setPath(response.getBody().getPath());
        standardError.setStatus(response.getStatusCode().value());
        standardError.setError(response.getBody().getError());

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertEquals(standardError.getStatus(), response.getBody().getStatus().intValue());
        assertEquals(standardError.getError(), response.getBody().getError());
        assertEquals(standardError.getMessage(), response.getBody().getMessage());
        assertEquals(standardError.getPath(), response.getBody().getPath());
        assertEquals(standardError.getTimestamp(), response.getBody().getTimestamp());
    }

    @Test
    void testRuleOfBusinessException() {
        String errorMessage = "Rule of business violation";
        when(ruleOfBusinessException.getMessage()).thenReturn(errorMessage);

        MockHttpServletRequest request = new MockHttpServletRequest();
        ResponseEntity<StandardError> response = controllerExceptionHandler.ruleOfBusinessException(
                ruleOfBusinessException, request);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals(HttpStatus.BAD_REQUEST.value(), response.getBody().getStatus());
        assertEquals("Rule Of Business", response.getBody().getError());
        assertEquals(errorMessage, response.getBody().getMessage());
        assertEquals(request.getRequestURI(), response.getBody().getPath());
    }

    @Test
    void testFileNotFoundException() {
        String errorMessage = "Arquivo não localizado";
        when(fileNotFoundException.getMessage()).thenReturn(errorMessage);

        MockHttpServletRequest request = new MockHttpServletRequest();
        ResponseEntity<StandardError> response = controllerExceptionHandler.fileNotFoundException(
                fileNotFoundException, request);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertEquals(HttpStatus.NOT_FOUND.value(), response.getBody().getStatus());
        assertEquals("FileNotFoundException.", response.getBody().getError());
        assertEquals(errorMessage, response.getBody().getMessage());
        assertEquals(request.getRequestURI(), response.getBody().getPath());
    }

    @Test
    void testAuthentication() {
        String errorMessage = "Erro de autenticação";
        when(authenticateErrorException.getMessage()).thenReturn(errorMessage);

        MockHttpServletRequest request = new MockHttpServletRequest();
        ResponseEntity<StandardError> response = controllerExceptionHandler.authentication(
                authenticateErrorException, request);

        assertEquals(HttpStatus.UNAUTHORIZED, response.getStatusCode());
        assertEquals(HttpStatus.UNAUTHORIZED.value(), response.getBody().getStatus());
        assertEquals("AuthenticateErrorException", response.getBody().getError());
        assertEquals(errorMessage, response.getBody().getMessage());
        assertEquals(request.getRequestURI(), response.getBody().getPath());
    }

    @Test
    public void testValidation() {
        BeanPropertyBindingResult bindingResult = new BeanPropertyBindingResult(null, "objectName");

        List<FieldError> fieldErrors = new ArrayList<>();
        FieldError fieldError1 = new FieldError("objectName", "fieldName1", "Error message 1");
        FieldError fieldError2 = new FieldError("objectName", "fieldName2", "Error message 2");
        fieldErrors.add(fieldError1);
        fieldErrors.add(fieldError2);

        bindingResult.addError(fieldError1);
        bindingResult.addError(fieldError2);

        HttpServletRequest request = mock(HttpServletRequest.class);
        when(request.getRequestURI()).thenReturn("/api/resource");

        MethodArgumentNotValidException exception = new MethodArgumentNotValidException((MethodParameter) null, bindingResult);

        ControllerExceptionHandler controllerExceptionHandler = new ControllerExceptionHandler();
        ResponseEntity<StandardError> response = controllerExceptionHandler.validation(exception, request);

        assertEquals(HttpStatus.UNPROCESSABLE_ENTITY, response.getStatusCode());
        assertEquals(HttpStatus.UNPROCESSABLE_ENTITY.value(), response.getBody().getStatus());
        assertEquals("Validation Error", response.getBody().getError());
        assertEquals("Error message 1", response.getBody().getMessage());
        assertEquals(request.getRequestURI(), response.getBody().getPath());

        List<FieldError> errorList = exception.getBindingResult().getFieldErrors();
        assertEquals(2, errorList.size());

        FieldMessage error1 = new FieldMessage();
        error1.setFieldName(errorList.get(0).getField());
        error1.setMessage(errorList.get(0).getDefaultMessage());
        assertEquals("fieldName1", error1.getFieldName());
        assertEquals("Error message 1", error1.getMessage());

        FieldMessage error2 = new FieldMessage();
        error2.setFieldName(errorList.get(1).getField());
        error2.setMessage(errorList.get(1).getDefaultMessage());
        assertEquals("fieldName2", error2.getFieldName());
        assertEquals("Error message 2", error2.getMessage());
    }

    @Test
    void testNonUniqueResultException() {
        String errorMessage = "Erro: A consulta retornou mais de um resultado quando esperava-se apenas um. " +
                "Verifique os critérios de busca e tente novamente com critérios mais específicos ou " +
                "use outra forma de filtragem. ";

        MockHttpServletRequest request = new MockHttpServletRequest();
        ResponseEntity<StandardError> response = controllerExceptionHandler.nonUniqueResultException(
                nonUniqueResultException, request);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals(HttpStatus.BAD_REQUEST.value(), response.getBody().getStatus());
        assertEquals("Non Unique Result", response.getBody().getError());
        assertEquals(errorMessage, response.getBody().getMessage());
        assertEquals(request.getRequestURI(), response.getBody().getPath());
    }

    @Test
    void testIdentifierGenerationException() {
        String errorMessage = "Houve um problema ao gerar um identificador único para o registro. " +
                "Por favor, revise os dados e tente novamente ou entre em contato com suporte técnico";

        MockHttpServletRequest request = new MockHttpServletRequest();
        ResponseEntity<StandardError> response = controllerExceptionHandler.identifierGenerationException(
                identifierGenerationException, request);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals(HttpStatus.BAD_REQUEST.value(), response.getBody().getStatus());
        assertEquals("Identifier Generation", response.getBody().getError());
        assertEquals(errorMessage, response.getBody().getMessage());
        assertEquals(request.getRequestURI(), response.getBody().getPath());
    }

    @Test
    void testMethodArgumentTypeMismatch() {
        String errorMessage = "O valor informado para a requisição é inválido. Por favor, verifique se os valores informados estão corretos e tente novamente";

        MockHttpServletRequest request = new MockHttpServletRequest();

        ResponseEntity<StandardError> response = controllerExceptionHandler.methodArgumentTypeMismatch(
                methodArgumentTypeMismatchException, request);

        assertEquals(HttpStatus.METHOD_NOT_ALLOWED, response.getStatusCode());
        assertEquals(HttpStatus.METHOD_NOT_ALLOWED.value(), response.getBody().getStatus());
        assertEquals("Method Argument Type Mismatch", response.getBody().getError());
        assertEquals(errorMessage, response.getBody().getMessage());
        assertEquals(request.getRequestURI(), response.getBody().getPath());
    }

    @Test
    void testHttpRequestMethodNotSupportedException() {
        String errorMessage = "O valor informado para a requisição é inválido. Por favor, verifique se os valores informados estão corretos e tente novamente";

        MockHttpServletRequest request = new MockHttpServletRequest();
        ResponseEntity<StandardError> response = controllerExceptionHandler.httpRequestMethodNotSupportedException(
                httpRequestMethodNotSupportedException, request);

        assertEquals(HttpStatus.METHOD_NOT_ALLOWED, response.getStatusCode());
        assertEquals(HttpStatus.METHOD_NOT_ALLOWED.value(), response.getBody().getStatus());
        assertEquals("Method Argument Type Mismatch", response.getBody().getError());
        assertEquals(errorMessage, response.getBody().getMessage());
        assertEquals(request.getRequestURI(), response.getBody().getPath());
    }

    @Test
    void testNoHandlerFoundException() {
        String errorMessage = "Não foi possível encontrar a página que você está procurando. " +
                "Verifique se a URL está correta ou tente novamente mais tarde.";

        MockHttpServletRequest request = new MockHttpServletRequest();
        ResponseEntity<StandardError> response = controllerExceptionHandler.noHandlerFoundException(
                noHandlerFoundException, request);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertEquals(HttpStatus.NOT_FOUND.value(), response.getBody().getStatus());
        assertEquals("No Handler Found", response.getBody().getError());
        assertEquals(errorMessage, response.getBody().getMessage());
        assertEquals(request.getRequestURI(), response.getBody().getPath());
    }

    @Test
    void testHandleDataIntegrityViolationException_UniqueConstraintViolation() {
        String errorMessage = "O campo 'field' com o valor (value) já existe. Por favor, insira um valor diferente " +
                "ou edite o registro existente.";

        MockHttpServletRequest request = new MockHttpServletRequest();
        when(dataIntegrityViolationException.getMessage()).thenReturn(errorMessage);

        ResponseEntity<StandardError> response = controllerExceptionHandler.handleDataIntegrityViolationException(
                dataIntegrityViolationException, request);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals(HttpStatus.BAD_REQUEST.value(), response.getBody().getStatus());
        assertEquals("Data Integrity Violation", response.getBody().getError());
        assertEquals("O campo 'field' com o valor (value) já existe. Por favor, insira um valor diferente " +
                "ou edite o registro existente.", response.getBody().getMessage());
        assertEquals(request.getRequestURI(), response.getBody().getPath());
    }

    @Test
    void testHandleDataIntegrityViolationException_ForeignKeyConstraintViolation() {
        String errorMessage = "Não foi possível atualizar/excluir o registro em: table_name. Parece que existe uma " +
                "dependência com: referenced_table_name. Por favor, verifique os dados que estão sendo modificados ou" +
                " excluídos e tente novamente.";

        MockHttpServletRequest request = new MockHttpServletRequest();
        when(dataIntegrityViolationException.getMessage()).thenReturn(errorMessage);

        ResponseEntity<StandardError> response = controllerExceptionHandler.handleDataIntegrityViolationException(
                dataIntegrityViolationException, request);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals(HttpStatus.BAD_REQUEST.value(), response.getBody().getStatus());
        assertEquals("Data Integrity Violation", response.getBody().getError());
        assertEquals("Não foi possível atualizar/excluir o registro em: table_name. Parece que existe " +
                "uma dependência com: referenced_table_name. Por favor, verifique os dados que estão sendo " +
                "modificados ou excluídos e tente novamente.", response.getBody().getMessage());
        assertEquals(request.getRequestURI(), response.getBody().getPath());
    }

    @Test
    void testHandleDataIntegrityViolationException_ConstraintViolation() {
        String errorMessage = "Campo 'value1' (value2) possui valor inválido";

        MockHttpServletRequest request = new MockHttpServletRequest();
        when(dataIntegrityViolationException.getMessage()).thenReturn(errorMessage);

        ResponseEntity<StandardError> response = controllerExceptionHandler.handleDataIntegrityViolationException(
                dataIntegrityViolationException, request);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals(HttpStatus.BAD_REQUEST.value(), response.getBody().getStatus());
        assertEquals("Data Integrity Violation", response.getBody().getError());
        assertEquals("Campo 'value1' (value2) possui valor inválido", response.getBody().getMessage());
        assertEquals(request.getRequestURI(), response.getBody().getPath());
    }

    @Test
    void testHandleDataIntegrityViolationException_OtherException() {
        String errorMessage = "Some other exception occurred.";

        MockHttpServletRequest request = new MockHttpServletRequest();
        when(dataIntegrityViolationException.getMessage()).thenReturn(errorMessage);

        ResponseEntity<StandardError> response = controllerExceptionHandler.handleDataIntegrityViolationException(
                dataIntegrityViolationException, request);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals(HttpStatus.BAD_REQUEST.value(), response.getBody().getStatus());
        assertEquals("Data Integrity Violation", response.getBody().getError());
        assertEquals("Some other exception occurred.", response.getBody().getMessage());
        assertEquals(request.getRequestURI(), response.getBody().getPath());
    }

    @Test
    public void testHandleHttpMessageNotReadableException_UnrecognizedPropertyException() {
        UnrecognizedPropertyException cause = mock(UnrecognizedPropertyException.class);
        String propertyName = "propertyName";
        String originalMessage = "originalMessage";

        when(cause.getPropertyName()).thenReturn(propertyName);
        when(cause.getOriginalMessage()).thenReturn(originalMessage);

        HttpMessageNotReadableException exception = new HttpMessageNotReadableException("Unrecognized property", cause);

        HttpServletRequest request = mock(HttpServletRequest.class);
        when(request.getRequestURI()).thenReturn("/api/resource");

        ResponseEntity<StandardError> response = controllerExceptionHandler.handleHttpMessageNotReadableException(exception, request);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals(HttpStatus.BAD_REQUEST.value(), response.getBody().getStatus());
        assertEquals("HttpMessageNotReadableException", response.getBody().getError());
        assertEquals("O campo '" + propertyName + "' possui valor inválido: " + originalMessage, response.getBody().getMessage());
        assertEquals(request.getRequestURI(), response.getBody().getPath());
    }
}