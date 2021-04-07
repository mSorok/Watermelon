package de.unijena.cheminf.watermelon.misc;


import de.unijena.cheminf.watermelon.mongomodel.WatermelonMolecule;
import de.unijena.cheminf.watermelon.mongomodel.WatermelonMoleculeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;



@Service
public class MoleculeCurator {

    @Autowired
    WatermelonMoleculeRepository watermelonMoleculeRepository;

    public void doWork(){

        List<WatermelonMolecule> watermelonMoleculeList = watermelonMoleculeRepository.findAll();

        ArrayList<WatermelonMolecule> moleculesToRemove = new ArrayList<>();

        for(WatermelonMolecule wm : watermelonMoleculeList){

            List<WatermelonMolecule> thisWmList = watermelonMoleculeRepository.findByInchikey(wm.inchikey);
            if(thisWmList.size()>1){
                //eliminate doubles
            }

            //check if in db more than one inchikey

            //check if AFCid is correct
            //TODO




            watermelonMoleculeRepository.save(wm);
        }



    }



    public String generateNewAfcId(){
        String newAfcid = "";



        return  newAfcid;
    }
}
