package com.talend.jreactive8583;

import com.solab.iso8583.IsoMessage;
import com.solab.iso8583.MessageFactory;
import com.solab.iso8583.parse.ConfigParser;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;

/**
 * Created by jcl on 17.11.17.
 */
public class MyISO8583Factory {

    final static String PARSING_GUIDE = "/Volumes/DATA/workspace" +
            "/IntelliJ_IDEA/jreactive8583/j8583.xml";
    final static String MSG_CHARSET = "UTF-8";

    public static MessageFactory<IsoMessage> create () throws IOException {
        MessageFactory<IsoMessage> mf = ConfigParser.createDefault();
        ConfigParser.configureFromUrl(mf,
                new File(PARSING_GUIDE).toURI().toURL());
        mf.setCharacterEncoding(Charset.forName(MSG_CHARSET).name());
        mf.setUseBinaryMessages(false);
        mf.setAssignDate(true);
        return mf;
    }
}
