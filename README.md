Requires:
 - JDK 11,
 - OpenJFX SDK 11
 - Maven
 
To build please run:
 mvn clean install
 
To run app, please run:
java --module-path %PATH_TO_OPENJFX_SDK%/lib --add-modules javafx.controls,javafx.fxml,javafx.graphics -jar ./target/casting_dss-1.0-SNAPSHOT-jar-with-dependencies.jar