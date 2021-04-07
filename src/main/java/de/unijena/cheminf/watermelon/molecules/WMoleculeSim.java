package de.unijena.cheminf.watermelon.molecules;

public class WMoleculeSim {

    WMolecule a;
    WMolecule b;
    Double tanomoto_extended;
    Double tanomoto_pubchem;

    public WMolecule getA() {
        return a;
    }

    public void setA(WMolecule a) {
        this.a = a;
    }

    public WMolecule getB() {
        return b;
    }

    public void setB(WMolecule b) {
        this.b = b;
    }

    public Double getTanomoto_extended() {
        return tanomoto_extended;
    }

    public void setTanomoto_extended(Double tanomoto_extended) {
        this.tanomoto_extended = tanomoto_extended;
    }

    public Double getTanomoto_pubchem() {
        return tanomoto_pubchem;
    }

    public void setTanomoto_pubchem(Double tanomoto_pubchem) {
        this.tanomoto_pubchem = tanomoto_pubchem;
    }
}
