package com.julioromano.batchimporter.batch;

import exceptions.ProcessingException;

public interface BatchWatcher {

    void watchFiles(String path) throws ProcessingException;

}
