package org.cloud.random.entity;

import java.util.List;

public class RelationResult {
    private List<String> directRelations;
    private List<String> indirectRelations;
    private int[][] matrix;

    public RelationResult(List<String> directRelations, List<String> indirectRelations, int[][] matrix) {
        this.directRelations = directRelations;
        this.indirectRelations = indirectRelations;
        this.matrix = matrix;
    }

    // Getters and Setters

    public List<String> getDirectRelations() {
        return directRelations;
    }

    public void setDirectRelations(List<String> directRelations) {
        this.directRelations = directRelations;
    }

    public int[][] getMatrix() {
        return matrix;
    }

    public void setMatrix(int[][] matrix) {
        this.matrix = matrix;
    }

    public List<String> getIndirectRelations() {
        return indirectRelations;
    }

    public void setIndirectRelations(List<String> indirectRelations) {
        this.indirectRelations = indirectRelations;
    }
}
