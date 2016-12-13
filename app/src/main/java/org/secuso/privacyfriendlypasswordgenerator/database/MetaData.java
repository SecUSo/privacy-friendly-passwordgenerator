package org.secuso.privacyfriendlypasswordgenerator.database;

/**
 * @author Karola Marky
 * @version 20160617
 */

public class MetaData {

    private int ID;
    private int POSITIONID;
    private String DOMAIN;
    private String USERNAME;
    private int LENGTH;
    private int HAS_NUMBERS;
    private int HAS_SYMBOLS;
    private int HAS_LETTERS_UP;
    private int HAS_LETTERS_LOW;
    private int ITERATION;

    public MetaData() {    }

    public MetaData(int ID, int POSITIONID, String DOMAIN, String USERNAME, int LENGTH, int HAS_NUMBERS, int HAS_SYMBOLS, int HAS_LETTERS_UP, int HAS_LETTER_LOW, int ITERATION) {

        this.ID=ID;
        this.POSITIONID = POSITIONID;
        this.DOMAIN=DOMAIN;
        this.USERNAME=USERNAME;
        this.LENGTH=LENGTH;
        this.HAS_NUMBERS=HAS_NUMBERS;
        this.HAS_SYMBOLS=HAS_SYMBOLS;
        this.HAS_LETTERS_UP=HAS_LETTERS_UP;
        this.HAS_LETTERS_LOW=HAS_LETTER_LOW;
        this.ITERATION=ITERATION;
    }

    public int getITERATION() {
        return ITERATION;
    }

    public void setITERATION(int ITERATION) {
        this.ITERATION = ITERATION;
    }

    public int getHAS_LETTERS_LOW() {
        return HAS_LETTERS_LOW;
    }

    public void setHAS_LETTERS_LOW(int HAS_LETTER_LOW) {
        this.HAS_LETTERS_LOW = HAS_LETTER_LOW;
    }

    public int getHAS_LETTERS_UP() {
        return HAS_LETTERS_UP;
    }

    public void setHAS_LETTERS_UP(int HAS_LETTERS_UP) {
        this.HAS_LETTERS_UP = HAS_LETTERS_UP;
    }

    public String getUSERNAME() {
        return USERNAME;
    }

    public void setUSERNAME(String USERNAME) {
        this.USERNAME = USERNAME;
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

    public int getPOSITIONID() {
        return POSITIONID;
    }

    public void setPOSITIONID(int POSITIONID) {
        this.POSITIONID = POSITIONID;
    }

    public int getID() {
        return ID;
    }

    public void setID(int ID) {
        this.ID = ID;
    }



}
