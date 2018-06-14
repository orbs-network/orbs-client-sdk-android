#!/bin/bash -xe

export CRYPTO_SDK_VERSION=${CRYPTO_SDK_VERSION-096919b0cf0f5a8b0cc57077d4369556332e4758}

export S3_PATH=s3://orbs-client-sdk/lib/$CRYPTO_SDK_VERSION

rm -rf native
aws s3 sync $S3_PATH native

mkdir -p native/headers
tar zxvf native/headers.tgz -C native/headers
