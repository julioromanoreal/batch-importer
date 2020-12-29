package com.julioromano.batchimporter.processing;

import com.julioromano.batchimporter.exceptions.ProcessingException;

import java.nio.file.Path;
import java.util.List;

public interface BatchProcessing {

    void process(Path dir, List<Path> files) throws ProcessingException;

    void produceOutput(BatchResult result) throws ProcessingException;
}
