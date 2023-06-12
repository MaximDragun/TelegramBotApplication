package org.example.exceptions;

public class UploadFileException extends RuntimeException {

    public UploadFileException(String message) {
        super(message);
    }

    public UploadFileException(Throwable cause) {
        super(cause);
    }
    public UploadFileException() {
        super();
    }
}