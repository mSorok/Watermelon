package de.unijena.cheminf.watermelononline.mongocollections;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import java.util.List;

@RepositoryRestResource(path="compound")
public interface WatermelonMoleculeRepository extends MongoRepository<WatermelonMolecule, String>, WatermelonMoleculeRepositoryCustom {

    @Query("{ afc_id : ?0}")
    public List<WatermelonMolecule> findByAfc_id(String afc_id);


    @Query("{unique_smiles : ?0}")
    public List<WatermelonMolecule> findByUnique_Smiles(String smiles);

    @Query("{absolute_smiles : ?0}")
    public List<WatermelonMolecule> findByAbsolute_smiles(String smiles);

    @Query("{original_smiles : ?0}")
    public List<WatermelonMolecule> findByOriginal_smiles(String smiles);

    public List<WatermelonMolecule> findByInchi(String inchi);

    public List<WatermelonMolecule> findByInchikey(String inchikey);


    @Query("{molecular_formula : ?0}")
    public List<WatermelonMolecule> findByMolecular_formula(String molecular_formula);


    public List<WatermelonMolecule> findByName(String name);


    @Query("{molecular_weight : ?0}")
    public List<WatermelonMolecule> findByMolecular_weight(String weight);


    @Query("{ $text: { $search: ?0 } }")
    public List<WatermelonMolecule> fuzzyNameSearch(String name);


    @Query("{ npl_noh_score: { $exists:false } }")
    List<WatermelonMolecule> findAllByNPLScoreComputed();

    @Query("{ apol: { $exists:false } }")
    List<WatermelonMolecule> findAllByApolComputed();

    @Query(value="{ pubchemBits : { $bitsAllSet : ?0  }}", fields = "{ _id:0,afc_id: 1, unique_smiles:1, absolute_smiles:1, molecular_formula:1, molecular_weight:1, npl_score:1 , name:1,  total_atom_number:1}" )
    List<WatermelonMolecule> findAllPubchemBitsSet(byte[] querybits) ;
    //  'coconut_id': 1, 'unique_smiles':1, 'clean_smiles':1, 'molecular_formula':1, 'molecular_weight':1, 'npl_score':1 , 'name':1, 'smiles':1



}
