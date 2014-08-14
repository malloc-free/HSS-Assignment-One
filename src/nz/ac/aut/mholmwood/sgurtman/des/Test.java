/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package nz.ac.aut.mholmwood.sgurtman.des;

/**
 *
 * @author vgc8609
 */
public class Test {
    
    public static void main(String[] args){
        
        byte[] key = {'1', '2', '4', '5', '6', '3', '4' };
        DESCipher cipher = new DESCipher(key);
        
        byte[] data = { 
            (byte)0x89, 0x50,       0x4E, 
            0x47,       0x0D,       0x0A, 
            0x1A,       0x0A,       0x00, 
            00,         00,         0x0D, 
            0x49,       0x48,       0x44,       
            0x52,       0x00,       00,         
            00,         0x62,       00,
            00,         00,         0x62, 
            0x08,       0x06,       00, 
            00,         00,         (byte)0xAB,
            (byte)0xA5, 0x06,       0x0E, 
            00,         00,         00, 
            0x06,       0x62,       0x4B, 
            0x47,       0x44,       00, 
            (byte)0xFF, 00,         (byte)0xFF, 
            00,         (byte)0xFF, (byte)0xA0, 
            (byte)0xBD, (byte)0xA7, (byte)0x93, 
            00,         00,         0x11, 
            (byte)0xCA, 0x49,       0x44, 
            0x41,       0x54,       (byte)0x78, 
            (byte)0x9C, (byte)0xED, (byte)0x9D, 
            0x7D,       0x74 };
  
        for(byte b : data) {
            System.out.print(b + ", ");
        }
        
        System.out.println();
       
        byte terminator = '\n';
        
        long[] enc = cipher.encrypt(data, terminator);
        
    
        byte[] dec = cipher.decrypt(enc, terminator);
        
     
        for(byte b : dec){
            System.out.print(b + ", ");
        }
    }
}
