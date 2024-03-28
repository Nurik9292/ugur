package tm.ugur.storage;

import jakarta.annotation.PostConstruct;
import net.coobird.thumbnailator.Thumbnails;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.util.FileSystemUtils;
import org.springframework.web.multipart.MultipartFile;
import tm.ugur.util.errors.storage.StorageException;
import tm.ugur.util.errors.storage.StorageFileNotFoundException;
import tm.ugur.util.files.FileResize;

import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Date;
import java.util.Objects;
import java.util.stream.Stream;

@Service
public class FileSystemStorageService implements StorageService{

    private Path rootLocation;
    private final StorageProperties storageProperties;
    private final FileResize fileResize;

    private final static Logger logger = LoggerFactory.getLogger(FileSystemStorageService.class);


    public FileSystemStorageService(StorageProperties storageProperties, FileResize fileResize) {

        this.storageProperties = storageProperties;
        this.fileResize = fileResize;
        if(storageProperties.getLocation().trim().isBlank()){
            throw new StorageException("File upload location can not be Empty.");
        }

        this.rootLocation = Paths.get(storageProperties.getLocation());
    }

    @PostConstruct
    @Override
    public void init() {
        try{
            Files.createDirectories(rootLocation);
        }catch (IOException e){
            logger.error("Could not initialize storage: " + e.getMessage());
            throw new StorageException("Could not initialize storage", e);
        }
    }

    @Override
    public String store(MultipartFile file, String folder, int width, int height) {
        try {
            if (Objects.isNull(file) || file.isEmpty()) {
                logger.warn("Failed to store empty file.");
                return "";
            }

            String originalName = file.getOriginalFilename();

            Path destinationFile = rootLocation.resolve(Paths.get(originalName))
                    .normalize().toAbsolutePath();


            if (!destinationFile.getParent().equals(rootLocation.toAbsolutePath())) {
                throw new StorageException("Cannot store file outside current directory.");
            }

            File tempFile = File.createTempFile("temp", null);
            file.transferTo(tempFile);

            Date date = new Date();
            String imageName = folder + "/" + date.getTime() + "-" + originalName;

            Files.createDirectories(Path.of(destinationFile.getParent() + "/" + folder));

            String newDestinationFile = destinationFile.getParent() + "/" + imageName;
            fileResize.resize(tempFile, new File(newDestinationFile), width, height);


            tempFile.delete();

            return "/api/images/" + imageName;

        } catch (IOException e) {
            logger.error("Failed to store file " + e.getMessage());
            throw new StorageException("Failed to store file.", e);
        }

    }

    public String store(byte[] imageBytes, String folder, int width, int height){
        InputStream inputStream = new ByteArrayInputStream(imageBytes);
        try {
            Date date = new Date();

            String imageName = folder + "/" + date.getTime() + ".jpg";

            String filePath = rootLocation + "/" + imageName;

            fileResize.resize(inputStream, filePath, width, height);

            return "/api/images/" + imageName;
        } catch (IOException e) {
            logger.error("Failed to store file from api" + e.getMessage());
            throw new StorageException("Failed to store file from api.", e);
        }

    }

    @Override
    public Stream<Path> loadAll() {
        return null;
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
            }
            else {
                throw new StorageFileNotFoundException(
                        "Could not read file: " + filename);

            }
        }
        catch (MalformedURLException e) {
            throw new StorageFileNotFoundException("Could not read file: " + filename, e);
        }
    }

    @Override
    public void deleteAll() {
        FileSystemUtils.deleteRecursively(rootLocation.toFile());
    }

    @Override
    public void delete(String path){
        System.out.println(rootLocation + "/" + path.substring(12));
        FileSystemUtils.deleteRecursively(new File(rootLocation + "/" + path.substring(12)));
    }
}
