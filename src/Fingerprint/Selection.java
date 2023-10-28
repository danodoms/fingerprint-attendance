package Fingerprint;

import com.digitalpersona.uareu.*;

public class Selection{
    
    //HARDWARE refers to fingerprint reader
    

public static ReaderCollection getReaderCollection()throws UareUException{
    ReaderCollection readerCollection = UareUGlobal.GetReaderCollection();
    readerCollection.GetReaders();
    
    return readerCollection;
}

public static boolean readerConnected(){
    try {
        ReaderCollection readerCollection = getReaderCollection();
        if (!readerCollection.isEmpty()) {
            Reader reader = readerCollection.get(0);
            System.out.println("Connected fingerprint reader: " + reader.GetDescription().name);
            return true;
        } else {
            System.out.println("No fingerprint reader found.");
        }
    } catch (UareUException e) {
        e.printStackTrace();
    }

    return false;
}

public static Reader getReader(){
    Reader reader = null;
    try {
        ReaderCollection readerCollection = getReaderCollection();
        reader = readerCollection.get(0);
    }catch (UareUException e){
        e.printStackTrace();
    }
    return reader;
    
}

}

