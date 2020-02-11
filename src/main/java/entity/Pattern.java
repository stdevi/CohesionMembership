package entity;

import java.util.ArrayList;
import java.util.List;

public class Pattern {
    private List<String> items;
    private int support;
    private int cohesionScore;

    public Pattern() {
        items = new ArrayList<>();
    }

    public Pattern(List<String> items, int support) {
        this.items = items;
        this.support = support;
    }

    public void add(String item) {
        items.add(item);
    }

    public List<String> getItems() {
        return items;
    }

    public void setItems(List<String> items) {
        this.items = items;
    }

    public int getSupport() {
        return support;
    }

    public void setSupport(int support) {
        this.support = support;
    }

    public int getCohesionScore() {
        return cohesionScore;
    }

    public void setCohesionScore(int cohesionScore) {
        this.cohesionScore = cohesionScore;
    }

    @Override
    public String toString() {
        return "Pattern{" +
                "items=" + items +
                ", length=" + items.size() +
                ", support=" + support +
                ", cohesionScore=" + cohesionScore +
                '}';
    }
}