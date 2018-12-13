# Rider

This is a project for CSc 652, Advanced Topics of Operating Systems, instructed
by Prof. John Hartman at the Department of Computer Science, University of
Arizona. The project is contributed by Yanmao Man and Priyamvadha
Gopalakrishnan.

Rider is a system monitoring library for microservices, written in Java. We use
Gradle to make the project:

```
gradle build
```

A `.jar` file is then generated under `build/libs/`. Copy this `.jar` file to
your microservice and call the library in your code in the following way:

```
(new rider.CPUPerProces("/path/to/logs", ms)).start();
```

Here, `/path/to/logs` is the directory where the logs file will be stored.
`ms` is recording interval in milliseconds, i.e., a record will be appended to
the log file every `ms` milliseconds.

Go nuts.
