package de.unijena.cheminf.watermelon.misc;


import org.openscience.cdk.interfaces.IAtomContainer;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

@Service
public class MoleculesUnifier {

    public ArrayList<IAtomContainer> joinTwoListsOfAtomContainers(ArrayList<IAtomContainer> list1, ArrayList<IAtomContainer> list2){

        ArrayList<IAtomContainer> fullList = new ArrayList<>();

        for(IAtomContainer ac2 : list2){
            ac2.setProperty("EXCLUDE", "NO");
        }

        for(IAtomContainer ac1 : list1){
            fullList.add(ac1);
            for(IAtomContainer ac2: list2){
                if(ac1.getProperty("INCHIKEY").equals(ac2.getProperty("INCHIKEY"))){
                    //add "EXCLUDE" to properties in ac2
                    ac2.setProperty("EXCLUDE", "YES");
                    //update ac1 properties
                    ac1.setProperty("source", ac1.getProperty("source") +";"+  ac2.getProperty("source"));
                    ac1.setProperty("iupac", ac2.getProperty("iupac"));
                }
            }
        }

        for(IAtomContainer ac2 : list2){
            if(ac2.getProperty("EXCLUDE").equals("NO")){
                fullList.add(ac2);
            }
        }



        return(fullList);

    }

}
