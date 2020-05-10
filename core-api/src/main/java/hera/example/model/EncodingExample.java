/*
 * @copyright defined in LICENSE.txt
 */

package hera.example.model;

import hera.api.encode.Decoder;
import hera.api.encode.Encoder;
import hera.api.model.BytesValue;
import hera.api.model.Signature;
import hera.example.AbstractExample;

public class EncodingExample extends AbstractExample {

  public static void main(String[] args) {

    /* Encode */
    // Heraj provides utils for encoding.
    {
      // To hex.
      {
        BytesValue bytesValue = BytesValue.of("test".getBytes());
        String encoded = bytesValue.getEncoded(Encoder.Hex);
        System.out.println(encoded);
      }

      // To base58.
      {
        BytesValue bytesValue = BytesValue.of("test".getBytes());
        String encoded = bytesValue.getEncoded(Encoder.Base58);
        System.out.println(encoded);
      }

      // To base58 with checksum.
      {
        BytesValue bytesValue = BytesValue.of("test".getBytes());
        String encoded = bytesValue.getEncoded(Encoder.Base58Check);
        System.out.println(encoded);
      }

      // To base64.
      {
        BytesValue bytesValue = BytesValue.of("test".getBytes());
        String encoded = bytesValue.getEncoded(Encoder.Base64);
        System.out.println(encoded);
      }
    }

    /* Decode */
    // Heraj provides utils for decoding.
    {
      // From hex.
      {
        String encoded = "74657374";
        BytesValue bytesValue = BytesValue.of(encoded, Decoder.Hex);
        System.out.println(bytesValue);
      }

      // From base58.
      {
        String encoded = "3yZe7d";
        BytesValue bytesValue = BytesValue.of(encoded, Decoder.Base58);
        System.out.println(bytesValue);
      }

      // From base58 with checksum.
      {
        String encoded = "LUC1eAJa5jW";
        BytesValue bytesValue = BytesValue.of(encoded, Decoder.Base58Check);
        System.out.println(bytesValue);
      }

      // From base64.
      {
        String encoded = "dGVzdA==";
        BytesValue bytesValue = BytesValue.of(encoded, Decoder.Base64);
        System.out.println(bytesValue);
      }
    }

    /* Example */
    {
      // Signature in base64.
      {
        String encoded = "MEUCIQDP3ywVXX1DP42nTgM6cF95GFfpoEcl4D9ZP+MHO7SgoQIgdq2UiEiSp23lcPFzCHtDmh7pVzsow5x1s8p5Kz0aN7I=";
        BytesValue rawSignature = BytesValue.of(encoded, Decoder.Base64);
        Signature signature = Signature.of(rawSignature);
        System.out.println(signature);
      }
    }

  }

}
