package de.unijena.cheminf.watermelon.mongomodel;

public class CompoundLevels {

    String unit;
    Double min_value;
    Double max_value;
    Double plus_minus;
    String plant_part;
    String source;
    String sourceOrder;
    String comment;


    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    public Double getMin_value() {
        return min_value;
    }

    public void setMin_value(Double min_value) {
        this.min_value = min_value;
    }

    public Double getMax_value() {
        return max_value;
    }

    public void setMax_value(Double max_value) {
        this.max_value = max_value;
    }

    public Double getPlus_minus() {
        return plus_minus;
    }

    public void setPlus_minus(Double plus_minus) {
        this.plus_minus = plus_minus;
    }

    public String getPlant_part() {
        return plant_part;
    }

    public void setPlant_part(String plant_part) {
        this.plant_part = plant_part;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public String getSourceOrder() {
        return sourceOrder;
    }

    public void setSourceOrder(String sourceOrder) {
        this.sourceOrder = sourceOrder;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }
}
