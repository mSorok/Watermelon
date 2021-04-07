package de.unijena.cheminf.watermelon.molecules;

import org.openscience.cdk.fingerprint.IBitFingerprint;
import org.openscience.cdk.interfaces.IAtomContainer;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Hashtable;

public class WMolecule {

    //private IAtomContainer molecule;

    public String compoundName = "";
    public HashSet<String> alternativeNames;
    public String iupacName = "";
    public String cas = "";
    public String kegg = "";
    public String hmdb = "";
    public String pubchem = "";
    public String chebi = "";
    public String foodb = "";
    public String lipidmaps = ""; //if "no entry found" - set to null
    public String molecularFormula = "";
    public Double molecularWeight;
    public ArrayList<String> sources;
    public String afc_id = "";

    private IAtomContainer atomContainer;



    public String original_smiles = "";
    public String inchi = "";
    public String inchikey = "";
    public String unique_smiles = "";


    public Integer contains_sugars;
    public boolean contains_ring_sugars;
    public boolean contains_linear_sugars;

    public Integer number_of_carbons;
    public Integer number_of_nitrogens;
    public Integer number_of_oxygens;
    public Integer number_of_rings;
    public Integer max_number_of_rings;
    public Integer min_number_of_rings;
    public Integer sugar_free_heavy_atom_number;
    public Integer sugar_free_total_atom_number;
    public Integer total_atom_number;
    public Integer bond_count;
    public String murko_framework;

    public Hashtable<String, Integer> ertlFunctionalFragments;
    public Hashtable<String, Integer> ertlFunctionalFragmentsPseudoSmiles;

    private IBitFingerprint extendedFingerprint;
    private IBitFingerprint pubchemFingerprint;


    // Molecular descriptors
    public Double alogp;
    public Double alogp2;
    public Double amralogp;
    public Double apol;
    public Double bpol;
    public Integer eccentricConnectivityIndexDescriptor;
    public Double fmfDescriptor;
    public Double fsp3;
    public Double fragmentComplexityDescriptor;
    public Integer numberSpiroAtoms;
    public Integer lipinskiRuleOf5Failures;
    public Double xlogp;
    public Double zagrebIndex;
    public Double topoPSA;
    public Double tpsaEfficiency;


    @Override
    public String toString() {
        return  compoundName + "\t" + iupacName + "\t" + cas + "\t" + kegg + "\t" +  hmdb + "\t" +  pubchem + "\t" + chebi + "\t" +  foodb + "\t" + lipidmaps + "\t"  + molecularFormula + "\t" + molecularWeight + "\t" + afc_id + "\t" + original_smiles + "\t"  +  inchikey + "\t" + unique_smiles + "\t" +contains_sugars+"\t" + contains_ring_sugars +"\t"+ contains_linear_sugars + "\t" + number_of_carbons + "\t" + number_of_nitrogens + "\t" + number_of_oxygens + "\t" + number_of_rings + "\t" + max_number_of_rings + "\t" + min_number_of_rings + "\t" + sugar_free_heavy_atom_number + "\t" + sugar_free_total_atom_number + "\t" + total_atom_number + "\t" + bond_count + "\t" + murko_framework +  "\t" + ertlFunctionalFragments + "\t" + ertlFunctionalFragmentsPseudoSmiles + "\t" + alogp + "\t" + alogp2 + "\t" + amralogp + "\t" + apol + "\t" + bpol + "\t" + eccentricConnectivityIndexDescriptor + "\t" + fmfDescriptor + "\t" + fsp3 + "\t" + fragmentComplexityDescriptor + "\t" + numberSpiroAtoms + "\t" + lipinskiRuleOf5Failures + "\t" + xlogp + "\t" + zagrebIndex + "\t" + topoPSA + "\t" + tpsaEfficiency;
    }

    public String getCompoundName() {
        return compoundName;
    }

    public void setCompoundName(String compoundName) {
        this.compoundName = compoundName;
    }

    public IAtomContainer getAtomContainer() {
        return atomContainer;
    }

    public void setAtomContainer(IAtomContainer atomContainer) {
        this.atomContainer = atomContainer;
    }

    public String getIupacName() {
        return iupacName;
    }

    public void setIupacName(String iupacName) {
        this.iupacName = iupacName;
    }

    public String getCas() {
        return cas;
    }

    public void setCas(String cas) {
        this.cas = cas;
    }

    public String getKegg() {
        return kegg;
    }

    public void setKegg(String kegg) {
        this.kegg = kegg;
    }

    public String getHmdb() {
        return hmdb;
    }

    public void setHmdb(String hmdb) {
        this.hmdb = hmdb;
    }

    public String getPubchem() {
        return pubchem;
    }

    public void setPubchem(String pubchem) {
        this.pubchem = pubchem;
    }

    public String getChebi() {
        return chebi;
    }

    public void setChebi(String chebi) {
        this.chebi = chebi;
    }

    public String getFoodb() {
        return foodb;
    }

    public void setFoodb(String foodb) {
        this.foodb = foodb;
    }

    public String getLipidmaps() {
        return lipidmaps;
    }

    public void setLipidmaps(String lipidmaps) {
        this.lipidmaps = lipidmaps;
    }

    public String getMolecularFormula() {
        return molecularFormula;
    }

    public void setMolecularFormula(String molecularFormula) {
        this.molecularFormula = molecularFormula;
    }

    public Double getMolecularWeight() {
        return molecularWeight;
    }

    public void setMolecularWeight(Double molecularWeight) {
        this.molecularWeight = molecularWeight;
    }

    public IBitFingerprint getPubchemFingerprint() {
        return pubchemFingerprint;
    }

    public void setPubchemFingerprint(IBitFingerprint pubchemFingerprint) {
        this.pubchemFingerprint = pubchemFingerprint;
    }

    public ArrayList<String> getSources() {
        return sources;
    }

    public void setSources(ArrayList<String> sources) {
        this.sources = sources;
    }

    public String getAfc_id() {
        return afc_id;
    }

    public void setAfc_id(String afc_id) {
        this.afc_id = afc_id;
    }



    public void setAlternativeNames(HashSet<String> alternativeNames) {
        this.alternativeNames = alternativeNames;
    }

    public String getOriginal_smiles() {
        return original_smiles;
    }

    public void setOriginal_smiles(String original_smiles) {
        this.original_smiles = original_smiles;
    }

    public String getInchi() {
        return inchi;
    }

    public void setInchi(String inchi) {
        this.inchi = inchi;
    }

    public String getInchikey() {
        return inchikey;
    }

    public void setInchikey(String inchikey) {
        this.inchikey = inchikey;
    }

    public String getUnique_smiles() {
        return unique_smiles;
    }

    public void setUnique_smiles(String unique_smiles) {
        this.unique_smiles = unique_smiles;
    }

    public boolean isContains_ring_sugars() {
        return contains_ring_sugars;
    }

    public void setContains_ring_sugars(boolean contains_ring_sugars) {
        this.contains_ring_sugars = contains_ring_sugars;
    }

    public boolean isContains_linear_sugars() {
        return contains_linear_sugars;
    }

    public void setContains_linear_sugars(boolean contains_linear_sugars) {
        this.contains_linear_sugars = contains_linear_sugars;
    }

    public Integer getNumber_of_carbons() {
        return number_of_carbons;
    }

    public void setNumber_of_carbons(Integer number_of_carbons) {
        this.number_of_carbons = number_of_carbons;
    }

    public Integer getNumber_of_nitrogens() {
        return number_of_nitrogens;
    }

    public void setNumber_of_nitrogens(Integer number_of_nitrogens) {
        this.number_of_nitrogens = number_of_nitrogens;
    }

    public Integer getNumber_of_oxygens() {
        return number_of_oxygens;
    }

    public void setNumber_of_oxygens(Integer number_of_oxygens) {
        this.number_of_oxygens = number_of_oxygens;
    }

    public Integer getNumber_of_rings() {
        return number_of_rings;
    }

    public void setNumber_of_rings(Integer number_of_rings) {
        this.number_of_rings = number_of_rings;
    }

    public Integer getMax_number_of_rings() {
        return max_number_of_rings;
    }

    public void setMax_number_of_rings(Integer max_number_of_rings) {
        this.max_number_of_rings = max_number_of_rings;
    }

    public Integer getMin_number_of_rings() {
        return min_number_of_rings;
    }

    public void setMin_number_of_rings(Integer min_number_of_rings) {
        this.min_number_of_rings = min_number_of_rings;
    }

    public Integer getSugar_free_heavy_atom_number() {
        return sugar_free_heavy_atom_number;
    }

    public void setSugar_free_heavy_atom_number(Integer sugar_free_heavy_atom_number) {
        this.sugar_free_heavy_atom_number = sugar_free_heavy_atom_number;
    }

    public Integer getSugar_free_total_atom_number() {
        return sugar_free_total_atom_number;
    }

    public void setSugar_free_total_atom_number(Integer sugar_free_total_atom_number) {
        this.sugar_free_total_atom_number = sugar_free_total_atom_number;
    }

    public Integer getTotal_atom_number() {
        return total_atom_number;
    }

    public void setTotal_atom_number(Integer total_atom_number) {
        this.total_atom_number = total_atom_number;
    }

    public Integer getBond_count() {
        return bond_count;
    }

    public void setBond_count(Integer bond_count) {
        this.bond_count = bond_count;
    }

    public String getMurko_framework() {
        return murko_framework;
    }

    public void setMurko_framework(String murko_framework) {
        this.murko_framework = murko_framework;
    }

    public Hashtable<String, Integer> getErtlFunctionalFragments() {
        return ertlFunctionalFragments;
    }

    public void setErtlFunctionalFragments(Hashtable<String, Integer> ertlFunctionalFragments) {
        this.ertlFunctionalFragments = ertlFunctionalFragments;
    }

    public Hashtable<String, Integer> getErtlFunctionalFragmentsPseudoSmiles() {
        return ertlFunctionalFragmentsPseudoSmiles;
    }

    public void setErtlFunctionalFragmentsPseudoSmiles(Hashtable<String, Integer> ertlFunctionalFragmentsPseudoSmiles) {
        this.ertlFunctionalFragmentsPseudoSmiles = ertlFunctionalFragmentsPseudoSmiles;
    }

    public IBitFingerprint getExtendedFingerprint() {
        return extendedFingerprint;
    }

    public void setExtendedFingerprint(IBitFingerprint extendedFingerprint) {
        this.extendedFingerprint = extendedFingerprint;
    }

    public Double getAlogp() {
        return alogp;
    }

    public void setAlogp(Double alogp) {
        this.alogp = alogp;
    }

    public Double getAlogp2() {
        return alogp2;
    }

    public void setAlogp2(Double alogp2) {
        this.alogp2 = alogp2;
    }

    public Double getAmralogp() {
        return amralogp;
    }

    public void setAmralogp(Double amralogp) {
        this.amralogp = amralogp;
    }

    public Double getApol() {
        return apol;
    }

    public void setApol(Double apol) {
        this.apol = apol;
    }

    public Double getBpol() {
        return bpol;
    }

    public void setBpol(Double bpol) {
        this.bpol = bpol;
    }

    public Integer getEccentricConnectivityIndexDescriptor() {
        return eccentricConnectivityIndexDescriptor;
    }

    public void setEccentricConnectivityIndexDescriptor(Integer eccentricConnectivityIndexDescriptor) {
        this.eccentricConnectivityIndexDescriptor = eccentricConnectivityIndexDescriptor;
    }

    public Double getFmfDescriptor() {
        return fmfDescriptor;
    }

    public void setFmfDescriptor(Double fmfDescriptor) {
        this.fmfDescriptor = fmfDescriptor;
    }

    public Double getFsp3() {
        return fsp3;
    }

    public void setFsp3(Double fsp3) {
        this.fsp3 = fsp3;
    }

    public Double getFragmentComplexityDescriptor() {
        return fragmentComplexityDescriptor;
    }

    public void setFragmentComplexityDescriptor(Double fragmentComplexityDescriptor) {
        this.fragmentComplexityDescriptor = fragmentComplexityDescriptor;
    }

    public Integer getNumberSpiroAtoms() {
        return numberSpiroAtoms;
    }

    public void setNumberSpiroAtoms(Integer numberSpiroAtoms) {
        this.numberSpiroAtoms = numberSpiroAtoms;
    }

    public Integer getLipinskiRuleOf5Failures() {
        return lipinskiRuleOf5Failures;
    }

    public void setLipinskiRuleOf5Failures(Integer lipinskiRuleOf5Failures) {
        this.lipinskiRuleOf5Failures = lipinskiRuleOf5Failures;
    }

    public Double getXlogp() {
        return xlogp;
    }

    public void setXlogp(Double xlogp) {
        this.xlogp = xlogp;
    }

    public Double getZagrebIndex() {
        return zagrebIndex;
    }

    public void setZagrebIndex(Double zagrebIndex) {
        this.zagrebIndex = zagrebIndex;
    }

    public Double getTopoPSA() {
        return topoPSA;
    }

    public void setTopoPSA(Double topoPSA) {
        this.topoPSA = topoPSA;
    }

    public Double getTpsaEfficiency() {
        return tpsaEfficiency;
    }

    public void setTpsaEfficiency(Double tpsaEfficiency) {
        this.tpsaEfficiency = tpsaEfficiency;
    }

    public HashSet<String> getAlternativeNames() {
        return alternativeNames;
    }

    public Integer getContains_sugars() {
        return contains_sugars;
    }

    public void setContains_sugars(Integer contains_sugars) {
        this.contains_sugars = contains_sugars;
    }
}
