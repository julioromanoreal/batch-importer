## Batch Importer

Program to read .dat files from a specific directory, analyze the files and produce a report with specific information.

-----

<details open="open">
  <summary>Table of Contents</summary>
  <ol>
    <li>
      <a href="#about-the-project">About The Project</a>
      <ul>
        <li><a href="#built-with">Built With</a></li>
      </ul>
    </li>
    <li>
      <a href="#getting-started">Getting Started</a>
      <ul>
        <li><a href="#prerequisites">Prerequisites</a></li>
        <li><a href="#installation">Installation</a></li>
      </ul>
    </li>
    <li><a href="#usage">Usage</a></li>
    <li><a href="#future-improvements">Future improvements</a></li>
    <li><a href="#alternative-solution">Alternative solution</a></li>
    <li><a href="#contributing">Contributing</a></li>
    <li><a href="#license">License</a></li>
    <li><a href="#contact">Contact</a></li>
  </ol>
</details>

---

## About The Project

This is a simple project containing an endpoint that triggers a process that reads .dat files from a specific directory, analyze them and produce a report with specific information.

This is an example of a file to be parsed:

```aidl
001ç1234567891234çPedroç50000
001ç3245678865434çPauloç40000.99
002ç2345675434544345çJose da SilvaçRural
002ç2345675433444345çEduardo PereiraçRural
003ç10ç[1-10-100,2-30-2.50,3-40-3.10]çPedro
003ç08ç[1-34-10,2-33-1.50,3-40-0.10]çPaulo
```

The first part of each line defines the type of data and each type has a different layout. These are the available options:

* `001`: Data from salesmans as in `001çCPFçNameçSalary`
* `002`: Data from customers as in `002çCNPJçNameçBusiness Area`
* `003`: Data from sales as in `003çSale IDç[Item ID-Item Quantity-Item Price]çSalesman name`

After processing all files within the specified directory, the system must create a file within the output directory containing the following information:

* Quantity of customers in the files
* Quantity of salesman in the files
* ID of the most expensive sale
* The worst salesman

The name of the output file should end with `.done.dat`

### Built With

* Java 15
* [Maven](https://github.com/apache/maven)

## Getting Started

To get a local copy up and running follow these simple steps.

### Prerequisites

Make sure the following prerequisites are installed in your machine:

* Java 15

Run the following command to confirm:

```sh-session
$ java -version
openjdk version "15.0.1" 2020-10-20
OpenJDK Runtime Environment (build 15.0.1+9-18)
OpenJDK 64-Bit Server VM (build 15.0.1+9-18, mixed mode, sharing)
```

* Maven


Run the following command to confirm:

```sh-session
$ mvn --version
Apache Maven 3.6.3 (cecedd343002696d0abb50b32b541b8a6ba2883f)
```



### Installation

1. Clone the repo
```sh
$ git clone https://github.com/julioromanoreal/batch-importer.git
```
2. Install Maven dependencies
```sh
$ mvn package
```
3. Set the required properties at `src/main/resources/application.properties`
    * `salesDataInDir`: Directory in which the .dat files will be
    * `salesDataOutDir`: Directory in which the output file will be created


4. Start the application
```sh
$ java -jar com.julioromano.batchimporter.Main
```

## Usage

The application will monitor the directory specified as the `salesDataInDir` and whenever new .dat files are detected, the process will automatically start so the program will read all .dat files in the directory specified, analyze them and produce a report in the directory specified as the `salesDataOutDir`

## Future improvements
* Consider the usage of [Spring Batch](https://spring.io/projects/spring-batch) or [Apache Spark](https://spark.apache.org/) to process and analyze the files
* Consider working with [Apache Hadoop](https://hadoop.apache.org/) to better deal with a larger set of data

## Alternative Solution

For an alternative version, using Spring Boot, Docker and PostgreSQL, please refer to [https://github.com/julioromanoreal/batch-importer-spring-boot](https://github.com/julioromanoreal/batch-importer-spring-boot)

## Contributing

Any contributions you make are **greatly appreciated**.

1. Fork the Project
2. Create your Feature Branch (`git checkout -b feature/AmazingFeature`)
3. Commit your Changes (`git commit -m 'Add some AmazingFeature'`)
4. Push to the Branch (`git push origin feature/AmazingFeature`)
5. Open a Pull Request

## License

Distributed under the MIT License. See `LICENSE` for more information.

## Contact

Julio Romano - [@julioromano_](https://twitter.com/julioromano_) - julio.romano@gmail.com

Project Link: [https://github.com/julioromanoreal/batch-importer](https://github.com/julioromanoreal/batch-importer)
