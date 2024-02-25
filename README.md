# Java-Obfuscation
Java utility to Obfuscate any kind of sensitive data. Data obfuscation is the process of replacing sensitive information with data that looks like real production information, making it useless to malicious actors. It is primarily used to test test software, Data masking, encryption, and tokenization are three common data obfuscation techniques.

This utility provides Data obfuscation (or data anonymization) using data masking techniques (replacing the real data with randomly generated fake data). You may create as many combinations as you want, the default is set to 200. There are two majors advantages with this technique: -
Masked data is still usable in its obfuscated form and maintains the data integrity
Once data is masked, the original values cannot be recovered.

Since this is a lightweight standalone Java program, it needs to be integrated with your ETL or any other system. Any values you wish to configure will be passed to this program as constructor argument so you can maintain the way you wish without any additional overhead. You only need to have either JRE or JDK for this program to work.

For the data integrity, you must remember to use the exactly same version of the class along with exactly the same parameters passed through it constructor. At any point in time you take the newer version or modify its parameters, you must ensure to regenerate the obfuscated values across the entire dataset. 

One additional note that this program will yield the same performance, threadsafe and you can obfuscate (any kind of sensitive data such as email, first/last name, phone number etc) billions on records in just few minutes.

Without taking further time, let us understand how to use this utility and how configure it for your business needs.
