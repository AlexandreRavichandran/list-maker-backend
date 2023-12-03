package com.medialistmaker.music.utils;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.InputStream;
import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.*;

class FileUtilsTest {

    @Autowired
    FileUtils fileUtils;

    @BeforeEach
    void beforeEach() {
        this.fileUtils = new FileUtils();
    }

    @Test
    void givenFolderPathWhenGetRandomFileInFolderShouldReturnInputStream() throws Exception {

        InputStream testGetRandomFile = this.fileUtils.getRandomFileInFolder("/music_posters");

        assertNotNull(testGetRandomFile);
    }

    @Test
    void givenEmptyFolderPathWhenGetRandomFileInFolderShouldReturnNull() throws Exception {

        InputStream testGetRandomFile = this.fileUtils.getRandomFileInFolder("/nonexistingfolder");

        assertNull(testGetRandomFile);
    }


    @Test
    void givenDirectoryPathWhenGetFilesInsideDirectoryByDirectoryPathShouldReturnAllFilesInDirectory() throws Exception {

        String[] testGetListOfFilesInsideFolder =
                this.fileUtils.getFilesInsideDirectoryByDirectoryPath("/music_posters");

        assertNotNull(testGetListOfFilesInsideFolder);
        assertNotEquals(0, testGetListOfFilesInsideFolder.length);
    }

    @Test
    void givenEmptyDirectoryPathWhenGetFilesInsideDirectoryByDirectoryPathShouldReturnEmptyList() throws Exception {

        String[] testGetListOfFilesInsideFolder =
                this.fileUtils.getFilesInsideDirectoryByDirectoryPath("/nonexistingfolder");

        assertNotNull(testGetListOfFilesInsideFolder);
        assertEquals(0, testGetListOfFilesInsideFolder.length);
    }

    @Test
    void givenListOfFilesWhenGetRandomFileShouldReturnRandomFileInList() {

        String[] files = {"Music 1", "Music 2", "Music 3"};

        String testGetRandomFile = this.fileUtils.getRandomFile(files);

        assertNotNull(testGetRandomFile);
        assertTrue(Arrays.asList(files).contains(testGetRandomFile));

    }

    @Test
    void givenListOfFilesWhenGetRandomFileAndOnlyOneFileShouldReturnTheFile() {

        String[] files = {"Music 1"};

        String testGetRandomFile = this.fileUtils.getRandomFile(files);

        assertNotNull(testGetRandomFile);
        assertEquals(files[0], testGetRandomFile);

    }
}