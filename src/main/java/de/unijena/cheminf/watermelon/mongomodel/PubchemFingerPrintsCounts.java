package de.unijena.cheminf.watermelon.mongomodel;

import java.util.ArrayList;

public class PubchemFingerPrintsCounts{

    public Integer count;
    public ArrayList<Integer> bits;

    public PubchemFingerPrintsCounts(Integer count, ArrayList<Integer> bits) {
        this.count = count;
        this.bits = bits;
    }
}
