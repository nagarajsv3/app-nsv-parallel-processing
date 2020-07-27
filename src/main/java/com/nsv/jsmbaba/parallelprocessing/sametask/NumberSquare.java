package com.nsv.jsmbaba.parallelprocessing.sametask;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class NumberSquare {
    private Integer number;
    private Integer square;

    NumberSquare(Integer number){
        this.number = number;
    }

    NumberSquare(Integer number, Integer square){
        this.number = number;
        this.square = square;
    }
}
