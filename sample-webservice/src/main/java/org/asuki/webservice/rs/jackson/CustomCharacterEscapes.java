package org.asuki.webservice.rs.jackson;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.ByteBuffer;

import com.fasterxml.jackson.core.SerializableString;
import com.fasterxml.jackson.core.io.CharacterEscapes;

public class CustomCharacterEscapes extends CharacterEscapes {

    private static final long serialVersionUID = 1L;

    private static final char SLASH = '/';
    private static final char ESCAPE = '\\';
    private static final char[] ESCAPE_SLASH = { ESCAPE, SLASH };

    private final int[] asciiEscapes;

    public CustomCharacterEscapes() {
        int[] esc = CharacterEscapes.standardAsciiEscapesForJSON();
        esc[SLASH] = CharacterEscapes.ESCAPE_CUSTOM;
        asciiEscapes = esc;
    }

    @Override
    public int[] getEscapeCodesForAscii() {
        return asciiEscapes;
    }

    @Override
    public SerializableString getEscapeSequence(int ch) {

        return new SerializableString() {

            @Override
            public String getValue() {
                return String.valueOf(ESCAPE_SLASH);
            }

            @Override
            public int charLength() {
                return 2;
            }

            @Override
            public char[] asQuotedChars() {
                return ESCAPE_SLASH;
            }

            @Override
            public byte[] asUnquotedUTF8() {
                return new byte[] { ESCAPE, SLASH };
            }

            @Override
            public byte[] asQuotedUTF8() {
                return new byte[] { ESCAPE, SLASH };
            }

            @Override
            public int appendQuoted(char[] arg0, int arg1) {
                return 0;
            }

            @Override
            public int appendQuotedUTF8(byte[] arg0, int arg1) {
                return 0;
            }

            @Override
            public int appendUnquoted(char[] arg0, int arg1) {
                return 0;
            }

            @Override
            public int appendUnquotedUTF8(byte[] arg0, int arg1) {
                return 0;
            }

            @Override
            public int putQuotedUTF8(ByteBuffer arg0) throws IOException {
                return 0;
            }

            @Override
            public int putUnquotedUTF8(ByteBuffer arg0) throws IOException {
                return 0;
            }

            @Override
            public int writeQuotedUTF8(OutputStream arg0) throws IOException {
                return 0;
            }

            @Override
            public int writeUnquotedUTF8(OutputStream arg0) throws IOException {
                return 0;
            }
        };
    }
}