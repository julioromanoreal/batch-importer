package com.julioromano.batchimporter.processing;

import com.julioromano.batchimporter.exceptions.DirectoryCreationException;
import com.julioromano.batchimporter.exceptions.ProcessOutputException;
import com.julioromano.batchimporter.exceptions.ProcessingException;

import java.nio.file.Path;
import java.util.List;

/**
 * {@code BatchProcessing} interface represents the contract in which a valid
 * Processing component should comply in order to keep the application working.
 *
 * {@code BatchProcessing} interface represents the methods to process a list of files
 * and produce the output report.
 *
 * @author  Julio Romano
 * @since   1.0
 */
public interface BatchProcessing {

    void process(List<Path> files) throws ProcessingException;

    void produceOutput(BatchResult result) throws ProcessOutputException, DirectoryCreationException;
}
