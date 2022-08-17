package com.vladi.karasove.smartravel.Helpers;

import android.text.Editable;
import android.text.TextWatcher;

import androidx.annotation.NonNull;

import com.google.android.material.textfield.TextInputLayout;

import java.util.ArrayList;
import java.util.regex.Pattern;

public class Validator {

    public static class Builder {
        private TextInputLayout textInputLayout;
        private ArrayList<Watcher> watchers = new ArrayList<>();
        private boolean isAlreadyBuild = false;

        public static Builder make(@NonNull TextInputLayout textInputLayout) {
            return new Builder(textInputLayout);
        }

        private Builder(@NonNull TextInputLayout textInputLayout) {
            this.textInputLayout = textInputLayout;
        }

        public Builder addWatcher(Watcher watcher) {
            if (!isAlreadyBuild) {
                this.watchers.add(watcher);
            }
            return this;
        }

        public Validator build() {
            if (!isAlreadyBuild) {
                isAlreadyBuild = true;
                Validator v = addValidator(textInputLayout, watchers);
                return v;
            }
            return null;
        }
    }

    private static Validator addValidator(TextInputLayout textInputLayout, ArrayList<Watcher> watchers) {
        Validator v = new Validator(textInputLayout, watchers);
        return v;
    }

    private TextInputLayout textInputLayout;
    private ArrayList<Watcher> watchers;

    private Validator(TextInputLayout textInputLayout, ArrayList<Watcher> watchers) {
        this.textInputLayout = textInputLayout;
        this.watchers = watchers;


        textInputLayout.getEditText().addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                String input = charSequence.toString();
                String result = check(input);
                textInputLayout.setError(result);
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
    }

    public String getError() {
        String input = textInputLayout.getEditText().getText().toString();
        return check(input);
    }

    public boolean validateIt() {
        String input = textInputLayout.getEditText().getText().toString();
        return check(input).equals("");
    }

    private String check(String input) {
        boolean result = true;
        for (Watcher watcher : watchers) {
            result = watcher.privateCheck(input);
            if (!result) {
                return watcher.error;
            }
        }
        return "";
    }


    public abstract static class Watcher {

        private String error;

        private Watcher(String error) {
            this.error = error;
        }

        public abstract boolean privateCheck(String input);
    }

    public static class Watcher_Email extends Watcher {

        public Watcher_Email(String error) {
            super(error);
        }

        @Override
        public boolean privateCheck(String input) {
            String ePattern = "^[a-zA-Z0-9.!#$%&'*+/=?^_`{|}~-]+@((\\[[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\])|(([a-zA-Z\\-0-9]+\\.)+[a-zA-Z]{2,}))$";
            Pattern pat = Pattern.compile(ePattern);
            if (!pat.matcher(input).matches()) {
                return false;
            }
            return true;
        }


    }

    public static class Watcher_StringEmpty extends Watcher {

        public Watcher_StringEmpty(String error) {
            super(error);
        }

        @Override
        public boolean privateCheck(String input) {
            if(input.isEmpty()){
                return false;
            }
            return true;
        }
    }


    public static class Watcher_String extends Watcher {

        public Watcher_String(String error) {
            super(error);
        }

        @Override
        public boolean privateCheck(String input) {
            if(!input.matches("[a-zA-Z]+")){
                return false;
            }
            return true;
        }
    }



    public static class Watcher_Integer extends Watcher {

        public Watcher_Integer(String error) {
            super(error);
        }

        @Override
        public boolean privateCheck(String input) {
            boolean isOk = true;
            try {
                int num = Integer.valueOf(input);
            } catch (Exception ex) {
                isOk = false;
            }

            return isOk;
        }
    }


    public static class Watcher_Number extends Watcher {

        public Watcher_Number(String error) {
            super(error);
        }

        @Override
        public boolean privateCheck(String input) {
            boolean isOk = true;
            try {
                double num = Double.valueOf(input);
            } catch (Exception ex) {
                isOk = false;
            }

            return isOk;
        }
    }


    public static class Watcher_MaximumOfLetter extends Watcher {

        private char c;
        private int n;

        public Watcher_MaximumOfLetter(String error, char c, int n) {
            super(error);
            this.c = c;
            this.n = n;
        }

        @Override
        public boolean privateCheck(String input) {
            String temp = input.replaceAll(""+c, "");
            if (input.length() - temp.length() > n) {
                return false;
            }
            return true;
        }
    }
}