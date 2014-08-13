/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package nz.ac.aut.mholmwood.sgurtman.des;

import java.util.Arrays;

/**
 * Class DESCipher
 * 
 * The primary class for the cipher, contains methods for encryption, decryption,
 * 
 * @author Michael Holmwood, Sam Gurtman
 */
public class DESCipher {
 
    private Key key;
    
    public DESCipher(byte[] userKey){
        key = Key.generateKey(padKey(userKey));
        
        System.out.println();
    }
    
    public DESCipher(long userKey){
        key = Key.generateKey(userKey);
    }
    
    public static void main(String[] args) {
        long value = 0;
        byte[] data = { 10, 26, 10, 0, 0, 0, 13, 12 };
        
        for(byte b : data) {
            value <<= 8;
            value |= b;
        }
        
        System.out.println(value);
        byte[] key = {'1', '2', '4', '5', '6', '3', '4' };
        
        DESCipher cipher = new DESCipher(key);
        
        long encrypted = cipher.start(value, true);
        System.out.println(encrypted);
        long decrypted = cipher.start(encrypted, false);
        System.out.println(decrypted);
        for(int x = 0; x < 8; x++) {
            System.out.println((byte)decrypted);
            decrypted >>= 8;
        }
    }
    
    /* Pad a 7 character key into 64 bit key
     * @param key String whose first 7 characters are the key
     * @return 64 bit long containing key properly padded
     */
    private long padKey(byte[] keyBytes) {
        long paddedKey = 0;
       
        for(int i = 0; i < 7; i++)
        {
            paddedKey = paddedKey << 8;
            paddedKey = ((paddedKey | keyBytes[i]) << 1); 
        }
        
        
        return paddedKey;
    }
    
    public long[] encrypt(byte [] plaintext, byte terminatingByte)
    {
       int x = 0, y;
       long data;
       long[] encrypted = new long[(plaintext.length / 8 + 
               ((plaintext.length % 8 == 0) ? 0 : 1))];
       long temp;
       while(x < plaintext.length) {
           data = 0;
           for(y = 0; y < 8; y++) {
               if(x < plaintext.length) {
                   temp = 0;
                   data <<= 8;
                   temp |= plaintext[x];
                   temp &= 0xFF;
                   data |= temp;
                   x++;
               }
               else {
                   break;
               }
           }
           
           for(; y < 8; y++) {
               System.out.println(y);
               data <<= 8;
               x++;
           }
           
           System.out.println("in: " + data);
           encrypted[(x / 8) - 1] = start(data, true);
       }
       
       return encrypted;
    }
    
    public byte [] decrypt(long [] encrypted, byte terminatingByte)
    {
        byte[] data = new byte[encrypted.length * 8];
        int x = 0;
        long decrypted = 0;
        while(x < encrypted.length) {
            decrypted = start(encrypted[x], false);
            System.out.println("out: " + decrypted);
            for(int y = 7; y >= 0; y--) {
                data[y + (x * 8)] = (byte)decrypted;
                decrypted >>= 8;
            }
            
            x++;
        }
        
        return data;
    }
    
    /**
     * Begins the encrypt/decrypt process. Performs the initial permutation,
     * splits the data into the left and right halves, calls method round,
     * then performs the final permutation on the data.
     * 
     * @param data - The data to be encrypted.
     * @param encryption - True for encryption, false for decryption.
     * @return - The encrypted data.
     */
    private long start(long data, boolean encryption){
        long initPerm = Permutation.INITIAL_PERMUTATION.permutateLong(data);
        
        //Split the code
        long rightData = initPerm & 0xFFFFFFFFl;
        long leftData = initPerm & 0xFFFFFFFF00000000l;
        leftData >>= 32;
        
//        if(initPerm < 0) {
//            leftData |= 0x80000000l;
//        }
       
        //Determite if we are starting from round 0 (encryption) or 
        //round 15 (decryption)
        int direction = (encryption) ? 0 : 15;
      
        //See ya in the next 16 rounds
        long output = round(leftData, rightData, key, direction, encryption);
        
        return Permutation.FINAL_PERMUTATION.permutateLong(output);
    }
    
    /**
     * Recursive method, performs the 16 rounds of encryption.
     * 
     * @param leftData - Left half of the data
     * @param rightData - Right half of the data
     * @param key - Key for this encryption/decryption
     * @param round - The current round
     * @param encryption - True for encryption, false for decryption
     * @return - The xOred value of L and F
     */
    public long round(long leftData, long rightData, Key key,
            int round, boolean encryption){
        
        //Get the result of the function
        long fData = function(rightData, key, round);
        
        fData ^= leftData;
        
        //Continue until all rounds complete
        if((encryption && ++round < 16) || (!encryption && --round >= 0)){
            fData = round(rightData, fData, key, round, encryption);
        }
        //swap and combine the function result and L16
        else{
            fData <<= 32;
            fData &= 0xFFFFFFFF00000000l;
            rightData &= 0x00000000FFFFFFFFl;
            fData |= rightData;
        }
            
        return fData;
    }
    
    /**
     * This method performs the function calculations. Calculates the
     * expansion, xOrs with the current sub key, applies the sBox calculations,
     * then applies the final permutation.
     * 
     * @param data - The data to perform the calculation on. 
     * @param key - The key to use for the xOring.
     * @param round - The current round.
     * @return - The result of the function.
     */
    public long function(long data, Key key, int round){
        long calcData = Permutation.EXPANSION.permutateLong(data);
        calcData = key.xOrData(calcData, round);
        calcData = SBox.applySBox(calcData);
        calcData =  Permutation.PERMUTATION.permutateLong(calcData);
        
        return calcData;
        
    }
}

