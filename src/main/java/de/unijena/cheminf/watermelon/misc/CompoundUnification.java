package de.unijena.cheminf.watermelon.misc;


import de.unijena.cheminf.watermelon.mongomodel.QuarantinedMolecule;
import de.unijena.cheminf.watermelon.mongomodel.QuarantinedMoleculeRepository;
import de.unijena.cheminf.watermelon.mongomodel.WatermelonMolecule;
import de.unijena.cheminf.watermelon.mongomodel.WatermelonMoleculeRepository;
import org.openscience.cdk.DefaultChemObjectBuilder;
import org.openscience.cdk.exception.CDKException;
import org.openscience.cdk.exception.InvalidSmilesException;
import org.openscience.cdk.fingerprint.*;
import org.openscience.cdk.interfaces.IAtomContainer;
import org.openscience.cdk.similarity.Tanimoto;
import org.openscience.cdk.smiles.SmilesParser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CompoundUnification {

    @Autowired
    WatermelonMoleculeRepository watermelonMoleculeRepository;

    @Autowired
    QuarantinedMoleculeRepository quarantinedMoleculeRepository;

    public void eliminateIdenticalMolecules(){

        SmilesParser sp = new SmilesParser(DefaultChemObjectBuilder.getInstance());
        ExtendedFingerprinter extendedFingerprinter = new ExtendedFingerprinter();
        MACCSFingerprinter maccsFingerprinter = new MACCSFingerprinter();
        SignatureFingerprinter signatureFingerprinter = new SignatureFingerprinter();
        //PubchemFingerprinter pubchemFingerprinter = new PubchemFingerprinter();
        ShortestPathFingerprinter shortestPathFingerprinter = new ShortestPathFingerprinter();
        HybridizationFingerprinter hybridizationFingerprinter = new HybridizationFingerprinter();

        List<WatermelonMolecule> allMolecules = watermelonMoleculeRepository.findAll();

        for(WatermelonMolecule wm1 : allMolecules){
            for(WatermelonMolecule wm2 : allMolecules){
                if(!wm1.afc_id.equals(wm2.afc_id)){
                    boolean toUnify = false;
                    if(wm1.absolute_smiles.equals(wm2.absolute_smiles) && wm1.unique_smiles.equals(wm2.unique_smiles)){
                        toUnify = true;
                        System.out.println(wm1.afc_id+" and "+wm2.afc_id + " unified on identical absolute smiles");
                    }else{
                        if( wm1.molecular_formula.equals(wm2.molecular_formula)  && !wm1.absolute_smiles.matches("^C+$")) { //not only pure lipid
                            // catculate Tanimoto
                            try {
                                IAtomContainer ac1 = sp.parseSmiles(wm1.unique_smiles);
                                IAtomContainer ac2 = sp.parseSmiles(wm2.unique_smiles);

                                Double tanimoto_extended = Tanimoto.calculate(extendedFingerprinter.getBitFingerprint(ac1), extendedFingerprinter.getBitFingerprint(ac2));
                                Double tanimoto_maccs = Tanimoto.calculate(maccsFingerprinter.getBitFingerprint(ac1), maccsFingerprinter.getBitFingerprint(ac2));
                                Double tanimoto_signature = Tanimoto.calculate(signatureFingerprinter.getBitFingerprint(ac1), signatureFingerprinter.getBitFingerprint(ac2));
                                Double tanimoto_hybrid = Tanimoto.calculate(hybridizationFingerprinter.getBitFingerprint(ac1), hybridizationFingerprinter.getBitFingerprint(ac2));

                                //Double tanimoto_sp = Tanimoto.calculate(shortestPathFingerprinter.getBitFingerprint(ac1), shortestPathFingerprinter.getBitFingerprint(ac2));
                                if (tanimoto_extended == 1 && tanimoto_maccs == 1 && tanimoto_signature == 1 && tanimoto_hybrid ==1) {
                                    toUnify = true;
                                }
                            } catch (CDKException | NullPointerException e) {
                                e.printStackTrace();
                            }
                        }
                    }


                    if(toUnify){

                        // the unification
                        WatermelonMolecule mainWm;
                        WatermelonMolecule qWm;
                        if(Integer.parseInt(wm1.afc_id.split("FC")[1])<Integer.parseInt(wm2.afc_id.split("FC")[1])){
                            mainWm = wm1;
                            qWm = wm2;
                        }else{
                            mainWm = wm2;
                            qWm = wm1;
                        }

                        if(!mainWm.compoundName.equals(qWm.compoundName)){
                            mainWm.alternativeNames.add(qWm.compoundName);
                        }
                        mainWm.alternativeNames.addAll(qWm.alternativeNames);

                        if(mainWm.iupacName == "" && qWm.iupacName != ""){
                            mainWm.iupacName = qWm.iupacName;
                        }

                        if(mainWm.cas == "" && qWm.cas != ""){
                            mainWm.cas = qWm.cas;
                        }

                        if(mainWm.kegg == "" && qWm.kegg != ""){
                            mainWm.kegg = qWm.kegg;
                        }

                        if(mainWm.hmdb == "" && qWm.hmdb != ""){
                            mainWm.hmdb = qWm.hmdb;
                        }

                        if(mainWm.pubchem == "" && qWm.pubchem != ""){
                            mainWm.pubchem = qWm.pubchem;
                        }

                        if(mainWm.chebi == "" && qWm.chebi != ""){
                            mainWm.chebi = qWm.chebi;
                        }

                        if(mainWm.foodb == "" && qWm.foodb != ""){
                            mainWm.foodb = qWm.foodb;
                        }

                        if(mainWm.lipidmaps == "" && qWm.lipidmaps != ""){
                            mainWm.lipidmaps = qWm.lipidmaps;
                        }

                        mainWm.sources.addAll(qWm.sources);
                        mainWm.xrefs.addAll(qWm.xrefs);

                        watermelonMoleculeRepository.save(mainWm);
                        QuarantinedMolecule qm = new QuarantinedMolecule();
                        qm.setQwm(qWm);
                        quarantinedMoleculeRepository.save(qm);
                        watermelonMoleculeRepository.delete(qWm);

                        System.out.println("Unified "+mainWm.afc_id+" and "+qWm.afc_id);
                        System.out.println("Removed "+qWm.afc_id+ " from main collection");





                    }


                }
            }
        }


    }






}
