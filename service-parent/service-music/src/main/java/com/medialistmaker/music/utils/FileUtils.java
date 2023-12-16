package com.medialistmaker.music.utils;

import org.springframework.stereotype.Component;

import java.io.InputStream;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Paths;
import java.util.Random;

import static java.util.Objects.isNull;

@Component
public class FileUtils {

    public InputStream getRandomFileInFolder(String path) throws URISyntaxException {

        String[] pictures = this.getFilesInsideDirectoryByDirectoryPath(path);

        if (isNull(pictures) || pictures.length == 0) {
            return null;
        }

        String pictureName = this.getRandomFile(pictures);

        return this.getClass().getResourceAsStream( path + "/" + pictureName);
    }

    public String[] getFilesInsideDirectoryByDirectoryPath(String folderPath) throws URISyntaxException {

        URL resource = this.getClass().getResource(folderPath);
        if(isNull(resource)) {
            return new String[0];
        }
        return Paths.get(resource.toURI()).toFile().list();
    }

    public String getRandomFile(String[] files) {

        if (files.length == 1) {
            return files[0];
        } else {
            Random random = new Random();
            return files[random.nextInt(files.length)];
        }
    }
}