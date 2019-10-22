package com.xajiusuo.jpa.down;

import java.io.BufferedWriter;

/**
 * Created by hadoop on 19-6-18.
 */
@FunctionalInterface
public interface WriterDown {

    void write(BufferedWriter out) throws Exception;

    static WriterDown to(WriterDown down){
        return down;
    }
}
