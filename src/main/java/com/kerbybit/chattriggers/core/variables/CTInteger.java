package com.kerbybit.chattriggers.core.variables;

public class CTInteger implements Variable {

    private Integer value;

    public CTInteger(Integer value) {
        this.value = value;
    }

    public Integer get() {
        return value;
    }

    public void set(Integer newVal) {
        value = newVal;
    }

    @Override
    public void call(String operation, String[] args) {
        switch (operation) {

        }
    }
}
