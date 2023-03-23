package idlreasonerchoco.advice;

import es.us.isa.idlreasonerchoco.configuration.IDLException;
import idlreasonerchoco.model.ErrorResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class IDLExceptionAdvice {

    @ExceptionHandler(IDLException.class)
    public ResponseEntity<ErrorResponse> handleIDLException(IDLException e) {
        ErrorResponse response = new ErrorResponse();
        response.setMessage(e.getMessage());
        return ResponseEntity.badRequest().body(response);
    }
}
