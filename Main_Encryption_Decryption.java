import java.io.*;
import java.util.*;

public class Main_Encryption_Decryption {
    private static Scanner reader;
    private static String fileDirectory;
    private static String fileName;
    private static String outputDirectory = null;
    private static String outputFile = "TranspositionResults.txt";
    public static void main(String[] args) {
        readFile();
        System.out.println("Encryption/Decryption Process is complete");
    }

    //readFile asks the user where's the file that has the encryption/decryption content
    public static void readFile() {
        System.out.println("Please Enter the directory of the text file you would like to encrypt or decrypt (without the filename)");
        Scanner keyboard = new Scanner(System.in);
        fileDirectory = keyboard.nextLine();
        System.out.println("Please enter the name of the file you are trying to encrypt or decrypt (Including the extension)");
        fileName = keyboard.nextLine();

        /*the Program then tries to iterate through each line and for each line it parses the line into 2 categories the first letter followed by the
        2 column transposition operations it will perform then the string it will encrypt or decrypt and saves it into operationtype and content respectively
        and calls the either encryptString or decryptString method according to the first letter in the operation type. It does this for every line in the text file
        */
        try {
            reader = new Scanner(new File(fileDirectory + "\\" + fileName));
            while (reader.hasNext()) {
                String operationType = reader.next().toUpperCase();
                String content = reader.next().toUpperCase();
                if (operationType.charAt(0) == 'E') {
                    System.out.println("Encryption method called");
                    encryptString(operationType, content);

                } else if (operationType.charAt(0) == 'D') {
                    System.out.println("Decryption method called");
                    decryptString(operationType, content);
                } else {
                    System.out.println("It seems the operation type letter " + operationType.charAt(0) + " is not a valid operation, please check the operation type for the string " + content + " and try again");
                    return;
                }
            }
            reader.close();
        } catch (Exception e) {
            System.out.println("Error! Could not find file, please try again");
            readFile();
        }
    }

    //encryptString parses the firs column and second column seperatley into firstColumnEncryption and secondColumnEncryption in order
    public static void encryptString(String readFileOperation, String readFileContent) throws IOException {
        int firstColumnEncryption = Character.getNumericValue(readFileOperation.charAt(1));
        int secondColumnEncryption = Character.getNumericValue(readFileOperation.charAt(2));
        int numOfRows;

        //checks if number of first string length is divisible b the number of columns for first operation, if not then round up and you will have the number of rows
        if (readFileContent.length() % firstColumnEncryption != 0) {
            numOfRows = (readFileContent.length() / firstColumnEncryption) + 1;
            //since length of string != the number of elements in our 2d array we must pad it by dummy letters to match the difference between the elemnents and string length
            int filler = (firstColumnEncryption * numOfRows) - readFileContent.length();
            for (int i = 0; i < filler; i++) {
                readFileContent += 'Z';
            }
            //else no need for padding and just set row size relative to the division
        } else {
            numOfRows = (readFileContent.length() / firstColumnEncryption);
        }
        //we begin populating the string in the first 2d array relative to the row x collumn size we obtained above, charcounter will help us keep track of parsing the string
        //into characters
        int charCounterFileContent = 0;
        char[][] unEncrypted2D = new char[numOfRows][firstColumnEncryption];
        String encryptedStringFirstPass = "";
        for (int i = 0; i < numOfRows; i++) {
            for (int j = 0; j < firstColumnEncryption; j++) {
                unEncrypted2D[i][j] = readFileContent.charAt(charCounterFileContent);
                charCounterFileContent++;
            }
        }
        //now we transpose the 2d array and read each row for every column appending it to a string variable with each iteration, we reset the character counter after finish
        for (int i = 0; i < firstColumnEncryption; i++) {
            for (int j = 0; j < numOfRows; j++) {
                encryptedStringFirstPass += unEncrypted2D[j][i];

            }
        }
        charCounterFileContent = 0;
        System.out.println("Encryption first pass completed, starting second encryption pass");

        //starting second column encryption by doing what we did above however this time we will use the new string we obtained as well as the second number of columns provided
        if (encryptedStringFirstPass.length() % secondColumnEncryption != 0) {
            numOfRows = (encryptedStringFirstPass.length() / secondColumnEncryption) + 1;
            int filler = (secondColumnEncryption * numOfRows) - encryptedStringFirstPass.length();
            for (int i = 0; i < filler; i++) {
                encryptedStringFirstPass += 'Z';
            }
        } else {
            numOfRows = (encryptedStringFirstPass.length() / secondColumnEncryption);
        }
        char[][] Encrypted2D = new char[numOfRows][secondColumnEncryption];
        //populate string into new 2d array
        for (int i = 0; i < numOfRows; i++) {
            for (int j = 0; j < secondColumnEncryption; j++) {
                Encrypted2D[i][j] = encryptedStringFirstPass.charAt(charCounterFileContent);
                charCounterFileContent++;
            }
        }
        String finalEncryptedString = "";

        //read the 2d array row by row for every column appending to a final encrypted string var until the iteration has finished
        for (int i = 0; i < secondColumnEncryption; i++) {
            for (int j = 0; j < numOfRows; j++) {
                finalEncryptedString += Encrypted2D[j][i];

            }
        }
        System.out.println("Encryption final pass completed, sending encrypted file to text File");
        //we output the final string to a method where it wll be appended to an external text file
        outputToTextFile(finalEncryptedString);
    }
    //pretty much does the same thing as the encryption method except that it does everything in reverse soooooooo yea
    public static void decryptString(String readFileOperation, String readFileContent) throws IOException {
        int firstrowDecryption = Character.getNumericValue(readFileOperation.charAt(1));
        int secondRowDecryption = Character.getNumericValue(readFileOperation.charAt(2));
        int numOfColumns;
        //I'll be honest I'm sure this if statement does nothing but I'm scared to take it out so we'll leave it
        if(readFileContent.length() % firstrowDecryption != 0){
            numOfColumns = ((readFileContent.length() / firstrowDecryption) + 1);
        }
        else{
            numOfColumns = (readFileContent.length() / firstrowDecryption);
        }
        int charaCounterFileContent = 0;
        char[][] unDecrypted2D = new char[firstrowDecryption][numOfColumns];
        String decryptedStringFirstPass = "";
        for(int i = 0; i < firstrowDecryption;i++){
            for (int j = 0; j < numOfColumns; j++){
                unDecrypted2D[i][j] = readFileContent.charAt(charaCounterFileContent);
                charaCounterFileContent++;
            }
        }
        for(int i = 0; i < numOfColumns; i++){
            for (int j = 0; j < firstrowDecryption; j++){
                decryptedStringFirstPass += unDecrypted2D[j][i];
            }
        }
        charaCounterFileContent = 0;
        System.out.println("Decryption first pass completed, starting second Decryption pass");


        if(decryptedStringFirstPass.length() % secondRowDecryption != 0){
            numOfColumns = ((decryptedStringFirstPass.length() / secondRowDecryption));
        }
        else{
            numOfColumns = (decryptedStringFirstPass.length() / secondRowDecryption);
        }
        char[][] Decrypted2D = new char[secondRowDecryption][numOfColumns];
        String decryptedStringFinalPass = "";
        for(int i = 0; i < secondRowDecryption;i++){
            for (int j = 0; j <numOfColumns; j++){
                Decrypted2D[i][j] = decryptedStringFirstPass.charAt(charaCounterFileContent);
                charaCounterFileContent++;
            }
        }
        for(int i = 0; i < numOfColumns; i++){
            for (int j = 0; j < secondRowDecryption; j++){
                decryptedStringFinalPass += Decrypted2D[j][i];
            }
        }
        charaCounterFileContent = 0;
        System.out.println("Decryption final pass completed, outputing final Decryption to text file");
        outputToTextFile(decryptedStringFinalPass);
    }

    //asks where the user would like to create a text file then creates it for them, if the file exists in the directory already then it appends to it
    public static void outputToTextFile(String stringToAppend) throws IOException {
        if(outputDirectory == null){
            System.out.println("Please enter the directory where you would like to create a file");
            Scanner directoryInput = new Scanner(System.in);
            outputDirectory = directoryInput.nextLine();
            System.out.println("The File " +outputFile +" has been created in the directory "+outputDirectory);

        }
        File output = new File(outputDirectory+"\\"+outputFile);
        FileWriter writer = new FileWriter(output, true);
        BufferedWriter buffer = new BufferedWriter(writer);
        PrintWriter pWriter = new PrintWriter(buffer);

        if(!output.exists()){
            output.createNewFile();
        }
        else{
            System.out.println("Appending to file");
        }
        pWriter.println(stringToAppend);
        pWriter.close();
    }


}