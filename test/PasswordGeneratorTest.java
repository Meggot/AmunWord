




import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import static junit.framework.Assert.assertEquals;
import org.junit.BeforeClass;
import org.junit.Test;
import amungenerator.InvalidPasswordFormat;
import amungenerator.PasswordGenerator;

/**
 * Tests the PasswordGenerator class.
 * Interestingly, it was not challenging to produce the PasswordGenerator implementation, but
 * it was challenging to effectively test that this implementation.
 * In order to test it effectively, we must generate some kind of mechanism for testing
 * to see if the password generated fit all specified modified. This proved challenging, for a number
 * of problems arose out of the domain of this demand.
 * 
 * Such as,
 * what if the user had not specified a modifier, but received it anyway?
 * What if the user had specified a modifier, but didn't receive it?
 * What is the user specified a length lower than 4, but selected ALL modifiers?
 * What if the user hadn't selected ANY modifiers?
 * 
 * In order to implement this testing mechanism, it was decided to use Java's Regex functionality,
 * with a HashMap of regexes and literal String types as an identifier of each modifier. Regex was chosen
 * for its easy to use notation, and the ability to use ranges. Later on these regexes could be changed
 * to use powerful quantifiers or set notation to further expand it's functionality.
 * 
 * @author BradleyW
 */
public class PasswordGeneratorTest {

    private static HashMap<String, String> regexes;
    
    public PasswordGeneratorTest() {

    }

    @BeforeClass
    public static void initEnvironment() {
        regexes = new HashMap();
        regexes.put("Uppercase", "[A-Z]");
        regexes.put("Lowercase", "[a-z]");
        regexes.put("Number", "[0-9]");
        regexes.put("Special", "[&&!$%&*@^]");
    }

    
    @Test
    /** This test was focused on testing this Regex system, as it's validation is essential
     * to the rest of the testing.
     */
    public void IdentifyTest() {
        //For convenience, the parameters are: 
        //length, uppercase, lowercase, numbers, special, string to compare
        boolean result;
        
        //Pass conditions.
        result = identify(10, true, true, true, true, "AbJiOK!l@2");
        assertEquals(result, true);
        result = identify(10, false, true, true, true, "lblill!l@2");
        assertEquals(result, true);
        result = identify(10, true, false, true, true, "FDSFDFDS@2");
        assertEquals(result, true);
        result = identify(10, true, true, false, true, "AbJiOK!l@h");
        assertEquals(result, true);
        result = identify(10, true, true, true, false, "AbJiOKjlj2");
        assertEquals(result, true);
        
        //Failure conditions, three types of fails.
        
        //fail because of incorrect length
        result = identify(5, true, true, false, true, "jhK@");//Too short
        assertEquals(result, false);
        result = identify(10, true, true, false, true, "AbJiOKDFSDFSSAFjlj@");//Too long
        assertEquals(result, false);
        
        // fail because it contains a chartype, but is not specified.
        result = identify(10, true, true, false, false, "AbJiOKjlj2");//contains numbers
        assertEquals(result, false);
        result = identify(10, true, true, false, false, "AbJiOKjlj@");//contains special
        assertEquals(result, false);
        result = identify(10, false, true, false, true, "AbJiOKjlj@");//contains uppercase
        assertEquals(result, false);
        result = identify(10, true, false, false, true, "AbJiOKjlj@");//contains lowercase
        assertEquals(result, false);
        
        // fail  because it doesn't contain a chartype, but it is specified.
        result = identify(10, true, true, true, true, "A5JiOKjljK");//Specified special
        assertEquals(result, false);
        result = identify(10, true, true, true, true, "Ab@iOKjljK");//Specified number
        assertEquals(result, false);
        result = identify(10, true, true, true, true, "AG@DOK6DSK");//Specified lowercase
        assertEquals(result, false);
        result = identify(10, true, true, true, true, "fb@7fdjajs");//Specified uppercase
        assertEquals(result, false);
    }

    
    @Test
    /**
     * Finally, we can actually test the PasswordGenerator implementation, using our 
     * proven-to-be valid identify() method.
     */
    public void GeneratorValidTests() throws InvalidPasswordFormat {
        PasswordGenerator passwordGen = new PasswordGenerator();

        //Test1
        //All modifiers are set to true
        String result = passwordGen.generatePassword(10, true, true, true, true);
        boolean conditionalPass = identify(10, true, true, true, true, result);
        System.out.println("test1: " + result);
        assertEquals(conditionalPass, true);

        //Test2
        //no uppercase
        result = passwordGen.generatePassword(10, false, true, true, true);
        conditionalPass = identify(10, false, true, true, true, result);
        System.out.println("test1: " + result);
        assertEquals(conditionalPass, true);

        //Test3
        //no lowercase
        result = passwordGen.generatePassword(10, true, false, true, true);
        conditionalPass = identify(10, true, false, true, true, result);
        System.out.println("test1: " + result);
        assertEquals(conditionalPass, true);

        //Test4
        //no numbers 
        result = passwordGen.generatePassword(10, true, true, false, true);
        conditionalPass = identify(10, true, true, false, true, result);
        System.out.println("test1: " + result);
        assertEquals(conditionalPass, true);

        //Test5
        //no special characters
        result = passwordGen.generatePassword(10, true, true, true, false);
        conditionalPass = identify(10, true, true, true, false, result);
        System.out.println("test1: " + result);
        assertEquals(conditionalPass, true);

        //Test6
        //only lowercase characters
        result = passwordGen.generatePassword(10, false, true, false, false);
        conditionalPass = identify(10, false, true, false, false, result);
        System.out.println("test1: " + result);
        assertEquals(conditionalPass, true);
    }

    //Exception testing, too short or all modifiers set to false.
    @Test(expected = InvalidPasswordFormat.class)
    public void generatorExceptionTooSmall() throws InvalidPasswordFormat {
        PasswordGenerator passwordGen = new PasswordGenerator();
        passwordGen.generatePassword(1, true, true, true, true);
    }

    @Test(expected = InvalidPasswordFormat.class)
    public void generatorExceptionAllModifiersFalse() throws InvalidPasswordFormat {
        PasswordGenerator passwordGen = new PasswordGenerator();
        passwordGen.generatePassword(10, false, false, false, false);
    }

    public boolean identify(int length, boolean uppercase, boolean lowercase, boolean number, boolean special, String result) {
        HashMap<String, Boolean> testModifiers = new HashMap();
        testModifiers.put("Uppercase", uppercase);
        testModifiers.put("Lowercase", lowercase);
        testModifiers.put("Number", number);
        testModifiers.put("Special", special);
        if (length == result.length()) {
            boolean regexCondition = true;
            for (String charType : regexes.keySet()) {
                Pattern p = Pattern.compile(regexes.get(charType));
                Matcher m = p.matcher(result); //Matcher class is a powerful tool
                boolean containsType = m.find(); //Which allows us to use the find() method, to see if ANY string matches the regex.
                //This is different from the classic matches() method which will see if ALL of the string matches the regex
                boolean testType = testModifiers.get(charType);
                if (containsType && !testType) {
                    System.out.println("Contains, but not specified: FAIL");
                    regexCondition = false;
                    break; //Save time, and break the loop as this test has failed.
                } else if (!containsType && testType) {
                    System.out.println("Specified, but not contained: FAIL");
                    regexCondition = false;
                    break;
                } // else if both booleans are false, then pass, if both are true, then pass.
            }
            if (regexCondition) {
                return true;
            }
        }
        return false;
    }
}
