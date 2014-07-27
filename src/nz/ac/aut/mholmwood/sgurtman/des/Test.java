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
        
        byte[] key = {'1', '6', '3', '3', '5', '6', '7', '0' };
        DESCipher cipher = new DESCipher(key);
        
        byte[] data = { '6', 'e', 'l', 'z', '0', 'n', '2', 'k', ' ', 'a', 'n', 'd' };
        byte terminator = '\n';
        
        long[] enc = cipher.encrypt(data, terminator);
        
        byte[] dec = cipher.decrypt(enc, terminator);
        
        for(byte b : dec){
            System.out.println((char)b);
        }
        
        
    }
    
}
