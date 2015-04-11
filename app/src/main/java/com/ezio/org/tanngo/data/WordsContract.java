package com.ezio.org.tanngo.data;

import android.provider.BaseColumns;

/**
 * Created by Ezio on 2015/4/2.
 */
public class WordsContract {

    // The "Content authority" is a name for the entire content provider, similar to the
    // relationship between a domain name and its website.  A convenient string to use for the
    // content authority is the package name for the app, which is guaranteed to be unique on the
    // device.
    //public static final String CONTENT_AUTHORITY = "com.ezio.org.tanngo";

    public static final class WordsEntry implements BaseColumns {

        //placeholder table name
        //think about how to distinguish different users' same words book
        //can't be used in real
        public static final String TABLE_NAME = "words_table";

        //cannot be null,text
        public static final String COLUMN_WORD = "word";

        //can be null,text
        public static final String COLUMN_KANA = "kana";

        //cannot be null,text
        public static final String COLUMN_DEFINITION = "definition";

        //can be null,text
        public static final String COLUMN_EXAMPLE_SENTENCE = "example_sentence";

        //can be null,int
        public static final String COLUMN_CONTINUOUS_RIGHT = "continuous_right";

        //can be null,int
        public static final String COLUMN_WRONG_TIMES = "wrong_times";

    }

}
