package com.mycompany.advertising.service;

import com.mycompany.advertising.components.api.AuthenticationFacade;
import com.mycompany.advertising.service.api.StorageService;
import com.mycompany.advertising.components.ImageResizer;
import com.mycompany.advertising.config.StorageProperties;
import com.mycompany.advertising.web.imagestorage.StorageException;
import com.mycompany.advertising.web.imagestorage.StorageFileNotFoundException;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.util.FileSystemUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Stream;

@Service
public class StorageServiceImpl implements StorageService {
    private final static Logger logger = Logger.getLogger(ImageResizer.class);
    private final Path rootLocation;
    @Autowired
    AuthenticationFacade authenticationFacade;

    @Autowired
    public StorageServiceImpl(StorageProperties properties) {
        this.rootLocation = Paths.get(properties.getRecourcefolder());
    }

    public static String getNextFileName(String filename, Path folder) {
        //String fullName = folder.toString() + "\\" + filename;
        if (!new File(folder.toString() + "\\" + filename).exists()) return filename;
        //String fileWithNoEx = filename;
        //String fileEx = null;
        //Matcher matcher = Pattern.compile("(.)[^.\\\\/]+$").matcher(filename);
        //if (matcher.find()) {
        //fileWithNoEx = filename.substring(0, matcher.start(1));
        //fileEx = filename.substring(matcher.start(1) + 1);
        String fileWithNoEx = getFileNameWithNoExtention(filename);
        String fileEx = getExtention(filename);
        int index = 0;
        Matcher matcher = Pattern.compile("\\((\\d+)\\)$").matcher(fileWithNoEx);
        if (matcher.find()) {
            fileWithNoEx = fileWithNoEx.substring(0, matcher.start(1) - 1);
            index = Integer.parseInt(matcher.group(1));
        }
        //}
        while (new File(folder.toString() + '\\' + fileWithNoEx + '(' + index + ")." + fileEx).exists()) {
            index++;
        }
        return fileWithNoEx + '(' + index + ")." + fileEx;
    }

    public static String getExtention(String filename) {
        int index = filename.lastIndexOf('.');
        if (index > 0) return filename.substring(index + 1, filename.length());
        else return null;
    }

    public static String getFileNameWithNoExtention(String filename) {
        int index = filename.lastIndexOf('.');
        if (index > 0) return filename.substring(0, index);
        else return filename;
    }

    @Override
    public List<String> storeImage(MultipartFile file) {
        ArrayList<String> result = new ArrayList<String>();
        String filename = getNextFileName(StringUtils.cleanPath(file.getOriginalFilename()), rootLocation);
        String domainName = authenticationFacade.getDomainName();
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
                logger.debug("try to store image file" + filename);
                Files.copy(inputStream, this.rootLocation.resolve(filename),
                        StandardCopyOption.REPLACE_EXISTING);
                logger.info("Successfully stored " + filename);
            }
            result.add(domainName + "/t/" + filename);
        } catch (IOException e) {
            throw new StorageException("Failed to store file " + filename, e);
        }
        String smallfilename = getFileNameWithNoExtention(filename)
                + "Small." + getExtention(filename);
        smallfilename = getNextFileName(smallfilename, rootLocation);
        try {
            ImageResizer.resize(rootLocation.toString() + "\\" + filename,
                    rootLocation.toString() + "\\" + smallfilename, 200, 200);
            result.add(domainName + "/t/" + smallfilename);
        } catch (IOException e) {
            e.printStackTrace();
        }
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
