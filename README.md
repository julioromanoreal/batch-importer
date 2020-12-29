## Batch Importer

Program to read .dat files from a specific directory, analyze the files and produce a report with specific information.

The solution requires JRE 15 and Maven.

To run it one should first build the project using Maven using the command

```
mvn package
```

And then execute the Main class, inside `src/main/java/com/julioromano/batchimporter`

To run the tests one can use the command

```
mvn test
```

To configure it, use the file `src/main/java/resources/application.properties`, replacing the following properties:
* `fileDelimiter`: The character that delimits different information in the input file
* `timeToStartProcess`: Time to wait before starting the process when a new file is detected. This is to make it possible for many huge files to be copied and processed together rather than starting the process right away, while other files are still being copied.
* `salesDataInDir`: Directory to be watched in order to get new input files to be processed
* `salesDataOutDir`: Directory where the output file will be saved

After running the `Main` class, everytime a new file gets created/copied into the `salesDataInDir` directory, the process will be automatically triggered.

The program will keep running indefinitely and will only be stopped by quiting the JVM process.

The files are read and analyzed line by line so avoiding a high usage of memory by not keeping too much information in the heap. 

It can be extended to work with other files by adding the new properties in the properties file and extending the functionality in `com.julioromano.batchimporter.batch.files.FileBatchWatcher`. In the future, it may also be replaced by a database file watcher by implementing the interface `com.julioromano.batchimporter.processing.BatchProcessing` creating the desired functionality.

This application was developed using plain Java, with no external dependencies (such as a database) and the whole analysis and processing taking place in memory. For a different solution, using Docker and PostgreSQL, please refer to [this repository](https://github.com/julioromanoreal/batch-importer-spring-boot).

### Future improvements

* Consider the usage of [Spring Batch](https://spring.io/projects/spring-batch) or [Apache Spark](https://spark.apache.org/) to process and analyze the files
* Consider working with [Apache Hadoop](https://hadoop.apache.org/) to better deal with a larger set of data