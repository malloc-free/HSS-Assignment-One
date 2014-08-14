/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package nz.ac.aut.mholmwood.sgurtman.secret.file;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.file.Files;
import java.nio.file.LinkOption;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import nz.ac.aut.mholmwood.sgurtman.des.DESCipher;

/**
 * Class SecretFile
 * 
 * Encrypts/Decrypts a file using the supplied file locations and key.
 * 
 * @author Michael Holmwood, Sam Gurtman
 */
public class SecretFile {
    
    //The path of the file to encrypt/decrypt.
    private Path srcPath;
    //The path to write the encrypted/decrypted data to.
    private Path destPath;
    //The cipher used for the decryption.
    private DESCipher cipher;
    
    //Private constructor, must use the static factory method.
    private SecretFile(){};
    
    /**
     * The static factory method for SecrectFile. Takes the name of the source
     * file, the file to write to. Throws an IOException if anything is borked.
     * 
     * @param srcName - Location of the source file to encrypt/decrypt.
     * @param destName - Location to write the encrypted/decrypted file to.
     * @return - The created SecretFile class.
     * 
     * @throws IOException - Borked file IO. 
     */
    public static SecretFile generate(String srcName, String destName) throws IOException{
 
        SecretFile secret = new SecretFile();
        secret.srcPath = Paths.get(srcName);
        secret.destPath = Paths.get(destName);
        
        //Checks to see if the file exists. If not, chucks an exception.
        if(!Files.exists(secret.srcPath, LinkOption.NOFOLLOW_LINKS)){
            throw new FileNotFoundException("File " + srcName + " not found");
        }
        
        Files.deleteIfExists(secret.destPath);
        
        return secret;
    }
    
    /**
     * Set a new destination path. Deletes the file if it already
     * exists.
     * 
     * @param destPath
     * @throws IOException 
     */
    public void setDestPath(String destPath) throws IOException{
        this.destPath = Paths.get(destPath);
        Files.deleteIfExists(this.destPath);
    }
    
    /**
     * Set a new source path.
     * 
     * @param srcPath
     * @throws FileNotFoundException 
     */
    public void setSrcPath(String srcPath) throws FileNotFoundException{
        this.srcPath = Paths.get(srcPath);
        
        if(!Files.exists(this.srcPath, LinkOption.NOFOLLOW_LINKS)){
            throw new FileNotFoundException("File " + this.srcPath + " not found");
        }
    }
    
    /**
     * Encrypt the file specified by srcPath into the file specified by
     * destPath.
     * 
     * @param key - The key to encrypt the file with.
     * 
     * @throws IOException 
     */
    public void encrypt(byte[] key) throws IOException{
       
        cipher = new DESCipher(key);
        
        byte[] fileBytes = Files.readAllBytes(srcPath);

        long[] encrypted = cipher.encrypt(fileBytes, (byte)'\n');
        
        ByteBuffer buf = ByteBuffer.allocate(encrypted.length * 8);

        for(int x = 0; x < encrypted.length; x++){   
            buf.putLong(x * 8, encrypted[x]);
        }

        Files.write(destPath, buf.array(), StandardOpenOption.CREATE);
    }
    
    /**
     * Decrypt the file specified by scrPath to the file specified by destPath, 
     * using the specified key.
     * 
     * @param key
     * @throws IOException 
     */
    public void decrypt(byte[] key) throws IOException{
       
        cipher = new DESCipher(key);
        
        byte[] toDecrypt = Files.readAllBytes(srcPath);
        ByteBuffer file = ByteBuffer.allocate(toDecrypt.length);
        file.put(toDecrypt);
        
        long[] value = new long[toDecrypt.length / 8];

        for(int x = 0; x < toDecrypt.length; x += 8){
            value[x / 8] = file.getLong(x);
        }

        byte[] decrypted = cipher.decrypt(value, (byte)'\n');

        Files.write(destPath, decrypted, StandardOpenOption.CREATE);
        
    }
    
}
