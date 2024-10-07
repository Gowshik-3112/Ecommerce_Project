package Arraylist;
import java.util.ArrayList;
import java.util.InputMismatchException;
import java.util.Scanner;

public class Voter {
    private String candidateName;
    private String voterID;
    private int age;

    public Voter(String candidateName, String voterID, int age) {
        this.candidateName = candidateName;
        this.voterID = voterID;
        this.age = age;
    }

    public String getCandidateName() { return candidateName; }
    public String getVoterID() { return voterID; }
    public int getAge() { return age; }

    public void displayVoterInfo() {
        System.out.println("Candidate Name: " + candidateName + ", Voter ID: " + voterID + ", Age: " + age);
    }
}

class VoterList {
    private ArrayList<Voter> voters;

    public VoterList() {
        voters = new ArrayList<>();
    }

    public void addVoter(String candidateName, String voterID, int age) {
        if (age >= 18) {
            Voter newVoter = new Voter(candidateName, voterID, age);
            voters.add(newVoter);
            System.out.println("Voter added successfully!");
        } else {
            System.out.println("Age must be 18 or older to register as a voter.");
        }
    }

    public void displayAllVoters() {
        System.out.println("\nVoter List:");
        for (Voter voter : voters) {
            voter.displayVoterInfo();
        }
    }

    public static void main(String[] args) {
        VoterList voterList = new VoterList();
        Scanner scanner = new Scanner(System.in);
        int choice = 0;

        do {
            System.out.println("\n--- Voter List Menu ---");
            System.out.println("1. Add Voter");
            System.out.println("2. Display All Voters");
            System.out.println("3. Exit");
            System.out.print("Enter your choice: ");

            try {
                choice = scanner.nextInt();
                scanner.nextLine(); // Consume newline

                switch (choice) {
                    case 1:
                        System.out.print("Enter Candidate Name: ");
                        String candidateName = scanner.nextLine();
                        System.out.print("Enter Voter ID: ");
                        String voterID = scanner.nextLine();
                        System.out.print("Enter Voter Age: ");

                        try {
                            int age = scanner.nextInt();
                            voterList.addVoter(candidateName, voterID, age);
                        } catch (InputMismatchException e) {
                            System.out.println("Invalid input for age. Please enter a number.");
                            scanner.next(); // Clear invalid input
                        }
                        break;
                    case 2:
                        voterList.displayAllVoters();
                        break;
                    case 3:
                        System.out.println("Exiting program...");
                        break;
                    default:
                        System.out.println("Invalid choice! Please enter a valid option.");
                }
            } catch (InputMismatchException e) {
                System.out.println("Invalid input for menu choice. Please enter a number.");
                scanner.next(); // Clear invalid input
            }
        } while (choice != 3);

        scanner.close();
    }
}
