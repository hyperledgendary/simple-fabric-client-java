/*
 * This Java source file was generated by the Gradle 'init' task.
 */
package org.example.fabric.client;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Reader;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;

import org.hyperledger.fabric.gateway.Contract;
import org.hyperledger.fabric.gateway.Gateway;
import org.hyperledger.fabric.gateway.Network;
import org.hyperledger.fabric.gateway.Wallet;
import org.hyperledger.fabric.gateway.Wallet.Identity;

import com.github.jknack.handlebars.Context;
import com.github.jknack.handlebars.Handlebars;
import com.github.jknack.handlebars.Template;

public interface Sample {

    public static final String CONTRACT_NAME = "fabcar";

    // network details
    public static final String CA_NAME = "ca-org1";
    public static final String CA_URL = "http://localhost:17054";
    public static final String CHANNEL_NAME = "mychannel";
    public static final String MSP_ID = "Org1MSP";
    public static final String ORDERER_NAME = "orderer.example.com";
    public static final String ORDERER_URL = "grpc://localhost:17050";
    public static final String ORG_NAME = "Org1";
    public static final String PEER_NAME = "peer0.org1.example.com";
    public static final String PEER_URL = "grpc://localhost:17051";

    static void main(String[] args) throws Exception {

        // process command line args
        if (args.length != 3) {
            System.out.println("Usage:\n\tjava <TBC>.jar identity certificate privateKey");
            System.exit(1);
        }

        String identity = args[0];
        Path certificatePath = Paths.get(args[1]).toAbsolutePath();
        Path privateKeyPath = Paths.get(args[2]).toAbsolutePath();

        System.out.println("identity: " + identity);
        System.out.println("certificate: " + certificatePath.toString());
        System.out.println("privateKey: " + privateKeyPath.toString());

        // create a wallet for the provided identity
        Wallet wallet = Wallet.createInMemoryWallet();

        Reader certificate = new FileReader(certificatePath.toFile());
        Reader privateKey = new FileReader(privateKeyPath.toFile());

        Identity id = Identity.createIdentity(MSP_ID, certificate, privateKey);
        wallet.put(identity, id);

        // prepare a connection profile
        Handlebars handlebars = new Handlebars();

        Template template = handlebars.compile("connection-profile.yaml");

        Map<String, Object> model = new HashMap<String, Object>();
        model.put("ca_name", CA_NAME);
        model.put("ca_url", CA_URL);
        model.put("channel_name", CHANNEL_NAME);
        model.put("orderer_name", ORDERER_NAME);
        model.put("orderer_url", ORDERER_URL);
        model.put("org_name", ORG_NAME);
        model.put("msp_id", MSP_ID);
        model.put("peer_name", PEER_NAME);
        model.put("peer_url", PEER_URL);

        Context context = Context.newContext(model);

        String connectionProfile = template.apply(context);

        // load a CCP
        // create temporary connection profile file since API does not accept streams yet
        File configFile = null;
        BufferedWriter out = null;

        try {
            configFile = File.createTempFile("hlfconn", ".yaml");
            configFile.deleteOnExit();

            out = new BufferedWriter(new FileWriter(configFile));
            out.write(connectionProfile);
            out.close();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (out != null) {
                out.close();
            }
        }

        Path configFilePath = configFile.toPath();

        Gateway.Builder builder = Gateway.createBuilder();
        builder.identity(wallet, identity).networkConfig(configFilePath);

        // create a gateway connection
        try (Gateway gateway = builder.connect()) {
          // get the network and contract
          Network network = gateway.getNetwork(CHANNEL_NAME);
          Contract contract = network.getContract(CONTRACT_NAME);

          // run some transactions!
          byte[] result = contract.submitTransaction("createCar", "CAR10", "VW", "Polo", "Grey", "Mary");
          System.out.println("createCar result: " + new String(result));

          result = contract.evaluateTransaction("queryCar", "CAR10");
          System.out.println("queryCar result: " + new String(result));

          result = contract.evaluateTransaction("queryAllCars");
          System.out.println("queryAllCars result: " + new String(result));

        } catch (Exception ex) {
          ex.printStackTrace();
        }
    }
}
