package ru.javawebinar.basejava.storage.FIleWR;

import ru.javawebinar.basejava.model.Resume;

import java.io.*;

public class JsonStreamFWR implements FileWriterReader{

    @Override
    public void writeResumeToFile(OutputStream os, Resume r) throws IOException {
        try(Writer writer = new OutputStreamWriter(os)){

        }
    }

    @Override
    public Resume readResumeFromFile(InputStream is) throws IOException {
        return null;
    }

}
