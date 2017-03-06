/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.sf.javaanpr.test;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Properties;
import static javafx.scene.input.KeyCode.T;
import javax.xml.parsers.ParserConfigurationException;
import net.sf.javaanpr.imageanalysis.CarSnapshot;
import net.sf.javaanpr.intelligence.Intelligence;
import static org.hamcrest.CoreMatchers.equalTo;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;
import org.junit.Rule;
import org.junit.rules.ErrorCollector;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.xml.sax.SAXException;

/**
 *
 * @author kevinturan
 */
@RunWith(value = Parameterized.class)
public class AssignmentDTest {

    private String actualLicensePlate;
    private String expectedLicensePlate;

    public AssignmentDTest(String actualLicensePlate, String expectedLicensePlate) {
        this.actualLicensePlate = actualLicensePlate;
        this.expectedLicensePlate = expectedLicensePlate;
    }

    @Parameters(name = "cars")
    public static Collection<Object[]> data() throws FileNotFoundException, IOException, ParserConfigurationException, SAXException {

        // initializing START 
        String snapshotDirPath = "src/test/resources/snapshots";
        String resultsPath = "src/test/resources/results.properties";
        InputStream resultsStream = new FileInputStream(new File(resultsPath));

        Properties properties = new Properties();
        properties.load(resultsStream);
        resultsStream.close();

        File snapshotDir = new File(snapshotDirPath);
        File[] snapshots = snapshotDir.listFiles();

        Intelligence intel = new Intelligence();
        // initializing END

        // creating collection to return
        Collection<Object[]> dataArr = new ArrayList();
        
        for (File snap : snapshots) {
            
            // setting up carSnap object
            CarSnapshot carSnap = new CarSnapshot(new FileInputStream(snap));

            // using objects
            String snapName = snap.getName();
            String expectedPlate = properties.getProperty(snapName);
            String actualPlate = intel.recognize(carSnap, false);
            
            // adding expected and actual values to array
            dataArr.add(new Object[]{
                actualPlate, expectedPlate
            });

            // closing inputstream
            carSnap.close();
        }

        return dataArr;

    }

    @Test
    public void testWithParameters() {

        // assert parameters using hamcrest
        assertThat(actualLicensePlate, equalTo(expectedLicensePlate));  
        
    }

}
