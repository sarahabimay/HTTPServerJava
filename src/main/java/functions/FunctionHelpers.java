package functions;

import java.nio.ByteBuffer;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.function.Function;

import static java.nio.charset.StandardCharsets.UTF_8;

public class FunctionHelpers {
    public static Function<String, String> calculateEtag = s -> {
        ByteBuffer buf = UTF_8.encode(s);
        MessageDigest digest;
        try {
            digest = java.security.MessageDigest.getInstance("SHA1");
            digest.update(buf);
            buf.mark();
            buf.reset();
            return String.format("%s", javax.xml.bind.DatatypeConverter.printHexBinary(digest.digest())).toLowerCase();
        } catch ( NoSuchAlgorithmException e ) {
            e.printStackTrace();
        }
        return "";
    };
}
