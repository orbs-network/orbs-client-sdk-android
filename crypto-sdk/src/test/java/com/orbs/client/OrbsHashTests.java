package com.orbs.client;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.junit.Assert;

import org.junit.Test;

import static org.junit.Assert.*;

public class OrbsHashTests {



  @Test
  public void serialize_request_for_hash() throws Exception {
    final String EXPECTED_REQUEST_JSON = "{\"header\":{\"contractAddressBase58\":\"abc\",\"senderAddressBase58\":\"zxc\",\"timestamp\":\"123\",\"version\":0},\"payload\":\"{some: json}\"}";
    SendTransactionRequest req = new SendTransactionRequest();
    req.header = new SendTransactionHeader.Builder()
      .withContractAddress("abc")
      .withSenderAddress("zxc")
      .withTimestamp("123")
      .build();
    req.payload = "{some: json}";

    Gson gson = OrbsClient.getGsonStableSerializer();

    String res = gson.toJson(req);
    assertEquals(res, EXPECTED_REQUEST_JSON);
  }

  @Test
  public void test_hash256_bytes() {
    final String DATA_TO_HASH = "kuku";
    final byte[] KUKU_HASH_BYTES = {-127, -91, 4, 50, -109, 74, -12, 100, 34, 39, -95, 86, 27, -16, -36, 58, -70, 15, 75, -126, -7, 4, -63, 30, 18, -24, 47, 97, -32, 52, -14, -38};
    try {
      byte[] kukuHashBytes = OrbsHashUtils.hash256(DATA_TO_HASH);
      Assert.assertArrayEquals(KUKU_HASH_BYTES, kukuHashBytes);
    }
    catch (Exception ex) {
      Assert.fail();
    }
  }

  @Test
  public void test_hash256_hex_string() {
    final String DATA_TO_HASH = "kuku";
    final String KUKU_HASH = "81A50432934AF4642227A1561BF0DC3ABA0F4B82F904C11E12E82F61E034F2DA";
    try {
      byte[] kukuHashBytes = OrbsHashUtils.hash256(DATA_TO_HASH);
      String kukuHashString = OrbsHashUtils.bytesToHex(kukuHashBytes);
      Assert.assertEquals(KUKU_HASH.toLowerCase(), kukuHashString);
    }
    catch (Exception ex) {
      Assert.fail();
    }
  }
}