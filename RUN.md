### Requirements

* openjdk version "11.0.2" 2019-01-15 (for development)
* docker

### How to run

There is a public docker image based on source
code: https://hub.docker.com/repository/docker/yushkevich/ict-nearby-earthquakes
You can run it as:

```
docker run -it yushkevich/ict-nearby-earthquakes:latest
```

### Development

Build:

```
 ./gradlew clean build
```

Run:

```
java -jar app/build/libs/app.jar
```

### Review comments
FEEDBACKS:

#### pros
* application builds and runs exactly as described in run.md
* results returned by an application are correct
* third party library used for response deserialization (FeatureCollection)
* Easy to understand
* tests for sorting, limit
#### cons
* Input validation checks only syntax (format of input), still value can make no sense (eg. negative value)
* No test for content returned by the repository
* No test for duplicates
* Deduplication doesn't work
* Single responsibility principle violated by putting in single class
* logic for filtering earthquakes
* reading input
* glue code for the application
* logic for filtering earthquake types is duplicated. Leaks to tests (filter is duplicated in test).
* Earthquake class could be immutable  
..................

#### Pros:
* code compiles and runs
* tests exist, using mockito
#### Cons:
* not hiding implementation behind interfaces
* the EarthquakeService is not the best clean code example, the service unnecessary reveals internal implementation, by exposing a feature filter in its API
* the implementation does not do any deduplication of earthquakes at the same location
* the Earthquake class could be immutable, equals/hashcode exist but never actually being used
* no error handling, exceptions are just being logged
* distance calculation algorithm is getting altitude parameters, not used code
* service test cover only happy path, some interesting cases are uncovered
* no lat/lng validation