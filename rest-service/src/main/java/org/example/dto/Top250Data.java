package org.example.dto;

import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Setter
@Getter
@ToString
public class Top250Data {
    private List<Top250DataDetail> items;
    private String errorMessage;
    public Top250Data()
    {
        errorMessage = "";
        items = new ArrayList<>();
    }

    public Top250Data(String errorMessage)
    {
        this.errorMessage = errorMessage;
        items = new ArrayList<>();
    }


}
