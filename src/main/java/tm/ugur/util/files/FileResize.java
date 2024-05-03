package tm.ugur.util.files;

import net.coobird.thumbnailator.Thumbnails;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;

@Component
public class FileResize {

    public void resize(File source, File destination, int width, int height) throws IOException {
        Thumbnails.of(source)
                .size(width, height)
                .toFile(destination);
    }

    public void resize(InputStream imageStream, String path, int width, int height) throws IOException {
        Thumbnails.of(imageStream)
                .size(width, height)
                .toFile(path);

    }

}
