package de.unijena.cheminf.watermelon.mongomodel;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import java.util.List;

public interface WatermelonMoleculeRepository extends MongoRepository<WatermelonMolecule, String>  {

    @Query("{ afc_id : ?0}")
    public List<WatermelonMolecule> findByAfc_id(String afc_id);

    List<WatermelonMolecule> findByInchikey(String inchikey);

    List<WatermelonMolecule> findByInchi(String inchi);

    @Query("{ $or: [ {chemicalTaxonomyNPclassifierPathway: ?0}, {chemicalTaxonomyNPclassifierSuperclass: ?0}, {chemicalTaxonomyNPclassifierClass: ?0} ] }")
    List<WatermelonMolecule> findByChemclass(String query);


}
