package tm.ugur.storage;

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

import javax.xml.crypto.Data;
import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Date;
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
    public String store(MultipartFile file) {
        try {
            if (file.isEmpty()) {
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

            fileResize.resize(tempFile, new File(destinationFile.getParent() + "/" + date.getTime() + "-" + originalName), 64, 64);

            tempFile.delete();

            return destinationFile.toString();

        } catch (IOException e) {
            logger.error("Failed to store file " + e.getMessage());
            throw new StorageException("Failed to store file.", e);
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
        FileSystemUtils.deleteRecursively(new File(path));
    }
}
