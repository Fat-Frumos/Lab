# External Lab
## Module#1: Build Tools - Gradle
### Task 1: Assemble custom jar utils-1.3.5.jar

#### In the project directory, you can run:

###### Builds and Test the fat Jar
#### `./gradlew clean shadowJar test`

###### Builds the application
#### `./gradlew clean build :dependencies`

###### Launches the test
#### `./gradlew test --tests com.epam.esm.StringUtilsTest`

###### Test compile Java
#### `./gradlew :app:compileTestJava`

###### Publish to local repository
#### `./gradlew publishToMavenLocal`

###### Monitoring
##### [Gradle Enterprise](https://gradle.com/s/3hhuvrxszyfjk)

