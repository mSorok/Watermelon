package de.unijena.cheminf.watermelononline.mongocollections;

import de.unijena.cheminf.watermelononline.model.AdvancedSearchModel;

import java.util.ArrayList;
import java.util.List;

public interface WatermelonMoleculeRepositoryCustom {


    List<WatermelonMolecule> advancedSearchWithCriteria (AdvancedSearchModel criterias, Integer maxResults);

    List<WatermelonMolecule> similaritySearch(ArrayList<Integer> reqbits, ArrayList<Integer> qfp, Integer qmin, Integer qmax, Integer qn, Double threshold, Integer maxResults );

    List<WatermelonMolecule> minMaxMolecularWeightSearch(Double minMolecularWeight, Double maxMolecularWeight, Integer maxResults);

    List<WatermelonMolecule> minMolecularWeightSearch(Double minMolecularWeight, Integer maxResults);

    List<WatermelonMolecule> maxMolecularWeightSearch(Double maxMolecularWeight, Integer maxResults);
}
