package com.medialistmaker.music.utils;

import org.springframework.stereotype.Component;

import java.util.Map;

import static java.util.Objects.nonNull;

@Component
public class DeezerParameterFormatter {

    public String formatParams(Map<String, String> params) {

        StringBuilder formattedParams = new StringBuilder();

        for (Map.Entry<String, String> param : params.entrySet()) {

            if(nonNull(param.getValue())) {
                formattedParams
                        .append(param.getKey())
                        .append(":")
                        .append(this.addDoubleQuotes(param.getValue()))
                        .append(" ");
            }

        }

        return formattedParams.toString();
    }

    private String addDoubleQuotes(String sentence) {
        return "\"" + sentence + "\"";
    }
}
