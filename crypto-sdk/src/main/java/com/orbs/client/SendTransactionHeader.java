package com.orbs.client;

import com.google.gson.annotations.SerializedName;

public class SendTransactionHeader {
  @SerializedName("version")
  public int version;
  @SerializedName("senderAddressBase58")
  public String senderAddressBase58;
  @SerializedName("timestamp")
  public String timestamp;
  @SerializedName("contractAddressBase58")
  public String contractAddressBase58;

  public SendTransactionHeader() {

  }

  public SendTransactionHeader(Builder builder) {
    this.version = builder.version;
    this.senderAddressBase58 = builder.senderAddressBase58;
    this.contractAddressBase58 = builder.contractAddressBase58;
    this.timestamp = builder.timestamp;
  }

  public static final class Builder {
    public int version;
    public String senderAddressBase58;
    public String timestamp;
    public String contractAddressBase58;

    public Builder withVersion(int version) {
      this.version = version;
      return this;
    }

    public Builder withSenderAddress(String senderAddress) {
      this.senderAddressBase58 = senderAddress;
      return this;
    }

    public Builder withTimestamp(String timestamp) {
      this.timestamp = timestamp;
      return this;
    }

    public Builder withContractAddress(String contractAddress) {
      this.contractAddressBase58 = contractAddress;
      return this;
    }

    public SendTransactionHeader build() throws Exception {
      if (this.version != 0) {
        throw new Exception("version must be 0");
      }
      if (this.senderAddressBase58 == null || this.contractAddressBase58 == null || this.timestamp == null) {
        throw new Exception("Header must have sender, contract and a timestamp");
      }
      return new SendTransactionHeader(this);
    }
  }
}
