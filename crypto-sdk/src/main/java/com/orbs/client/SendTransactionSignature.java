package com.orbs.client;

import com.google.gson.annotations.SerializedName;

public class SendTransactionSignature {
  @SerializedName("publicKeyHex")
  public String publicKeyHex;
  @SerializedName("signatureHex")
  public String signatureHex;

  public SendTransactionSignature(Builder builder) {
    this.publicKeyHex = builder.publicKeyHex;
    this.signatureHex = builder.signatureHex;
  }

  public static final class Builder {
    private String publicKeyHex;
    private String signatureHex;

    public Builder withPublicKeyHex(String hex) {
      this.publicKeyHex = hex;
      return this;
    }

    public Builder withSignatureData(String dataHexString) {
      this.signatureHex = dataHexString;
      return this;
    }

    public SendTransactionSignature build() throws Exception {
      if (this.publicKeyHex == null || this.signatureHex == null) {
        throw new Exception("Must set public key and signature");
      }
      return new SendTransactionSignature(this);
    }
  }
}
