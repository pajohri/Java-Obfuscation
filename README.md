# Java - Data Obfuscation Through Masking (J-DOTM) Utility
Java Data Obfuscation Through Masking (J-DOTM) is a utility to Obfuscate any kind of sensitive data. Data obfuscation is the process of replacing sensitive information with data that looks like real production information, making it useless to malicious actors. It is primarily used to test the software.
_Data masking_, _encryption_, and _tokenization_ are three common data obfuscation techniques.

This utility provides Data obfuscation (or data anonymization) using data masking techniques (_replacing the real data with randomly generated fake data_). You may create as many combinations as you want, the default is set to 200. There are following majors advantages with this technique: -

1. Masked data is usable in its obfuscated form
2. It also maintains the data integrity
3. Mask values cannot be recovered.

Since this is a lightweight standalone Java utility, it needs to be integrated with your ETL or any other system. Any values you wish to configure will be passed to this utility as constructor argument so you can maintain the configuration values the way you wish without any additional overhead. You only need to have either JRE or JDK version 8 or above for this utility to work.

For the data integrity, you must remember to use the exactly same version of the utility along with exactly the same parameters passed through its constructor. At any point in time you take the newer version or modify its parameters, you must ensure to regenerate the obfuscated values across the entire dataset. 

This utility is highly performant, it is thread-safe and you can obfuscate (_any kind of sensitive data such as email, first/last name, phone number etc_) billions of records in just few minutes.

Without taking further time, let us understand how to use this utility and how to configure it for your need


**QUESTION 1 - How to obfuscate  sensitive data such as email, first/last name, phone number?**

See working example below.

        String fn = "James";
        String ln = "Donald";
        String fullName = "James Donald";
        String email = "James.Donald@testdomain.com";     
        String phone = "(512) 788-9999";     
        //instantiate the class by passing a seed value. Seed value will ensure you get the same result. 
        //If you change this seed value to a new number you will get a new set of output
        PJObfuscateUtil obfuscateUtil = new PJObfuscateUtil(100);
        //invoke getObfuscated method to reterive the obfucated value
        System.out.println(fn+" -> "+obfuscateUtil.getObfuscated(fn));
        System.out.println(ln+" -> "+obfuscateUtil.getObfuscated(ln));
        System.out.println(fullName+" -> "+obfuscateUtil.getObfuscated(fullName));        
        System.out.println(email+" -> "+obfuscateUtil.getObfuscated(email));
        System.out.println(phone+" -> "+obfuscateUtil.getObfuscated(phone));


        Here is the output I got from this utility: -
        James -> Mtypx
        Donald -> Gjonbg
        James Donald -> Zbism Kqhbxk
        James.Donald@testdomain.com -> Mdgzl.Qhxdnq@fzlfqhgdrx.shg
        (512) 788-9999 -> (341) 022-9999


**QUESTION 2 - In the example above, why the first name, last name and full name does not match (_it may break the data integrity_). James converted to _Mtypx_, Donald convert to _Gjonbg_, I would expect _James Donald_ should be “_Mtypx Gjonbg_” instead of “_Zbism Kqhbxk_”?**

As I mentioned above, the default combinations are set to 200 which means whenever you pass any input value to getObfuscated() method, for each input it will calculate a hash between 0 and 200 (_whatever number of combinations you have configured, default is 200_). Based on the calculated hash, it will decide which combination to use. Since the calculated hash is different for “_James_”, “_Donald_” and “_James Donald_”, combination selected to obfuscate the input value is also different. Hence generated values are different.

**QUESTION 3 - Can I set the combination size from default 200 to 1 to ensure it generates the same obfuscated value the each time?**

Yes you can and value can be passed an an argument to its constructors _(see question 9 below for constructor argument)_  but it is not **recommended**. If somebody can find that ‘J’ is converted to ‘M’ and ‘a’ to ‘t’ and so on, it will be very easy to reverse engineer and malicious actors can hack the system which definitely you don’ t want to do. See the next question on how to deal with this situation.

**QUESTION 4 - I have a database table that has three columns, first_name, last_name and full_name. To ensure data integrity I need to ensure full_name should match with first_name/last_name individually, how can I achieve it?**

To get the desired output, for the full name, you need to use a utility method getObfuscatedBySpace(String). Here is the working example

        String fn = "James";
        String ln = "Donald";
        String fullName = "James Donald";
        String email = "James.Donald@testdomain.com";     
        String phone = "(512) 788-9999";     
        PJObfuscateUtil obfuscateUtil = new PJObfuscateUtil(100);
        System.out.println(fn+" -> "+obfuscateUtil.getObfuscated(fn));
        System.out.println(ln+" -> "+obfuscateUtil.getObfuscated(ln));
        System.out.println(fullName+" -> "+obfuscateUtil.getObfuscatedBySpace(fullName));        
        System.out.println(email+" -> "+obfuscateUtil.getObfuscated(email));
        System.out.println(phone+" -> "+obfuscateUtil.getObfuscated(phone));        

**QUESTION 5 - I tried to use this utility and noticed that obfuscated values of “_James_” and “_james_” are not exactly same and it will break the data integrity, what should I do?**

You can use another constructor argument value set to _true_, here is an example. Notice the second argument _isCaseInsensitiveObfuscate_ which is set to **true** (default value is **false**). If you set this value to _true_, it will ensure upper and lower case will always yeield the same result.

        String name1 = "JAMES";
        String name2 = "james";
        PJObfuscateUtil obfuscateUtil = new PJObfuscateUtil(100, true);
        System.out.println(name1+" -> "+obfuscateUtil.getObfuscated(name1));
        System.out.println(name2+" -> "+obfuscateUtil.getObfuscated(name2)); 

**QUESTION 6 - Our production database has redundant data, especially the phone number formats are different in different places. For example one table has phone number as  _(123) 456-7890_ while another table has _+1 1234567890_. Is there a way to ensure same obfuscated value is being generated to ensure the data integrity?**

Yes there is a way. You have to pass all the values in a list to ensure same hash is being used across all the values in that list to yield the same result. Here is an example. Return value will also be List of String values

        String ph1 = "(123) 456 7890";
        String ph2 = "+1 1234567890";
        PJObfuscateUtil obfuscateUtil = new PJObfuscateUtil(100, true);
        System.out.println("Result -> "+obfuscateUtil.getObfuscated(Arrays.asList(ph1, ph2)));


**QUESTION 7 - I noticed there is another utility method that you called out above _getObfuscatedBySpace(String)_, how is this different from passing a list of elements?**

Good question. When you pass list of String elements, you essentially use the same hash value that is being generated from the first list element to the rest of all the remaining list values.

But when you use   _getObfuscatedBySpace_() method instead, each word between white spaces are treated as individual input value that can yield separate hash value. You have to decide which option is suited for your need and invoke the right method accordingly. 

In ideal situation if your database does not have redundant values you will never need to use any of these getObfuscatedBySpace() or  getObfuscated(List<String>) methods. They both should be considered as the helper methods.

**QUESTION 8 - Is this utility thread-safe?**

Yes, you can invoke it simultaneously from multiple threads or processes. Object is also immutable in nature.

**QUESTION 9 - In the examples above I noticed for phone numbers, characters like _‘(‘_, _‘)’_, _‘+’_ did not change. Although this looks correct to maintain the readability but can I change the default rules**

Yes you can overwrite the rules as per your need. 

        Let us take one use case
        1. You want vowels should be randomly shuffled with another vowels only
        2. Numbers should be randomly shuffled with another number only 
           but 0 value should remain unchanged
        3. You want only 100 total combination (default is set to 200)
        4. You want the utiity to work only for US keyboard character 
           only (ascii from  32 to 127)
        5. You want to replace characters ‘(‘, ‘)’ with ‘|’
        
Here is the full code for the above use case: -  

        Map<List<int[]>, Integer> map = new LinkedHashMap<>(); // initialize the userMap 
        // randomly shuffled vowels only uppercase, note second parameter as 1
        map.put(Arrays.asList(new int[] { 'A', 'E', 'I', 'O', 'U' }), 1); 
        // randomly shuffled vowels only lowercase, note second parameter as 1
        map.put(Arrays.asList(new int[] { 'a', 'e', 'i', 'o', 'u' }), 1); 
        // ensure 0 is replaced with 0
        map.put(Arrays.asList(new int[] { '0' }), 1); 
        // Replace ‘(‘, ‘)’ with ‘|’. In this case you also have to pass the
        // optional second int[] and size must match. That is why I added '|' two times
        map.put(Arrays.asList(new int[] { '(', ')' }, new int[] { '|', '|'}), 1); 
        //instantiate the utility
        PJObfuscateUtil obfuscateUtilObj = new PJObfuscateUtil(12345, map, 32, 127, true, 100); 
        String s1 = "(800) 334-5343";
        String s2 = "John j Jonny";
        String s3 = "Mike.cater@Gmail.Com";
        System.out.println(s1 +" -> "+obfuscateUtilObj.getObfuscated(s1));
        System.out.println(s2 +" -> "+obfuscateUtilObj.getObfuscated(s2));
        System.out.println(s3+ " -> "+obfuscateUtilObj.getObfuscated(s3));

Description of the parameters passed to the constructors **_new PJObfuscateUtil(12345, map, 32, 127, true, 100)_** : -

        1. 1st Parameter is the seed value (that can be any long number)
        2. 2nd Parameter is the  LinkedHashMap. You can use it to overwrite the default 
           rules for your need. In the int array you can either pass characters itself
           or its ASCII or Unicode value or the combination of character, ASCII, unicode
        3. 3rd and 4th parameters are start and end Index. It means only characters whose
           ASCII value falls in the range of 32 and 127 will be obfuscated and rest of 
           them will be ignored or remain unobfuscated. Passing the start and end index
           is critical to limit the memory footprint. The bigger the difference between 
           start and end index, higher will be the memory footprint but it does not have
           any impact on the performance.
        4. 5th parameter is isCaseInsensitiveObfuscate. If you wish to convert small and
           cap characters with same random character then pass this value as true
        5. 6th parameter is maxCombination, it tells how many sets of different random 
           data is maintained by the utility. The higher the number, higher will be the
           memory footprint but it does not have any impact on the performance.

**QUESTION 10 - Does it support _multilingual_ characters?**

Yes it does, you can use any language or characters as you wish. Please remember to specify the correct start or end index value else outside range characters will not be obfuscated. For details around character set and its ASCII/Unicode value, please refer this page

        https://www.ssec.wisc.edu/~tomw/java/unicode.html

For the multilingual support, please note that I have implemented only for English and Devanagri. For rest of the languages, you can either add rules into getObfuscatedArray() method or pass it as map as constructor argument. I have not implemented this as I believe for each language you will like to replace numbers with numbers, upper-case to upper-case and lower-case to lower-case. There could be more semantics specific to the language. Since I am not an expert so decided to keep the rules empty and will look forward for your help and support and update the rules eventually.

I have also added very basic rules for Arabic, Japanese, and Chinese language just to show how simple it is, without going into language specific intricacies. If you need any help and support please send me an email.


**QUESTION 11 - Is this utility free to use?**

YES but I appreciate your feedback or your company name for the reference purpose only.

**QUESTION 12 - Do you provide professional support if required?**

YES, you can reach out directly to my email pajohri@yahoo.com

