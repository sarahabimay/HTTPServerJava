# HTTP Server Java

This is my apprenticeship HTTP Server project, written in Java.  It satisfies the acceptence tests of the FitNesse [Cob-Spec](https://github.com/8thlight/cob_spec).

###Requirements:
* Java 8
* Gradle 
  * Gradle Dependencies:

```testCompile group: 'junit', name: 'junit', version: '4.11'
compile group: 'com.google.guava', name:'guava', version:'18.0'
compile group: 'org.apache.commons', name: 'commons-io', version: '1.3.2'
compile group: 'org.apache.commons', name: 'commons-lang3', version: '3.4'```

###Acceptence Test Requirements:
* Fitnesse Cob-Spec tests.

###How To Run the Server (Mac/OS):
1. Clone the repo `git clone https://github.com/sarahabimay/HTTPServerJava.git`
2. Navigate to the repo: `cd HTTPServerJava`
3. Build the jar: `./gradlew clean build`
4. Run the server:
  * In IntelliJ: run the main from:  src/main/java/server/HTTPServerMain.java
  * In terminal: `java -jar /Users/{*user_name*}/{*path_to_server_dir*}/HTTPServerJava/build/libs/http_server_sarah-1.0-SNAPSHOT.jar`
  
5. Arguments:
  * `-p` : port flag; optional (defaults to 5000)
  * `-d` : public directory flag; optional (defaults to the cob-spec public dir)
  
6. Once the server is running, open a browser page to: `localhost:5000/` and you should see the public directory contents.

###How to Run the Cob-Spec Acceptence Tests

**All of the instructions that follow are in the [cob-spec github repo](https://github.com/8thlight/cob_spec), but I've regurgitated them here**

1. Clone the repo: `git clone git@github.com:8thlight/cob_spec.git`
2. Navigate to the repo: `cd cob_spec`
3. Compile the java code: `mvn package`
4. Start the FitNesse Server: `java -jar fitnesse.jar -p 9090`
5. In your browser go to: `http://localhost:9090`
6. Click on **HttpTestSuite** and then **Edit** in the nav bar.
7. Edit the **SERVER_START_COMMAND** field to be something like:
  * `{java -jar /Users/{*user_name*}/{*path_to_server_dir*}/HTTPServerJava/build/libs/http_server_sarah-1.0-SNAPSHOT.jar }`
  or
  *`{java -cp /Users/sarahjohnston/Sarah/http_server_sarah/build/libs/http_server_sarah-1.0-SNAPSHOT.jar HttpServerMain}`
8. Edit the **PUBLIC_DIR** field to be the location of the Cob-Spec public dir, which will be something like:
  * `/Users/*user_name*/*path_to_cob_spec_repo*/CobSpec/public`

9. **MAKE SURE THESE TWO LINES DO NOT HAVE A `-` BEFORE THE KEYWORD `!define` AS THIS COMMENTS THE LINE OUT.**

9. Click **SAVE**.
10. Click **SUITE** to run the all of the acceptence tests.
11. Bask in the GREEN glow!
