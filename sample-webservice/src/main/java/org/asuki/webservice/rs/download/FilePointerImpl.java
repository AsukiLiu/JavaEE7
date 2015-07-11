package org.asuki.webservice.rs.download;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.time.Instant;
import java.util.Optional;

import com.google.common.base.MoreObjects;
import com.google.common.hash.HashCode;
import com.google.common.hash.Hashing;
import com.google.common.io.Files;
import com.google.common.net.MediaType;

public class FilePointerImpl implements FilePointer {

    private final File file;
    private final HashCode tag;
    private final MediaType mediaTypeOrNull;

    public FilePointerImpl(File file) {
        try {
            this.file = file;
            this.tag = Files.hash(file, Hashing.sha512());

            String contentType = java.nio.file.Files.probeContentType(file
                    .toPath());

            this.mediaTypeOrNull = contentType != null ? MediaType
                    .parse(contentType) : null;
        } catch (IOException e) {
            throw new IllegalArgumentException(e);
        }
    }

    @Override
    public InputStream open() {
        try {
            return new BufferedInputStream(new FileInputStream(file));
        } catch (FileNotFoundException e) {
            throw new IllegalArgumentException(e);
        }
    }

    @Override
    public long getSize() {
        return file.length();
    }

    @Override
    public String getOriginalName() {
        return file.getName();
    }

    @Override
    public String getEtag() {
        return tag.toString();
    }

    @Override
    public Optional<MediaType> getMediaType() {
        return Optional.ofNullable(mediaTypeOrNull);
    }

    @Override
    public boolean matchesEtag(String requestEtag) {
        return getEtag().equals(requestEtag);
    }

    @Override
    public Instant getLastModified() {
        return Instant.ofEpochMilli(file.lastModified());
    }

    @Override
    public boolean modifiedAfter(Instant clientTime) {
        return !clientTime.isBefore(getLastModified());
    }

    @Override
    public String toString() {
        // @formatter:off
        return MoreObjects.toStringHelper(this)
                .add("file", file)
                .add("originalName", getOriginalName())
                .add("size", getSize())
                .add("tag", tag)
                .add("mediaType", mediaTypeOrNull)
                .toString();
        // @formatter:on
    }
}
