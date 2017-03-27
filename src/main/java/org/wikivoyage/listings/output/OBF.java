package org.wikivoyage.listings.output;

import net.osmand.IProgress;
import net.osmand.data.preparation.IndexCreator;
import org.wikivoyage.listings.entity.Listing;
import org.xml.sax.SAXException;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.sql.SQLException;

public class OBF implements OutputFormat {
    private boolean userDefined;
    private String workingDir;
    private String tempXmlFilename;

    public OBF(boolean userDefined, String workingDir, String tempXmlFilename) {
        this.userDefined = userDefined;
        this.workingDir = workingDir;
        this.tempXmlFilename = tempXmlFilename;
    }

    @Override
    public void write(Iterable<Listing> pois, String outputFilename, String dumpDate) throws WriteOutputException {
        try {
            OsmXml osmXml = new OsmXml(userDefined);
            osmXml.write(pois, tempXmlFilename, dumpDate);
            createObf(tempXmlFilename, workingDir, "pois.obf");
            Files.move(Paths.get(workingDir + "/pois.obf"), Paths.get(outputFilename));
        } catch (IOException | SAXException | SQLException | InterruptedException e) {
            throw new WriteOutputException();
        }
    }

    public static void createObf(String outputFilename, String workingDir, String mapFile) throws IOException, SAXException, SQLException, InterruptedException {
        IndexCreator creator = new IndexCreator(new File(workingDir));
        creator.setMapFileName(mapFile);
        creator.setIndexPOI(true);
        creator.generateIndexes(new File(outputFilename), IProgress.EMPTY_PROGRESS, null, null, null, null);
    }

    public String getDefaultExtension()
    {
        if (userDefined) {
            return ".user-defined.obf";
        } else {
            return ".obf";
        }
    }
}
