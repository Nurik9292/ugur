package tm.ugur.storage;

import jakarta.annotation.PostConstruct;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.util.FileSystemUtils;
import org.springframework.web.multipart.MultipartFile;
import tm.ugur.errors.storage.StorageException;
import tm.ugur.errors.storage.StorageFileNotFoundException;
import tm.ugur.util.files.FileResize;

import java.io.*;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
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
            String extension = originalName.endsWith("webp") ? originalName.substring(originalName.indexOf("webp")) : "";

            Path destinationFile = rootLocation.resolve(Paths.get(originalName))
                    .normalize().toAbsolutePath();


            File tempFile = File.createTempFile("temp", null);
            file.transferTo(tempFile);

            String imageName = generateUniqueFilename(folder, originalName);

            Files.createDirectories(Path.of(destinationFile.getParent() + "/" + folder));
            String newDestinationFile = destinationFile.getParent() + "/" + imageName;

            if("webp".equals(extension))
                Files.copy(tempFile.toPath(), Paths.get(newDestinationFile), StandardCopyOption.REPLACE_EXISTING);
            else
                fileResize.resize(tempFile, new File(newDestinationFile), width, height);

            tempFile.delete();

            return "/api/images/" + imageName;

        } catch (IOException e) {
            logger.error("Failed to store file " + e.getMessage());
            throw new StorageException("Failed to store file.", e);
        }

    }

    public String store(byte[] imageBytes, String folder, int width, int height){

        try(InputStream inputStream = new ByteArrayInputStream(imageBytes);) {
            Files.createDirectories(Path.of( rootLocation + "/" + folder));

            String imageName = generateUniqueFilename(folder, "image.jpg");
            String filePath = rootLocation + "/" + imageName;

            fileResize.resize(inputStream, filePath, width, height);

            return "/api/images/" + imageName;
        } catch (IOException e) {
            logger.error("Failed to store file from api" + e.getMessage());
            throw new StorageException("Failed to store file from api.", e);
        }

    }

    public String store(MultipartFile file, String folder) {
        try {
            if (Objects.isNull(file) || file.isEmpty()) {
                logger.warn("Failed to store empty file.");
                return "";
            }

            String originalName = file.getOriginalFilename();

            Path destinationFile = rootLocation.resolve(Paths.get(originalName))
                    .normalize().toAbsolutePath();


            String imageName = generateUniqueFilename(folder, originalName);

            Files.createDirectories(Path.of(destinationFile.getParent() + "/" + folder));

            String newDestinationFile = destinationFile.getParent() + "/" + imageName;

            Files.copy(file.getInputStream(),  Path.of(newDestinationFile), StandardCopyOption.REPLACE_EXISTING);

            return "/api/images/" + imageName;

        } catch (IOException e) {
            logger.error("Failed to store file " + e.getMessage());
            throw new StorageException("Failed to store file.", e);
        }

    }

    private String generateUniqueFilename(String folder, String originalName) {
        String timestamp = String.valueOf(new Date().getTime());
        return folder + "/" + timestamp + "-" + originalName;
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

        FileSystemUtils.deleteRecursively(new File(rootLocation + "/" + path.substring(12)));
    }
}
