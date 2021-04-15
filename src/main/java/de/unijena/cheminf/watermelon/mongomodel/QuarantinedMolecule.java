package de.unijena.cheminf.watermelon.mongomodel;

import org.springframework.data.annotation.Id;

public class QuarantinedMolecule {

    @Id
    String id;


    WatermelonMolecule qwm;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public WatermelonMolecule getQwm() {
        return qwm;
    }

    public void setQwm(WatermelonMolecule qwm) {
        this.qwm = qwm;
    }
}
