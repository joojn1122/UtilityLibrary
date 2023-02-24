package com.joojn.utils.interfaces;

import java.io.IOException;
import java.nio.charset.Charset;

public interface IWriteable {

    void write(byte[] content, boolean append) throws IOException;
    Charset encoding();

    default void write(String text) throws IOException
    {
        this.write(text.getBytes(this.encoding()));
    }

    default void write(byte[] content) throws IOException {
        this.write(content, false);
    }

    default void append(String text) throws IOException
    {
        this.write(text.getBytes(this.encoding()), true);
    }

    default void append(byte[] content) {
        this.append(content);
    }

}
