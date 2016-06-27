/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package amungenerator;

/**
 *
 * @author BradleyW
 */
public class InvalidPasswordFormat extends Exception{
    
      public InvalidPasswordFormat() {}

      public InvalidPasswordFormat(String message)
      {
         super(message);
      }
    
}
