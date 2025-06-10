package iti.jets.misk.exceptions;

import iti.jets.misk.dtos.ApiResponse;
import org.hibernate.annotations.Synchronize;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.ErrorResponseException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler {

    private static final Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    // For handling Not Found Product Exception
    @ExceptionHandler(ProductNotFoundException.class)
    public ResponseEntity<ApiResponse> handleProductNotFound(ProductNotFoundException ex) {
        ApiResponse apiResponse = ApiResponse.error(ex.getMessage());
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(apiResponse);
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<String> handleIllegalArgument(IllegalArgumentException e) {
        logger.error("Validation error: {}", e.getMessage());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
    }
    @ExceptionHandler(CartException.class)
    public ResponseEntity<ApiResponse<String>> handleCartException(CartException ex) {
        return ResponseEntity.badRequest()
                .body(ApiResponse.error(ex.getMessage()));
    }

    // For handling Not Found User Exception
    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<String> handleUserNotFound(UserNotFoundException ex) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage());
    }

    // For handling Not Found Address Exception
    @ExceptionHandler(AddressNotFoundException.class)


    public ResponseEntity<String> handleAddressNotFound(AddressNotFoundException ex) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage());
    }


    // For handling order confirmation Exception
    @ExceptionHandler(OrderConfirmationException.class)
    public ResponseEntity<ApiResponse> handleOrderConfirmationException(OrderConfirmationException ex) {
        ApiResponse apiResponse = ApiResponse.error(ex.getMessage());
        return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY).body(apiResponse);
    }


    public ResponseEntity<ApiResponse> handleException(UserRegisterationException ex) {
        ApiResponse apiResponse= ApiResponse.error(ex.getMessage());
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(apiResponse);

    }

    public ResponseEntity<ApiResponse> handleException(UserAlreadyExistException ex) {
        ApiResponse apiResponse= ApiResponse.error(ex.getMessage());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(apiResponse);

    }

    @ExceptionHandler(InsufficientRecordsException.class)
    public ResponseEntity<ApiResponse> handleInsufficientRecordsExceptionException(InsufficientRecordsException ex) {
        ApiResponse apiResponse = ApiResponse.error(ex.getMessage());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(apiResponse);

    }


    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<ApiResponse<String>> handleAccessDenied(AccessDeniedException ex) {
        return ResponseEntity
                .status(HttpStatus.FORBIDDEN)
                .body(ApiResponse.error("You are not authorized to access this resource"));
    }


    @ExceptionHandler(GeneralErrorException.class)
    public ResponseEntity<ApiResponse<String>> handleGeneralErrorException(GeneralErrorException ex) {
        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(ApiResponse.error(ex.getMessage()));
    }

}