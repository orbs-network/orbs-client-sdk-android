package com.orbs.client;


import com.orbs.cryptosdk.Address;
import com.orbs.cryptosdk.CryptoSDK;
import com.orbs.cryptosdk.ED25519Key;

import org.junit.Test;

import static org.junit.Assert.*;


public class ContractUnitTests {
  static {
    CryptoSDK.initialize();
  }

  final String PRIVATE_KEY = "3f81e53116ee3f860c154d03b9cabf8af71d8beec210c535ed300c0aee5fcbe7";
  final String PUBLIC_KEY = "b9a91acbf23c22123a8253cfc4325d7b4b7a620465c57f932c7943f60887308b";

  @Test
  public void test_response_parsing() {
    Address address = new Address(PUBLIC_KEY,"640ed3", "T");
    OrbsHost endpoint = new OrbsHost(false, "dont_care", 80);
    OrbsClient client = new OrbsClient(endpoint, address , new ED25519Key(PUBLIC_KEY, PRIVATE_KEY));
    final String SERVER_RESOPNSE = "{\"transactionId\": \"some_id\"}";
    SendTransactionResponse res = client.parseSendTransactionResponse(SERVER_RESOPNSE);
    assertEquals(res.transactionId, "some_id");
  }

  @Test
  public void test_generateSendTransactionPayload() throws Exception {
    Address address = new Address(PUBLIC_KEY,"640ed3", "T");
    OrbsHost endpoint = new OrbsHost(false, "dont_care", 80);
    OrbsClient client = new OrbsClient(endpoint, address , new ED25519Key(PUBLIC_KEY, PRIVATE_KEY));
    OrbsContract contract = new OrbsContract(client, "dont_care");
    final String METHOD_NAME = "some_method";
    final int ARG_FOR_TEST = 1;
    String res = contract.generateSendTransactionPayload(METHOD_NAME, new Object[]{ARG_FOR_TEST});
    assertEquals("{\"method\":\"some_method\",\"args\":[1]}", res);
  }

  @Test
  public void test_generateCallPayload() throws Exception {
    Address address = new Address(PUBLIC_KEY,"640ed3", "T");
    OrbsHost endpoint = new OrbsHost(false, "dont_care", 80);
    OrbsClient client = new OrbsClient(endpoint, address , new ED25519Key(PUBLIC_KEY, PRIVATE_KEY));
    OrbsContract contract = new OrbsContract(client, "dont_care");
    final String METHOD_NAME = "some_method";
    final int ARG_FOR_TEST = 1;
    String res = contract.generateSendTransactionPayload(METHOD_NAME, new Object[]{ARG_FOR_TEST});
    assertEquals("{\"method\":\"some_method\",\"args\":[1]}", res);
  }
}
