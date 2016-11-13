package org.secuso.privacyfriendlypasswordgenerator.database;

/**
 * Created by yonjuni on on 17.06.16.
 */

public class MetaData {

    private int ID;
    private String DOMAIN;
    private int LENGTH;
    private int HAS_NUMBERS;
    private int HAS_SYMBOLS;
    private int HAS_LETTERS;
    private int ITERATION;

    public MetaData() {    }

    public MetaData(int ID, String DOMAIN, int LENGTH, int HAS_NUMBERS, int HAS_SYMBOLS, int HAS_LETTERS, int ITERATION) {

        this.ID=ID;
        this.DOMAIN=DOMAIN;
        this.LENGTH=LENGTH;
        this.HAS_NUMBERS=HAS_NUMBERS;
        this.HAS_SYMBOLS=HAS_SYMBOLS;
        this.HAS_LETTERS=HAS_LETTERS;
        this.ITERATION=ITERATION;
    }

    public int getITERATION() {
        return ITERATION;
    }

    public void setITERATION(int ITERATION) {
        this.ITERATION = ITERATION;
    }

    public int getHAS_LETTERS() {
        return HAS_LETTERS;
    }

    public void setHAS_LETTERS(int HAS_LETTERS) {
        this.HAS_LETTERS = HAS_LETTERS;
    }

    public int getHAS_SYMBOLS() {
        return HAS_SYMBOLS;
    }

    public void setHAS_SYMBOLS(int HAS_SYMBOLS) {
        this.HAS_SYMBOLS = HAS_SYMBOLS;
    }

    public int getHAS_NUMBERS() {
        return HAS_NUMBERS;
    }

    public void setHAS_NUMBERS(int HAS_NUMBERS) {
        this.HAS_NUMBERS = HAS_NUMBERS;
    }

    public int getLENGTH() {
        return LENGTH;
    }

    public void setLENGTH(int LENGTH) {
        this.LENGTH = LENGTH;
    }

    public String getDOMAIN() {
        return DOMAIN;
    }

    public void setDOMAIN(String DOMAIN) {
        this.DOMAIN = DOMAIN;
    }

    public int getID() {
        return ID;
    }

    public void setID(int ID) {
        this.ID = ID;
    }



}
