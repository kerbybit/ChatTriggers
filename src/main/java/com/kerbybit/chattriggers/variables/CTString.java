package com.kerbybit.chattriggers.variables;

public class CTString extends Variable {

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
