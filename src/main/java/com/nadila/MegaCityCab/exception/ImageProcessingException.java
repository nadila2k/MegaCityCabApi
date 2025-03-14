package com.nadila.MegaCityCab.exception;

import java.io.IOException;

public class ImageProcessingException extends Throwable {
    public ImageProcessingException(String errorProcessingImage, IOException e) {
        super(errorProcessingImage, e);
    }
}
