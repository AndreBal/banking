package com.balash.banking.service.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

public class TextToFileSaver {

    private static final Logger LOGGER = LoggerFactory.getLogger(TextToFileSaver.class);

    private static final TextToFileSaver INSTANCE = new TextToFileSaver();

    private TextToFileSaver(){}

    public static TextToFileSaver getInstance(){
        return INSTANCE;
    }

    public void saveToTXTFile(String fileName, String textToSave){

        File folder = new File("check");
        if (!folder.exists()) {
            if (folder.mkdir()) {
                LOGGER.debug("Folder 'check' created.");
            } else {
                LOGGER.error("Failed to create 'check' folder.");
                return;
            }
        }

        File file = new File(folder, fileName+".txt");
        try {
            FileWriter fileWriter = new FileWriter(file, StandardCharsets.UTF_8); //, StandardCharsets.UTF_16
            fileWriter.write(textToSave);
            fileWriter.close();
            LOGGER.debug("String saved to file: " + file.getAbsolutePath());
        } catch (IOException e) {
            LOGGER.error("An error occurred while saving the string to a file with filename "+fileName,e);
        }

    }
}
