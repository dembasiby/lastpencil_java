package lastpencil;

import java.util.Random;
import java.util.Scanner;
import java.util.concurrent.atomic.AtomicReference;

public class Main {
    public static void main(String[] args) {
        playTheGame();
    }

    public static void playTheGame() {
        System.out.println("How many pencils would you like to use:");

        int pencilCount = initializeTheNumberOfPencils();

        System.out.println("Who will be the first (John, Jack):");
        String firstPlayer = getFirstPlayer();
        String humanPlayer = "John";
        String botPlayer = "Jack";
        String pencils = buildPencils(pencilCount);

        boolean isFirstPlayer = humanPlayer.equalsIgnoreCase(firstPlayer);

        playTheGame(pencils, isFirstPlayer, humanPlayer, botPlayer);

    }

    private static void playTheGame(String pencils, boolean firstTurn,
                                    String humanPlayer, String botPlayer) {
        String currentPlayer = firstTurn ? humanPlayer : botPlayer;

        System.out.println(pencils);
        System.out.printf("%s's turn!%n", currentPlayer);

        pencils = updatePencilNumber(pencils, botPlayer, currentPlayer);

        if (pencils.isEmpty()) {
            System.out.println(pencils);
            String winner = humanPlayer.equals(currentPlayer) ? botPlayer : humanPlayer;

            System.out.printf("%s won %s%n", winner, winner.equals(botPlayer) ? ":" : "!");
            return;
        }

        playTheGame(pencils, !firstTurn, humanPlayer, botPlayer);
    }

    private static String updatePencilNumber(String pencils, String botPlayer, String currentPlayer) {
        int toRemove = currentPlayer.equals(botPlayer) ?
                botPlayerMove(pencils) :
                generateNumberOfSticksToRemove(pencils.length());

        int diff = pencils.length() - toRemove;

        return diff > 0 ? pencils.substring(0, diff) : "";

    }

    private static int botPlayerMove(String pencils) {
        int length = pencils.length();
        Random random = new Random();
        int toRemove = length == 1 ? length : (
                        isInLosingPosition(length) ? random.nextInt(3) + 1 :
                        applyTheWinningStrategy(length));

        System.out.println(toRemove);
        return toRemove;
    }

    private static int applyTheWinningStrategy(int length) {
        int modLength = length % 4;

        return switch (modLength) {
            case 0 -> 3;
            case 3 -> 2;
            default -> 1;
        };
    }

    private static boolean isInLosingPosition(int length) {
        return length % 4 == 1;
    }

    private static String getFirstPlayer() {
        Scanner scanner = new Scanner(System.in);
        String firstPlayer = scanner.next();

        if (!"John".equals(firstPlayer) && !"Jack".equals(firstPlayer)) {
            System.out.println("Choose between 'John' and 'Jack'");
            firstPlayer = getFirstPlayer();
        }

        return firstPlayer;
    }

    private static int initializeTheNumberOfPencils() {
        AtomicReference<Integer> pencils = new AtomicReference<>(0);
        Scanner scanner = new Scanner(System.in);

        try {
            String numStr = scanner.nextLine().trim();
            if (numStr.isEmpty()) {
                throw new NumberFormatException();
            }

            int number = Integer.parseInt(numStr);

            if (number <= 0) {
                throw new RuntimeException();
            }
            pencils.set(number);
        } catch (NumberFormatException e) {
            System.out.println("The number of pencils should be numeric ");
            pencils.set(initializeTheNumberOfPencils());
        } catch (RuntimeException e) {
            System.out.println("The number of pencils should be positive");
            pencils.set(initializeTheNumberOfPencils());
        }

        return pencils.get();
    }

    private static String buildPencils(int sticks) {
        return "|".repeat(Math.max(0, sticks));
    }

    private static int generateNumberOfSticksToRemove(int pencils) {
        Scanner scanner = new Scanner(System.in);
        var input = -1;

        try {
            String inputStr = scanner.nextLine().trim();
            input = Integer.parseInt(inputStr);

            if (input < 1 || input > 3) {
                throw new NumberFormatException();
            } else if (input > pencils) {
                throw new RuntimeException();
            }
        } catch (NumberFormatException e) {
            System.out.println("Possible values: '1', '2' or '3'");
            return generateNumberOfSticksToRemove(pencils);
        } catch (RuntimeException e) {
            System.out.println("too many pencils");
            return generateNumberOfSticksToRemove(pencils);
        }
        return input;
    }
}
