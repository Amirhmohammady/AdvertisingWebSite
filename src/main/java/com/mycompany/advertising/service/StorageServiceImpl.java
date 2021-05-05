package com.mycompany.advertising.service;

import com.mycompany.advertising.api.StorageService;
import com.mycompany.advertising.components.FileManager;
import com.mycompany.advertising.components.ImageResizer;
import com.mycompany.advertising.config.StorageProperties;
import com.mycompany.advertising.web.imagestorage.StorageException;
import com.mycompany.advertising.web.imagestorage.StorageFileNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.util.FileSystemUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

@Service
public class StorageServiceImpl implements StorageService {

    private final Path rootLocation;

    @Autowired
    public StorageServiceImpl(StorageProperties properties) {
        this.rootLocation = Paths.get(properties.getFixlocation());
    }

    @Override
    public List<String> storeImage(MultipartFile file) {
        ArrayList<String> result = new ArrayList<String>();
        String filename = FileManager.getNextFileName(StringUtils.cleanPath(file.getOriginalFilename()), rootLocation);
        try {
            if (file.isEmpty()) {
                throw new StorageException("Failed to store empty file " + filename);
            }
            if (filename.contains("..")) {
                // This is a security check
                throw new StorageException(
                        "Cannot store file with relative path outside current directory "
                                + filename);
            }
            try (InputStream inputStream = file.getInputStream()) {
                Files.copy(inputStream, this.rootLocation.resolve(filename),
                        StandardCopyOption.REPLACE_EXISTING);
            }
        } catch (IOException e) {
            throw new StorageException("Failed to store file " + filename, e);
        }
        String smallfilename = FileManager.getFileNameWithNoExtention(filename)
                + "Small." + FileManager.getExtention(filename);
        smallfilename = FileManager.getNextFileName(smallfilename, rootLocation);
        try {
            ImageResizer.resize(rootLocation.toString() + "\\" + filename,
                    rootLocation.toString() + "\\" + smallfilename, 200, 200);
        } catch (IOException e) {
            e.printStackTrace();
        }
        result.add(filename);
        result.add(smallfilename);
        return result;
    }

    @Override
    public Stream<Path> loadAll() {
        try {
            return Files.walk(this.rootLocation, 1)
                    .filter(path -> !path.equals(this.rootLocation))
                    .map(this.rootLocation::relativize);
        } catch (IOException e) {
            throw new StorageException("Failed to read stored files", e);
        }
    }

    @Override
    public Path load(String filename) {
        return rootLocation.resolve(filename);
    }

    @Override
    public Resource loadAsResource(String filename) {
        try {
            Path file = load(filename);
            Resource resource = new UrlResource(file.toUri());
            if (resource.exists() || resource.isReadable()) {
                return resource;
            } else {
                throw new StorageFileNotFoundException(
                        "Could not read file: " + filename);

            }
        } catch (MalformedURLException e) {
            throw new StorageFileNotFoundException("Could not read file: " + filename, e);
        }
    }

    @Override
    public void deleteAll() {
        FileSystemUtils.deleteRecursively(rootLocation.toFile());
    }

    @Override
    public void init() {
        try {
            Files.createDirectories(rootLocation);
        } catch (IOException e) {
            e.printStackTrace();
            throw new StorageException("Could not initialize storage", e);
        }
    }
}
