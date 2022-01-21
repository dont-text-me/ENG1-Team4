package com.isometric.game;

import java.util.Arrays;
import java.util.Random;

public class MapGenerator {
//    Higher values make larger islands less likely.
    double growthWeight = 0.01;

    //    Define the board
    public int size = 64;
    int islands = size / 2;
    public String[][] board = new String[size][size];
    public String[] map = new String[size];
    // Fill each row with 1.0

    public MapGenerator() {

        for (String[] row: board) {
            Arrays.fill(row, "9");
        }

        Random spawn = new Random();
        Random probability = new Random();
        int x;
        int y;
        int z;
        int count;
        double chance = 0;

//        Loop for placing initial islands.
        for (int i = 0; i < islands; i++) {
            x = spawn.nextInt(size - 20) + 10;
            y = spawn.nextInt(size - 20) + 10;
            board[x][y] = "0";
//            For "growing" the islands
            double temp = probability.nextDouble();
            while (temp > chance) {
                x += (spawn.nextInt(2)-1);
                y += (spawn.nextInt(2)-1);
                board[x][y] = "0";
                chance += growthWeight;
//                System.out.println(chance);
//                System.out.println(temp);
//                System.out.println(String.valueOf(x)+String.valueOf(y));
                temp = probability.nextDouble();
            }
        }
//        This loop needs to run multiple times to check for errors during generation.
        for (z = 0; z < 4; z++) {
            for (y = 1; y < size-2; y++) {
                for (x = 1; x < size-2; x++) {

                    count = 0;

                    if (board[y + 1][x].equals("0")) {
                        count += 1;
                    }
                    if (board[y - 1][x].equals("0")) {
                        count += 1;
                    }
                    if (board[y][x + 1].equals("0")) {
                        count += 1;
                    }
                    if (board[y][x - 1].equals("0")) {
                        count += 1;
                    }
//                    System.out.println(count);
                    if (count >= 3) {
                        board[y][x] = "0";
                    }

                    if ((board[y + 1][x].equals("0")) && (board[y - 1][x].equals("0"))) {
                        board[y][x] = "0";
                    }
                    if ((board[y][x + 1].equals("0")) && (board[y][x - 1].equals("0"))) {
                        board[y][x] = "0";
                    }
                    if ((board[y + 1][x + 1].equals("0")) && (board[y - 1][x - 1].equals("0"))) {
                        board[y][x] = "0";
                    }
                    if ((board[y - 1][x + 1].equals("0")) && (board[y + 1][x - 1].equals("0"))) {
                        board[y][x] = "0";
                    }
                }
            }
        }

        for (y = 2; y < (size-2); y++) {
            for (x = 2; x < (size - 2); x++) {

                if (board[y][x].equals("0")) {
//                    straight bottom
                    if (board[y + 1][x].equals("9")) {
                        if ((board[y + 1][x - 1].equals("9")) || (board[y + 1][x - 1].equals("4"))) {
                            if ((board[y + 1][x + 1].equals("9")) || (board[y + 1][x + 1].equals("4"))) {
                                if ((board[y + 2][x].equals("9")) || (board[y + 2][x].equals("2"))) {
                                    board[y + 1][x] = "4";
                                }
                            }
                        }
                    }

                    //                straight left
                    if (board[y][x - 1].equals("9")) {
                        if ((board[y - 1][x - 1].equals("9")) || (board[y - 1][x - 1].equals("1"))) {
                            if ((board[y + 1][x - 1].equals("9")) || (board[y + 1][x - 1].equals("1"))) {
                                if ((board[y][x - 2].equals("9")) || (board[y][x - 2].equals("3"))) {
                                    board[y][x - 1] = "1";
                                }
                            }
                        }
                    }

                    //                straight right
                    if (board[y][x + 1].equals("9")) {
                        if ((board[y - 1][x + 1].equals("9")) || (board[y - 1][x + 1].equals("3"))) {
                            if ((board[y + 1][x + 1].equals("9")) || (board[y + 1][x + 1].equals("3"))) {
                                if ((board[y][x + 2].equals("9")) || (board[y][x + 2].equals("1"))) {
                                    board[y][x + 1] = "3";
                                }
                            }
                        }
                    }

                    //                straight top
                    if (board[y - 1][x].equals("9")) {
                        if ((board[y - 1][x - 1].equals("9")) || (board[y - 1][x - 1].equals("2"))) {
                            if ((board[y - 1][x + 1].equals("9")) || (board[y - 1][x + 1].equals("2"))) {
                                if ((board[y - 2][x].equals("9")) || (board[y - 2][x].equals("4"))) {
                                    board[y - 1][x] = "2";
                                }
                            }
                        }
                    }
                }
            }
        }

        for (y = 1; y < (size - 2); y++) {
            for (x = 1; x < (size - 2); x++) {
                if (board[y][x].equals("9")) {
                    if (board[y + 1][x].equals("0")) {
                        if (board[y][x + 1].equals("0")) {
                            board[y][x] = "j";
                        } else if (board[y][x - 1].equals("0")) {
                            board[y][x] = "k";
                        }
                    }

                    if (board[y - 1][x].equals("0")) {
                        if (board[y][x + 1].equals("0")) {
                            board[y][x] = "i";
                        } else if (board[y][x - 1].equals("0")) {
                            board[y][x] = "l";
                        }
                    }
                }
            }
        }

        for (y = 0; y < (size - 1); y++) {
            for (x = 0; x < (size - 1); x++) {
                if ((x > 1) && (x < (size - 2))) {
                    if ((y > 1) && (y < (size - 2))) {
                        if (board[y][x].equals("9")) {
                            if (Arrays.asList("1", "j").contains(board[y + 1][x])) {
                                if (Arrays.asList("2", "j").contains(board[y][x + 1])) {
                                    board[y][x] = "b";
                                }
                            }
                        }
                    }
                }
            }
        }

        for (y = 0; y < (size - 1); y++) {
            for (x = 0; x < (size - 1); x++) {
                if ((x > 1) && (x < (size - 2))) {
                    if ((y > 1) && (y < (size - 2))) {
                        if (board[y][x].equals("9")) {
                            if (Arrays.asList("1", "i").contains(board[y - 1][x])) {
                                if (Arrays.asList("4", "i").contains(board[y][x + 1])) {
                                    board[y][x] = "a";
                                }
                            }
                        }
                    }
                }
            }
        }

        for (y = 0; y < (size-1); y++) {
            for (x = 0; x < (size - 1); x++) {
                if ((x > 1) && (x < (size - 2))) {
                    if ((y > 1) && (y < (size - 2))) {
                        if (board[y][x].equals("9")) {
                            if (Arrays.asList("4", "l").contains(board[y][x - 1])) {
                                if (Arrays.asList("3", "l").contains(board[y - 1][x])) {
                                    board[y][x] = "d";
                                }
                            }
                        }
                    }
                }
            }
        }


        for (y = 0; y < (size - 1); y++) {
            for (x = 0; x < (size - 1); x++) {
                if ((x > 1) && (x < (size - 2))) {
                    if ((y > 1) && (y < (size - 2))) {
                        if (board[y][x].equals("9")) {
                            if (Arrays.asList("3", "k").contains(board[y + 1][x])) {
                                if (Arrays.asList("2", "k").contains(board[y][x - 1])) {
                                    board[y][x] = "c";
                                }
                            }
                        }
                    }
                }
            }
        }



        String out = Arrays.deepToString(board);
        for (String[] row: board) {
            System.out.println(Arrays.toString(row));
        }
        StringBuilder output = new StringBuilder();
        int counter = 0;
        for (String[] row:board) {
            for (String column:row) {
                output.append(column);
            }
            map[counter] = output.toString();
            counter += 1;
            output = new StringBuilder();
        }
    }

}
