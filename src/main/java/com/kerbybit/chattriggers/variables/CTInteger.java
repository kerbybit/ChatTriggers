package com.kerbybit.chattriggers.variables;

public class CTInteger extends Variable {

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
