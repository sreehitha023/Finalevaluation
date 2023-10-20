package com.msil.evaluation.advice;

import com.msil.evaluation.constants.StringConstants;
import com.msil.evaluation.dto.ErrorResponse;
import com.msil.evaluation.exceptionHandler.*;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;


@ControllerAdvice
@Log4j2
public class CustomExceptionHandler extends ResponseEntityExceptionHandler {

  @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleUserNotFoundException(UserNotFoundException ex)
  {
      log.info(StringConstants.Exception+"handleUserNotFoundException");
        ErrorResponse errorResponse = new ErrorResponse();
        errorResponse.setErrorName(ex.getClass().getSimpleName());
        errorResponse.setMessage(ex.getMessage());
        errorResponse.setErrorCode(String.valueOf(HttpStatus.NOT_FOUND));
        log.info(errorResponse);
        return new ResponseEntity<>(errorResponse, HttpStatus.INTERNAL_SERVER_ERROR);
    }
  @ExceptionHandler(DuplicateEntryException.class)
    public ResponseEntity<ErrorResponse > handleDuplicateEntryException(DuplicateEntryException e)
  {
      log.info(StringConstants.Exception+"handleDuplicateEntryException");
      ErrorResponse errorResponse = new ErrorResponse();
      errorResponse.setErrorName(e.getClass().getSimpleName());
      errorResponse.setMessage(e.getMessage());
      errorResponse.setErrorCode(String.valueOf(HttpStatus.NOT_FOUND));
      log.info(errorResponse);
      return new ResponseEntity<>(errorResponse,HttpStatus.INTERNAL_SERVER_ERROR);
  }
  @ExceptionHandler(ArgumentConstraintViolation.class)
    public ResponseEntity<ErrorResponse> handleArgumentConstraintViolation(ArgumentConstraintViolation argumentConstraintViolation)
  {
      log.info(StringConstants.Exception+"handleArgumentConstraintViolation");
      ErrorResponse errorResponse = new ErrorResponse();
      errorResponse.setErrorName(argumentConstraintViolation.getClass().getSimpleName());
      errorResponse.setPath("/trading/search");
      errorResponse.setMessage(argumentConstraintViolation.getMessage());
      errorResponse.setErrorCode(String.valueOf((HttpStatus.NOT_FOUND)));
      log.info(errorResponse);
      return new ResponseEntity<>(errorResponse,HttpStatus.INTERNAL_SERVER_ERROR);
  }

  @ExceptionHandler(InvalidPasswordException.class)
    public ResponseEntity<ErrorResponse> handleInvalidPasswordException(InvalidPasswordException invalidPasswordException)
  {
      log.info(StringConstants.Exception+"handleInvalidPasswordException");
      ErrorResponse errorResponse = new ErrorResponse();
      errorResponse.setErrorName(invalidPasswordException.getClass().getSimpleName());
      errorResponse.setPath("/trading/register");
      errorResponse.setMessage(invalidPasswordException.getMessage());
      errorResponse.setErrorCode(String.valueOf(HttpStatus.ACCEPTED));
      log.info(errorResponse);
      return new ResponseEntity<>(errorResponse,HttpStatus.INTERNAL_SERVER_ERROR);
  }
    @ExceptionHandler(CustomException.class)
    public ResponseEntity<ErrorResponse> handleCustomException(CustomException e) {
        log.info(StringConstants.Exception+"handleCustomException");
        ErrorResponse errorResponse = new ErrorResponse();
        errorResponse.setErrorName(e.getClass().getSimpleName());
        errorResponse.setMessage(e.getMessage());
        errorResponse.setErrorCode(String.valueOf(HttpStatus.BAD_REQUEST));
        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }

}
