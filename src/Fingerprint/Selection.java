package Fingerprint;

import com.digitalpersona.uareu.*;
import java.util.concurrent.TimeUnit;

public class Selection{
    public static Reader reader;

public static ReaderCollection getReaderCollection()throws UareUException{
    ReaderCollection readerCollection = UareUGlobal.GetReaderCollection();
    readerCollection.GetReaders();
    
    return readerCollection;
}

public static boolean readerIsConnected(){
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
        if (!readerCollection.isEmpty()){
            reader = readerCollection.get(0);
        }
    }catch (UareUException e){
        e.printStackTrace();
    }
    return reader;
    
}

public static Reader waitAndGetReader(){
       
       Reader reader = null;
        do {
            reader = getReader();
            if(reader == null){
                 System.out.println("No fingerprint reader found. Waiting for a reader to be connected...");
            }else{
                System.out.println("Connected fingerprint reader: " + reader.GetDescription().name);
                return reader;
            }

            // Wait for a few seconds before checking again
            try {
                TimeUnit.SECONDS.sleep(3); // You can adjust the sleep duration as needed
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }while (reader == null);
           
        return reader;
    

    }
}

