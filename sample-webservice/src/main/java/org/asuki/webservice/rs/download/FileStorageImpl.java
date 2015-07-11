package org.asuki.webservice.rs.download;

import java.io.File;
import java.net.URL;
import java.util.Optional;

import javax.inject.Inject;

import org.slf4j.Logger;

public class FileStorageImpl implements FileStorage {

    @Inject
    private Logger log;

    @Override
    public Optional<FilePointer> findFile(String filename) {
        log.info("Downloading {}", filename);

        final URL url = getClass().getResource("/" + filename);

        if (url == null) {
            return Optional.empty();
        }

        return Optional.of(new FilePointerImpl(new File(url.getFile())));
    }

}
