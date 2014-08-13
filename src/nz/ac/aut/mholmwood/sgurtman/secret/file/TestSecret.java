/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package nz.ac.aut.mholmwood.sgurtman.secret.file;

import java.io.IOException;

/**
 *
 * @author 
 */
public class TestSecret {
    private static String name = "/home/michael/player.png";
    
    public static void main(String args[]) throws IOException{
        byte[] key = new byte[7];
        
        System.out.println("Please enter the key");
        
        for(int x = 0; x < 7; x++){
            key[x] = (byte)System.in.read();
        }
        
        try {
            SecretFile file = SecretFile.generate(name, "/home/michael/playerenc.g");
           
            file.encrypt(key);
            file.setSrcPath("/home/michael/playerenc.g");
            file.setDestPath("/home/michael/playerdec.png");
            file.decrypt(key);
            
        } catch (IOException ex) {
            System.out.println("Error: " + ex.getMessage());
            System.exit(-1);
        }   
    }
}
