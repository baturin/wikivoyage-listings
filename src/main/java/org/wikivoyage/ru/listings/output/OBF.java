package org.wikivoyage.ru.listings.output;

import net.osmand.IProgress;
import net.osmand.data.preparation.IndexCreator;
import org.xml.sax.SAXException;

import java.io.File;
import java.io.IOException;
import java.sql.SQLException;

public class OBF {
    public static void createObf(String outputFilename, String workingDir, String mapFile) throws IOException, SAXException, SQLException, InterruptedException {
        IndexCreator creator = new IndexCreator(new File(workingDir));
        creator.setMapFileName(mapFile);
        creator.setIndexPOI(true);
        creator.generateIndexes(new File(outputFilename), IProgress.EMPTY_PROGRESS, null, null, null, null);
    }
}
