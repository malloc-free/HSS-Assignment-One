/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package nz.ac.aut.mholmwood.sgurtman.des;

/**
 * Key class, creates the sequence of keys required for DES encryption.
 * All required keys are created at the beginning, as this will aid in 
 * decryption.
 * 
 * @author Michael Holmwood <mholmwood@chaosnet.co.nz>, Sam Gurtman
 */
public class Key {
    
    //The key rotation schedule
    public static final int[] ROTATIONS = {
        1, 1, 2, 2, 2, 2, 2, 2, 1, 2, 2, 2, 2, 2, 2, 1   
    };
  
    //The subkeys for this key.
    private long[] subKeys;
    
    /**
     * Static method for the creation of the key. 
     * @param initKey - The key to use for encryption.
     * @return - The complete key with subkeys for all rounds.
     */
    public static Key generateKey(long initKey){
        
        Key key = new Key();
        key.subKeys = new long[16];
        
        //Calculate each key half.
        long rightKey = Permutation.KEY_RIGHT.permutateLong(initKey);
        long leftKey = Permutation.KEY_LEFT.permutateLong(initKey);
        
        //Create the subkeys. Shift left for each round.
        for(int x = 0; x < 16; x++){
            rightKey = rotateKey(rightKey, x);
            leftKey = rotateKey(leftKey, x);
            long joined = leftKey;
            joined <<= 28;
            joined |= rightKey;
            key.subKeys[x] = Permutation.PERMUTED_CHOICE_2.permutateLong(joined);
        }
        
        return key;
    }
    
   /**
    * xOr the supplied data, for the given round.
    * 
    * @param data - The data to exclusive or.
    * @param round - Determines the subkey to be used.
    * @return - The calculated data.
    */
    public long xOrData(long data, int round){
        return data ^ subKeys[round];
    }
    
     
    /**
     * Rotate the given key according to the schedule.
     * 
     * @param rotateKey - The key to rotate.
     * @param round - The round to rotate it for.
     * @return - The rotated key.
     */
    private static long rotateKey(long rotateKey, int round){

        //Rotate left the nuber of times specified in the schedule
        for(int x = ROTATIONS[round]; x > 0; x--){
           
            rotateKey <<= 1;
            
            long checkLeft = rotateKey & 0x10000000;
            
            //Rotate the 25th bit, if it is set.
            if(checkLeft > 0){
                rotateKey ^= checkLeft;
                checkLeft >>= 28;
                rotateKey |= checkLeft;
            }
        }

        return rotateKey;
    }
    
 
}
