/*
 * This Java source file was generated by the Gradle 'init' task.
 */
package org.example.fabric.client;

import java.nio.file.Path;
import java.nio.file.Paths;

import org.hyperledger.fabric.gateway.Contract;
import org.hyperledger.fabric.gateway.Gateway;
import org.hyperledger.fabric.gateway.Network;
import org.hyperledger.fabric.gateway.Wallet;

public interface Sample {
  static void main(String[] args) throws Exception {

    // Create a new wallet for managing identities.
    Wallet wallet = Wallet.createInMemoryWallet();

    // load a CCP
    Path networkConfigPath = Paths.get(args[0]).toAbsolutePath();

    Gateway.Builder builder = Gateway.createBuilder();
    builder.identity(wallet, "user1").networkConfig(networkConfigPath);

    // create a gateway connection
    try (Gateway gateway = builder.connect()) {
      // get the network and contract
      Network network = gateway.getNetwork("mychannel");
      Contract contract = network.getContract("fabcar");

      byte[] result = contract.submitTransaction("createCar", "CAR10", "VW", "Polo", "Grey", "Mary");
      System.out.println(result);

      result = contract.evaluateTransaction("queryAllCars");
      System.out.println(new String(result));

    } catch (Exception ex) {
      ex.printStackTrace();
    }
  }
}