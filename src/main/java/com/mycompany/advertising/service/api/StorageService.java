package com.mycompany.advertising.service.api;

import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.Set;

/**
 * Created by Amir on 11/5/2019.
 */
public interface StorageService {
    List<URL> storeImage(MultipartFile file) throws IOException;

    List<URL> getAllImagesURL();

    void deleteUnusedImages();

    void deleteImageByUrl(URL imgURL);

    List<String> getAllDomainsName();
}
