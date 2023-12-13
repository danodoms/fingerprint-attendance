package Fingerprint;

import com.digitalpersona.uareu.*;
import java.util.concurrent.TimeUnit;

public class Selection extends Thread{
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

    public static void closeAndOpenReader() throws UareUException{
        if(readerIsConnected()){
            try{
                reader.Close();
                reader.Open(Reader.Priority.COOPERATIVE);
            }catch (UareUException ex){
                reader.Open(Reader.Priority.COOPERATIVE);
            }
        }
    }

    public static void closeReader() throws UareUException{
        if(readerIsConnected()){
            try{
                reader.Close();
            }catch (UareUException ex){
                reader.Open(Reader.Priority.COOPERATIVE);
            }
        }
    }

    public static Reader waitAndGetReader(){
       Reader reader = null;
       while(true){
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
       }
    }

    public void Run(){
        reader = waitAndGetReader();
    }
}

