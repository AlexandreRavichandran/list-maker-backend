package com.medialistmaker.movie.controller;

import com.medialistmaker.movie.exception.notfoundexception.CustomNotFoundException;
import com.medialistmaker.movie.utils.FileUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.InputStream;
import java.net.URISyntaxException;

import static java.util.Objects.isNull;

@RestController
@RequestMapping("/api/movies/pictures")
public class PictureController {

    @Autowired
    FileUtils fileUtils;

    @GetMapping(value = "/illustrations/random", produces = MediaType.IMAGE_JPEG_VALUE)
    public ResponseEntity<InputStreamResource> getRandomIllustrationPicture() throws URISyntaxException, CustomNotFoundException {

        InputStream stream = this.fileUtils.getRandomFileInFolder("/movie_illustrations");

        if(isNull(stream)) {
            throw new CustomNotFoundException("Picture not found");
        }

        return new ResponseEntity<>(new InputStreamResource(stream), HttpStatus.OK);
    }

    @GetMapping(value = "/posters/random", produces = MediaType.IMAGE_JPEG_VALUE)
    public ResponseEntity<InputStreamResource> getRandomPosterPicture() throws URISyntaxException, CustomNotFoundException {

        InputStream stream = this.fileUtils.getRandomFileInFolder("/movie_posters");

        if(isNull(stream)) {
            throw new CustomNotFoundException("Picture not found");
        }

        return new ResponseEntity<>(new InputStreamResource(stream), HttpStatus.OK);
    }

}
