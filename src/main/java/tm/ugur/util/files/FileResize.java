package tm.ugur.util.files;

import net.coobird.thumbnailator.Thumbnails;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;

@Component
public class FileResize {

    public void resize(File source, File destination, int width, int height) throws IOException {
        Thumbnails.of(source)
                .size(width, height)
                .toFile(destination);
    }

}
