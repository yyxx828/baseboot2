package com.xajiusuo.jpa.down;

import java.io.OutputStream;

/**
 * Created by hadoop on 19-6-18.
 */
@FunctionalInterface
public interface OutputStreamDown {

    void write(OutputStream out) throws Exception;

    static OutputStreamDown to(OutputStreamDown down){
        return down;
    }
}
