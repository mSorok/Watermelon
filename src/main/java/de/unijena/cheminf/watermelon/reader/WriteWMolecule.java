package de.unijena.cheminf.watermelon.reader;

import de.unijena.cheminf.watermelon.molecules.WMolecule;
import de.unijena.cheminf.watermelon.mongomodel.WatermelonMolecule;
import de.unijena.cheminf.watermelon.mongomodel.WatermelonMoleculeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Service
public class WriteWMolecule {


    @Autowired
    WatermelonMoleculeRepository watermelonMoleculeRepository;

    public void writeFromDatabase(File file){

        List<WatermelonMolecule> allMolecules = watermelonMoleculeRepository.findAll();


        String header =   "compoundName\tiupacName\tcas\tkegg\thmdb\tpubchem\tchebi\tfoodb\tlipidmaps\tmolecularFormula\tmolecularWeight\tafc_id\tinchikey\tsmiles\tinchi";

        try {
            FileWriter writer = new FileWriter(file);
            writer.write(header+ System.lineSeparator());

            for(WatermelonMolecule mol : allMolecules){
                String line = mol.compoundName+"\t"+mol.iupacName+"\t"+mol.cas+"\t"+mol.kegg+"\t"+mol.hmdb+"\t"+mol.pubchem+"\t"+mol.chebi+"\t"+mol.foodb+"\t"+mol.lipidmaps+"\t"+mol.molecular_formula+"\t"+mol.molecular_weight+"\t"+mol.afc_id+"\t"+mol.inchikey+"\t"+mol.absolute_smiles+"\t"+mol.inchi;
                writer.write(line+System.lineSeparator());
            }


            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }


    }


    public void writeLongFromDatabase(File file){

        List<WatermelonMolecule> allMolecules = watermelonMoleculeRepository.findAll();


        String header =   "compoundName\tiupacName\tcas\tkegg\thmdb\tpubchem\tchebi\tfoodb\tlipidmaps\tmolecularFormula\tmolecularWeight\tafc_id\tinchikey\tsmiles\tinchi\talogp\tapol\teccentricConnectivityIndexDescriptor\tfsp3\tzagrebIndex\ttopoPSA\ttpsaEfficiency\tlipinskiRuleOf5Failures\tkappaShapeIndex1";

        try {
            FileWriter writer = new FileWriter(file);
            writer.write(header+ System.lineSeparator());

            for(WatermelonMolecule mol : allMolecules){
                String line = mol.compoundName+"\t"+mol.iupacName+"\t"+mol.cas+"\t"+mol.kegg+"\t"+mol.hmdb+"\t"+mol.pubchem+"\t"+mol.chebi+"\t"+mol.foodb+"\t"+mol.lipidmaps+"\t"+mol.molecular_formula+"\t"+mol.molecular_weight+"\t"+mol.afc_id+"\t"+mol.inchikey+"\t"+mol.absolute_smiles+"\t"+mol.inchi+"\t"+mol.alogp+"\t"+mol.apol+"\t"+mol.eccentricConnectivityIndexDescriptor+"\t"+mol.fsp3+"\t"+mol.zagrebIndex+"\t"+mol.topoPSA+"\t"+mol.tpsaEfficiency+"\t"+mol.lipinskiRuleOf5Failures+"\t"+mol.kappaShapeIndex1;
                writer.write(line+System.lineSeparator());
            }


            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }


    }


    public void writeAllToFile(ArrayList<WMolecule> moleculeList, File file){



        String header =   "compoundName\tiupacName\tcas\tkegg\thmdb\tpubchem\tchebi\tfoodb\tlipidmaps\tmolecularFormula\tmolecularWeight\tafc_id\toriginal_smiles\tinchikey\tCanonical_Smiles\tcontains_sugars\tcontains_ring_sugars\tcontains_linear_sugars\tnumber_of_carbons\tnumber_of_nitrogens\tnumber_of_oxygens\tnumber_of_rings\tmax_number_of_rings\tmin_number_of_rings\tsugar_free_heavy_atom_number\tsugar_free_total_atom_number\ttotal_atom_number\tbond_count\tmurko_framework\tertlFunctionalFragments\tertlFunctionalFragmentsPseudoSmiles\talogp\talogp2\tamralogp\tapol\tbpol\teccentricConnectivityIndexDescriptor\tfmfDescriptor\tfsp3\tfragmentComplexityDescriptor\tnumberSpiroAtoms\tlipinskiRuleOf5Failures\txlogp\tzagrebIndex\ttopoPSA\ttpsaEfficiency";


        try {
            FileWriter writer = new FileWriter(file);
            writer.write(header+ System.lineSeparator());

            for(WMolecule mol : moleculeList){
                writer.write(mol.toString()+System.lineSeparator());
            }


            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public void writeSimple(ArrayList<WMolecule> moleculeList, File file){
        try {
            FileWriter writer = new FileWriter(file);

            for(WMolecule mol : moleculeList){
                writer.write(mol.getUnique_smiles()+" "+mol.getAfc_id()+System.lineSeparator());
            }


            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public void writeSmiles(ArrayList<WMolecule> moleculeList, File file){

        try {
            FileWriter writer = new FileWriter(file);

            for(WMolecule mol : moleculeList){
                writer.write(mol.getUnique_smiles()+System.lineSeparator());
            }


            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

}
