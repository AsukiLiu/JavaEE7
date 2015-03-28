package org.asuki.common.javase;

import static org.hamcrest.Matchers.*;
import static org.hamcrest.MatcherAssert.*;
import static com.google.common.io.BaseEncoding.base64;
import static com.google.common.io.BaseEncoding.base64Url;
import static java.lang.System.out;
import static java.util.Base64.getDecoder;
import static java.util.Base64.getEncoder;
import static java.util.Base64.getMimeEncoder;
import static java.util.Base64.getUrlEncoder;
import static org.apache.commons.codec.binary.Base64.encodeBase64;
import static org.apache.commons.codec.binary.Base64.encodeBase64Chunked;
import static org.apache.commons.codec.binary.Base64.encodeBase64URLSafe;

import java.nio.charset.StandardCharsets;
import java.util.Arrays;

import org.testng.annotations.Test;

public class Base64Test {
    private static final String TEXT = "test";

    @Test
    public void testBasic() {

        String encoded = getEncoder().encodeToString(
                TEXT.getBytes(StandardCharsets.UTF_8));
        out.println(encoded);

        String decoded = new String(getDecoder().decode(encoded),
                StandardCharsets.UTF_8);
        assertThat(decoded, is(TEXT));
    }

    @Test
    public void testCompare() {
        byte[] bytes = TEXT.getBytes();

        byte[] java8BasicEncoded = getEncoder().encode(bytes);
        byte[] java8UrlEncoded = getUrlEncoder().encode(bytes);
        byte[] java8UrlEncodedNoPad = getUrlEncoder().withoutPadding().encode(bytes);
        byte[] java8MimeEncoded = getMimeEncoder().encode(bytes);

        byte[] commonsBasicEncoded = encodeBase64(bytes);
        byte[] commonsUrlEncoded = encodeBase64URLSafe(bytes);
        byte[] commonsMimeEncoded = encodeBase64Chunked(bytes);

        byte[] guavaBasicEncoded = base64().encode(bytes).getBytes();
        byte[] guavaUrlEncoded = base64Url().encode(bytes).getBytes();

        assertThat(Arrays.equals(java8BasicEncoded, commonsBasicEncoded), is(true));
        assertThat(Arrays.equals(java8UrlEncoded, commonsUrlEncoded), is(false));
        assertThat(Arrays.equals(java8UrlEncodedNoPad, commonsUrlEncoded), is(true));
        assertThat(Arrays.equals(java8MimeEncoded, commonsMimeEncoded),is(false));

        assertThat(Arrays.equals(java8BasicEncoded, guavaBasicEncoded), is(true));
        assertThat(Arrays.equals(java8BasicEncoded, guavaUrlEncoded), is(true));
    }
}
