package de.unijena.cheminf.watermelon.reader;


import de.unijena.cheminf.watermelon.mongomodel.CompoundLevels;
import de.unijena.cheminf.watermelon.mongomodel.WatermelonMolecule;
import de.unijena.cheminf.watermelon.mongomodel.WatermelonMoleculeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.w3c.dom.stylesheets.LinkStyle;

import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Service
public class ReadNutrientLevels {

    @Autowired
    WatermelonMoleculeRepository watermelonMoleculeRepository;


    public void readLevelsTable(File file){
        System.out.println("Reading compound levels from file "+file.getName());

        BufferedReader bufferedReader = null;
        try {
            bufferedReader = new BufferedReader(new FileReader(file));


        ArrayList<String> header = new ArrayList<String>(Arrays.asList(bufferedReader.readLine().split("\t")));

            if (header != null){
                Integer indexOfSource = null;
                Integer indexOfAfc = null;
                Integer indexOfComment = null;
                Integer indexOfSourceOrder = null;
                Integer indexOfPlantPart = null;
                Integer indexOfAmountLow = null;
                Integer indexOfAmountHigh = null;
                Integer indexOfPlusMinus = null;
                Integer indexOfUnits = null;

                //Compound	Alternate names	AFC_ID	plant_part	amount_low	amount_high	plus_minus	units	Comment	Source	SourceOrder

                for (String item : header) {

                    if (item.toLowerCase().contains("afc_id")){ indexOfAfc = header.indexOf(item); }
                    if (item.toLowerCase().contains("plant_part")){ indexOfPlantPart = header.indexOf(item); }
                    if (item.toLowerCase().contains("amount_low")){ indexOfAmountLow = header.indexOf(item); }
                    if (item.toLowerCase().contains("amount_high")){ indexOfAmountHigh = header.indexOf(item); }
                    if (item.toLowerCase().contains("plus_minus")){ indexOfPlusMinus = header.indexOf(item); }
                    if (item.toLowerCase().contains("units")){ indexOfUnits = header.indexOf(item); }
                    if (item.toLowerCase().contains("comment")){ indexOfComment = header.indexOf(item); }
                    if (item.toLowerCase().contains("sourceorder")){ indexOfSourceOrder = header.indexOf(item); }
                    if (item.toLowerCase().contains("source") && !item.toLowerCase().contains("sourceorder")){ indexOfSource = header.indexOf(item); }
                }
                System.out.println(header);


                //read the rest of the file
                int count = 1;
                String line;

                while ((line = bufferedReader.readLine()) != null && count <= 600000) {

                    ArrayList<String> dataline = new ArrayList<String>(Arrays.asList(line.split("\t")));

                    if( dataline.size()>=Math.max(indexOfAmountLow+1,indexOfAmountHigh+1 ) && !dataline.get(indexOfAfc).equals("") && ( !dataline.get(indexOfAmountLow).equals("") || !dataline.get(indexOfAmountHigh).equals("") ) && ( !dataline.get(indexOfAmountLow).equals("NR") || !dataline.get(indexOfAmountHigh).equals("NR") )){

                        String afc_id = dataline.get(indexOfAfc);

                        //create new CompoundLevelsObject
                        CompoundLevels compoundLevels = new CompoundLevels();
                        if(!dataline.get(indexOfAmountLow).equals("") && !dataline.get(indexOfAmountLow).equals("NR")){
                            compoundLevels.setMin_value(Double.parseDouble(dataline.get(indexOfAmountLow)));
                        }
                        if(!dataline.get(indexOfAmountHigh).equals("") && !dataline.get(indexOfAmountHigh).equals("NR")){
                            compoundLevels.setMax_value(Double.parseDouble(dataline.get(indexOfAmountHigh)));
                        }
                        if(!dataline.get(indexOfPlusMinus).equals("") && !dataline.get(indexOfPlusMinus).equals("NR") ){
                            compoundLevels.setPlus_minus(Double.parseDouble(dataline.get(indexOfPlusMinus)));
                        }
                        if(!dataline.get(indexOfUnits).equals("") && !dataline.get(indexOfUnits).equals("NR")){
                            compoundLevels.setUnit(dataline.get(indexOfUnits));
                        }
                        if(!dataline.get(indexOfPlantPart).equals("")){
                            compoundLevels.setPlant_part(dataline.get(indexOfPlantPart));
                        }
                        if(!dataline.get(indexOfSource).equals("")){
                            compoundLevels.setSource(dataline.get(indexOfSource));
                        }
                        if(!dataline.get(indexOfComment).equals("")){
                            compoundLevels.setComment(dataline.get(indexOfComment));
                        }

                        List<WatermelonMolecule> lwm = watermelonMoleculeRepository.findByAfc_id(afc_id);
                        if(lwm.size()>0) {
                            WatermelonMolecule wm = watermelonMoleculeRepository.findByAfc_id(afc_id).get(0);
                            wm.compoundLevels.add(compoundLevels);

                            watermelonMoleculeRepository.save(wm);
                        }

                    }
                }
            }

        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
