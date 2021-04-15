package de.unijena.cheminf.watermelon.misc;


import de.unijena.cheminf.watermelon.mongomodel.WatermelonMolecule;
import de.unijena.cheminf.watermelon.mongomodel.WatermelonMoleculeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class MiscFix {

    @Autowired
    WatermelonMoleculeRepository watermelonMoleculeRepository;

    public void aprilFix(){

        //delete two entries
        // AFC001940 and AFC001941

        WatermelonMolecule wm  = watermelonMoleculeRepository.findByAfc_id("AFC001940").get(0) ;
        watermelonMoleculeRepository.delete(wm);
        wm  = watermelonMoleculeRepository.findByAfc_id("AFC001941").get(0) ;
        watermelonMoleculeRepository.delete(wm);

        // fix entries

        //AFC000928
        wm  = watermelonMoleculeRepository.findByAfc_id("AFC000928").get(0) ;
        wm.lipidmaps = "";
        watermelonMoleculeRepository.save(wm);

        //AFC001194
        //cas:15352-77-9; kegg: XXX; hmdb: HMDB0036111; pubchem: 27208; chebi: XXX; foodb: FDB014957; lipidmaps: XXX; afc_id: AFC001194
        wm  = watermelonMoleculeRepository.findByAfc_id("AFC001194").get(0) ;
        wm.lipidmaps = "";
        wm.foodb = "FDB014957";
        wm.chebi = "";
        wm.pubchem = "27208";
        wm.hmdb = "HMDB0036111";
        wm.kegg="";
        wm.cas = "15352-77-9";
        watermelonMoleculeRepository.save(wm);

        //AFC001937
        //cas:506-17-2; kegg: C21944; hmdb: HMDB0240219; pubchem: 5282761; chebi: 50464; foodb: FDB002952; lipidmaps: LMFA01030076; afc_id: AFC001937
        wm  = watermelonMoleculeRepository.findByAfc_id("AFC001937").get(0) ;
        wm.lipidmaps = "LMFA01030076";
        wm.foodb = "FDB002952";
        wm.chebi = "50464";
        wm.pubchem = "5282761";
        wm.hmdb = "HMDB0240219";
        wm.kegg="C21944";
        wm.cas = "506-17-2";
        watermelonMoleculeRepository.save(wm);

        //AFC001938
        //cas: 481-19-6; kegg: XXX; hmdb: HMDB0033825; pubchem: 131751494; chebi: 166889; foodb: FDB011990; lipidmaps: LMST01040203; afc_id: AFC001938
        wm  = watermelonMoleculeRepository.findByAfc_id("AFC001938").get(0) ;
        wm.lipidmaps = "LMST01040203";
        wm.foodb = "FDB011990";
        wm.chebi = "166889";
        wm.pubchem = "131751494";
        wm.hmdb = "HMDB0033825";
        wm.kegg="";
        wm.cas = "481-19-6";
        watermelonMoleculeRepository.save(wm);

        //AFC001939
        //cas: 1981-50-6; kegg: XXX; hmdb: HMDB0062437; pubchem: 5282748; chebi: 84328; foodb: XXX; lipidmaps: LMFA01030060; afc_id: AFC001939
        wm  = watermelonMoleculeRepository.findByAfc_id("AFC001939").get(0) ;
        wm.lipidmaps = "LMFA01030060";
        wm.foodb = "";
        wm.chebi = "84328";
        wm.pubchem = "5282748";
        wm.hmdb = "HMDB0062437";
        wm.kegg="";
        wm.cas = "1981-50-6";
        watermelonMoleculeRepository.save(wm);



    }
}
