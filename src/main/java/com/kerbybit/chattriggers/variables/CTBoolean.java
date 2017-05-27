package com.kerbybit.chattriggers.variables;

public class CTBoolean extends Variable {

    private Boolean value;

    public CTBoolean(Boolean value) {
        this.value = value;
    }

    public Boolean get() {
        return value;
    }

    public void set(Boolean newVal) {
        value = newVal;
    }

    @Override
    public void call(String operation, String[] args) {
        switch (operation) {

        }
    }
}
