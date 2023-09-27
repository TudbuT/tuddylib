package de.tudbut.algorithm;

import de.tudbut.parsing.TudSort;

import java.util.ArrayList;
import java.util.Arrays;

public class AlgorithmAI<T> extends Algorithm<T> {
    
    private ArrayList<Association> associations = new ArrayList<>();
    private int nextChoiceID = 1;
    
    public AlgorithmAI(Association[] associations) {
        this.associations = new ArrayList<>(Arrays.asList(associations));
        for (int i = 0; i < associations.length; i++) {
            if(associations[i].choiceID > nextChoiceID + 1) {
                nextChoiceID = associations[i].choiceID + 1;
            }
        }
    }
    
    public AlgorithmAI() {
    }
    
    public Association[] getAssociations() {
        return associations.toArray(new Association[0]);
    }
    
    public void reward(float amount) {
        int lastChoiceID = nextChoiceID - 1;
        associations = new ArrayList<>(Arrays.asList(TudSort.sort(associations.toArray(new Association[0]), association -> association.choiceID)));
        for (int j = 1, i = associations.size() - 1; i >= 0; i--) {
            Association association = associations.get(i);
            if(association.choiceID < lastChoiceID) {
                j *= 0.5;
                lastChoiceID = association.choiceID;
            }
            association.score += amount * j;
        }
    }
    
    public void punish(float amount) {
        int lastChoiceID = nextChoiceID - 1;
        associations = new ArrayList<>(Arrays.asList(TudSort.sort(associations.toArray(new Association[0]), association -> association.choiceID)));
        for (int j = 1, i = associations.size() - 1; i >= 0; i--) {
            Association association = associations.get(i);
            if(association.choiceID < lastChoiceID) {
                j *= 0.5;
                lastChoiceID = association.choiceID;
            }
            association.score -= amount * j;
        }
    }
    
    @Override
    public T solve(Input<T>[] inputs) {
        float highest = -Float.MAX_VALUE;
        Input<T> best = null;
        for (int i = 0; i < inputs.length; i++) {
            float score = score(inputs[i]);
            if(score > highest) {
                highest = score;
                best = inputs[i];
            }
        }
        if(best != null) {
            for (int i = 0; i < best.data.length; i++) {
                Association association = associationFromString(best.data[i]);
                if(association == null)
                    continue;
                association.choiceID = nextChoiceID;
            }
            nextChoiceID++;
            return best.t;
        } else {
            nextChoiceID++;
            return null;
        }
    }
    
    private float score(Input<T> input) {
        float r = 0;
        for (int i = 0; i < input.data.length; i++) {
            try {
                int finalI = i;
                if (associations.stream().noneMatch(association -> association.string != null && input.data[finalI] != null && association.string.equals(input.data[finalI]))) {
                    Association association = new Association();
                    association.string = input.data[i];
                    associations.add(association);
                }
                Association association = associationFromString(input.data[i]);
                if (association == null)
                    continue;
                r += association.score;
            } catch (NullPointerException ignore) { }
        }
        return r;
    }
    
    private Association associationFromString(String s) {
        for (int i = 0; i < associations.size(); i++) {
            if(associations.get(i).string.equals(s))
                return associations.get(i);
        }
        return null;
    }
    
    public static class Association {
        private String string;
        private float score = 0.0f;
        private int choiceID = 0;
        
        public Association() { }
        
        public Association(String s, float score) {
            this.string = s;
            this.score = score;
        }
        
        public String toString() {
            return string + ":" + score + " " + choiceID + ";";
        }
    }
}
