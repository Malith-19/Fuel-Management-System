import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;

import static java.lang.System.exit;

public class ArraysVersion {
    private static String[] Q1;
    private static String[] Q2;
    private static String[] Q3;
    private static String[] ServedCustomers;

    private static int Q1Pointer;
    private static int Q2Pointer;
    private static int Q3Pointer;
    private static int ServedPointer;

    private static int fuelStock;
    private static int maxPerQueue = (int)(6600/10)/3;

    public static void main(String[] args) throws IOException {
        fuelStock = 6600;


        Q1 = new String[maxPerQueue];
        Q2 = new String[maxPerQueue];
        Q3 = new String[maxPerQueue];
        ServedCustomers = new String[1000];

        Q1Pointer = 0;
        Q2Pointer = 0;
        Q3Pointer = 0;
        ServedPointer = 0;




        while (true) {
            checkFuelStatus();
            System.out.println("************************************************************\n");
            System.out.println("FUEL QUEUE MANAGEMENT SYSTEM MENU OPTIONS.\n");
            System.out.println("100 or VFQ: View all Fuel Queues.");
            System.out.println("101 or VEQ: View all Empty Queues.");
            System.out.println("102 or ACQ: Add customer to a Queue.");
            System.out.println("103 or RCQ: Remove a customer from a Queue.");
            System.out.println("104 or PCQ: Remove a served customer.");
            System.out.println("105 or VCS: View Customers Sorted in alphabetical order.");
            System.out.println("106 or SPD: Store Program Data into file.");
            System.out.println("107 or LPD: Load Program Data from file.");
            System.out.println("108 or STK: View Remaining Fuel Stock.");
            System.out.println("109 or AFS: Add Fuel Stock.");
            System.out.println("999 or EXT: Exit the Program.");
            System.out.println("\n************************************************************");

            Scanner input = new Scanner(System.in);
            System.out.println("\nSelect an option from the menu above to proceed: ");
            String customerOption = input.nextLine();


            switch (customerOption) {
                case "100", "VFQ" -> ArraysVersion.displayAllQueues();
                case "101", "VEQ" -> ArraysVersion.displayEmptyQueues();
                case "102", "ACQ" -> ArraysVersion.addCustomer(input);
                case "103", "RCQ" -> ArraysVersion.removeCustomer(input);
                case "104", "PCQ" -> ArraysVersion.removeServedCustomer(input);
                case "105", "VCS" -> ArraysVersion.viewCustomersSorted();
                case "106", "SPD" -> ArraysVersion.storeFile();
                case "107", "LPD" -> ArraysVersion.loadFile();
                case "108", "STK" -> ArraysVersion.viewStock();
                case "109", "AFS" -> ArraysVersion.addStock(input);
                case "999", "EXT" -> ArraysVersion.exitProgram();
                default -> System.out.println("\nInvalid Selection, Try Again.");
            }
//            break;
        }
    }

    //default methods
    private static void displayAllQueues(){
        displayOneQueue(Q1,"1");
        displayOneQueue(Q2,"2");
        displayOneQueue(Q3,"3");
    }


    private static void displayEmptyQueues(){
        if(checkEmpty(Q1)){
            System.out.println("Queue no.1 is empty!");
        }
        if(checkEmpty(Q2)){
            System.out.println("Queue no.2 is empty!");
        }
        if(checkEmpty(Q3)){
            System.out.println("Queue no.3 is empty!");
        }
    }

    private static void addCustomer(Scanner input){

        System.out.println("Enter the customer's name: ");
        String name = input.nextLine();


        int minNumber = getMinNumberFromThree(Q1Pointer,Q2Pointer,Q3Pointer);
        if(minNumber==Q1Pointer){
            Q1[Q1Pointer] = name;
            Q1Pointer++;
        }else if(minNumber==Q2Pointer){
            Q2[Q2Pointer] = name;
            Q2Pointer++;
        }else{
            Q3[Q3Pointer] = name;
            Q3Pointer++;
        }

        fuelStock -= 10;

        System.out.println("Customer added successfully!");

    }

    private static void removeCustomer(Scanner input){
        System.out.println("Enter the queue number: ");
        int queueNumber = input.nextInt();

        System.out.println("Enter the location: ");
        int location = input.nextInt();
        String name = "";

        switch (queueNumber){
            case 1:
                name = Q1[location];
                Q1 = adjustQueue(Q1,Q1Pointer,location);
                Q1Pointer--;
                break;
            case 2:
                name = Q2[location];
                Q2 = adjustQueue(Q2,Q2Pointer,location);
                Q2Pointer--;
                break;
            case 3:
                name = Q3[location];
                Q3 = adjustQueue(Q3,Q3Pointer,location);
                Q3Pointer--;
                break;
        }
        ServedCustomers[ServedPointer] = name;
        ServedPointer++;
        fuelStock -= 10;


    }

    private static void removeServedCustomer(Scanner input){
        System.out.println("Enter the location: ");
        int location = input.nextInt();

        ServedCustomers = adjustQueue(ServedCustomers,ServedPointer,location);
        ServedPointer--;

        System.out.println("Served customer in that location removed successfully!\n");
    }

    private static void viewCustomersSorted(){
        String[] allQueues = joinAllQueues();
        String[] sorted = sortArray(allQueues);
        displayOneQueue(sorted,"1,2,3");
    }

    private static void storeFile() throws IOException {
        FileWriter Q1File = new FileWriter("Q1.txt");
        Q1File.write(arrayToString(Q1));
        Q1File.close();

        FileWriter Q2File = new FileWriter("Q2.txt");
        Q2File.write(arrayToString(Q2));
        Q2File.close();

        FileWriter Q3File = new FileWriter("Q3.txt");
        Q3File.write(arrayToString(Q3));
        Q3File.close();

        FileWriter servedFile = new FileWriter("served.txt");
        servedFile.write(arrayToString(ServedCustomers));
        servedFile.close();
    }

    private static void loadFile(){
        Q1 = stringToArray("Q1.txt");
        Q2 = stringToArray("Q2.txt");
        Q3 = stringToArray("Q3.txt");
        ServedCustomers = stringToArray("served.txt");

        Q1Pointer = getQueueSize(Q1);
        Q2Pointer = getQueueSize(Q2);
        Q3Pointer = getQueueSize(Q3);
        ServedPointer = getQueueSize(ServedCustomers);


    }

    private static void viewStock(){
        System.out.println(String.valueOf(fuelStock)+" of litres left in the tank!");
    }

    private static void addStock(Scanner input){
        System.out.println("Enter the fuel amount adding: ");
        int fuelAdding = input.nextInt();
        fuelStock += fuelAdding;
    }

    private static void exitProgram(){
        exit(0);
    }


    //helping methods

    private static void displayOneQueue(String[] Q,String queueNumber){
        if(checkEmpty(Q)){
            System.out.println("Queue no."+queueNumber+" is empty!");
        }else{
            System.out.println("Customer name list of Queue no."+queueNumber);
            int count = 0;
            for(String s:Q){
                count++;
                if(s==null){
                    break;
                }else{
                    System.out.println(String.valueOf(count)+". "+s);
                }

            }
        }



    }

    private static boolean checkEmpty(String[] Q){
        if(Q[0] == null){
            return true;
        }else{
            return false;
        }
    }

    private static int getMinNumberFromThree(int n1,int n2,int n3){
        int[] numbers = {n1,n2,n3};
        int minNumber = 1000;
        for(int number:numbers){
            if(number<minNumber){
                minNumber = number;
            }
        }
        return minNumber;
    }

    private static void checkFuelStatus(){
        if(fuelStock==500){
            System.out.println("Warning! only 500 litres remaining!\n");
        }
    }

    private static String[] adjustQueue(String[] Q,int pointer,int currentPosition){


        for(int i=currentPosition;i<=pointer+1;i++){
            Q[i] = Q[i+1];
        }
        return Q;
    }

    private static String arrayToString(String[] array){
        String output = "";

        for(String name:array){
            if(name == null){
                break;
            }else{
                output += name+"\n";
            }
        }
        return output;
    }

    private static String[] stringToArray(String filename) {
        String[] array = new String[maxPerQueue];
        try {
            File Obj = new File(filename);
            Scanner Reader = new Scanner(Obj);
            int counter = 0;

            while (Reader.hasNextLine()) {
                String data = Reader.nextLine();
                array[counter] = data;
                counter++;
//                System.out.println(data);
            }
            Reader.close();
        } catch (FileNotFoundException e) {
            System.out.println("An error has occurred.");
            e.printStackTrace();
        }

        return array;
    }

    private static String[] sortArray(String[] array){
        int size = getQueueSize(array);

        for(int i = 0; i<size-1; i++) {
            for (int j = i+1; j<size; j++) {
                if(array[i].compareTo(array[j])>0) {
                    String temp = array[i];
                    array[i] = array[j];
                    array[j] = temp;
                }
            }
        }
        return array;
    }

    private static String[] joinAllQueues(){
        String[] array = new String[maxPerQueue];
        int counter = 0;
        for(String name:Q1){
            if(name == null){
                break;
            }else{
                array[counter] = name;
                counter++;
            }
        }
        for(String name:Q2){
            if(name == null){
                break;
            }else{
                array[counter] = name;
                counter++;
            }
        }

        for(String name:Q3){
            if(name == null){
                break;
            }else{
                array[counter] = name;
                counter++;
            }
        }
//        System.out.println(Arrays.toString(array));
        return array;
    }

    private static int getQueueSize(String[] array){
        int counter = 0;
        for(String name:array){
            if(name == null){
                return counter;
            }else{
                counter++;
            }
        }

        return counter;
    }
}