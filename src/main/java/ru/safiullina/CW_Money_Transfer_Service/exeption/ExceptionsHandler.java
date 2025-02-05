package ru.safiullina.CW_Money_Transfer_Service.exeption;


import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import ru.safiullina.CW_Money_Transfer_Service.model.ErrorResponsesDTO;

@ControllerAdvice
public class ExceptionsHandler {

    @ExceptionHandler(ErrorConfirmation.class)
    public ResponseEntity<ErrorResponsesDTO> handlerErrorConfirmation(ErrorConfirmation exception){
        return new ResponseEntity<>(new ErrorResponsesDTO(exception.getMessage(), 0), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(ErrorInputData.class)
    public ResponseEntity<ErrorResponsesDTO> handlerErrorInputData(ErrorInputData exception){
        return new ResponseEntity<>(new ErrorResponsesDTO(exception.getMessage(), 0), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(ErrorTransfer.class)
    public ResponseEntity<ErrorResponsesDTO> handlerErrorTransfer(ErrorTransfer exception){
        return new ResponseEntity<>(new ErrorResponsesDTO(exception.getMessage(), 0), HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
