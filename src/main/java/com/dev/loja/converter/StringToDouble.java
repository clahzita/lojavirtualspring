package com.dev.loja.converter;

import groovyjarjarpicocli.CommandLine;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
public class StringToDouble implements Converter<String,Double> {
    @Override
    public Double convert(String source) {
        source = source.trim();

        if(!source.isEmpty()){
            source = source.replace(".","").replace(",",".");
            return Double.parseDouble(source);

        }

        return 0.;
    }
}
