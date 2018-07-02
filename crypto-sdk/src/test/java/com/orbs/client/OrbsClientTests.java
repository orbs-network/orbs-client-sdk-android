package com.orbs.client;


import com.google.gson.Gson;
import com.orbs.cryptosdk.Address;
import com.orbs.cryptosdk.ED25519Key;

import org.junit.Test;

import java.net.URL;
import java.util.Date;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;


public class OrbsClientTests {

  final String PRIVATE_KEY = "3f81e53116ee3f860c154d03b9cabf8af71d8beec210c535ed300c0aee5fcbe7";
  final String PUBLIC_KEY = "b9a91acbf23c22123a8253cfc4325d7b4b7a620465c57f932c7943f60887308b";


  @Test
  public void test_url_building() {
    Address address = new Address(PUBLIC_KEY,"640ed3", "T");
    OrbsHost endpoint = new OrbsHost(false, "some.host.network", 80);
    OrbsClient client = new OrbsClient(endpoint, address , new ED25519Key(PUBLIC_KEY, PRIVATE_KEY));
    URL url = client.buildUrlForRequest("some/path");
    assertEquals(url.toString(), "http://some.host.network/some/path");
  }

  @Test
  public void test_url_building_custom_port() {
    Address address = new Address(PUBLIC_KEY,"640ed3", "T");
    OrbsHost endpoint = new OrbsHost(false, "some.host.network", 3242);
    OrbsClient client = new OrbsClient(endpoint, address , new ED25519Key(PUBLIC_KEY, PRIVATE_KEY));
    URL url = client.buildUrlForRequest("some/path");
    assertEquals(url.toString(), "http://some.host.network:3242/some/path");
  }

  @Test
  public void test_url_building_ssl() {
    Address address = new Address(PUBLIC_KEY,"640ed3", "T");
    OrbsHost endpoint = new OrbsHost(true, "some.host.network", 443);
    OrbsClient client = new OrbsClient(endpoint, address , new ED25519Key(PUBLIC_KEY, PRIVATE_KEY));
    URL url = client.buildUrlForRequest("some/path");
    assertEquals(url.toString(), "https://some.host.network/some/path");
  }

  @Test
  public void test_url_building_ssl_custom_port() {
    Address address = new Address(PUBLIC_KEY,"640ed3", "T");
    OrbsHost endpoint = new OrbsHost(true, "some.host.network", 1443);
    OrbsClient client = new OrbsClient(endpoint, address , new ED25519Key(PUBLIC_KEY, PRIVATE_KEY));
    URL url = client.buildUrlForRequest("some/path");
    assertEquals(url.toString(), "https://some.host.network:1443/some/path");
  }

  @Test
  public void test_genereateTransactionRequest() throws Exception {
    Address senderAddress = new Address(PUBLIC_KEY,"640ed3", "T");
    OrbsHost endpoint = new OrbsHost(true, "some.host.network", 1443);
    OrbsClient client = new OrbsClient(endpoint, senderAddress , new ED25519Key(PUBLIC_KEY, PRIVATE_KEY));

    // do not want to test the contract here
    String payload = "{\"method\": \"some_method\",\"args\": []";
    Address contractAddress = new Address(PUBLIC_KEY, "101010", "T");
    String res = client.generateTransactionRequest(contractAddress, payload);
    Gson gson = new Gson();
    SendTransactionRequest tranDeserialized = gson.fromJson(res, SendTransactionRequest.class);
    assertEquals(tranDeserialized.payload, payload);
    assertEquals(tranDeserialized.header.version, 0);
    assertEquals(tranDeserialized.header.senderAddressBase58, senderAddress.toString());
    assertEquals(tranDeserialized.header.contractAddressBase58, contractAddress.toString());
    long tsAsNumber = Long.parseLong(tranDeserialized.header.timestamp);
    long now = new Date().getTime();
    long tenSeconds = 10000;
    assertTrue("timestamp is not in the future", tsAsNumber < now);
    assertTrue("timestamp is recent", tsAsNumber + tenSeconds > now);

    // make sure we have a valid signature
    assertEquals(tranDeserialized.signatureData.publicKeyHex, "b9a91acbf23c22123a8253cfc4325d7b4b7a620465c57f932c7943f60887308b");
    assertTrue("check that a signature was generated (may be invalid, but generated, sig checks are elsewhere)", tranDeserialized.signatureData.signatureHex.length() > 0);
  }

  @Test
  public void test_generateCallRequest() {
    Address senderAddress = new Address(PUBLIC_KEY,"640ed3", "T");
    OrbsHost endpoint = new OrbsHost(true, "some.host.network", 1443);
    OrbsClient client = new OrbsClient(endpoint, senderAddress , new ED25519Key(PUBLIC_KEY, PRIVATE_KEY));

    String payload = "{\"method\": \"some_method\",\"args\": []";
    Address contractAddress = new Address(PUBLIC_KEY, "101010", "T");
    String res = client.generateCallRequest(contractAddress, payload);
    Gson gson = new Gson();
    CallContractRequest callDeserialized = gson.fromJson(res, CallContractRequest.class);
    assertEquals(callDeserialized.payload, payload);
    assertEquals(callDeserialized.contractAddressBase58, contractAddress.toString());
    assertEquals(callDeserialized.senderAddressBase58, senderAddress.toString());
  }

  @Test
  public void test_transaction_signature() throws Exception {
    Address senderAddress = new Address(PUBLIC_KEY,"640ed3", "T");
    OrbsHost endpoint = new OrbsHost(true, "some.host.network", 1443);
    OrbsClient client = new OrbsClient(endpoint, senderAddress , new ED25519Key(PUBLIC_KEY, PRIVATE_KEY));

    SendTransactionRequest req = new SendTransactionRequest();
    req.header = new SendTransactionHeader.Builder()
            .withContractAddress("abc")
            .withSenderAddress("zxc")
            .withTimestamp("123")
            .build();
    req.payload = "{some: json}";

    String sig = client.generateSignatureForTransaction(req);
    String EXPECTED_SIG = "6bf8a236b3d899a864a9ab6328313cc4ce9f75d22e421d13d11c9e7c66aabf93b8de7bc614f16aa30562ad4593285d8e6f4115b10bb362835324ebb6fd42990b";
    assertEquals(EXPECTED_SIG, sig);
  }

  @Test
  public void test_sendTransation_slug() {
    assertEquals("public/sendTransaction", OrbsClient.SEND_TRANSACTION_SLUG);
  }

  @Test
  public void test_callContract_slug() {
    assertEquals("public/callContract", OrbsClient.CALL_CONTRACT_SLUG);
  }
}
