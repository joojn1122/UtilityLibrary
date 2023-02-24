package com.joojn.utils;

import com.joojn.utils.interfaces.IReadable;
import com.joojn.utils.interfaces.IWriteable;

import java.io.*;
import java.net.URI;
import java.nio.charset.Charset;
import java.nio.file.Files;

public class FileWrapper extends File implements IWriteable, IReadable {

    private final Charset encoding;

    public FileWrapper(String pathname, Charset encoding) {
        super(pathname);

        this.encoding = encoding;
    }

    public FileWrapper(String parent, String child, Charset encoding) {
        super(parent, child);

        this.encoding = encoding;
    }

    public FileWrapper(File parent, String child, Charset encoding) {
        super(parent, child);

        this.encoding = encoding;
    }

    public FileWrapper(URI uri, Charset encoding) {
        super(uri);

        this.encoding = encoding;
    }

    private void checkNotDirectory(String reason) {
        if (isDirectory())
        {
            throw new IllegalArgumentException(reason);
        }
    }

    @Override
    public void write(byte[] content, boolean append) throws IOException {
        checkNotDirectory("Cannot write to directory");

        try(OutputStream stream = new FileOutputStream(this, append))
        {
            stream.write(content);
        }
    }

    @Override
    public byte[] readBytes() throws IOException {
        checkNotDirectory("Cannot read from directory");

        byte[] buff;

        try (InputStream stream = Files.newInputStream(toPath()))
        {
            buff = new byte[stream.available()];
            int read = stream.read(buff);

            if (read != buff.length)
            {
                throw new IOException("Could not read all bytes");
            }
        }

        return buff;
    }

    @Override
    public Charset encoding() {
        return this.encoding;
    }


    /**
     * Gets the base name of the file, file.txt -> file
     */
    public String getBaseName() {
        checkNotDirectory("Cannot get base name of directory");

        String name = getName();

        return name.substring(
                0,
                name.lastIndexOf(".")
        );
    }

    /**
     * Gets the extension of the file, file.txt -> txt
     */
    public String getExtension() {
        checkNotDirectory("Cannot get extension of directory");

        String name = getName();

        return name.substring(
                name.lastIndexOf(".") + 1
        );
    }

    /**
     * Returns true if the file is empty
     */
    public boolean isEmpty() {
        return length() == 0;
    }

}
