/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package amungenerator;

import java.util.ArrayList;
import java.util.Collections;
import java.util.PriorityQueue;
import java.util.Queue;
import java.util.Random;

/**
 *
 * @author BradleyW
 */
public class PasswordGenerator {

    public PasswordGenerator() {

    }

    /**Generates a password based on the modifiers provided in the method headers.
     * 
     * @param length length of the password to be generated. Must never be below 4.
     * @param uppercase if to include uppercase characters: A-Z
     * @param lowercase if to include lowercase characters: a-z
     * @param number if to include numbers: 0-9
     * @param special if to include special characters: !$%&*@^
     * @return returns the password as a String
     * @throws InvalidPasswordFormat if all modifiers are set to false, or the length is less than 4, will return this Exception.
     */
    public String generatePassword(int length, boolean uppercase, boolean lowercase, boolean number, boolean special) throws InvalidPasswordFormat {
        //Check for error cases.
        if (length < 4) {
            throw new InvalidPasswordFormat("Passwords cannot be less than 4 characters");
        }
        if (!uppercase && !lowercase && !number && !special) {
            throw new InvalidPasswordFormat("You cannot have no modifiers set");
        }

        String returnString = "";
        ArrayList charLists = new ArrayList();
        PriorityQueue workingQueue = new PriorityQueue(); //My implementation utiizes java.util's queue, to make sure each set of characters has been used.
        
        if (uppercase) {
            charLists.add("ABCDEFGHIJKLMNOPQRSTUVWXYZ");
        }
        if (lowercase) {
            charLists.add("abcdefghijklmnopqrstuvwxyz");
        }
        if (number) {
            charLists.add("0123456789");
        }
        if (special) {
            charLists.add("!$%&*@^");
        }
        
        while (returnString.length() < length) {
            String charlist = (String) workingQueue.poll();
            if (charlist == null) {
                Collections.shuffle(charLists); //Shuffle the entries, to try and make the generated password less predicatable.
                workingQueue = new PriorityQueue(charLists);  //assign a new Queue to the collection. This uses the iterator, so it is never shuffled.
                //TODO: Try and make this shuffle, it seems that as the Queue uses the collections iterator it does not care for order.
                continue; //Continue to next iteration.
            }
            String newCharacter = generatePerCharlist(charlist);
            returnString = returnString + newCharacter;
        }
        return returnString;
    }

    /**
     * Generates a String, 1 length character based on a char list provided.
     * Uses a RandomNumberGenerator to generate an index position of this charlist.
     * @param charList list of chars, IE "ABCDE ..YZ"
     * @return returns a randomly selected character
     */
    private String generatePerCharlist(String charList) {
        Random randomIndex = new Random();
        Character c = charList.charAt(randomIndex.nextInt(charList.length()));
        return String.valueOf(c);
    }
}
