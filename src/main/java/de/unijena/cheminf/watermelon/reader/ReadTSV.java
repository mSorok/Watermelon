package de.unijena.cheminf.watermelon.reader;


import de.unijena.cheminf.watermelon.misc.BeanUtil;
import de.unijena.cheminf.watermelon.misc.MoleculeChecker;
import de.unijena.cheminf.watermelon.mongomodel.WatermelonMolecule;
import de.unijena.cheminf.watermelon.mongomodel.WatermelonMoleculeRepository;
import net.sf.jniinchi.INCHI_OPTION;
import org.openscience.cdk.DefaultChemObjectBuilder;
import org.openscience.cdk.exception.CDKException;
import org.openscience.cdk.exception.InvalidSmilesException;
import org.openscience.cdk.inchi.InChIGenerator;
import org.openscience.cdk.inchi.InChIGeneratorFactory;
import org.openscience.cdk.interfaces.IAtomContainer;
import org.openscience.cdk.interfaces.IBond;
import org.openscience.cdk.smiles.SmiFlavor;
import org.openscience.cdk.smiles.SmilesGenerator;
import org.openscience.cdk.smiles.SmilesParser;
import org.openscience.cdk.tools.CDKHydrogenAdder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


@Service
public class ReadTSV {

    String [] moleculesToExclude = {"coenzyme A", "NADPH", "NADP", "NADH", "NADP+", "NAD+", "ATP", "adenosine monophosphate", "ADP", "AMP", "adenosine 5'-phosphosulfate", "ADP-ribose",
    "ADP ribose 1''-phosphate", "uridine-5'-monophosphate", "UDP", "UMP", "UTP", "uridine-5'-diphosphate", "uridine 5'-triphosphate", "adenosine 3',5'-bisphosphate",
    "CMP", "CDP", "CTP", "cytidine-5'-triphosphate", "TMP", "TDP", "TTP", "cytidine-5'-triphosphate", "uridine 5'-triphosphate", "uridine-5'-monophosphate", "UMP", "UDP", "UTP", "uridine-5'-diphosphate",
    "thymidine-5'-phosphate", "thymidine-5'-diphosphate", "thymidine-5'-triphosphate", "AICA ribonucleotide", "succinyl-CoA", "acetyl coenzyme A" , "(S)-NADHX", "(R)-NADHX",
    "ribose 1''-phosphate", "GMP", "GDP", "GTP", "guanosine-5'-diphosphate", "guanosine-5'-monophosphate", "guanosine-5'-triphosphate"};

    String [] aminoAcids = {"serine", "D-serine", "L-serine", "alanine", "D-alanine", "L-alanine", "valine", "D-valine", "L-valine", "threonine", "D-threonine", "L-threonine",
    "tryptophan", "D-tryptophan", "L-tryptophan", "isoleucine", "D-isoleucine", "L-isoleucine", "arginine", "L-arginine", "D-arginine", "tyrosine", "L-tyrosine", "D-tyrosine",
    "lysine", "L-lysine", "D-lysine", "cysteine", "L-cysteine", "D-cysteine", "valine", "L-valine", "D-valine", "proline", "L-proline", "D-proline", "asparagine", "L-asparagine", "D-asparagine"};

    String [] afcIdToExclude = {"AFC000003", "AFC000010", "AFC000068", "AFC000018","AFC000196","AFC000201","AFC000346","AFC000499","AFC000540","AFC000632","AFC000785","AFC000740","AFC000797",
            "AFC000806","AFC000885","AFC001100","AFC001390","AFC001422","AFC001472","AFC001664","AFC001665","AFC001684","AFC001693","AFC001694","AFC001695","AFC001696","AFC001697","AFC001698",
            "AFC001699","AFC001702","AFC001743", "AFC000035", "AFC000524", "AFC001452", "AFC001795", "AFC001794"};



    ArrayList<String> moleculesToExcludeArray = new ArrayList<>(Arrays.asList(moleculesToExclude));
    ArrayList<String> aminoAcidsArray = new ArrayList<>(Arrays.asList(aminoAcids));
    ArrayList<String> afcidArray = new ArrayList<>(Arrays.asList(afcIdToExclude));


    //TODO : exclude the AFC ids

    File file;
    ArrayList<IAtomContainer> listOfMolecules;
    //MoleculeChecker moleculeChecker;

    @Autowired
    MoleculeChecker moleculeChecker;

    @Autowired
    WatermelonMoleculeRepository watermelonMoleculeRepository;


    public void readFromDirectoryAndInsertInMongo(String directory){

        File dataDir = new File(directory);
        File[] listOfDataFiles = dataDir.listFiles();


        SmilesGenerator absoluteSmilesGenerator = new SmilesGenerator(SmiFlavor.Absolute );
        SmilesGenerator uniqueSmilesGenerator = new SmilesGenerator(SmiFlavor.Absolute );

        SmilesParser sp = new SmilesParser(DefaultChemObjectBuilder.getInstance());


        for(File file : listOfDataFiles){
            if(file.getName().endsWith("tsv")) {

                System.out.println("Reading file "+file.getName());

                try {

                    BufferedReader bufferedReader = new BufferedReader(new FileReader(file));

                    ArrayList<String> header = new ArrayList<String>(Arrays.asList(bufferedReader.readLine().split("\t")));


                    if (header != null){


                        Integer indexOfName = null;
                        Integer indexOfIupac = null;
                        Integer indexOfSynonyms = null;
                        Integer indexOfCas = null;
                        Integer indexOfKegg = null;
                        Integer indexOfHmdb = null;
                        Integer indexOfPubchem = null;
                        Integer indexOfChebi = null;
                        Integer indexOfFoodb = null;
                        Integer indexOfLipidmaps = null;
                        Integer indexOfSource = null;
                        Integer indexOfAfc = null;
                        Integer indexOfOriSmiles = null;


                        for (String item : header) {
                            if (item.toLowerCase().contains("compound") || item.toLowerCase().contains("nametraditional") ){ indexOfName = header.indexOf(item); }
                            if (item.toLowerCase().equals("iupac")){ indexOfIupac = header.indexOf(item); }
                            if (item.toLowerCase().contains("alternative")){ indexOfSynonyms = header.indexOf(item); }
                            if (item.toLowerCase().contains("cas_id")){ indexOfCas = header.indexOf(item); }
                            if (item.toLowerCase().contains("kegg")){ indexOfKegg = header.indexOf(item); }
                            if (item.toLowerCase().contains("hmdb")){ indexOfHmdb = header.indexOf(item); }
                            if (item.toLowerCase().contains("pubchem")){ indexOfPubchem = header.indexOf(item); }
                            if (item.toLowerCase().contains("chebi")){ indexOfChebi = header.indexOf(item); }
                            if (item.toLowerCase().contains("food")){ indexOfFoodb = header.indexOf(item); }
                            if (item.toLowerCase().contains("lipid")){ indexOfLipidmaps = header.indexOf(item); }
                            if (item.toLowerCase().contains("source") || item.toLowerCase().contains("referencecleaneddoi") ){ indexOfSource = header.indexOf(item); }
                            if (item.toLowerCase().contains("afc_id")){ indexOfAfc = header.indexOf(item); }
                            if (item.toLowerCase().contains("smiles")){ indexOfOriSmiles = header.indexOf(item); } //is the original smiles
                        }
                        System.out.println(header);
                        System.out.println(indexOfOriSmiles);


                        //read the rest of the file
                        int count = 1;
                        String line;


                        while ((line = bufferedReader.readLine()) != null && count <= 600000) {

                            ArrayList<String> dataline = new ArrayList<String>(Arrays.asList(line.split("\t")));
                            if (dataline.size() >=indexOfOriSmiles && indexOfOriSmiles != null && !dataline.get(indexOfOriSmiles).equals("X")) {


                                try {
                                    IAtomContainer molecule = sp.parseSmiles(dataline.get(indexOfOriSmiles));
                                    molecule.setProperty("MOL_NUMBER_IN_FILE", Integer.toString(count));
                                    if(indexOfAfc != null && !dataline.get(indexOfAfc).equals("")) {
                                        molecule.setID(dataline.get(indexOfAfc));
                                    }


                                    if(moleculesToExcludeArray.contains(dataline.get(indexOfName)) || afcidArray.contains(dataline.get(indexOfAfc))){
                                        System.out.println(dataline.get(indexOfName));
                                        molecule = null;
                                    }

                                    if(molecule != null) {

                                        molecule = this.moleculeChecker.checkMolecule(molecule);
                                    }




                                    if(molecule != null) {



                                        WatermelonMolecule wm = new WatermelonMolecule();

                                        wm.setOriginal_smiles(dataline.get(indexOfOriSmiles));

                                        CDKHydrogenAdder adder = CDKHydrogenAdder.getInstance(molecule.getBuilder() );
                                        try {
                                            adder.addImplicitHydrogens(molecule);
                                        } catch (CDKException e) {
                                            e.printStackTrace();
                                        }


                                        wm.setAbsolute_smiles(absoluteSmilesGenerator.create(molecule));
                                        wm.setUnique_smiles(uniqueSmilesGenerator.create(molecule));

                                        ArrayList<String> inchis = this.calculateInchis(molecule);
                                        if(inchis.size()>=2) {
                                            wm.setInchi(inchis.get(0));
                                            wm.setInchikey(inchis.get(1));
                                        }



                                        //filling in the rest of data
                                        if (indexOfName != null) {
                                            molecule.setProperty("compoundName", dataline.get(indexOfName));
                                            wm.setCompoundName(dataline.get(indexOfName));
                                        }

                                        if (indexOfIupac != null && !dataline.get(indexOfIupac).equals("")) {
                                            molecule.setProperty("iupac", dataline.get(indexOfIupac));
                                            wm.setIupacName(dataline.get(indexOfIupac));
                                        }
                                        if (indexOfSynonyms != null && !dataline.get(indexOfSynonyms).equals("")) {
                                            molecule.setProperty("synonyms", dataline.get(indexOfSynonyms));
                                            wm.alternativeNames.add(dataline.get(indexOfSynonyms));
                                        }
                                        if (indexOfCas != null && !dataline.get(indexOfCas).equals("")) {
                                            molecule.setProperty("cas", dataline.get(indexOfCas));
                                            wm.setCas(dataline.get(indexOfCas));
                                        }

                                        if (indexOfKegg != null && !dataline.get(indexOfKegg).equals("") && !dataline.get(indexOfKegg).equals("no entry found")) {
                                            molecule.setProperty("kegg", dataline.get(indexOfKegg));
                                            wm.setKegg(dataline.get(indexOfKegg));
                                        }

                                        if (indexOfHmdb != null && !dataline.get(indexOfHmdb).equals("") && !dataline.get(indexOfHmdb).equals("no entry found")) {
                                            molecule.setProperty("hmdb", dataline.get(indexOfHmdb));
                                            wm.setHmdb(dataline.get(indexOfHmdb));
                                        }

                                        if (indexOfPubchem != null && !dataline.get(indexOfPubchem).equals("") && !dataline.get(indexOfPubchem).equals("no entry found")) {
                                            molecule.setProperty("pubchem", dataline.get(indexOfPubchem));
                                            wm.setPubchem(dataline.get(indexOfPubchem));
                                        }

                                        if (indexOfChebi != null && !dataline.get(indexOfChebi).equals("") && !dataline.get(indexOfChebi).equals("no entry found")) {
                                            molecule.setProperty("chebi", dataline.get(indexOfChebi));
                                            wm.setChebi(dataline.get(indexOfChebi));
                                        }

                                        if (indexOfFoodb != null && !dataline.get(indexOfFoodb).equals("") && !dataline.get(indexOfFoodb).equals("no entry found")) {
                                            molecule.setProperty("foodb", dataline.get(indexOfFoodb));
                                            wm.setFoodb(dataline.get(indexOfFoodb));
                                        }

                                        if (indexOfLipidmaps != null && !dataline.get(indexOfLipidmaps).equals("") && !dataline.get(indexOfLipidmaps).equals("no entry found")) {
                                            molecule.setProperty("lipidmaps", dataline.get(indexOfLipidmaps));
                                            wm.setLipidmaps(dataline.get(indexOfLipidmaps));
                                        }

                                        if (indexOfSource != null && !dataline.get(indexOfSource).equals("")) {
                                            molecule.setProperty("source", dataline.get(indexOfSource));
                                            wm.sources.add(dataline.get(indexOfSource));
                                        }

                                        if (indexOfAfc != null) {
                                            molecule.setProperty("afc", dataline.get(indexOfAfc));
                                            wm.setAfc_id(dataline.get(indexOfAfc));
                                        }


                                        //wm.setAtomContainer(molecule);

                                        this.watermelonMoleculeRepository.save(wm);
                                    }


                                } catch (InvalidSmilesException e) {
                                    e.printStackTrace();
                                 }




                            }

                            count++;
                        }






                    }
                }catch (IOException|CDKException e ) {
                    e.printStackTrace();
                }
            }
        }
    }



    public ArrayList<IAtomContainer> readFile(File file){
        this.listOfMolecules = new ArrayList<IAtomContainer>();

        System.out.println("Reading file "+file.getName());

        SmilesGenerator smilesGenerator = new SmilesGenerator(SmiFlavor.Absolute );
        SmilesParser sp = new SmilesParser(DefaultChemObjectBuilder.getInstance());

        this.file = file;

        try{
            BufferedReader bufferedReader = new BufferedReader(new FileReader(this.file));

            ArrayList<String> header = new ArrayList<String>(Arrays.asList(bufferedReader.readLine().split("\t")));


            if (header != null){

                Integer indexOfName = null;
                Integer indexOfIupac = null;
                Integer indexOfSynonyms = null;
                Integer indexOfCas = null;
                Integer indexOfKegg = null;
                Integer indexOfHmdb = null;
                Integer indexOfPubchem = null;
                Integer indexOfChebi = null;
                Integer indexOfFoodb = null;
                Integer indexOfLipidmaps = null;
                Integer indexOfSource = null;
                Integer indexOfAfc = null;
                Integer indexOfOriSmiles = null;

                for (String item : header) {
                    if (item.toLowerCase().contains("compound") || item.toLowerCase().contains("nametraditional") ){
                        indexOfName = header.indexOf(item);
                    }
                    if (item.toLowerCase().equals("iupac")){
                        indexOfIupac = header.indexOf(item);
                    }

                    if (item.toLowerCase().contains("alternative")){
                        indexOfSynonyms = header.indexOf(item);
                    }

                    if (item.toLowerCase().contains("cas_id")){
                        indexOfCas = header.indexOf(item);
                    }

                    if (item.toLowerCase().contains("kegg")){
                        indexOfKegg = header.indexOf(item);
                    }

                    if (item.toLowerCase().contains("hmdb")){
                        indexOfHmdb = header.indexOf(item);
                    }

                    if (item.toLowerCase().contains("pubchem")){
                        indexOfPubchem = header.indexOf(item);
                    }

                    if (item.toLowerCase().contains("chebi")){
                        indexOfChebi = header.indexOf(item);
                    }

                    if (item.toLowerCase().contains("food")){
                        indexOfFoodb = header.indexOf(item);
                    }
                    if (item.toLowerCase().contains("lipid")){
                        indexOfLipidmaps = header.indexOf(item);
                    }

                    if (item.toLowerCase().contains("source") || item.toLowerCase().contains("referencecleaneddoi") ){
                        indexOfSource = header.indexOf(item);
                    }

                    if (item.toLowerCase().contains("afc_id")){
                        indexOfAfc = header.indexOf(item);
                    }

                    if (item.toLowerCase().contains("smiles")){
                        indexOfOriSmiles = header.indexOf(item);
                    }
                }


                //read the rest of the file
                int count = 1;
                String line;

                while ((line = bufferedReader.readLine()) != null && count <= 600000) {

                    ArrayList<String> dataline = new ArrayList<String>(Arrays.asList(line.split("\t")));
                    if(dataline.size()==15){

                        IAtomContainer molecule = null;

                        if (indexOfOriSmiles != null && !dataline.get(indexOfOriSmiles).equals("X")) {
                            //System.out.println(count + " " + dataline.get(indexOfOriSmiles));
                            try {

                                molecule = sp.parseSmiles(dataline.get(indexOfOriSmiles));

                                molecule.setProperty("original_smiles", dataline.get(indexOfOriSmiles));




                                //filling in the rest of data
                                if (indexOfName != null) {
                                    molecule.setProperty("compoundName", dataline.get(indexOfName));
                                }

                                if (indexOfIupac != null && !dataline.get(indexOfIupac).equals("")) {
                                    molecule.setProperty("iupac", dataline.get(indexOfIupac));
                                }
                                if (indexOfSynonyms != null && !dataline.get(indexOfSynonyms).equals("")) {
                                    molecule.setProperty("synonyms", dataline.get(indexOfSynonyms));
                                }
                                if (indexOfCas != null && !dataline.get(indexOfCas).equals("")) {
                                    molecule.setProperty("cas", dataline.get(indexOfCas));
                                }

                                if (indexOfKegg != null && !dataline.get(indexOfKegg).equals("") && !dataline.get(indexOfKegg).equals("no entry found")) {
                                    molecule.setProperty("kegg", dataline.get(indexOfKegg));
                                }

                                if (indexOfHmdb != null && !dataline.get(indexOfHmdb).equals("") && !dataline.get(indexOfHmdb).equals("no entry found")) {
                                    molecule.setProperty("hmdb", dataline.get(indexOfHmdb));
                                }

                                if (indexOfPubchem != null && !dataline.get(indexOfPubchem).equals("") && !dataline.get(indexOfPubchem).equals("no entry found")) {
                                    molecule.setProperty("pubchem", dataline.get(indexOfPubchem));
                                }

                                if (indexOfChebi != null && !dataline.get(indexOfChebi).equals("") && !dataline.get(indexOfChebi).equals("no entry found")) {
                                    molecule.setProperty("chebi", dataline.get(indexOfChebi));
                                }

                                if (indexOfFoodb != null && !dataline.get(indexOfFoodb).equals("") && !dataline.get(indexOfFoodb).equals("no entry found")) {
                                    molecule.setProperty("foodb", dataline.get(indexOfFoodb));
                                }

                                if (indexOfLipidmaps != null && !dataline.get(indexOfLipidmaps).equals("") && !dataline.get(indexOfLipidmaps).equals("no entry found")) {
                                    molecule.setProperty("lipidmaps", dataline.get(indexOfLipidmaps));
                                }

                                if (indexOfSource != null && !dataline.get(indexOfSource).equals("")) {
                                    molecule.setProperty("source", dataline.get(indexOfSource));
                                }

                                if (indexOfAfc != null) {
                                    molecule.setProperty("afc", dataline.get(indexOfAfc));
                                }

                                //do check and more


                                molecule = moleculeChecker.checkMolecule(molecule);

                                //excluding some molecules
                                if(moleculesToExcludeArray.contains(dataline.get(indexOfName)) || aminoAcidsArray.contains(dataline.get(indexOfName))){
                                    System.out.println(dataline.get(indexOfName));
                                    molecule = null;
                                }

                                if (molecule != null) {
                                    try {
                                        try {
                                            List options = new ArrayList();
                                            options.add(INCHI_OPTION.SNon);
                                            options.add(INCHI_OPTION.ChiralFlagOFF);
                                            options.add(INCHI_OPTION.AuxNone);
                                            InChIGenerator gen = InChIGeneratorFactory.getInstance().getInChIGenerator(molecule, options);

                                            molecule.setProperty("INCHI", gen.getInchi());
                                            molecule.setProperty("INCHIKEY", gen.getInchiKey());


                                        } catch (CDKException e) {
                                            Integer totalBonds = molecule.getBondCount();
                                            Integer ib = 0;
                                            while (ib < totalBonds) {

                                                IBond b = molecule.getBond(ib);
                                                if (b.getOrder() == IBond.Order.UNSET) {
                                                    b.setOrder(IBond.Order.SINGLE);

                                                }
                                                ib++;
                                            }
                                            List options = new ArrayList();
                                            options.add(INCHI_OPTION.SNon);
                                            options.add(INCHI_OPTION.ChiralFlagOFF);
                                            options.add(INCHI_OPTION.AuxNone);
                                            InChIGenerator gen = InChIGeneratorFactory.getInstance().getInChIGenerator(molecule, options);

                                            molecule.setProperty("INCHI", gen.getInchi());
                                            molecule.setProperty("INCHIKEY", gen.getInchiKey());

                                        }

                                        CDKHydrogenAdder adder = CDKHydrogenAdder.getInstance(molecule.getBuilder() );
                                        try {
                                            adder.addImplicitHydrogens(molecule);
                                        } catch (CDKException e) {
                                            e.printStackTrace();
                                        }

                                        //generate unique absolute smiles
                                        String unique_smiles = smilesGenerator.create(molecule);
                                        molecule.setProperty("unique_smiles", unique_smiles);

                                        listOfMolecules.add(molecule);
                                    } catch (CDKException e) {
                                        e.printStackTrace();
                                    }
                                }

                            } catch (InvalidSmilesException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                    count++;
                }
            }
        } catch (
                IOException e ) {
            e.printStackTrace();
        }
        return this.listOfMolecules;
    }



    public ArrayList<String> calculateInchis(IAtomContainer molecule){

        ArrayList<String> inchidata = new ArrayList<>();



        try {
            try {
                List options = new ArrayList();
                options.add(INCHI_OPTION.SNon);
                options.add(INCHI_OPTION.ChiralFlagOFF);
                options.add(INCHI_OPTION.AuxNone);
                InChIGenerator gen = InChIGeneratorFactory.getInstance().getInChIGenerator(molecule, options);

                inchidata.add(gen.getInchi());
                inchidata.add(gen.getInchiKey());


            } catch (CDKException e) {
                Integer totalBonds = molecule.getBondCount();
                Integer ib = 0;
                while (ib < totalBonds) {
                    IBond b = molecule.getBond(ib);
                    if (b.getOrder() == IBond.Order.UNSET) {
                        b.setOrder(IBond.Order.SINGLE);

                    }
                    ib++;
                }
                List options = new ArrayList();
                options.add(INCHI_OPTION.SNon);
                options.add(INCHI_OPTION.ChiralFlagOFF);
                options.add(INCHI_OPTION.AuxNone);
                InChIGenerator gen = InChIGeneratorFactory.getInstance().getInChIGenerator(molecule, options);

                inchidata.add(gen.getInchi());
                inchidata.add(gen.getInchiKey());

            }




        } catch (CDKException e) {
            e.printStackTrace();
        }

        return inchidata;
    }









}
