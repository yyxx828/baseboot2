package com.xajiusuo.jpa.servlet;

import javax.servlet.ReadListener;
import javax.servlet.ServletInputStream;
import java.io.*;

/**
 * Created by hadoop on 19-6-14.
 */
public class JscpInputStream extends ServletInputStream {

    private ServletInputStream inputStream;

    private ByteArrayOutputStream baos = new ByteArrayOutputStream();

    public JscpInputStream(ServletInputStream inputStream) {
        this.inputStream = inputStream;
    }

    public byte[] getByte(){
        return baos.toByteArray();
    }

    public synchronized String toString(String charsetName){
        try{
            return baos.toString(charsetName);
        }catch (Exception e){
            return null;
        }
    }

    @Override
    public int read() throws IOException {
        int c = inputStream.read();
        if(c != -1){
            baos.write(c);
        }
        return c;
    }

    @Override
    public boolean isFinished() {
        return inputStream.isFinished();
    }

    @Override
    public boolean isReady() {
        return inputStream.isReady();
    }

    @Override
    public void setReadListener(ReadListener readListener) {
        inputStream.setReadListener(readListener);
    }
}
