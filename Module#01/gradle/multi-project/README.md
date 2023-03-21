# External Lab
## Module#1: Build Tools - Gradle
### Task#2: Create a project multi-project with two subprojects core and api

#### In the project directory, you can run:

###### Builds and Test the fat Jar
#### `./gradlew clean shadowJar test`

###### Builds the application
#### `./gradlew clean build :dependencies`

###### Launches the test
#### `./gradlew test --tests com.epam.esm.core.UtilsTest`

###### Test compile Java
#### `./gradlew :core:compileTestJava`

###### Runs the application
#### `./gradlew :api:run`

###### Publish to local repository
#### `./gradlew publishToMavenLocal`

###### Monitoring
##### [Gradle Enterprise](https://gradle.com/s/rvpsluhijhbps)

