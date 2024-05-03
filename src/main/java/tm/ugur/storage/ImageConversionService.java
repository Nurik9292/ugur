package tm.ugur.storage;

import org.springframework.stereotype.Service;

@Service
public class ImageConversionService {

//    public void convertImageToWebP(String sourcePath, String targetPath) throws IOException {
//        try (BufferedImage image = ImageIO.read(new File(sourcePath))) {
//            ImageWriter writer = ImageIO.getImageWriter(ImageType.WEBP);
//            ImageWriteParam writeParam = writer.getDefaultWriteParam();
//
//            // Set compression quality (0-100, higher is better quality)
//            writeParam.setCompressionMode(ImageWriteParam.MODE_COPY_FROM_FILE);
//            writeParam.setCompressionQuality(0.8f);
//
//            writer.setOutput(new FileOutputStream(targetPath));
//            writer.writeImage(image, writeParam);
//        }
//    }
}
