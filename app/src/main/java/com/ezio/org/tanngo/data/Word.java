package com.ezio.org.tanngo.data;

/**
 * Created by Ezio on 2015/4/12.
 */
public class Word {

    private String word;
    private String kana;
    private String definition;
    private String example_sentence;


    public String getWord() {
        return word;
    }

    public String getKana() {
        return kana;
    }

    public String getDefinition() {
        return definition;
    }

    public String getExample_sentence() {
        return example_sentence;
    }


    public void setWord(String word) {
        this.word = word;
    }

    public void setKana(String kana) {
        this.kana = kana;
    }

    public void setDefinition(String definition) {
        this.definition = definition;
    }

    public void setExample_sentence(String example_sentence) {
        this.example_sentence = example_sentence;
    }
}
