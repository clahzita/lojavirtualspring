package com.dev.loja.converter;

import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
public class DoubleToString implements Converter<Double,String> {

    @Override
    public String convert(Double source) {
        if(!source.isNaN() && source != null){
            return String.format("%.2f",source);
        }

        return "0,00";
    }
}
