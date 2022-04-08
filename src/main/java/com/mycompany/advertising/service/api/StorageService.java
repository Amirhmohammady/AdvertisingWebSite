package com.mycompany.advertising.service.api;

import org.springframework.core.io.Resource;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Path;
import java.util.List;
import java.util.stream.Stream;

/**
 * Created by Amir on 11/5/2019.
 */
public interface StorageService {
    void init();

    List<String> storeImage(MultipartFile file) throws IOException;

    //will delete
    Stream<Path> loadAll() throws IOException;

    //will delete
    Path load(String filename);

    //will add
    //void deleteUnusedImages();
    void deleteAll();
}
