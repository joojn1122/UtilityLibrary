package com.joojn.utils;

import com.joojn.utils.interfaces.IReadable;
import com.joojn.utils.interfaces.IWriteable;

import java.io.*;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

public class FileUtil {

    private FileUtil() {}

    public static File getJar() throws URISyntaxException {
        URL url = FileUtil.class.getProtectionDomain().getCodeSource().getLocation();
        return new File(url.toURI());
    }

    public static FileWrapper get(String path)
    {
        return new FileWrapper(path, StandardCharsets.UTF_8);
    }

    public static IReadable get(InputStream stream, Charset charset) {

        return new IReadable() {
            @Override
            public byte[] readBytes() throws IOException {
                byte[] buff = new byte[stream.available()];

                stream.read(buff);
                stream.close();

                return buff;
            }

            @Override
            public Charset encoding() {
                return charset;
            }
        };

    };

    public static IWriteable get(OutputStream stream, Charset charset)
    {
        return new IWriteable() {
            @Override
            public void write(byte[] content, boolean append) throws IOException {
                stream.write(content);
                stream.close();
            }

            @Override
            public Charset encoding() {
                return charset;
            }
        };
    }

}
