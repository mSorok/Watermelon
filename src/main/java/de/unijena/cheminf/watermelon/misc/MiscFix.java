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


        //AFC001194
        wm  = watermelonMoleculeRepository.findByAfc_id("AFC001194").get(0) ;
        wm.compoundName = "beta-bisabolol";
        watermelonMoleculeRepository.save(wm);

        //AFC001749
        //cas: XXX; kegg: XXX; hmdb: XXX; pubchem: 46878391; chebi: 57530; foodb: XXX; lipidmaps: XXX
        wm  = watermelonMoleculeRepository.findByAfc_id("AFC001749").get(0) ;
        wm.chebi = "57530";
        wm.pubchem = "46878391";
        watermelonMoleculeRepository.save(wm);


        //AFC001894
        // (+)-catechin <- to alternative names
        wm  = watermelonMoleculeRepository.findByAfc_id("AFC001894").get(0) ;
        wm.alternativeNames.add("(+)-catechin");
        watermelonMoleculeRepository.save(wm);


        //AFC001643
        //cas: 14364-08-0; kegg: C10433; hmdb: HMDB0036937; pubchem: 5281761; chebi: 614; foodb: FDB015906; lipidmaps: XXX
        wm  = watermelonMoleculeRepository.findByAfc_id("AFC001643").get(0) ;
        wm.foodb = "FDB015906";
        wm.chebi = "614";
        wm.pubchem = "5281761";
        wm.hmdb = "HMDB0036937";
        wm.kegg="C10433";
        wm.cas = "14364-08-0";
        watermelonMoleculeRepository.save(wm);

        //AFC001645
        //cas: 29741-07-9; kegg: XXX; hmdb: XXX; pubchem: 14630703; chebi: XXX; foodb: FDB021689; lipidmaps: XXX
        wm  = watermelonMoleculeRepository.findByAfc_id("AFC001645").get(0) ;
        wm.foodb = "FDB021689";
        wm.pubchem = "14630703";
        wm.cas = "29741-07-9";
        watermelonMoleculeRepository.save(wm);



        //AFC001646
        //cas: XXX; kegg: XXX; hmdb: XXX; pubchem: 14406834; chebi: XXX; foodb: XXX; lipidmaps: XXX
        wm  = watermelonMoleculeRepository.findByAfc_id("AFC001646").get(0) ;
        wm.pubchem = "14406834";
        watermelonMoleculeRepository.save(wm);

        //AFC001658
        //cas: XXX; kegg: XXX; hmdb: XXX; pubchem: 13889681; chebi: XXX; foodb: XXX; lipidmaps: XXX
        wm  = watermelonMoleculeRepository.findByAfc_id("AFC001658").get(0) ;
        wm.pubchem = "13889681";
        watermelonMoleculeRepository.save(wm);

        //AFC001703
        //cas: 38784-79-1; kegg: XXX; hmdb: HMDB0037426; pubchem: 57390614; chebi: 68881; foodb: XXX; lipidmaps: LMPK12111672
        wm  = watermelonMoleculeRepository.findByAfc_id("AFC001703").get(0) ;
        wm.lipidmaps = "LMPK12111672";
        wm.chebi = "68881";
        wm.pubchem = "57390614";
        wm.hmdb = "HMDB0037426";
        wm.cas = "38784-79-1";
        watermelonMoleculeRepository.save(wm);

        //AFC001704
        //cas: 2392-95-2; kegg: XXX; hmdb: XXX; pubchem: 21606527; chebi: 68882; foodb: FDB016490; lipidmaps: LMPK12111740
        wm  = watermelonMoleculeRepository.findByAfc_id("AFC001704").get(0) ;
        wm.lipidmaps = "LMPK12111740";
        wm.foodb = "FDB016490";
        wm.chebi = "68882";
        wm.pubchem = "21606527";
        wm.cas = "2392-95-2";
        watermelonMoleculeRepository.save(wm);

        //AFC001710
        //cas: 5154-41-6; kegg: XXX; hmdb: XXX; pubchem: 12309350; chebi: XXX; foodb: XXX; lipidmaps: LMPK12110649
        wm  = watermelonMoleculeRepository.findByAfc_id("AFC001710").get(0) ;
        wm.lipidmaps = "LMPK12110649";
        wm.pubchem = "12309350";
        wm.cas = "5154-41-6";
        watermelonMoleculeRepository.save(wm);

        //AFC001756
        //cas: 14292-40-1; kegg: XXX; hmdb: XXX; pubchem: 14282775; chebi: XXX; foodb: XXX; lipidmaps: XXX
        wm  = watermelonMoleculeRepository.findByAfc_id("AFC001756").get(0) ;
        wm.pubchem = "14282775";
        wm.cas = "14292-40-1";
        watermelonMoleculeRepository.save(wm);


        //AFC001210
        //cas: 61276-17-3; kegg: C10501; hmdb: HMDB0034843; pubchem: 5281800; chebi: 132853; foodb: FDB013409; lipidmaps: XXX
        wm  = watermelonMoleculeRepository.findByAfc_id("AFC001210").get(0) ;
        wm.foodb = "FDB013409";
        wm.chebi = "132853";
        wm.pubchem = "5281800";
        wm.hmdb = "HMDB0034843";
        wm.kegg="C10501";
        wm.cas = "61276-17-3";
        watermelonMoleculeRepository.save(wm);




    }
}
