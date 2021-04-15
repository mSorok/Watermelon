package de.unijena.cheminf.watermelon.misc;


import de.unijena.cheminf.watermelon.molecules.WMolecule;
import de.unijena.cheminf.watermelon.mongomodel.PubchemFingerPrintsCounts;
import de.unijena.cheminf.watermelon.mongomodel.WatermelonMolecule;
import de.unijena.cheminf.watermelon.mongomodel.WatermelonMoleculeRepository;
import org.openscience.cdk.AtomContainer;
import org.openscience.cdk.DefaultChemObjectBuilder;
import org.openscience.cdk.aromaticity.Aromaticity;
import org.openscience.cdk.aromaticity.ElectronDonation;
import org.openscience.cdk.atomtype.CDKAtomTypeMatcher;
import org.openscience.cdk.exception.CDKException;
import org.openscience.cdk.exception.InvalidSmilesException;
import org.openscience.cdk.fingerprint.ExtendedFingerprinter;
import org.openscience.cdk.fingerprint.PubchemFingerprinter;
import org.openscience.cdk.fragment.MurckoFragmenter;
import org.openscience.cdk.graph.Cycles;
import org.openscience.cdk.hash.MoleculeHashGenerator;
import org.openscience.cdk.interfaces.*;
import org.openscience.cdk.isomorphism.UniversalIsomorphismTester;
import org.openscience.cdk.qsar.DescriptorValue;
import org.openscience.cdk.qsar.descriptors.molecular.*;
import org.openscience.cdk.qsar.result.DoubleArrayResult;
import org.openscience.cdk.qsar.result.DoubleResult;
import org.openscience.cdk.qsar.result.IntegerResult;
import org.openscience.cdk.ringsearch.AllRingsFinder;
import org.openscience.cdk.silent.SilentChemObjectBuilder;
import org.openscience.cdk.smiles.SmiFlavor;
import org.openscience.cdk.smiles.SmilesGenerator;
import org.openscience.cdk.smiles.SmilesParser;
import org.openscience.cdk.tools.CDKHydrogenAdder;
import org.openscience.cdk.tools.manipulator.AtomContainerManipulator;
import org.openscience.cdk.tools.manipulator.AtomTypeManipulator;
import org.openscience.cdk.tools.manipulator.MolecularFormulaManipulator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tools.ErtlFunctionalGroupsFinder;
import tools.ErtlFunctionalGroupsFinderUtility;

import java.util.*;

@Service
public class PropertiesComputer {

    Aromaticity aromaticityModel = new Aromaticity(ElectronDonation.daylight(), Cycles.or(Cycles.all(), Cycles.cdkAromaticSet()));
    ErtlFunctionalGroupsFinder ertlFunctionalGroupsFinder  = ErtlFunctionalGroupsFinderUtility.getErtlFunctionalGroupsFinderGeneralizingMode();
    MoleculeHashGenerator efgHashGenerator = ErtlFunctionalGroupsFinderUtility.getFunctionalGroupHashGenerator();
    SmilesGenerator efgSmilesGenerator = new SmilesGenerator(SmiFlavor.Unique | SmiFlavor.UseAromaticSymbols);
    PubchemFingerprinter pubchemFingerprinter = new PubchemFingerprinter( SilentChemObjectBuilder.getInstance() );
    ExtendedFingerprinter extendedFingerprinter = new ExtendedFingerprinter();

    UniversalIsomorphismTester universalIsomorphismTester = new UniversalIsomorphismTester();

    @Autowired
    SugarRemovalService sugarRemovalService;

    @Autowired
    WatermelonMoleculeRepository watermelonMoleculeRepository;


    public void processAndComputeProperties(){

        SmilesParser sp = new SmilesParser(DefaultChemObjectBuilder.getInstance());
        List<WatermelonMolecule> molecules = watermelonMoleculeRepository.findAll();

        sugarRemovalService.prepareSugars();

        for(WatermelonMolecule wm : molecules){

            try {
                IAtomContainer ac =  sp.parseSmiles(wm.getAbsolute_smiles());


                wm = computeBasics(wm, ac);

                wm = computeMolecularDescriptors(wm, ac);

                wm = computeFingerprints(wm, ac);

                wm = computeErtlFunctionalGroups(wm, ac);

                wm = sugarRemoval(wm, ac);


                watermelonMoleculeRepository.save(wm);

            } catch (InvalidSmilesException e) {
                e.printStackTrace();
            }



        }



    }


    public ArrayList<WMolecule> processAndComputeProperties(ArrayList<IAtomContainer> acList){
        ArrayList<WMolecule> extendedList = new ArrayList<>();

        sugarRemovalService.prepareSugars();

        for(IAtomContainer ac : acList){
            WMolecule wm = createWMolecule(ac);

            wm = computeBasics(wm, ac);

            wm = computeMolecularDescriptors(wm);

            wm = computeFingerprints(wm);

            wm = computeErtlFunctionalGroups(wm, ac);

            wm = sugarRemoval(wm, ac);

            extendedList.add(wm);
        }
        return extendedList;
    }




    public WatermelonMolecule sugarRemoval(WatermelonMolecule wm, IAtomContainer ac){

        IAtomContainer aglycon = sugarRemovalService.removeSugarsFromAtomContainer(ac);

        //compare if anything was removed
        if (aglycon != null) {

            try {
                if (!universalIsomorphismTester.isIsomorph(aglycon, ac)) {
                    wm.setContains_sugar(1);
                    wm.setContains_linear_sugars(aglycon.getProperty("CONTAINS_LINEAR_SUGAR"));
                    wm.setContains_ring_sugars(aglycon.getProperty("CONTAINS_CIRCULAR_SUGAR"));
                } else {
                    //molecule doesn't contain sugar
                    wm.setContains_sugar(0);
                }
            } catch (CDKException e) {
                System.out.println("Failed detecting isomorphism between molecules with and without sugar");
                e.printStackTrace();
            }
        }else{
            wm.setContains_sugar(2);
        }



        return wm;
    }



    public WMolecule sugarRemoval(WMolecule wm, IAtomContainer ac){

        IAtomContainer aglycon = sugarRemovalService.removeSugarsFromAtomContainer(ac);

        //compare if anything was removed
        if (aglycon != null) {

            try {
                if (!universalIsomorphismTester.isIsomorph(aglycon, ac)) {
                    wm.setContains_sugars(1);
                    wm.setContains_linear_sugars(aglycon.getProperty("CONTAINS_LINEAR_SUGAR"));
                    wm.setContains_ring_sugars(aglycon.getProperty("CONTAINS_CIRCULAR_SUGAR"));
                } else {
                    //molecule doesn't contain sugar
                    wm.setContains_sugars(0);
                }
            } catch (CDKException e) {
                System.out.println("Failed detecting isomorphism between molecules with and without sugar");
                e.printStackTrace();
            }
        }else{
            wm.setContains_sugars(2);
        }



        return wm;
    }


    public WMolecule computeMolecularDescriptors(WMolecule wm){

        IChemObjectBuilder builder = DefaultChemObjectBuilder.getInstance();

        try {
            //AlogP
            ALOGPDescriptor alogpDescriptor = new ALOGPDescriptor();

            alogpDescriptor.initialise(builder);
            DescriptorValue alogpvalue = alogpDescriptor.calculate(wm.getAtomContainer());
            DoubleArrayResult alogpresults = (DoubleArrayResult) alogpvalue.getValue();
            wm.setAlogp(alogpresults.get(0));
            wm.setAlogp2(alogpresults.get(1));
            wm.setAmralogp(alogpresults.get(2));

            APolDescriptor aPolDescriptor = new APolDescriptor();
            aPolDescriptor.initialise(builder);
            DescriptorValue apolvalue = aPolDescriptor.calculate(wm.getAtomContainer());
            DoubleResult apolresult = (DoubleResult) apolvalue.getValue();
            wm.setApol(apolresult.doubleValue());

            BPolDescriptor bPolDescriptor = new BPolDescriptor();
            bPolDescriptor.initialise(builder);
            DescriptorValue bpolvalue = bPolDescriptor.calculate(wm.getAtomContainer());
            DoubleResult bpolresult = (DoubleResult) bpolvalue.getValue();
            wm.setBpol(bpolresult.doubleValue());

            EccentricConnectivityIndexDescriptor eccentricConnectivityIndexDescriptor = new EccentricConnectivityIndexDescriptor();
            eccentricConnectivityIndexDescriptor.initialise(builder);
            DescriptorValue eccenValue = eccentricConnectivityIndexDescriptor.calculate(wm.getAtomContainer());
            IntegerResult eccenResult = (IntegerResult) eccenValue.getValue();
            wm.setEccentricConnectivityIndexDescriptor(eccenResult.intValue());


            FMFDescriptor fmfDescriptor = new FMFDescriptor();
            fmfDescriptor.initialise(builder);
            DescriptorValue fmfValue = fmfDescriptor.calculate(wm.getAtomContainer());
            DoubleResult fmfResult = (DoubleResult) fmfValue.getValue();
            wm.setFmfDescriptor(fmfResult.doubleValue());

            FractionalCSP3Descriptor fractionalCSP3Descriptor = new FractionalCSP3Descriptor();
            fractionalCSP3Descriptor.initialise(builder);
            DescriptorValue fsp3value = fractionalCSP3Descriptor.calculate(wm.getAtomContainer());
            DoubleResult fsp3result = (DoubleResult) fsp3value.getValue();
            wm.setFsp3(fsp3result.doubleValue());

            FragmentComplexityDescriptor fragmentComplexityDescriptor = new FragmentComplexityDescriptor();
            fragmentComplexityDescriptor.initialise(builder);
            DescriptorValue fcdValue = fragmentComplexityDescriptor.calculate(wm.getAtomContainer());
            DoubleResult fcdResults = (DoubleResult) fcdValue.getValue();
            wm.setFragmentComplexityDescriptor(fcdResults.doubleValue());

            RuleOfFiveDescriptor ruleOfFiveDescriptor = new RuleOfFiveDescriptor();
            ruleOfFiveDescriptor.initialise(builder);
            DescriptorValue ruleOfFiveValue = ruleOfFiveDescriptor.calculate(wm.getAtomContainer());
            IntegerResult ruleOfFiveResult = (IntegerResult) ruleOfFiveValue.getValue();
            wm.setLipinskiRuleOf5Failures(ruleOfFiveResult.intValue());

            SpiroAtomCountDescriptor spiroAtomCountDescriptor = new SpiroAtomCountDescriptor();
            spiroAtomCountDescriptor.initialise(builder);
            DescriptorValue spiroatomValue = spiroAtomCountDescriptor.calculate(wm.getAtomContainer());
            IntegerResult spiroatomResult = (IntegerResult) spiroatomValue.getValue();
            wm.setNumberSpiroAtoms(spiroatomResult.intValue());

            XLogPDescriptor xLogPDescriptor = new XLogPDescriptor();
            xLogPDescriptor.initialise(builder);
            DescriptorValue xlogpValues = xLogPDescriptor.calculate(wm.getAtomContainer());
            DoubleResult xlogpResult = (DoubleResult) xlogpValues.getValue();
            wm.setXlogp(xlogpResult.doubleValue());

            ZagrebIndexDescriptor zagrebIndexDescriptor = new ZagrebIndexDescriptor();
            zagrebIndexDescriptor.initialise(builder);
            DescriptorValue zagrebIndexValue = zagrebIndexDescriptor.calculate(wm.getAtomContainer());
            DoubleResult zagrebIndexresult = (DoubleResult) zagrebIndexValue.getValue();
            wm.setZagrebIndex(zagrebIndexresult.doubleValue());

            TPSADescriptor tpsaDescriptor = new TPSADescriptor();
            tpsaDescriptor.initialise(builder);
            DescriptorValue tpsaValue = tpsaDescriptor.calculate(wm.getAtomContainer());
            DoubleResult tpsaResult = (DoubleResult) tpsaValue.getValue();
            wm.setTopoPSA(tpsaResult.doubleValue());

            FractionalPSADescriptor fractionalPSADescriptor = new FractionalPSADescriptor();
            fractionalPSADescriptor.initialise(builder);
            DescriptorValue ftpsaValue = fractionalPSADescriptor.calculate(wm.getAtomContainer());
            DoubleResult ftpsaResult = (DoubleResult) ftpsaValue.getValue();
            wm.setTpsaEfficiency(ftpsaResult.doubleValue());



        } catch (CDKException e) {
            e.printStackTrace();
        }










        try {
            MurckoFragmenter murckoFragmenter = new MurckoFragmenter(true, 3);
            murckoFragmenter.generateFragments(wm.getAtomContainer());
            if (murckoFragmenter.getFragments() != null && murckoFragmenter.getFragments().length > 0) {
                wm.setMurko_framework(murckoFragmenter.getFrameworks()[0]);
            }

        } catch (CDKException | NullPointerException e) {
            //e.printStackTrace();
            System.out.println("Failed creating Murcko fragment");
            wm.setMurko_framework("");
        }

        return wm;
    }





    public WatermelonMolecule computeMolecularDescriptors(WatermelonMolecule wm, IAtomContainer ac){

        IChemObjectBuilder builder = DefaultChemObjectBuilder.getInstance();

        try {
            //AlogP
            ALOGPDescriptor alogpDescriptor = new ALOGPDescriptor();

            alogpDescriptor.initialise(builder);
            DescriptorValue alogpvalue = alogpDescriptor.calculate(ac);
            DoubleArrayResult alogpresults = (DoubleArrayResult) alogpvalue.getValue();
            wm.setAlogp(alogpresults.get(0));
            wm.setAlogp2(alogpresults.get(1));
            wm.setAmralogp(alogpresults.get(2));

            APolDescriptor aPolDescriptor = new APolDescriptor();
            aPolDescriptor.initialise(builder);
            DescriptorValue apolvalue = aPolDescriptor.calculate(ac);
            DoubleResult apolresult = (DoubleResult) apolvalue.getValue();
            wm.setApol(apolresult.doubleValue());

            BPolDescriptor bPolDescriptor = new BPolDescriptor();
            bPolDescriptor.initialise(builder);
            DescriptorValue bpolvalue = bPolDescriptor.calculate(ac);
            DoubleResult bpolresult = (DoubleResult) bpolvalue.getValue();
            wm.setBpol(bpolresult.doubleValue());

            EccentricConnectivityIndexDescriptor eccentricConnectivityIndexDescriptor = new EccentricConnectivityIndexDescriptor();
            eccentricConnectivityIndexDescriptor.initialise(builder);
            DescriptorValue eccenValue = eccentricConnectivityIndexDescriptor.calculate(ac);
            IntegerResult eccenResult = (IntegerResult) eccenValue.getValue();
            wm.setEccentricConnectivityIndexDescriptor(eccenResult.intValue());


            FMFDescriptor fmfDescriptor = new FMFDescriptor();
            fmfDescriptor.initialise(builder);
            DescriptorValue fmfValue = fmfDescriptor.calculate(ac);
            DoubleResult fmfResult = (DoubleResult) fmfValue.getValue();
            wm.setFmfDescriptor(fmfResult.doubleValue());

            FractionalCSP3Descriptor fractionalCSP3Descriptor = new FractionalCSP3Descriptor();
            fractionalCSP3Descriptor.initialise(builder);
            DescriptorValue fsp3value = fractionalCSP3Descriptor.calculate(ac);
            DoubleResult fsp3result = (DoubleResult) fsp3value.getValue();
            wm.setFsp3(fsp3result.doubleValue());

            FragmentComplexityDescriptor fragmentComplexityDescriptor = new FragmentComplexityDescriptor();
            fragmentComplexityDescriptor.initialise(builder);
            DescriptorValue fcdValue = fragmentComplexityDescriptor.calculate(ac);
            DoubleResult fcdResults = (DoubleResult) fcdValue.getValue();
            wm.setFragmentComplexityDescriptor(fcdResults.doubleValue());

            RuleOfFiveDescriptor ruleOfFiveDescriptor = new RuleOfFiveDescriptor();
            ruleOfFiveDescriptor.initialise(builder);
            DescriptorValue ruleOfFiveValue = ruleOfFiveDescriptor.calculate(ac);
            IntegerResult ruleOfFiveResult = (IntegerResult) ruleOfFiveValue.getValue();
            wm.setLipinskiRuleOf5Failures(ruleOfFiveResult.intValue());

            SpiroAtomCountDescriptor spiroAtomCountDescriptor = new SpiroAtomCountDescriptor();
            spiroAtomCountDescriptor.initialise(builder);
            DescriptorValue spiroatomValue = spiroAtomCountDescriptor.calculate(ac);
            IntegerResult spiroatomResult = (IntegerResult) spiroatomValue.getValue();
            wm.setNumberSpiroAtoms(spiroatomResult.intValue());

            XLogPDescriptor xLogPDescriptor = new XLogPDescriptor();
            xLogPDescriptor.initialise(builder);
            DescriptorValue xlogpValues = xLogPDescriptor.calculate(ac);
            DoubleResult xlogpResult = (DoubleResult) xlogpValues.getValue();
            wm.setXlogp(xlogpResult.doubleValue());

            ZagrebIndexDescriptor zagrebIndexDescriptor = new ZagrebIndexDescriptor();
            zagrebIndexDescriptor.initialise(builder);
            DescriptorValue zagrebIndexValue = zagrebIndexDescriptor.calculate(ac);
            DoubleResult zagrebIndexresult = (DoubleResult) zagrebIndexValue.getValue();
            wm.setZagrebIndex(zagrebIndexresult.doubleValue());

            TPSADescriptor tpsaDescriptor = new TPSADescriptor();
            tpsaDescriptor.initialise(builder);
            DescriptorValue tpsaValue = tpsaDescriptor.calculate(ac);
            DoubleResult tpsaResult = (DoubleResult) tpsaValue.getValue();
            wm.setTopoPSA(tpsaResult.doubleValue());

            FractionalPSADescriptor fractionalPSADescriptor = new FractionalPSADescriptor();
            fractionalPSADescriptor.initialise(builder);
            DescriptorValue ftpsaValue = fractionalPSADescriptor.calculate(ac);
            DoubleResult ftpsaResult = (DoubleResult) ftpsaValue.getValue();
            wm.setTpsaEfficiency(ftpsaResult.doubleValue());



        } catch (CDKException e) {
            e.printStackTrace();
        }



        try {
            MurckoFragmenter murckoFragmenter = new MurckoFragmenter(true, 3);
            murckoFragmenter.generateFragments(ac);
            if (murckoFragmenter.getFragments() != null && murckoFragmenter.getFragments().length > 0) {
                wm.setMurko_framework(murckoFragmenter.getFrameworks()[0]);
            }

        } catch (CDKException | NullPointerException e) {
            //e.printStackTrace();
            System.out.println("Failed creating Murcko fragment");
            wm.setMurko_framework("");
        }

        return wm;
    }


    public WMolecule computeFingerprints(WMolecule wm){

        try {
            wm.setExtendedFingerprint(extendedFingerprinter.getBitFingerprint(wm.getAtomContainer()));
            wm.setPubchemFingerprint(pubchemFingerprinter.getBitFingerprint(wm.getAtomContainer()));
        } catch (CDKException e) {
            e.printStackTrace();
        }

        return wm;
    }



    public WatermelonMolecule computeFingerprints(WatermelonMolecule wm, IAtomContainer ac){

        CDKAtomTypeMatcher matcher = CDKAtomTypeMatcher.getInstance(ac.getBuilder());
        CDKHydrogenAdder adder = CDKHydrogenAdder.getInstance(ac.getBuilder() );
        for (int j = 0; j < ac.getAtomCount(); j++) {
            IAtom atom = ac.getAtom(j);
            IAtomType type = null;
            try {
                type = matcher.findMatchingAtomType(ac, atom);
                AtomTypeManipulator.configure(atom, type);
            } catch (CDKException e) {
                e.printStackTrace();
            }

        }

        try {
            adder.addImplicitHydrogens(ac);
        } catch (CDKException e) {
            e.printStackTrace();
        }


        AtomContainerManipulator.removeNonChiralHydrogens(ac);

        try {

            String s = pubchemFingerprinter.getBitFingerprint(ac).asBitSet().toString();
            ArrayList<Integer> pcl = new ArrayList<>();
            s = s.replace(" ", "");
            s = s.replace("\"", "");
            s = s.replace("{", "");
            s = s.replace("}", "");
            String[] sl = s.split(",");
            for (String c : sl) {
                try {
                    pcl.add(Integer.parseInt(c));
                } catch (NumberFormatException e) {
                    e.printStackTrace();
                }
            }
            wm.setPubchemFingerprint(pcl);


            BitSet bitsOn = pubchemFingerprinter.getBitFingerprint(ac).asBitSet();
            String pubchemBitString = "";

            for (int i = 0; i <= bitsOn.length(); i++) {
                if (bitsOn.get(i)) {
                    pubchemBitString += "1";
                } else {
                    pubchemBitString += "0";
                }
            }

            wm.setPubchemBits(bitsOn.toByteArray());
            wm.setPubchemBitsString(pubchemBitString);





            BitSet spf = pubchemFingerprinter.getBitFingerprint(ac).asBitSet();
            ArrayList<Integer> indexes = new ArrayList<>();
            for (int i = spf.nextSetBit(0); i != -1; i = spf.nextSetBit(i + 1)) {
                indexes.add(i);
            }

            PubchemFingerPrintsCounts pcClass = new PubchemFingerPrintsCounts(indexes.size(),indexes );

            wm.pfCounts = pcClass;

            wm.pubfp = null;



        } catch (CDKException | UnsupportedOperationException e) {
            e.printStackTrace();
        }

        return wm;
    }




    public WMolecule computeBasics(WMolecule wm, IAtomContainer ac){
        AllRingsFinder arf = new AllRingsFinder();
        MolecularFormulaManipulator mfm = new MolecularFormulaManipulator();
        AtomContainerManipulator acm = new AtomContainerManipulator();



        // count rings
        try {
            IRingSet rs = arf.findAllRings(ac, 20);

            wm.setMax_number_of_rings(rs.getAtomContainerCount());

            Cycles cycles = Cycles.sssr(ac);
            IRingSet rings  = cycles.toRingSet();
            wm.setMin_number_of_rings(rings.getAtomContainerCount()); //SSSR


        } catch (CDKException e) {
            System.out.println("Too complex: "+wm.getUnique_smiles());
        }

        //compute molecular formula
        wm.setMolecularFormula(mfm.getString(mfm.getMolecularFormula(ac) ));

        //compute number of carbons, of nitrogens, of oxygens
        wm.setNumber_of_carbons(mfm.getElementCount(mfm.getMolecularFormula(ac), "C"));

        wm.setNumber_of_oxygens(mfm.getElementCount(mfm.getMolecularFormula(ac), "O"));

        wm.setNumber_of_nitrogens(mfm.getElementCount(mfm.getMolecularFormula(ac), "N"));

        wm.setMolecularWeight( acm.getMass(ac) );
        // cleaning the NaNs
        if(wm.getMolecularWeight().isNaN()){
            wm.setMolecularWeight(0.0);
        }
        //get bond count
        IBond[] bonds = acm.getBondArray(ac);
        int bondCount = 0;
        for(IBond b : bonds){
            if(b.getAtomCount() == 2){
                if(!b.getAtom(0).getSymbol().equals("H") && !b.getAtom(1).getSymbol().equals("H")){
                    bondCount++;
                }
            }
        }
        wm.setBond_count(bondCount);
        return wm;
    }

    public WatermelonMolecule computeBasics(WatermelonMolecule wm, IAtomContainer ac){
        AllRingsFinder arf = new AllRingsFinder();
        MolecularFormulaManipulator mfm = new MolecularFormulaManipulator();
        AtomContainerManipulator acm = new AtomContainerManipulator();

        wm.setTotal_atom_number(ac.getAtomCount());

        int heavyAtomCount = 0;
        for(IAtom a : ac.atoms()){
            if(!a.getSymbol().equals("H")){
                heavyAtomCount=heavyAtomCount+1;
            }
        }
        wm.setHeavy_atom_number(heavyAtomCount);




        // count rings
        try {
            IRingSet rs = arf.findAllRings(ac, 20);

            wm.setMax_number_of_rings(rs.getAtomContainerCount());

            Cycles cycles = Cycles.sssr(ac);
            IRingSet rings  = cycles.toRingSet();
            wm.setMin_number_of_rings(rings.getAtomContainerCount()); //SSSR


        } catch (CDKException e) {
            System.out.println("Too complex: "+wm.getUnique_smiles());
        }

        //compute molecular formula
        wm.setMolecular_formula(mfm.getString(mfm.getMolecularFormula(ac) ));

        //compute number of carbons, of nitrogens, of oxygens
        wm.setNumber_of_carbons(mfm.getElementCount(mfm.getMolecularFormula(ac), "C"));

        wm.setNumber_of_oxygens(mfm.getElementCount(mfm.getMolecularFormula(ac), "O"));

        wm.setNumber_of_nitrogens(mfm.getElementCount(mfm.getMolecularFormula(ac), "N"));

        wm.setMolecular_weight( acm.getMass(ac) );
        // cleaning the NaNs
        if(wm.getMolecular_weight().isNaN()){
            wm.setMolecular_weight(0.0);
        }
        //get bond count
        IBond[] bonds = acm.getBondArray(ac);
        int bondCount = 0;
        for(IBond b : bonds){
            if(b.getAtomCount() == 2){
                if(!b.getAtom(0).getSymbol().equals("H") && !b.getAtom(1).getSymbol().equals("H")){
                    bondCount++;
                }
            }
        }
        wm.setBond_count(bondCount);
        return wm;
    }



    public WMolecule createWMolecule(IAtomContainer ac){
        WMolecule wm = new WMolecule();

        wm.setOriginal_smiles(ac.getProperty("original_smiles"));
        wm.setUnique_smiles(ac.getProperty("unique_smiles"));

        wm.setAtomContainer(ac);

        if(ac.getProperties().containsKey("INCHI")){
            wm.setInchi(ac.getProperty("INCHI"));
        }

        if(ac.getProperties().containsKey("INCHIKEY")){
            wm.setInchikey(ac.getProperty("INCHIKEY"));
        }


        if(ac.getProperties().containsKey("compoundName")){
            wm.setCompoundName(ac.getProperty("compoundName"));
        }

        if(ac.getProperties().containsKey("iupac")){
            wm.setIupacName(ac.getProperty("iupac"));
        }

        if(ac.getProperties().containsKey("synonyms")){

            wm.alternativeNames = new HashSet<>();
            String [] t = ac.getProperty("synonyms").toString().split(";");
            if(t.length>0){
                for(String s : t){
                    wm.alternativeNames.add(s);
                }
            }
        }

        if(ac.getProperties().containsKey("source")){

            wm.sources = new ArrayList<>();
            String [] t = ac.getProperty("source").toString().split(";");
            if(t.length>0){
                for(String s : t){
                    wm.sources.add(s);
                }
            }
        }


        if(ac.getProperties().containsKey("cas")) {
            wm.setCas(ac.getProperty("cas"));
        }

        if(ac.getProperties().containsKey("kegg")) {
            wm.setKegg(ac.getProperty("kegg"));
        }

        if(ac.getProperties().containsKey("hmdb")) {
            wm.setHmdb(ac.getProperty("hmdb"));
        }

        if(ac.getProperties().containsKey("pubchem")) {
            wm.setPubchem(ac.getProperty("pubchem"));
        }

        if(ac.getProperties().containsKey("chebi")) {
            wm.setChebi(ac.getProperty("chebi"));
        }

        if(ac.getProperties().containsKey("foodb")) {
            wm.setFoodb(ac.getProperty("foodb"));
        }

        if(ac.getProperties().containsKey("lipidmaps")) {
            wm.setLipidmaps(ac.getProperty("lipidmaps"));
        }

        if(ac.getProperties().containsKey("afc")) {
            wm.setAfc_id(ac.getProperty("afc"));
        }


        return wm;

    }



    public WMolecule computeErtlFunctionalGroups(WMolecule wm, IAtomContainer ac){

        List<IAtomContainer> functionalGroupsGeneralized;

        // Addition of implicit hydrogens & atom typer
        CDKAtomTypeMatcher matcher = CDKAtomTypeMatcher.getInstance(ac.getBuilder());
        CDKHydrogenAdder adder = CDKHydrogenAdder.getInstance(ac.getBuilder() );
        for (int j = 0; j < ac.getAtomCount(); j++) {
            IAtom atom = ac.getAtom(j);
            IAtomType type = null;
            try {
                type = matcher.findMatchingAtomType(ac, atom);
                AtomTypeManipulator.configure(atom, type);
            } catch (CDKException e) {
                e.printStackTrace();
            }

        }

        try {
            adder.addImplicitHydrogens(ac);
        } catch (CDKException e) {
            e.printStackTrace();
        }
        AtomContainerManipulator.convertImplicitToExplicitHydrogens(ac);
        AtomContainerManipulator.removeNonChiralHydrogens(ac);

        try{

            ac = ErtlFunctionalGroupsFinderUtility.applyFiltersAndPreprocessing(ac, aromaticityModel);
            functionalGroupsGeneralized = ertlFunctionalGroupsFinder.find(ac, false);
            if (!functionalGroupsGeneralized.isEmpty()) {

                wm.ertlFunctionalFragments = new Hashtable<>();
                wm.ertlFunctionalFragmentsPseudoSmiles = new Hashtable<>();

                HashMap<Long, IAtomContainer> tmpResultsMap = new HashMap<>(functionalGroupsGeneralized.size(), 1);
                for (IAtomContainer functionalGroup : functionalGroupsGeneralized) {
                    Long hashCode = efgHashGenerator.generate(functionalGroup);
                    if (tmpResultsMap.keySet().contains(hashCode)) {
                        int tmpFrequency = tmpResultsMap.get(hashCode).getProperty("FREQUENCY");
                        tmpResultsMap.get(hashCode).setProperty("FREQUENCY", tmpFrequency + 1);
                    } else {
                        functionalGroup.setProperty("FREQUENCY", 1);
                        tmpResultsMap.put(hashCode, functionalGroup);
                    }
                }


                for (Long tmpHashCode : tmpResultsMap.keySet()) {
                    IAtomContainer tmpFunctionalGroup = tmpResultsMap.get(tmpHashCode);
                    String tmpFGSmilesCode = null;
                    try {
                        tmpFGSmilesCode = efgSmilesGenerator.create(tmpFunctionalGroup);

                        String tmpFGPseudoSmilesCode = ErtlFunctionalGroupsFinderUtility.createPseudoSmilesCode(tmpFunctionalGroup);


                        int tmpFrequency = tmpFunctionalGroup.getProperty("FREQUENCY");

                        wm.ertlFunctionalFragments.put(tmpFGSmilesCode, tmpFrequency);
                        wm.ertlFunctionalFragmentsPseudoSmiles.put(tmpFGPseudoSmilesCode, tmpFrequency);

                    } catch (CDKException e) {
                        e.printStackTrace();
                    }
                }




            }
        }catch(NullPointerException e){
            System.out.println("Failed to compute the Ertl functional groups for "+wm.getInchikey());
        }

        return wm;

    }




    public WatermelonMolecule computeErtlFunctionalGroups(WatermelonMolecule wm, IAtomContainer ac){

        List<IAtomContainer> functionalGroupsGeneralized;

        // Addition of implicit hydrogens & atom typer
        CDKAtomTypeMatcher matcher = CDKAtomTypeMatcher.getInstance(ac.getBuilder());
        CDKHydrogenAdder adder = CDKHydrogenAdder.getInstance(ac.getBuilder() );
        for (int j = 0; j < ac.getAtomCount(); j++) {
            IAtom atom = ac.getAtom(j);
            IAtomType type = null;
            try {
                type = matcher.findMatchingAtomType(ac, atom);
                AtomTypeManipulator.configure(atom, type);
            } catch (CDKException e) {
                e.printStackTrace();
            }

        }

        try {
            adder.addImplicitHydrogens(ac);
        } catch (CDKException e) {
            e.printStackTrace();
        }
        AtomContainerManipulator.convertImplicitToExplicitHydrogens(ac);
        AtomContainerManipulator.removeNonChiralHydrogens(ac);

        try{

            ac = ErtlFunctionalGroupsFinderUtility.applyFiltersAndPreprocessing(ac, aromaticityModel);
            functionalGroupsGeneralized = ertlFunctionalGroupsFinder.find(ac, false);
            if (!functionalGroupsGeneralized.isEmpty()) {

                wm.ertlFunctionalFragments = new Hashtable<>();
                wm.ertlFunctionalFragmentsPseudoSmiles = new Hashtable<>();

                HashMap<Long, IAtomContainer> tmpResultsMap = new HashMap<>(functionalGroupsGeneralized.size(), 1);
                for (IAtomContainer functionalGroup : functionalGroupsGeneralized) {
                    Long hashCode = efgHashGenerator.generate(functionalGroup);
                    if (tmpResultsMap.keySet().contains(hashCode)) {
                        int tmpFrequency = tmpResultsMap.get(hashCode).getProperty("FREQUENCY");
                        tmpResultsMap.get(hashCode).setProperty("FREQUENCY", tmpFrequency + 1);
                    } else {
                        functionalGroup.setProperty("FREQUENCY", 1);
                        tmpResultsMap.put(hashCode, functionalGroup);
                    }
                }


                for (Long tmpHashCode : tmpResultsMap.keySet()) {
                    IAtomContainer tmpFunctionalGroup = tmpResultsMap.get(tmpHashCode);
                    String tmpFGSmilesCode = null;
                    try {
                        tmpFGSmilesCode = efgSmilesGenerator.create(tmpFunctionalGroup);

                        String tmpFGPseudoSmilesCode = ErtlFunctionalGroupsFinderUtility.createPseudoSmilesCode(tmpFunctionalGroup);


                        int tmpFrequency = tmpFunctionalGroup.getProperty("FREQUENCY");

                        wm.ertlFunctionalFragments.put(tmpFGSmilesCode, tmpFrequency);
                        wm.ertlFunctionalFragmentsPseudoSmiles.put(tmpFGPseudoSmilesCode, tmpFrequency);

                    } catch (CDKException e) {
                        e.printStackTrace();
                    }
                }




            }
        }catch(NullPointerException e){
            System.out.println("Failed to compute the Ertl functional groups for "+wm.getInchikey());
        }

        return wm;

    }





}
