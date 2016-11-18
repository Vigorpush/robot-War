package view;
import java.io.File;
import java.net.URL;
import java.util.Map;
import java.util.TreeMap;
import java.io.IOException;
import javax.imageio.ImageIO;
import javafx.scene.image.Image;
import java.net.URISyntaxException;
import java.awt.image.BufferedImage;

/**
 * Class for reading resources needed by the game. 
 */
public class ResourceLoader {

    private static final Map<String, Object> cache = new TreeMap<>();
    private static final ClassLoader loader = ResourceLoader.class.getClassLoader();

    // Singleton pattern
    private ResourceLoader() {
    }

    public static Image getImageResource(String fileName) {
        return new Image(loader.getResourceAsStream("images/" + fileName));
    }

    public static File getFileImageResource(URL imgURL, String filename) throws IOException {
        BufferedImage bufferedImage = ImageIO.read(imgURL);
        File f = new File(loader.getResource("images/").toExternalForm() + filename);
        int ind = filename.lastIndexOf(".");
        String ext = "png";
        if (ind != -1) {
            ext = filename.substring(ind, filename.length());
        }
        ImageIO.write(bufferedImage, ext, f);
        return f;
    }

    public static File getFileResource(String fileName) throws URISyntaxException {
        return new File(loader.getResource(fileName).toURI());
    }

    public static String getStringResource(String fileName) {
        return loader.getResource(fileName).toExternalForm();
    }

   
}