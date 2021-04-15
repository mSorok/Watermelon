package de.unijena.cheminf.watermelon.misc;


import de.unijena.cheminf.watermelon.mongomodel.WatermelonMolecule;
import de.unijena.cheminf.watermelon.mongomodel.WatermelonMoleculeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.thymeleaf.util.StringUtils;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

@Service
public class FixIdentifiers {



    @Autowired
    WatermelonMoleculeRepository watermelonMoleculeRepository;


    public String prefix= "AFC";


    public void clearIDs(){

        System.out.println("clearing coconut ids");

        List<WatermelonMolecule> allunp = watermelonMoleculeRepository.findAll();

        for(WatermelonMolecule unp : allunp){

            unp.setAfc_id("");

            watermelonMoleculeRepository.save(unp);


        }

        System.out.println("done");

    }

    public void importIDs(String filename) {

        try {
            BufferedReader bufferedReader = new BufferedReader(new FileReader( new File(filename)));


            int count = 1;
            String line;


            while ((line = bufferedReader.readLine()) != null){
                if(!line.startsWith("afc_id")) {
                    //ArrayList<String> dataline = new ArrayList<String>(Arrays.asList(line.split(","))); //coconut_id = 0, inchikey = 1
                    String[] dataTab = line.split(",") ;

                    List<WatermelonMolecule> unplist = watermelonMoleculeRepository.findByInchikey(dataTab[1]);


                    if (!unplist.isEmpty()) {
                        for (WatermelonMolecule unp : unplist) {
                            unp.setAfc_id(dataTab[0]);
                            watermelonMoleculeRepository.save(unp);
                        }

                    } else {
                        System.out.println("BAD! Could not find " + dataTab[0] + " in the new version of WATERMELON!");
                    }


                }
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }



    public void createDeNovoIDs(String prefix){

        this.prefix = prefix;

        List<WatermelonMolecule> allunp = watermelonMoleculeRepository.findAll();

        int count = 1;
        for(WatermelonMolecule unp : allunp){


            String afc_id = prefix + StringUtils.repeat("0", 6-StringUtils.length(count)) + count;

            unp.setAfc_id(afc_id);

            watermelonMoleculeRepository.save(unp);

            count++;

        }

    }


    public void createDeNovoIDs(){

        List<WatermelonMolecule> allunp = watermelonMoleculeRepository.findAll();

        int count = 1;
        for(WatermelonMolecule unp : allunp){


            String afc_id = prefix + StringUtils.repeat("0", 6-StringUtils.length(count)) + count;

            unp.setAfc_id(afc_id);

            watermelonMoleculeRepository.save(unp);

            count++;

        }

    }

    public void createIDforNewMolecules(){

        int max_id = 0;
        List<WatermelonMolecule> allnp = watermelonMoleculeRepository.findAll();

        ArrayList<WatermelonMolecule> unpWithoutId = new ArrayList<>();

        for(WatermelonMolecule np : allnp){

            if(np.afc_id.equals("") || np.afc_id.isEmpty() || np.afc_id.isBlank() || np.afc_id.contains(".")){
                unpWithoutId.add(np);

            }else if(np.afc_id.startsWith("AFC") ){
                int afc_tmp = Integer.parseInt( np.getAfc_id().split("FC")[1] );
                if(afc_tmp>max_id ){
                    max_id = afc_tmp;
                }
            }

        }


        max_id+=1;
        for(WatermelonMolecule ildnp : unpWithoutId){
            String afc_id = prefix + StringUtils.repeat("0", 6-StringUtils.length(max_id)) + max_id;
            ildnp.setAfc_id(afc_id);
            System.out.println("New AFC id: "+afc_id);
            watermelonMoleculeRepository.save(ildnp);
            max_id+=1;
        }

    }

}
