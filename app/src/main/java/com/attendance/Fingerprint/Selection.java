package com.attendance.Fingerprint;

import com.digitalpersona.uareu.Reader;
import com.digitalpersona.uareu.ReaderCollection;
import com.digitalpersona.uareu.UareUException;
import com.digitalpersona.uareu.UareUGlobal;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
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
                System.out.println("readerIsConnected method: Connected fingerprint reader: " + reader.GetDescription().name);
                return true;
            } else {
                System.out.println("No fingerprint reader found.");
            }
        } catch (UareUException e) {
            e.printStackTrace();
        }

        return false;
    }

    public static boolean readerIsConnected_noLogging(){
        try {
            ReaderCollection readerCollection = getReaderCollection();
            if (!readerCollection.isEmpty()) {
                return true;
            }
        } catch (UareUException e) {
            e.printStackTrace();
        }
        return false;
    }

//    public static Reader getReader(){
//        Reader reader = null;
//        try {
//            ReaderCollection readerCollection = getReaderCollection();
//            if (!readerCollection.isEmpty()){
//                reader = readerCollection.get(0);
//            }
//        }catch (UareUException e){
//            e.printStackTrace();
//        }
//        return reader;
//    }

    public static void getReader(){
        try {
            if (readerIsConnected()){
                ReaderCollection readerCollection = getReaderCollection();
                reader = readerCollection.get(0);
            }
        }catch (UareUException e){
            e.printStackTrace();
        }
    }

    public static void closeAndOpenReader() throws UareUException{
        if(readerIsConnected_noLogging()){
            try{
                reader.Close();
                reader.Open(Reader.Priority.COOPERATIVE);
            }catch (UareUException ex){
                reader.Open(Reader.Priority.COOPERATIVE);
            }
        }
    }

    public static void closeReader() throws UareUException{
        if(readerIsConnected_noLogging()){
            try{
                reader.Close();
            }catch (UareUException ex){
                reader.Open(Reader.Priority.COOPERATIVE);
            }
        }
    }

    public static void waitAndGetReader(){
        ExecutorService executor = Executors.newFixedThreadPool(1);
        // Execute a task using the executor
        executor.execute(() -> {
            while(true && ThreadFlags.programIsRunning){
                getReader();
                if(reader == null){
                    System.out.println("No fingerprint reader found. Waiting for a reader to be connected...");
                }else{
                    System.out.println("Connected fingerprint reader: " + reader.GetDescription().name);
                    try {
                        closeAndOpenReader();
                    } catch (UareUException e) {
                        throw new RuntimeException(e);
                    }
                }

                // Wait for a few seconds before checking again
                try {
                    TimeUnit.SECONDS.sleep(5); // You can adjust the sleep duration as needed
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            }
        });
//        executor.shutdown();
    }

    public void Run(){
        waitAndGetReader();
    }
}

