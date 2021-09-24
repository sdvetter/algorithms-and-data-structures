package Oblig3.Oppgave1;

import java.lang.reflect.InvocationTargetException;
import java.util.List;
import java.util.Scanner;

public class Oppgave1 {

    public static void main(String[] args) {
        Scanner s = new Scanner(System.in);
        DoublyLinkedList dll1 = new DoublyLinkedList();
        DoublyLinkedList dll2 = new DoublyLinkedList();


        System.out.println("Write numbers and operation, ex: 12 + 6 ");
        String input = s.nextLine();
        String[] split = input.split("\\s+");


        // Creates lists from input
        for (int i = 0; i < split[0].length(); i++) {
            dll1.insertBehind(split[0].charAt(i) - '0');
        }
        for (int i = 0; i < split[2].length(); i++) {
            dll2.insertBehind(split[2].charAt(i) - '0');
        }


        if (split[1].equals("+")) {
            printList(listAddition(dll1, dll2).getHead());
        } else if (split[1].equals("-")){
            printList(listSubtraction(dll1, dll2).getHead());
        } else System.out.println(" Skriv inn +/- som operasjon");


        s.close();
    }

    // Method for addition of lists
    public static DoublyLinkedList listAddition(DoublyLinkedList dll1, DoublyLinkedList dll2) {
        DoublyLinkedList addedList = new DoublyLinkedList();

        // Base cases
        if (dll1 == null) {
            return dll2;
        }
        if (dll2 == null) {
            return dll1;
        }

        int size1 = dll1.getNrElementes();
        int size2 = dll2.getNrElementes();

        // Makes lists the same size by inserting 0 infront of the smaller list
        if (size1 > size2) {
            for (int i = 0; i < (size1 - size2); i++) {
                dll2.insertInfront(0);
            }
        } else if (size2 > size1) {
            for (int i = 0; i < (size2 - size1); i++) {
                dll1.insertInfront(0);
            }
        }

        int storedValue = 0;            // The takeaway or overflow of previously added numbers
        Node tempNode = dll1.getTail(); // used to iterate through first list
        Node n = dll2.getTail();        // used to iterate through second list
        while (tempNode != null) {

            int currentValue = tempNode.getValue() + n.getValue() + storedValue;
            if (currentValue < 10) {                                 // Simply adds number to list if sum of each node is less then 10
                addedList.insertInfront(currentValue);
                storedValue = 0;                                     // Set overflow as 0
            } else {
                addedList.insertInfront(currentValue % 10);    // Adds the remainder to the list and increments the stored value for next loop
                storedValue = 1;
            }
            tempNode = tempNode.previous;
            n = n.previous;
        }

        if (storedValue > 0){                       // Checks if we still have an overflow for last digit
            addedList.insertInfront(1);
        }
        return addedList;
    }


    // Method for subtraction of lists
    public static DoublyLinkedList listSubtraction(DoublyLinkedList dll1, DoublyLinkedList dll2){
        DoublyLinkedList subtractedList = new DoublyLinkedList();
        boolean s = false;      // Used to check if size of list 1 is smaller than list 2

        // Base cases
        if (dll1 == null) {
            return dll2;
        }

        if (dll2 == null) {
            return dll1;
        }

        int size1 = dll1.getNrElementes();
        int size2 = dll2.getNrElementes();

        // Makes lists the same size
        if (size1 > size2) {
            for (int i = 0; i < (size1 - size2); i++) {
                dll2.insertInfront(0);
            }
        } else if (size1 < size2) {
            s = true;
            for (int i = 0; i < (size2 - size1); i++) {
                dll1.insertInfront(0);
            }
        }
        int storedValue = 0;
        Node n1 = dll1.getTail();  // used to iterate through first list
        Node n2 = dll2.getTail();  // used to iterate through second list
        Node n3 = dll1.getHead();  // used to get the head of the list
        Node n4 = dll2.getHead();

        boolean r = true;
        int c = 0;
        while (n1 != null) {

            while (r){
                if (n3.getValue() > n4.getValue()){     // Check if first digit in list 1 is greater than in list 2
                    c = 1;
                    r = false;
                } else if (n3.getValue() < n4.getValue()){  // Check if first digit in list 1 is lesser than in list 2
                    c = -1;
                    r = false;
                } else if (n3.next == null && n3.getValue() == n4.getValue()){  // if list 1 and 2 are equal
                    c = 0;
                    r = false;
                } else {
                    n3 = n3.next;
                    n4 = n4.next;
                }
            }

            int currentValue = (n1.getValue() - storedValue) - n2.getValue();  // Calculate what is subtracted and how much that will be added
            int cv2 = (n2.getValue() - (n1.getValue() + storedValue));

            if (c == 1) {            // dll1 is largest, follow standard subtraction procedure
                if (currentValue < 0) {
                    subtractedList.insertInfront(10 + currentValue);    // the digit will be negative, so we takeaway from next digit in number
                    storedValue = 1;                                          // Indicates that we need to takeaway, it will be summed next loop
                } else {
                    subtractedList.insertInfront(currentValue);               // Reset takeaway index
                    storedValue = 0;
                }
            } else if (c == -1) {   // dll2 is largest, therefore we expect a negative answer
                s = true;
                if (n1.previous == null && n2.getValue() - n1.getValue() - storedValue == 0){} // Used to check if first digit in list is 0, blocking the toNegative() in line174
                else if (cv2 < 0) {
                    subtractedList.insertInfront((10 + cv2));
                    storedValue = 1;
                } else {
                    subtractedList.insertInfront(cv2);
                    storedValue = 0;
                }
            } else {        // If the lists are same sized
                subtractedList.insertInfront(0);
                return subtractedList;
            }

            n1 = n1.previous;   // iterate
            n2 = n2.previous;
        }

        if (s){
            subtractedList.getHead().toNegative();      // if list 1 was smaller than list 2, we need to make it negative
        }

        return subtractedList;
    }


    public static void printList(Node n){
        Node temp = n;
        while (temp != null) {
            System.out.print(temp.value + " ");
            temp = temp.next;
        }
    }

}







