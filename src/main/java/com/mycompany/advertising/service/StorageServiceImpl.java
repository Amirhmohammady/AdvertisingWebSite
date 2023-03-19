package com.mycompany.advertising.service;

import com.mycompany.advertising.components.ImageResizer;
import com.mycompany.advertising.components.api.AuthenticationFacade;
import com.mycompany.advertising.config.StorageProperties;
import com.mycompany.advertising.model.to.AdvertiseTo;
import com.mycompany.advertising.service.api.AdvertiseService;
import com.mycompany.advertising.service.api.StorageService;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.PostConstruct;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.lang.invoke.MethodHandles;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

@Service
public class StorageServiceImpl implements StorageService {
    private static final Logger logger = Logger.getLogger(MethodHandles.lookup().lookupClass());
    private final Path rootLocation;
    @Autowired
    AuthenticationFacade authenticationFacade;
    @Autowired
    AdvertiseService advertiseService;

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
    public List<URL> storeImage(MultipartFile file) throws IOException {
        ArrayList<URL> result = new ArrayList<URL>();
        String filename = getNextFileName(StringUtils.cleanPath(file.getOriginalFilename()), rootLocation);
        String domainName = authenticationFacade.getDomainName();
        if (file.isEmpty()) {
            throw new IOException("Failed to store empty file " + filename);
        }
        if (filename.contains("..")) {
            // This is a security check
            throw new IOException("Cannot store file with relative path outside current directory " + filename);
        }
        try (InputStream inputStream = file.getInputStream()) {
            logger.debug("try to store image file" + filename);
            Files.copy(inputStream, this.rootLocation.resolve(filename),
                    StandardCopyOption.REPLACE_EXISTING);
            logger.info("Successfully stored " + filename);
        }
        result.add(new URL(domainName + "/t/" + filename));
        String smallfilename = getFileNameWithNoExtention(filename)
                + "Small." + getExtention(filename);
        smallfilename = getNextFileName(smallfilename, rootLocation);
        ImageResizer.resize(rootLocation.toString() + "\\" + filename,
                rootLocation.toString() + "\\" + smallfilename, 200, 200);
        result.add(new URL(domainName + "/t/" + smallfilename));
        return result;
    }

    @Override
    public List<URL> getAllImagesURL() {
        List<URL> rslt = new ArrayList<>();
        File file = new File(this.rootLocation.toString());
        for (String f : file.list()) {
            try {
                URL tmp = new URL(authenticationFacade.getDomainName() + "/t/" + f);
                rslt.add(tmp);
            } catch (MalformedURLException e) {
                logger.warn("Failed to read stored files " + f);
                System.out.println(e.getMessage());
            }
        }
        /*List<Path> pathes = Files.walk(this.rootLocation, 1)
                    .filter(path -> !path.equals(this.rootLocation))
                    .map(this.rootLocation::relativize).collect(Collectors.toList());*/
        return rslt;
    }

    @Override
    public void deleteUnusedImages() {
        List<String> diskImages = this.getAllImagesURL().stream().map(url -> Paths.get(url.getPath()).getFileName().toString()).collect(Collectors.toList());
        //List<URL> images = this.getAllImagesURL();
        //Set<AdvertiseTo> advs = advertiseService.findAllByDomainNames(this.getAllDomainsName());
        List<String> databaseImages = new ArrayList<>();
        for (AdvertiseTo adv : advertiseService.findAllByDomainNames(this.getAllDomainsName())) {
            if (adv.getImageUrl1() != null)
                databaseImages.add(Paths.get(adv.getImageUrl1().getPath()).getFileName().toString());
            if (adv.getSmallImageUrl1() != null)
                databaseImages.add(Paths.get(adv.getSmallImageUrl1().getPath()).getFileName().toString());
        }

        for (String diskImage : diskImages)
            if (!databaseImages.contains(diskImage)) {
                File file = new File(rootLocation.toFile(), diskImage);
                if (file.delete()) {
                    logger.debug(diskImage + " was deleted successfully");
                } else {
                    logger.warn("can not delete file " + diskImage);
                }
            }
    }

    @Override
    public void deleteImageByUrl(URL imgURL) {
        File file = new File(rootLocation.toFile(), Paths.get(imgURL.getPath()).getFileName().toString());
        if (file.delete()) {
            logger.debug(imgURL + " was deleted successfully");
        } else {
            logger.warn("can not delete file " + imgURL);
        }
    }

    @Override
    public List<String> getAllDomainsName() {
        List<String> result = new ArrayList<>();
        result.add("127.0.0.1");
        result.add("localhost");
        return result;
    }

    /*@Override
    public Stream<Path> loadAll() throws IOException {
        try {
            return Files.walk(this.rootLocation, 1)
                    .filter(path -> !path.equals(this.rootLocation))
                    .map(this.rootLocation::relativize);
        } catch (IOException e) {
            logger.error("Failed to read stored files", e);
            throw new IOException("Failed to read stored files");
        }
    }*/

    /*@Override
    public Path load(String filename) {
        return rootLocation.resolve(filename);
    }

    @Override
    public void deleteAll() {
        FileSystemUtils.deleteRecursively(rootLocation.toFile());
    }*/

    @PostConstruct
    public void init() {
        try {
            Files.createDirectories(rootLocation);
        } catch (IOException e) {
            e.printStackTrace();
            logger.fatal("Could not initialize storage", e);
        }
    }
}
