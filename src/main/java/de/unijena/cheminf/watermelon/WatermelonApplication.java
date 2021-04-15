package de.unijena.cheminf.watermelon;

import com.fasterxml.jackson.databind.ObjectMapper;
import de.unijena.cheminf.watermelon.comparator.CompareToPublic;
import de.unijena.cheminf.watermelon.comparator.CompareToSelf;
import de.unijena.cheminf.watermelon.misc.*;
import de.unijena.cheminf.watermelon.molecules.WMolecule;
import de.unijena.cheminf.watermelon.molecules.WMoleculeSim;
import de.unijena.cheminf.watermelon.reader.ReadTSV;
import de.unijena.cheminf.watermelon.reader.WriteWMolecule;
import org.openscience.cdk.interfaces.IAtomContainer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.io.File;
import java.util.ArrayList;


@SpringBootApplication
public class WatermelonApplication  implements CommandLineRunner {


    @Autowired
    CompareToSelf compareToSelf;


    @Autowired
    CompareToPublic compareToPublic;


    @Autowired
    ReadTSV readTSV;

    @Autowired
    MoleculesUnifier moleculesUnifier;


    @Autowired
    PropertiesComputer propertiesComputer;

    @Autowired
    WriteWMolecule writeWMolecule;

    @Autowired
    FingerprintsCountsFiller fingerprintsCountsFiller;

    @Autowired
    org.springframework.data.mongodb.core.MongoTemplate mongoTemplate;

    @Autowired
    MoleculeCurator moleculeCurator;


    @Autowired
    FixIdentifiers fixIdentifiers;

    @Autowired
    MiscFix miscFix;

    @Autowired
    CompoundUnification compoundUnification;


    public static void main(String[] args) {
        SpringApplication.run(WatermelonApplication.class, args);
    }

    @Override
    public void run(String... args) throws Exception {


        //mongoTemplate.getDb().drop();


        //read TSV file with watermelon molecules

        /*ArrayList<IAtomContainer> watermelonUSDA = readTSV.readFile(new File("data/watermelon_compoundsJ_20201228.tsv"));

        ArrayList<IAtomContainer> watermelonLOTUS = readTSV.readFile(new File("data/watermelon.tsv"));

        System.out.println(watermelonUSDA.size());
        System.out.println(watermelonLOTUS.size());

        ArrayList<IAtomContainer> watermelonAC = moleculesUnifier.joinTwoListsOfAtomContainers(watermelonUSDA, watermelonLOTUS);
        System.out.println(watermelonAC.size());
        System.out.println("done");*/
/*
        System.out.println("Reading files in "+args[0]);
        readTSV.readFromDirectoryAndInsertInMongo(args[0]);

        // compute all necessary properties
        System.out.println("computing molecular properties");
        propertiesComputer.processAndComputeProperties();

        fingerprintsCountsFiller.doWork();
        System.out.println("done");


        moleculeCurator.doWork();
        fixIdentifiers.createIDforNewMolecules();

        miscFix.aprilFix();
*/
        compoundUnification.eliminateIdenticalMolecules();

        System.out.println("writing to file");

        writeWMolecule.writeFromDatabase(new File("data/curated_watermelon_molecules_20211504.tsv"));


        /*writeWMolecule.writeAllToFile(watermelonMolecules, new File("data/curated_watermelon_molecules.tsv"));
        writeWMolecule.writeSimple(watermelonMolecules, new File("data/curated_watermelon_molecules_simple.tsv"));
        writeWMolecule.writeSmiles(watermelonMolecules, new File("data/curated_watermelon_molecules_smiles.txt"));
        */
        /*ObjectMapper objectMapper = new ObjectMapper();

        System.out.println("saving the curated-annotated molecules to JSON");
        objectMapper.writeValue(new File("data/watermelon_molecules_curated_annotated.json"), watermelonMolecules);
        System.out.println("done");

        */


        // compare to self
        //System.out.println("comparing watermelon molecules between them (Tanimoto on extended and PubChem FPs)");
        //ArrayList<WMoleculeSim> similarityToSelfList = compareToSelf.runComparison(watermelonMolecules);

        //compareToSelf.writeSimilarityToFile(similarityToSelfList, new File("data/similarityToSelf_tanimoto04.tsv"), 0.4);

        System.out.println("Done");



        //Compare to zinc (in vitro) , chembl, drugbank, PubChem





    }
}
