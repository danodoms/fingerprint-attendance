/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

package Controller;
import com.digitalpersona.uareu.*;


/**
 *
 * @author admin
 */


public class fingerprint{
    public static void main(String[] args) {
        
     ReaderCollection readerCollection = null;
        try {
            // Initialize the ReaderCollection
            readerCollection = UareUGlobal.GetReaderCollection();

            // Get the list of available readers from the ReaderCollection
            readerCollection.GetReaders();

            // Check if there are any connected fingerprint readers
            if (readerCollection.isEmpty()) {
                System.out.println("No fingerprint reader found.");
            } else {
                // Get the first connected fingerprint reader (you can loop through all readers if needed)
                Reader reader = readerCollection.get(0);

                // Now you have a Reader object for the connected fingerprint reader
                System.out.println("Connected fingerprint reader: " + reader.GetDescription().name);
            }
        } catch (UareUException e) {
            e.printStackTrace();
        }
        
    }
}

