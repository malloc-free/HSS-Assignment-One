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
        
        byte[] data = { (byte)0x89, (byte)0x50, (byte)0x4E, 
            (byte)0x47, (byte)0x0D, (byte)0x0A, (byte)0x1A,
            (byte)0x0A, 00, 00, 00, (byte)0x0D, (byte)0x49, 
            (byte)0x48, (byte)0x44, (byte)0x52, (byte)0x00,
            00, 00, (byte)0x62, 00, 00, 00, (byte)0x62, 
            (byte)0x08, (byte)0x06, 00, 00, 00, (byte)0xAB,
            (byte)0xA5, (byte)0x06, (byte)0x0E, 00, 00, 00, 
            (byte)0x06, (byte)0x62, (byte)0x4B, (byte)0x47,
            (byte)0x44, 00, (byte)0xFF, 00, (byte)0xFF, 00,
            (byte)0xFF, (byte)0xA0, (byte)0xBD, (byte)0xA7,
            (byte)0x93, 00, 00, (byte)0x11, (byte)0xCA, (byte)0x49,
            (byte)0x44, (byte)0x41, (byte)0x54, (byte)0x78, 
            (byte)0x9C, (byte)0xED, (byte)0x9D, (byte)0x7D,// };
            (byte)0x74 };// (byte)0x54 };// E5 9D C7 3F 33 99 D7 7B 67 26 01 49 04 79 15 02 09 6C 11 A9 2F 14 71 CB 0A 54 5B 2C 2E 48 A1 B6 D2 AD 6C EB E9 E9 0B A8 3D A5 5D 7B E4 B0 DA 17 BB 47 6B D1 EA 22 5D A4 A1 F6 14 D7 B2 AD 2D 82 85 62 40 74 41 B0 BE 10 5E 24 };
        //byte[] data = { 8, 6, 116, 2, 4, 3, 3, 6};
  
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
    
    /* expected = 7959849495355140842 */
    
}
