package pj.util;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 *
 * @author Pankaj Johri (pajohri@yahoo.com)
 *        
 *         Java Data Obfuscation Through Masking (J-DOTM) Utility is a lightweight program 
 *         that can be easily integrated to your ETL and/or configuration file. 
 *         You just have to pass the right parameters to its constructor         
 *         
 *         The purpose of this class it to Obfuscate any kind of sensitive data such as
 *         email, first/last name, phone number etc, and also maintain the data integrity 
 *         with data that looks like real production data, making it useless to malicious actors,
 *         without causing performance hit to virtually any size of detest.
 *         
 *         The program uses masking technique (replacing the real data with randomly 
 *         generated fake data). You may create as many combinations as you want,
 *         the default is set to 200.
 *         
 *         The data Obfuscation yields the same result as long as you use the same seed value,
 *         seed value should be decided by the admin and should not be shared across users for
 *         the data privacy. Seed value is a mandatory value that can be any long type number passed
 *         through the constructor while initiating the object of this immutable class.
 *         
 *         For the data integrity, you must remember to use the exactly same version of the class along
 *         with exactly the same parameters passed through it constructor. At any point in time you
 *         modify either the class or its parameters, you must ensure to regenerate the obfuscated values
 *         across entire dataset. 
 *         
 *         The class will work by instantiating one of the many constructors 
 *         then invoke getObfuscated() method and pass the value that needs
 *         to be Obfuscated. Method will return the Obfuscated value of type
 *         String. 
 *         
 *         There are multiple constructors in the class and it takes the following arguments: -
 *              int seedValue :
 *                  Seed value is any long number, it will ensure you get the same output 
 *                  as long as the same value is passed
 *              Map<List<int[]>, Integer> userMap :
 *                  If default implementation is not good enough in getOutputCharValueArray() method for 
 *                  your business needs, you can change the implementation by passing user defined LinkedHashMap. 
 *                  Remember it is LinkedHashMap, that ensure the iteration order, which is normally the order in 
 *                  which keys were inserted.
 *                  You can initialize it as follows: -
 *                      Map<List<int[]>, Integer> map = new LinkedHashMap<>(); 
 *                  Here are few examples
 *                      a. If you want to replace vowels with another set vowels only then you can use
 *                          map.put(Arrays.asList(new int[] { 'A', 'E', 'I', 'O', 'U' }), 1);
 *                          map.put(Arrays.asList(new int[] { 'a', 'e', 'i', 'o', 'u' }), 1);
 *                          
 *                          First parameter in the map is list of int array and second argument is 
 *                          number 1 which tells that you are passing each characters individually.
 *                          
 *                          Instead of characters you can also pass the ASCII or the unicode value represented 
 *                          by a character that will yield the same result as follows: -
 *                          map.put(Arrays.asList(new int[] { 65, 69, 73, 79, 85 }), 1);
 *                          
 *                          Where 'A' ascii value is 65, 'E' ascii value is 69 and so on
 *                          
 *                      b. You can also replace a range of characters  with another range. For
 *                         example if you wish to replace all the lowercase with randomly distributed uppercase
 *                         case then use the following syntax: -
 *                           map.put(Arrays.asList(new int[] { 'a', 'z' }, new int[] { 'A', 'Z' }), 0);
 *                    
 *                          First parameter in map is list of int array and second argument is 
 *                          number 0 which tells you are passing characters as range. You can optinoally pass Ascii/unicode value
 *                          in place of characters or mix and match as needed. Ensure that range must be in the
 *                          ascending order. For example {'a', 'z'} is correct but {'z', 'a'} is incorrect
 *                          
 *                      NOTE : In the list (first parameter), you can optionally add second element as int array
 *                             if you want to use another set of characters to replace with. Otherwise adding second
 *                             list element is not needed. In the example above, where we want to replace lowercase 
 *                             with uppercase, there is a need to add the 2nd element to the list. Instead if you just
 *                             want to re-shuflle characters, you will not need to pass the 2nd element to the list
 *                          
 *              int startRange
 *              int endRange
 *                  If you are dealing with only english keyboard then you can specify the startRange as 32 and 
 *                  endRange as 127. Narrow range will also reduce the memory footprint of the program. 
 *                  Remember that this program can also obfuscate multilingual data, that also demands more memory. 
 *                  It is advisable to specify the start and end range 
 *                  depending on the type of character sets you are dealing with. If any character falls
 *                  outside the specified range, it will not be obfuscated.
 *                  
 *              boolean isCaseInsensitiveObfuscate
 *                  If you wish to convert small and cap characters with same random character then pass this value as true
 *                  
 *              int maxCombination
 *                  It tells how many sets of different random data is maintained by the program. The default value is set
 *                  to 200. Which means if you use same set of characters in different input values, there would be 200
 *                  combinations and it would be very hard for any malicious actor to reverse engineer. 
 *                  But on the other side if you increase the number to a very big number, it will demand more memory
 *                  footprint. You need to decide the optimum number for your use case.
 *                  
 *                  For each input value that you wish to Obfuscate, a hash is calculated, the hash value will fall 
 *                  in the range of 0 to maxCombination specified. Based on this hash value, the program will 
 *                  decide which combination to use to Obfuscate the data. In simple words, if abcd is converted 
 *                  to wxyz then acbd will not be wyxz as hash will be different for abcd and acbd. The program will
 *                  select one out of many available combinations based on the hash value and the maxCombination specified.
 *         
 *
 *         To handle different languages, enhance the method getOutputCharValueArray(). I have provided a basic 
 *         implementation for English and Devnagri only. It is very easy to extend and I need your help and support. 
 *         Optionally you can also use  userMap to pass the new implementation as a constructor argument or to 
 *         overwrite the default behavior. 
 *         Each time you put a new value into the userMap, it will overwrite the rules with latest one defined by you
 *         for the selected range or characters only. This provide you all the flexiblity you will need to fine tune
 *         this program to your need.
 *         
 *         If your business logic demands different obfuscation rules and one rule will not suffice, then you should define
 *         multiple userMap(s) and create multiple instances one for each rule. Although the need to create multiple
 *         instance will not be required in most of the scenarios.
 *
 *         Example with a use case
 *         I want vowels should be randomly shuffled with another vowels only
 *         Numbers should be randomly shuffled with another number only but 0 value should remain unchanged
 *         I want only 100 total combination
 *         I want my program is limited to US keyboard character values only (from ascii 32 to 127)
 *         Small and capital letters should be randomly shuffled with the same characters.
 *         
 *         Here is the full code for the above use case: -         
 *         Map<List<int[]>, Integer> map = new LinkedHashMap<>(); // initialize the userMap 
 *         map.put(Arrays.asList(new int[] { 'A', 'E', 'I', 'O', 'U' }), 1); // randomly shuffled vowels only, note second parameter as 1
 *         map.put(Arrays.asList(new int[] { 'a', 'e', 'i', 'o', 'u' }), 1); // randomly shuffled vowels only, note second parameter as 1
 *         map.put(Arrays.asList(new int[] { '0' }), 1); // ensure 0 is replaced with 0
 *         PJObfuscateUtil obfuscateUtilObj = new PJObfuscateUtil(1934233, map, 32, 127, true, 100); //instantiate the program
 *         String obfuscatedValue1 = obfuscateUtilObj.getObfuscated("(800) 334-5343");
 *         String obfuscatedValue2 = obfuscateUtilObj.getObfuscated("John j Jonny");
 *         String obfuscatedValue3 = obfuscateUtilObj.getObfuscated("Mike.cater@Gmail.Com");
 *         String multilingual = obfuscateUtilObj.getObfuscated("123 Thank You १२३ शुक्रिया ありがとうございました 谢谢你"); //To see multilingual obfuscation to work, increase the endRange
 *         System.out.println("obfuscatedValue1"+obfuscatedValue1);
 *         System.out.println("obfuscatedValue2"+obfuscatedValue2);
 *         System.out.println("obfuscatedValue3"+obfuscatedValue3);
 *         System.out.println("multilingual"+multilingual);
 *
 */

public final class PJObfuscateUtil {
    private static final Pattern PATTERN_BY_SPACE = Pattern.compile("(\\s*)(\\S+)?(\\s*)");
    private static final int MAX_CHAR_INDEX = 65534;
    private int maxCombination = 1;
    private long seedValue;
    private List<int[]> masterList = null;
    private int[] caseInsensitiveList = null;
    private boolean isCaseInsensitiveObfuscate = false;
    private int startRange = 0;
    private int endRange = MAX_CHAR_INDEX;      
    private Map<List<int[]>, Integer> userMap = new LinkedHashMap<>();
    public PJObfuscateUtil(int seedValue) {
        this(seedValue, false);
    }
    public PJObfuscateUtil(int seedValue, boolean isCaseInsensitiveObfuscate) {
        this(seedValue, isCaseInsensitiveObfuscate, 200);
    }
    public PJObfuscateUtil(int seedValue, boolean isCaseInsensitiveObfuscate, int maxCombination) {
        this(seedValue, null, -1, -1, isCaseInsensitiveObfuscate, maxCombination);
    }
    public PJObfuscateUtil(int seedValue, Map<List<int[]>, Integer> userMap, boolean isCaseInsensitiveObfuscate, int maxCombination) {
        this(seedValue, userMap, -1, -1, isCaseInsensitiveObfuscate, maxCombination);
    }  
    public PJObfuscateUtil(int seedValue, Map<List<int[]>, Integer> userMap, boolean isCaseInsensitiveObfuscate) {
        this(seedValue, userMap, -1, -1, isCaseInsensitiveObfuscate, 200);
    }      
    public PJObfuscateUtil(int seedValue, Map<List<int[]>, Integer> userMap, int startRange, int endRange, boolean isCaseInsensitiveObfuscate, int maxCombination) {
        this.seedValue = seedValue;
        if (userMap != null)
            this.userMap = userMap;
        if (startRange > 0)
            this.startRange = startRange;
        if (endRange > 0 && this.startRange < endRange)
            this.endRange = endRange;
        this.isCaseInsensitiveObfuscate = isCaseInsensitiveObfuscate;        
        if (maxCombination > 0)
            this.maxCombination = maxCombination;
        this.masterList = new ArrayList<>(maxCombination);
        for (int i = 0; i <= maxCombination; i++)
            masterList.add(getObfuscatedArray(i));
        if (isCaseInsensitiveObfuscate) {
            caseInsensitiveList = new int[MAX_CHAR_INDEX];
            for (int i = 0; i < MAX_CHAR_INDEX; i++)
                caseInsensitiveList[i] = Character.toUpperCase((char) i);
        }
    }
    private final int[] getArrayInPlay(char[] input) {
        int hash = 0;
        int length = input.length - 1;
        if (length > 4)
            if (isCaseInsensitiveObfuscate)
                hash = (caseInsensitiveList[input[0]] + caseInsensitiveList[input[length/4]] + caseInsensitiveList[input[length/2]]
                        + caseInsensitiveList[input[length*3/4]] + caseInsensitiveList[input[length]]) % maxCombination;
            else
                hash = (input[0] + input[length/4] + input[length/2] + input[length*3/4] + input[length]) % maxCombination;
        else if (length > 1)
            if (isCaseInsensitiveObfuscate)
                hash = (caseInsensitiveList[input[0]] + caseInsensitiveList[input[length/2]] + caseInsensitiveList[input[length]]) % maxCombination;
            else
                hash = (input[0] + input[length/2] + input[length]) % maxCombination;
        else if (length > 0)
            if (isCaseInsensitiveObfuscate)
                hash = (caseInsensitiveList[input[0]] + caseInsensitiveList[input[length]]) % maxCombination;
            else
                hash = (input[0] + input[length]) % maxCombination;          
        else
            if (isCaseInsensitiveObfuscate)
                hash = caseInsensitiveList[input[0]] % maxCombination;
            else
                hash = input[0] % maxCombination;            
        return masterList.get(hash);
    }

    private int[] getObfuscatedArray(int offset) {
        Map<List<int[]>, Integer> map = new LinkedHashMap<>();
        // obfuscate numeric values from 0-9, specify range
        map.put(Arrays.asList(new int[] { 48, 57 }, new int[] { 48, 57 }), 0);
        // obfuscate upper case from A-Z, specify range
        map.put(Arrays.asList(new int[] { 'A', 'Z' }), 0);
        // obfuscate lower case from a-z, specify range
        map.put(Arrays.asList(new int[] { 97, 122 }), 0);

        //To replace vowels with vowels, you may override it like this or pass userMap as an argument
        //Note this way you get more control over the range that has gaps in ascii value
        //Also you can specify characters than its int/ascii/unicode value to make it more readable
//      map.put(new int[] {'a', 'e', 'i', 'o', 'u'}, 1);
//      map.put(new int[] {'A', 'E', 'I', 'O', 'U'}, 1);

        // obfuscate Devnagri.
        // Create multiple subset of characters that you want to Obfuscate by dividing it into multiple
        // chunks as demonstrated below. It will also yield better performance
        map.put(Arrays.asList(new int[] { 2309, 2361 }), 0);
        map.put(Arrays.asList(new int[] { 2392, 2401 }), 0);
        map.put(Arrays.asList(new int[] { 2406, 2415 }), 0);

        // obfuscate Arabic. Note here character sets are not divided into
        // multiple subset but you can do so as needed and extend similar logic for another language
        map.put(Arrays.asList(new int[] { 1536, 1791 }), 0);

        // Japanese
        map.put(Arrays.asList(new int[] { 12352, 12447 }), 0);

        // obfuscate Chinese. You can divide range into multiple smaller subsets as needed
        map.put(Arrays.asList(new int[] { 19968, 40959 }), 0);

        // You can add more character sets as desired as demonstrated above
        // using multiple examples. 
        // If certain range of character sets are not
        // handled in this method it will not participate in obfuscation
        // If you write or modify rules for any language, please share with me so that I can
        // include into the program for others to use.

        // For finding the character sets range refer the following link
        // https://www.ssec.wisc.edu/~tomw/java/unicode.html
        
        int[] obfuscatedOutputArray = new int[MAX_CHAR_INDEX];
        Arrays.setAll(obfuscatedOutputArray, i -> i++);
        processOutputCharArray(offset, map, obfuscatedOutputArray);
        processOutputCharArray(offset, this.userMap, obfuscatedOutputArray);
        return Arrays.copyOfRange(obfuscatedOutputArray, this.startRange, this.endRange+1); 
    }

    private void processOutputCharArray(int offset, Map<List<int[]>, Integer> map, int[] obfuscatedOutputArray)
    {
        for (Map.Entry<List<int[]>, Integer> entry : map.entrySet()) {
            if (entry.getValue() == 1) { // it means you have specific index whose value should be shuffled  
                int input[] = entry.getKey().get(0);
                int output[] = Arrays.copyOf(input, input.length);
                if (entry.getKey().size() > 1)
                    output = entry.getKey().get(1);
                prepareOutputCharArray(obfuscatedOutputArray, seedValue + offset, input, output);
            }
            else { // it means you have a range you want to shuffle
                int input[] = entry.getKey().get(0);
                int output[] = Arrays.copyOf(input, input.length);
                if (entry.getKey().size() > 1)
                    output = entry.getKey().get(1);
                prepareOutputCharArray(obfuscatedOutputArray, seedValue + offset, input[0], input[1], output[0], output[1]);
            }            
        }
    }

    private final String getObfuscated(char[] inputArray) {
        return getObfuscated(getArrayInPlay(inputArray), inputArray);
    }
    
    private final String getObfuscated(int[] arrayInPlay, char[] inputArray) {
        int size = arrayInPlay.length;
        char[] returnArray = new char[inputArray.length];
        for (int i = 0; i < inputArray.length; i++) {
            int index = inputArray[i] - this.startRange;
            if (index > -1 && index < size)
                returnArray[i] = (char) arrayInPlay[index];
            else
                returnArray[i] = (char)inputArray[i];
        }
        return new String(returnArray);
    }    
    private static int[] getRandomArray(Random random, int size) {
        int[] randomArray = new int[size];
        Arrays.setAll(randomArray, i -> i++);
        shuffleArray(randomArray, random);
        return randomArray;
    }

    private static void shuffleArray(int[] array, Random random) {
        for (int i = 0; i < array.length; i++) {
            int rand = random.nextInt(array.length);
            int temp = array[rand];
            array[rand] = array[i];
            array[i] = temp;
        }
    }     

    private static int[] prepareOutputCharArray(int[] obfuscatedOutputArray, long seed, int[] inputArray, int[] outputArray) {
        if (inputArray != null && inputArray.length >= 1 && outputArray != null && outputArray.length >= 1 && inputArray.length == outputArray.length) {
            shuffleArray(outputArray, new Random(seed));
            for (int i = 0; i < inputArray.length; i++) {
                obfuscatedOutputArray[inputArray[i]] = (char) outputArray[i];
            }
        }
        return obfuscatedOutputArray;
    }

    private static int[] prepareOutputCharArray(int[] obfuscatedOutputArray, long seed, int inFrom, int inTo, int outFrom, int outTo) {
        if (inFrom < inTo && outFrom < outTo && inFrom-inTo == outFrom-outTo) {
            int[] fillArray = getRandomArray(new Random(seed), (outTo - outFrom + 1));
            int startPoint = 0;
            for (int i = inFrom; i < (inTo + 1); i++) {
                obfuscatedOutputArray[i] = (char) fillArray[startPoint] + outFrom;
                startPoint++;
            }
        }
        return obfuscatedOutputArray;
    }
    public List<String> getObfuscated(List<String> input) {
        List<String> output = null;
        if (input == null || input.size() == 0)
            output = input;
        else {
            output = new ArrayList<>(input.size());
            int[] arrayInPlay = null;
            for (int i=0; i<input.size(); i++) {
                if (input.get(i) == null || input.get(i).length() == 0)
                    output.add(input.get(i));
                else {
                    if (arrayInPlay == null) arrayInPlay = getArrayInPlay(input.get(i).toCharArray());
                    output.add(getObfuscated(arrayInPlay, input.get(i).toCharArray()));
                }
            }
        }
        return output;
    } 
    private static final String getMatchedGroup(int group, Matcher m) {
        if (m.group(group) == null) return "";
        else return m.group(group);
    }
    public final String getObfuscatedBySpace(String input) {
        if (input == null || input.length() == 0)
            return input;
        else {
            StringBuilder stringBuilder = new StringBuilder(input.length());
            Matcher matcher = PATTERN_BY_SPACE.matcher(input);
            while (matcher.find()) {
                stringBuilder.append(getMatchedGroup(1, matcher));
                stringBuilder.append(getObfuscated(getMatchedGroup(2, matcher)));
                stringBuilder.append(getMatchedGroup(3, matcher));
            }
            return stringBuilder.toString();
        }
    }
    public final String getObfuscated(String input) {
        if (input == null || input.length() == 0)
            return input;
        else
            return getObfuscated(input.toCharArray());
    }
    public String getObfuscated(double input) {
        char[] inputArray = ("" + input).toCharArray();
        return getObfuscated(inputArray);
    }
    public String getObfuscated(long input) {
        return getObfuscated(("" + input).toCharArray());
    }
    public String getObfuscated(char input) {
        return getObfuscated("" + input);
    }
    
    public static void main(String[] args) {   
        System.out.println(200%1000);
//        long start = System.currentTimeMillis();       
        Map<List<int[]>, Integer> map = new LinkedHashMap<>();
//        map.put(Arrays.asList(new int[] { 'A', 'E', 'I', 'O', 'U' }), 1);
//        map.put(Arrays.asList(new int[] { 'a', 'e', 'i', 'o', 'u' }), 1);
//        map.put(Arrays.asList(new int[] {'1'}, new int[] {'1'}), 1);
//        map.put(Arrays.asList(new int[] {'1', '2','3','4','5','6','7','8','9'}), 1);
        PJObfuscateUtil obfuscateUtilObj = new PJObfuscateUtil(2336, map, 32, -1, true, 100);
        long start = System.currentTimeMillis();   
        String s1 = "(512) 507-2470";
        String s2 = "+ 1 512 507-2470";
        String s3 = "pankaj.johar@gmail.com";
        String s4 = "Pankaj.Johar@gmail.com";
        String s5 = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
        String s6 = "abcdefghijklmnopqrstuvwxyz";
        String s7 = "0123456789";
        String s8 = "123 Thank You १२३ शुक्रिया ありがとうございました 谢谢你";
        String s9 = "इतनी शक्ति हमें देना दाता, मन का विश्वास कमज़ोर हो ना";
        String s10 = "Pankaj";
        String s11 = "Johri";
        String s12 = "Pankaj Johri";        
        List<String> s13_list = Arrays.asList(s1, s2, s3);

        System.out.println(s1+" -> "+obfuscateUtilObj.getObfuscated(s1));
        System.out.println(s2+" -> "+obfuscateUtilObj.getObfuscated(s2));
        System.out.println(s3+" -> "+obfuscateUtilObj.getObfuscated(s3));
        System.out.println(s4+" -> "+obfuscateUtilObj.getObfuscated(s4));
        System.out.println(s5+" -> "+obfuscateUtilObj.getObfuscated(s5));
        System.out.println(s6+" -> "+obfuscateUtilObj.getObfuscated(s6));
        System.out.println(s7+" -> "+obfuscateUtilObj.getObfuscated(s7));
        System.out.println(s8+" -> "+obfuscateUtilObj.getObfuscated(s8));
        System.out.println(s9+" -> "+obfuscateUtilObj.getObfuscated(s9));
        
        System.out.println(s13_list+" -> "+obfuscateUtilObj.getObfuscated(s13_list));
        

        System.out.println(s10+" -> "+obfuscateUtilObj.getObfuscated(s10));
        System.out.println(s11+" -> "+obfuscateUtilObj.getObfuscated(s11));
        System.out.println(s12+" -> <"+obfuscateUtilObj.getObfuscated(s12)+">");
        System.out.println(s12+" -> <"+obfuscateUtilObj.getObfuscatedBySpace(s12)+">");
            
        
        long end = System.currentTimeMillis();
        System.out.println("Total time - " + (end - start) + " milli seconds");        
        
    }
}