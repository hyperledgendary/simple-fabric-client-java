# simple-fabric-client-java

This is a simple self contained Hyperledger Fabric client example using the Gateway Java API.

It is written to work with the FabCar sample deployed using the [Blockchain vscode extension](https://github.com/IBM-Blockchain/blockchain-vscode-extension) local fabric network.

## Building the sample

The fabric-gateway-java sample can be built using the included gradle wrapper:

```
git clone https://github.com/hyperledgendary/simple-fabric-client-java.git
cd simple-fabric-client-java
./gradlew shadow
```

## Running the sample

You need to have installed and instantiated the FabCar sample using the vscode extension's local fabric, and exporting the local_fabric_wallet before running the sample.

Run the following command in the _simple-fabric-client-java_ directory:

```
java -jar build/libs/simpleFabricClient.jar admin <path_to_exported_wallet>
```

It's also possible to run the sample with a certificate and private key using the following command:

```
java -jar build/libs/simpleFabricClient.jar admin <path_to_certificate> <path_to_private_key>
```
