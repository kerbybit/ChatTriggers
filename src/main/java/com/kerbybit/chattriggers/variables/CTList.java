package com.kerbybit.chattriggers.variables;

import java.util.ArrayList;

public class CTList extends Variable {

    private ArrayList<Variable> values;

    public CTList(ArrayList<Variable> values) {
        this.values = values;
    }

    public ArrayList get() {
        return values;
    }

    public void set(ArrayList<Variable> newVals) {
        values = newVals;
    }

    @Override
    public void call(String operation, String[] args) {
        switch (operation) {

        }
    }
}
