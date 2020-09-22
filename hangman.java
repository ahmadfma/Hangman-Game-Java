import java.io.*;
import java.util.*;

/**
 *
 * @author = Ahmad Fathanah M.Adil
 *
 **/

class objectClass {

    String objectGame;
    String clue;
    private int amountOfLines = 0;

    void getObject() throws IOException {
        File database = new File("HangmanGame_Object.txt");
        File previousNumberFile = new File("Previous_Number.txt");

        if (!database.exists()) {

            /*
             * if you want to add more object, goto object.txt file (NOTE : Run this program first so the file will created)
             * then, add new object with format (objectName_Clue)
             * make sure the number of objectName's character is less than 40 to avoid Score bug
             * make sure the format is correct to avoid Error
             * symbol "_" can only used once, don't use it more than once to avoid bug
             *
             */

            if (database.createNewFile()) {
                BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(database));
                bufferedWriter.write("AHMAD FATHANAH_Creator of this program");
                bufferedWriter.newLine();
                bufferedWriter.write("HASANUDDIN UNIVERSITY_Top University in South Sulawesi, Indonesia");
                bufferedWriter.newLine();
                bufferedWriter.write("INDONESIA_country colonized by the Netherlands and Japan");
                bufferedWriter.newLine();
                bufferedWriter.write("SOEKARNO_The First President in Indonesia");
                bufferedWriter.newLine();
                bufferedWriter.write("CORONA_Virus that spreads in 2020");
                bufferedWriter.newLine();
                bufferedWriter.write("17 AUGUST 1945_The date of Indonesia's independence ");
                bufferedWriter.newLine();
                bufferedWriter.write("Sukarni Kartodiwirjo_The person who kidnapped and urged Soekarno to proclaim independence");
                bufferedWriter.newLine();
                bufferedWriter.write("fatmawati_people who sew the Indonesian heritage flag");
                bufferedWriter.newLine();
                bufferedWriter.write("nagasaki_City that was bombed by the Americans on August 6, 1945");
                bufferedWriter.newLine();
                bufferedWriter.write("Radjiman Wedyodiningrat_Former chairman of BPUPKI");
                bufferedWriter.newLine();
                bufferedWriter.write("Rengasdengklok_The kidnapping incident of Soekarno and Hatta");
                bufferedWriter.newLine();
                bufferedWriter.write("Sayuti Melik_Typist of the Indonesian independence proclamation script");
                bufferedWriter.newLine();
                bufferedWriter.write("Hirosima_City that was bombed by the Americans on August 9, 1945");
                bufferedWriter.newLine();
                bufferedWriter.write("Soedirman_Commander-in-chief of the first Indonesian National Army");
                bufferedWriter.newLine();
                bufferedWriter.write("Tadashi Maeda_The owner of the house where the proclamation script was formulated");
                bufferedWriter.newLine();

                bufferedWriter.flush();
                bufferedWriter.close();
            }

        }

        if (!previousNumberFile.exists()) {
            previousNumberFile.createNewFile();
        }

        checkHowMuchLinesInFile();
        int lineNumber, previous;

        //check if lineNumber is same with the previous number
        BufferedReader cekPreviousNumber = new BufferedReader(new FileReader(previousNumberFile));
        BufferedWriter writePrevNum = new BufferedWriter(new FileWriter(previousNumberFile));
        String prevNum = cekPreviousNumber.readLine();
        if (prevNum != null) {
            previous = Integer.parseInt(prevNum);
            do {
                lineNumber = getRandomNumber();
            } while (previous == lineNumber);
            writePrevNum.write("" + lineNumber);
            writePrevNum.newLine();
            writePrevNum.flush();

        } else {
            lineNumber = getRandomNumber();
            writePrevNum.write("" + lineNumber);
            writePrevNum.newLine();
            writePrevNum.flush();
        }

        int checkPoint = 0;

        BufferedReader bufferedReader = new BufferedReader(new FileReader(database));

        String data = bufferedReader.readLine();
        while (data != null) {
            checkPoint++;

            if (checkPoint == lineNumber) {
                Scanner datascan = new Scanner(data);
                datascan.useDelimiter("_");

                this.objectGame = datascan.next();
                this.clue = datascan.next();
                break;
            }

            data = bufferedReader.readLine();
        }

        bufferedReader.close();
        writePrevNum.close();
        cekPreviousNumber.close();
    }

    int getRandomNumber () {
        int min = 1;
        int max = this.amountOfLines;
        int random_Number = (int)(Math.random() * (max - min + 1) + min);
        return random_Number;
    }

    void checkHowMuchLinesInFile () throws IOException {

        BufferedReader bufferedReader = new BufferedReader(new FileReader("HangmanGame_Object.txt"));
        String data = bufferedReader.readLine();;

        while (data != null) {
            this.amountOfLines++;
            data = bufferedReader.readLine();
        }

        bufferedReader.close();
    }

}

public class hangman {

    public static void main(String[] args) throws IOException {
        getReady();
    }

    private static void getReady () {
        String man =        "    0      PLEASE HELP ME :(\n" +
                        "\t\t   \\|/   \n" +
                        "\t\t    | \n" +
                        "\t\t   / \\ \n";

        Thread thread = new Thread() {
            public void run() {
                try {
                    clrscr();
                    System.out.println("========== HANGMAN by Ahmad Fathanah ==========");
                    System.out.print("\n\t\t");
                    System.out.println(man);
                    System.out.println();
                    System.out.print("\tLoading");
                    for (int i = 1; i <= 3; i++) {
                        String k = ".";
                        System.out.print(k);
                        k += k;
                        sleep(1000);
                    }
                    System.out.println("\n\tGet Ready !!!");
                    sleep(1500);

                    Start();

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        };

        thread.start();
    }

    private static void Start() throws IOException {
        Scanner in = new Scanner(System.in);

        objectClass object = new objectClass();
        object.getObject();
        String objectGuess = object.objectGame;
        String clue = object.clue;

        char[] OBJECT = objectGuess.toCharArray();
        String[] gameObject = new String[OBJECT.length];
        ArrayList<String> answer = new ArrayList<>();

        //gameObject is used to help using Equality
        for (int i = 0; i < OBJECT.length; i++) {
            String a = "" + OBJECT[i];
            gameObject[i] = a.toUpperCase();
        }

        //fill the answer list with "_" as default value
        for (int i = 0; i < gameObject.length; i++) {
            if (!gameObject[i].equals(" ")) {
                answer.add("_");
            } else {
                answer.add(" ");
            }
        }

        int amountOfCorrectInput = gameObject.length;
        int chance = 6;
        int checkPoint = 0;
        int Score = 0;
        int maxScore = 500;
        int addScore = maxScore / amountOfCorrectInput;

        //reduce amountOfCorrectInput for every spaces in object
        //fungsi variable amountOfCorrectInput adalah menghitung jumlan inputan benar
        for (int i = 0; i < gameObject.length; i++) {
            String check = answer.get(i);
            if (check.equals(" ")) {
                amountOfCorrectInput--;
            }
        }

        clrscr();
        System.out.println("========== HANGMAN by Ahmad Fathanah ==========");

        while (true) {
            System.out.println("\nYour Score : " + Score);
            System.out.println("Chance     : " + chance);
            //print hangman
            printHangman(chance);

            //print answer
            System.out.println();
            if (gameObject.length > 16) {
                System.out.print("\n    ");
            } else {
                System.out.print("\n\t    ");
            }
            printUnderlinedChar(answer);

            //get input
            System.out.println("\n\nClue : " + clue);
            System.out.print("\nGuess a Letter : ");
            char input = in.next().charAt(0);
            String letter = Character.toString(input);
            String letterToUpper = letter.toUpperCase();

            for (int i = 0; i < gameObject.length; i++) {

                if (letterToUpper.equals(gameObject[i])) {
                    //check if the input was entered previously
                    String check = answer.get(i);
                    if (check.equals("_")) {
                        //if the input is correct and not entered previously then replace default value with the input
                        answer.set(i, letter.toUpperCase());
                        amountOfCorrectInput--;
                        Score += addScore;
                    } else {
                        chance--;   //if the input was entered previously then reduce chance
                        addScore = addScore - 2;
                        break;      //break for loop to avoid if there are same letter in another index
                    }
                }
                else {
                    //if the input is not correct, checkpoint will be length of gameObject
                    checkPoint++;
                }

            }

            if (checkPoint == gameObject.length){
                //if checkPoint is same with length of gameObject then reduce the chance
                chance--;
                addScore = addScore - 2;

            }

            //refresh
            checkPoint = 0;

            clrscr();
            System.out.println("========== HANGMAN by Ahmad Fathanah ==========");

            if (amountOfCorrectInput == 0 || chance == 0) {
                //if amountOfCorrectInput is 0 or chance is 0 then game is over
                break;
            }

        }

        if (amountOfCorrectInput == 0) {
            clrscr();
            if (chance == 6) {
                Score = maxScore;
            }
            System.out.println("========== HANGMAN by Ahmad Fathanah ==========");
            System.out.print("\n\t\t");
            System.out.println(man);
            if (gameObject.length > 16) {
                System.out.print("\n    ");
            } else {
                System.out.print("\n\t    ");
            }
            printUnderlinedChar(answer);
            System.out.println("\n\nCongratulations, You Just Save him !");
            System.out.println("Your Score : " + Score);
        }

        if (chance == 0) {
            clrscr();
            Score = 0;
            System.out.println("========== HANGMAN by Ahmad Fathanah ==========\n");
            System.out.println(rightLeg);
            System.out.println("\nYour Friend is dead, You Loose !");
            System.out.print  ("\nCorrect Answer : ");
            for (int i = 0; i < gameObject.length; i++) {
                System.out.print(gameObject[i] + " ");
            }
            System.out.println();
            System.out.println("Your Score     : " + Score);
        }

        //Play Again ?
        System.out.println("\n===============================================");
        System.out.print  ("Press 'Y' to play again :  ");
        String input = in.next();
        if (input.equalsIgnoreCase("y")) {
            getReady();
        } else {
            System.out.println("SEE YOU NEXT TIME !");
            File prevNumFile = new File("Previous_Number.txt");
            prevNumFile.delete();
        }

    }

    private static void printUnderlinedChar (ArrayList<String> arr) {
        for (int i = 0; i < arr.size(); i++) {

            String el = arr.get(i);

            if (!el.equals("_") && !el.equals(" ")) {
                System.out.print((char)27 +"[4m" + el);
                System.out.print((char)27 +"[0m");
                System.out.print(" ");
            } else {
                if (el.equals(" ")) {
                    System.out.print(" " + " ");
                } else {
                    System.out.print((char)27 +"[4m" + " ");
                    System.out.print((char)27 +"[0m");
                    System.out.print(" ");
                }
            }

        }
        System.out.println();
    }

    private static void printHangman (int chance) {

        if (chance == 6) {
            System.out.println(base);
        } else if (chance == 5) {
            System.out.println(head);
        } else if (chance == 4) {
            System.out.println(body);
        } else if (chance == 3) {
            System.out.println(leftHand);
        } else if (chance == 2) {
            System.out.println(rightHand);
        } else if (chance == 1) {
            System.out.println(leftLeg);
        }

    }

    final static  String man =          "    0      THANK YOU FOR SAVING ME :)\n" +
                                    "\t\t   /|\\   \n" +
                                    "\t\t    | \n" +
                                    "\t\t   / \\ \n";

    final static String base =  "    ____________\n" +
                                "    |          +\n" +
                                "    |\n" +
                                "    |\n" +
                                "    |\n" +
                                "    |\n" +
                                "    |\n" +
                                "    |\n" +
                                "------------";

    final static String head =      "    ____________\n" +
                                    "    |          +\n" +
                                    "    |          0\n" +
                                    "    |\n" +
                                    "    |\n" +
                                    "    |\n" +
                                    "    |\n" +
                                    "    |\n" +
                                    "------------";

    final static String body =      "    ____________\n" +
                                    "    |          +\n" +
                                    "    |          0\n" +
                                    "    |          | \n" +
                                    "    |          |\n" +
                                    "    |\n" +
                                    "    |\n" +
                                    "    |\n" +
                                    "------------";

    final static String leftHand =  "    ____________\n" +
                                    "    |          +\n" +
                                    "    |          0\n" +
                                    "    |         \\| \n" +
                                    "    |          |\n" +
                                    "    |\n" +
                                    "    |\n" +
                                    "    |\n" +
                                    "------------";

    final static String rightHand =   "    ____________\n" +
                                      "    |          +\n" +
                                      "    |          0\n" +
                                      "    |         \\|/ \n" +
                                      "    |          |\n" +
                                      "    |\n" +
                                      "    |\n" +
                                      "    |\n" +
                                      "------------";

    final static String leftLeg =    "    ____________\n" +
                                     "    |          +\n" +
                                     "    |          0\n" +
                                     "    |         \\|/ \n" +
                                     "    |          |\n" +
                                     "    |         /\n" +
                                     "    |\n" +
                                     "    |\n" +
                                     "------------";

    final static String rightLeg =    "    ____________\n" +
                                      "    |          +\n" +
                                      "    |          0\n" +
                                      "    |         \\|/ \n" +
                                      "    |          |\n" +
                                      "    |         / \\\n" +
                                      "    |\n" +
                                      "    |\n" +
                                      "------------";

    private static void clrscr() {
        try {
            if (System.getProperty("os.name").contains("Windows"))
                new ProcessBuilder("cmd", "/c", "cls").inheritIO().start().waitFor();
            else
                Runtime.getRuntime().exec("clear");
        } catch (IOException | InterruptedException ex) {}
    }

}
