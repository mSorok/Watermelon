package de.unijena.cheminf.watermelon.mongomodel;

import org.springframework.data.mongodb.repository.MongoRepository;

public interface QuarantinedMoleculeRepository extends MongoRepository<QuarantinedMolecule, String> {
}
