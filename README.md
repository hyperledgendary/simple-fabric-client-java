# simple-fabric-client-java

This is a simple self contained Hyperledger Fabric client example using the Gateway Java API.

It is written to work with the FabCar sample deployed using the [Blockchain vscode extension](https://github.com/IBM-Blockchain/blockchain-vscode-extension) local fabric network.

## Before starting

Before using the sample, you may need to build fabric-gateway-java locally first due to an [issue regarding how it is published](https://jira.hyperledger.org/projects/FGJ/issues/FGJ-30):

```
git clone https://github.com/hyperledger/fabric-gateway-java.git
cd fabric-gateway-java
mvn install -DskipTests
```

Note: you will need Maven installed for this step.

## Building the sample

The fabric-gateway-java sample can be built using the included gradle wrapper:

```
git clone https://github.com/hyperledgendary/simple-fabric-client-java.git
cd simple-fabric-client-java
./gradlew shadow
```

## Running the sample

You need to have installed and instantiated the FabCar sample using vscode, and exporting the local_fabric_wallet before running the sample.

From the _simple-fabric-client-java_ directory using the following command:

```
java -jar build/libs/simpleFabricClient.jar admin <path_to_exported_wallet>
```

It's also possible to run the sample with a certificate and private key using the following command:

```
java -jar build/libs/simpleFabricClient.jar admin <path_to_certificate> <path_to_private_key>
```
