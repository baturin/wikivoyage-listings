package org.wikivoyage.listings.output;

import org.wikivoyage.listings.entity.WikivoyagePOI;

import java.io.FileOutputStream;
import java.io.ObjectOutputStream;

public class JavaSerializedObject implements OutputFormat {
    @Override
    public void write(Iterable<WikivoyagePOI> pois, String outputFilename, String dumpDate) throws WriteOutputException {
        try {
            for (WikivoyagePOI poi: pois) {
                FileOutputStream fos = new FileOutputStream(outputFilename, true);
                ObjectOutputStream oos = new ObjectOutputStream(fos);
                oos.writeUnshared(poi);
                oos.close();
                fos.close();
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
            throw new WriteOutputException();
        }
    }

    @Override
    public String getDefaultExtension() {
        return "java-serialized.bin";
    }
}
