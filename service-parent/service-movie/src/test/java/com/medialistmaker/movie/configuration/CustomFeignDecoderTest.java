package com.medialistmaker.movie.configuration;

import com.medialistmaker.movie.exception.badrequestexception.CustomBadRequestException;
import com.medialistmaker.movie.exception.servicenotavailableexception.ServiceNotAvailableException;
import feign.Request;
import feign.RequestTemplate;
import feign.Response;
import feign.Util;
import org.junit.jupiter.api.Test;

import static java.util.Collections.emptyMap;
import static org.junit.jupiter.api.Assertions.*;

class CustomFeignDecoderTest {

    private final CustomFeignDecoder feignDecoder = new CustomFeignDecoder();

    private final RequestTemplate requestTemplate = new RequestTemplate();

    @Test
    void whenResponseIsBetween400And499ShouldThrowBadRequestException() {

        Response response = Response.builder()
                .status(400)
                .reason("TestError")
                .request(Request.create(Request.HttpMethod.GET,"/test" ,emptyMap(), null, Util.UTF_8,this.requestTemplate))
                .build();

        Exception e = this.feignDecoder.decode("test", response);

        assertEquals(CustomBadRequestException.class, e.getClass());
        assertEquals(response.reason(), e.getMessage());
    }

    @Test
    void whenResponseIsBetween500And599ShouldThrowServiceNotAvailableException() {

        Response response = Response.builder()
                .status(500)
                .reason("TestError")
                .request(Request.create(Request.HttpMethod.GET,"/test" ,emptyMap(), null, Util.UTF_8,this.requestTemplate))
                .build();

        Exception e = this.feignDecoder.decode("test", response);

        assertEquals(ServiceNotAvailableException.class, e.getClass());
        assertEquals(response.reason(), e.getMessage());

    }
}