package org.asuki.webservice.rs.download;

import java.util.Optional;

public interface FileStorage {
    Optional<FilePointer> findFile(String filename);
}
