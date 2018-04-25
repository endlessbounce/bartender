package by.khlebnikov.bartender.utility;

import by.khlebnikov.bartender.constant.Constant;

import javax.xml.bind.DatatypeConverter;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * Utility class to convert received from users images from a string form to a file
 */
public class Converter {

    // Actions ------------------------------------------------------------------------------------

    /**
     * Converts an input string representing a BASE64 format image into an actual image.
     * Writes it to the disk with a random name
     *
     * @param base64String input string in BASE64 format
     * @param relativePath relative path to the WEB-INF folder
     * @return absolute path to the saved on the disk image
     * @throws IOException is thrown if an I/O error occurs
     */
    public static String convertBase64ToImage(String base64String, String relativePath) throws IOException {
        String[] partArr = base64String.split(Constant.COMMA);
        String base64Image = partArr[1];

        String extension = partArr[0].replace(Constant.BASE64_START, Constant.DOT)
                .replace(Constant.BASE64, Constant.EMPTY);


        String imagePath = Constant.IMG_FOLDER + CodeGenerator.uniqueId() + extension;

        File outputFile = new File(relativePath + imagePath);

        byte[] imageByte = DatatypeConverter.parseBase64Binary(base64Image);
        try (BufferedOutputStream out = new BufferedOutputStream(new FileOutputStream(outputFile))) {
            out.write(imageByte);
        }

        return imagePath;
    }
}
