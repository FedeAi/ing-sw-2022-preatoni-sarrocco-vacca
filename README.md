# Final Project of Software Engineering
![picture](src/main/resources/graphics/backgrounds/menu.jpg)
## Grade: 30 cum laude 
## Getting Started
Java implementation of the table game Eriantys by the italian company Cranio Creations as part of the Bachelor of Science thesis. This is formally part of the Software Engineering course at Politecnico di Milano (A.Y. 2021 / 2022).
The aim of this project is the design and realization of a distributed client-server software based on object-oriented programming, using the MVC (Model-View-Controller) design model. 

More information about the game can be found [here](https://www.craniocreations.it/prodotto/eriantys)

## Requirements
* The game requires [Java 17](https://www.oracle.com/java/technologies/downloads/#java17) to run.
* In order to properly display the CLI client an UTF-8 and ANSI colors compatible shell is needed.
* To build the project, Maven is required.
## Features
The features we have realized are listed as follows:
* Base rules ☑️
* Complete rules ☑️
* Socket ☑️
* CLI ☑️
* GUI ☑️
* Additional feature - 12 character cards ☑️
* Additional feature - Multiple games ☑️
* Additional feature - Disconnection recovery ☑️
## Building the project
* The project is build as an Uber-jar that is comprehensive of CLI, GUI and Server.
* The executable provides a small launcher with all the options, no arguments are needed to run it.
After cloning the repo, build the project by running:
```
cd ing-sw-2022-preatoni-sarrocco-vacca
mvn clean package
```
You will find the output jar in the newly created "target" folder with the name GC30-1.0-SNAPSHOT-jar-with-dependencies.jar.
## Running the executable
To run the [jar](https://github.com/PSV-polimi-2022/ing-sw-2022-preatoni-sarrocco-vacca/blob/main/deliveries/jars) executable just issue this command in the console:
```
java -jar GC30-1.0-SNAPSHOT-jar-with-dependencies.jar
```
## Sidenote for Apple Silicon macOS users
For AArch64-based Macs we have provided a specific [jar](https://github.com/PSV-polimi-2022/ing-sw-2022-preatoni-sarrocco-vacca/blob/main/deliveries/jars/arm64). This is due to a JavaFX dependency conflict, as explained [here](https://www.reddit.com/r/JavaFX/comments/twye9j/javafx_on_m1_and_intel_macs/).
## Sidenote for Windows users
On Windows, if don't plan on using the Windows Terminal app, in order to properly display the CLI client you should enable the console UTF-8 character encoding beta feature.
## Docs
In this section you will find all the documentation regarding the project:

### UML diagrams
- [Initial MVC diagram](https://github.com/PSV-polimi-2022/ing-sw-2022-preatoni-sarrocco-vacca/tree/main/deliveries/UML/initial/)
- [Final - Model](https://github.com/PSV-polimi-2022/ing-sw-2022-preatoni-sarrocco-vacca/tree/main/deliveries/UML/final/model)
- [Final - Controller](https://github.com/PSV-polimi-2022/ing-sw-2022-preatoni-sarrocco-vacca/tree/main/deliveries/UML/final/controller)
- [Final - Listeners](https://github.com/PSV-polimi-2022/ing-sw-2022-preatoni-sarrocco-vacca/tree/main/deliveries/UML/final/listeners)
- [Final - Client and Server](https://github.com/PSV-polimi-2022/ing-sw-2022-preatoni-sarrocco-vacca/tree/main/deliveries/UML/final/client-server)
- [Initial Sequence Diagram](https://github.com/PSV-polimi-2022/ing-sw-2022-preatoni-sarrocco-vacca/blob/main/deliveries/Sequence%20Diagram/SequenceDiagram_Initial.pdf)

### JavaDoc
You can find our JavaDocs in the following [link](https://github.com/PSV-polimi-2022/ing-sw-2022-preatoni-sarrocco-vacca/tree/main/deliveries/docs)
### Peer Reviews
- [Model and Controller](https://github.com/PSV-polimi-2022/ing-sw-2022-preatoni-sarrocco-vacca/blob/main/deliveries/PeerReview/Peer_Review_UML.pdf)
- [Network](https://github.com/PSV-polimi-2022/ing-sw-2022-preatoni-sarrocco-vacca/blob/main/deliveries/PeerReview/Peer_Review_2_Network.pdf)

### Tests with Coverage
![This is an image](https://github.com/PSV-polimi-2022/ing-sw-2022-preatoni-sarrocco-vacca/blob/main/deliveries/TestCoverage/Test.png)

<a name="built"></a>
### Built with
* [Java 17](https://www.oracle.com/java/technologies/downloads/#java17)
* [IntelliJ IDEA](https://www.jetbrains.com/idea/)
* [JavaFx](https://openjfx.io)
* [Maven](https://maven.apache.org)
* [JUnit](https://junit.org/junit5/)

### Libraries e Plugins
|Libraries/Plugin|Description|
|------------|-----------|
|__maven__|Project management tool that is based on POM (project object model)|
|__junit__|Unit testing framework for the Java programming language|
|__JavaFx__|Java set of graphics and media packages|
|__UML-Generator__|Usefull plugin for autogenerate UML by the source code|
|__Lucidchart__|UML drawing tool|
-------------------------------


## List of Authors:

 - *[Davide Preatoni](https://github.com/)*
 - *[Federico Sarrocco](https://github.com/)*
 - *[Alessandro Vacca](https://github.com/)*
 
