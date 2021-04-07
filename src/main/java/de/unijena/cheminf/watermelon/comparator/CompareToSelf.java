package de.unijena.cheminf.watermelon.comparator;


import de.unijena.cheminf.watermelon.molecules.WMolecule;
import de.unijena.cheminf.watermelon.molecules.WMoleculeSim;
import org.openscience.cdk.similarity.Tanimoto;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

@Service
public class CompareToSelf {

    public ArrayList<WMoleculeSim> runComparison(ArrayList<WMolecule> wMoleculeArrayList){
        ArrayList<WMoleculeSim> simList = new ArrayList<>();
        ArrayList<String> molDone = new ArrayList<>();

        Integer count = 0;
        for(WMolecule wm1 : wMoleculeArrayList ){
            count++;
            for(WMolecule wm2 : wMoleculeArrayList){
                if(wm1.getAfc_id() != wm2.getAfc_id() && !molDone.contains(wm1.getAfc_id()+"$"+wm2.getAfc_id()) && !molDone.contains(wm2.getAfc_id()+"$"+wm1.getAfc_id()) ){
                    double tanimoto_extended = Tanimoto.calculate(wm1.getExtendedFingerprint(), wm2.getExtendedFingerprint());
                    double tanimoto_pubchem = Tanimoto.calculate(wm1.getPubchemFingerprint(), wm2.getPubchemFingerprint());

                    WMoleculeSim sim = new WMoleculeSim();
                    sim.setA(wm1);
                    sim.setB(wm2);
                    sim.setTanomoto_extended(tanimoto_extended);
                    sim.setTanomoto_pubchem(tanimoto_pubchem);

                    //System.out.println(tanimoto_extended);

                    molDone.add(wm1.getAfc_id()+"$"+wm2.getAfc_id());
                }
            }

            if(count%200==0){
                System.out.println("Molecules A compared: "+count);
            }
        }





        return simList;


    }


    public void writeSimilarityToFile(ArrayList<WMoleculeSim> wMoleculeSimArrayList, File output, Double tanimoto_threshold){
        try {
            FileWriter writer = new FileWriter(output);

            writer.write("moleculeA\tmoleculeB\ttanomoto_extended\ttanimoto_pubchem"+ System.lineSeparator());

            for(WMoleculeSim simObj: wMoleculeSimArrayList) {

                if(simObj.getTanomoto_extended()>= tanimoto_threshold || simObj.getTanomoto_pubchem() >= tanimoto_threshold) {
                    writer.write(simObj.getA().getAfc_id()+"\t"+simObj.getB().getAfc_id()+"\t"+simObj.getTanomoto_extended()+"\t"+simObj.getTanomoto_extended() + System.lineSeparator());
                }
            }
            writer.close();


        } catch (IOException e) {
            e.printStackTrace();
        }

    }




}
