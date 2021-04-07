package de.unijena.cheminf.watermelon.misc;



import de.unijena.cheminf.watermelon.mongomodel.PubFingerprintsCounts;
import de.unijena.cheminf.watermelon.mongomodel.PubFingerprintsCountsRepository;
import de.unijena.cheminf.watermelon.mongomodel.WatermelonMolecule;
import de.unijena.cheminf.watermelon.mongomodel.WatermelonMoleculeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Hashtable;
import java.util.List;

@Service
public class FingerprintsCountsFiller {

    @Autowired
    WatermelonMoleculeRepository watermelonMoleculeRepository;

    @Autowired
    PubFingerprintsCountsRepository pubFingerprintsCountsRepository;


    public void doWork(){

        System.out.println("Generating PubChemFingerprints counts");

        Hashtable<Integer, Integer> counts = new Hashtable<>();

        List<WatermelonMolecule> allWMs = watermelonMoleculeRepository.findAll();


        for(WatermelonMolecule wm : allWMs){

            if(!wm.pubchemFingerprint.isEmpty()){
                for(Integer bitOnIndex : wm.pubchemFingerprint){

                    if(counts.containsKey(bitOnIndex)){
                        //add count
                        counts.put(bitOnIndex, counts.get(bitOnIndex) + 1);

                    }else{
                        //create new key
                        counts.put(bitOnIndex, 1);
                    }

                }
            }

        }

        for(Integer bitOnIndex: counts.keySet() ){
            PubFingerprintsCounts pubFingerprintsCounts = new PubFingerprintsCounts();
            pubFingerprintsCounts.id = bitOnIndex;
            pubFingerprintsCounts.count = counts.get(bitOnIndex);
            pubFingerprintsCountsRepository.save(pubFingerprintsCounts);

        }




        System.out.println("done");

    }



}
