package com.epam.resource.util;

import com.epam.resource.exceptions.ResourceException;
import org.apache.commons.io.IOUtils;

import java.io.IOException;
import java.io.InputStream;

public class FileUtils {
    private FileUtils() {}

    public static byte[] streamToByteArray(InputStream dataStream) {
        byte[] byteArray;
        try {
            byteArray = IOUtils.toByteArray(dataStream);
        } catch (IOException e) {
            throw new ResourceException("Could not parse audio stream");
        }
        return byteArray;
    }
}
