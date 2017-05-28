package com.kerbybit.chattriggers.core.variables;

public class CTString implements Variable {

    private String value;

    public CTString (String value) {
        this.value = value;
    }

    public String get () {
        return value;
    }

    public void set (String newVal) {
        value = newVal;
    }

    @Override
    public void call (String operation, String[] args) {
        switch (operation.toLowerCase()) {
            case "append":
                opAppend(args);
        }
    }

    private void opAppend (String[] args) {
        StringBuilder sb = new StringBuilder(value);

        for (String toAppend : args) {
            sb.append(toAppend);
        }

        value = sb.toString();
    }
}
