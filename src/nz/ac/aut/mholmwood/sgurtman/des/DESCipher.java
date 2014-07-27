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
       
        long [] encryptedArray;
       
        byte [] terminatedArray = new byte[plaintext.length + 1];

        int longArraySize = terminatedArray.length / 8;
        
        if(terminatedArray.length % 8 > 0)
        {
            longArraySize++;
        }
       
        encryptedArray = new long[longArraySize];
        
        System.arraycopy(plaintext, 0, terminatedArray, 0, plaintext.length);
        
        terminatedArray[plaintext.length] = terminatingByte;

         
        for(int i = 0; i < terminatedArray.length; i += 8)
        {
            long toUse = 0;
            for(int a = 0; a < 8; a++)
            {
                toUse = toUse << 8;
                if((i+a) >= terminatedArray.length )
                {
                    byte random = 0;
                    toUse = toUse | random;
                }
                else
                {
                    toUse = toUse | terminatedArray[i+a];
                }
            }
            encryptedArray[i/8] = start(toUse, true);
            
        }
        
        return encryptedArray;
       
    }
    
    public byte [] decrypt(long [] encrypted, byte terminatingByte)
    {
        byte [] bytesDecrypted = new byte[encrypted.length * 8];
        int size = 0;
        
        for(int i = 0; i < encrypted.length; i++)
        {
            
            long decrypted = start(encrypted[i],  false);
            for(int a = 0; a < 8; a++)
            {
                
                byte nextByte = (byte)(decrypted >> ((7-a)* 8)); 
                if(nextByte == terminatingByte)
                {
                    break;
                }
                else
                {
                    bytesDecrypted[size] = nextByte;
                    size++;
                }
              
            }
        }
        return Arrays.copyOf(bytesDecrypted, size);
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
        long leftData = initPerm & 0x7FFFFFFF00000000l;
        leftData >>= 32;
        
        if(initPerm < 0) {
            leftData |= 0x80000000l;
        }
       
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

