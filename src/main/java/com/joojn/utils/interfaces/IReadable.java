package com.joojn.utils.interfaces;

import java.io.IOException;
import java.nio.charset.Charset;

public interface IReadable {

    byte[] readBytes() throws IOException;
    Charset encoding();

    default String read() throws IOException {
        return new String(
                this.readBytes(),
                this.encoding()
        );
    }

}
