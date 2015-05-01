package com.projects.android.protomoldpuzzle;

import java.util.ArrayList;

/**
 * Created by jrslagle on 3/6/15.
 */
public class Board {

    private ArrayList<Block> placedBlocks;
    private ArrayList<String> missingPieces;
    private char[][][] board;

    public Board(ArrayList<String> colors) {
        placedBlocks = new ArrayList<>();
        missingPieces = colors;
        board = new char[4][4][4];
        for (int x = 0; x < 4; x++) {
            for (int y = 0; y < 4; y++) {
                for (int z = 0; z < 4; z++) {
                        board[x][y][z] = '_';
                }
            }
        }
    }

    public Board(Board original) {
        this.placedBlocks = (ArrayList)original.placedBlocks.clone();
        this.missingPieces = (ArrayList)original.missingPieces.clone();
        // manually copy board over
        this.board = new char[4][4][4];
        for (int x = 0; x < 4; x++) {
            for (int y = 0; y < 4; y++) {
                for (int z = 0; z < 4; z++) {
                    this.board[x][y][z] = original.board[x][y][z];
                }
            }
        }
    }

    public ArrayList<String> getMissingPieces() {return missingPieces;}

    public char[][][] getBoard() {return board;}

    public ArrayList<Block> getPlacedBlocks() {return placedBlocks;}

    public void addPiece(Block newBlock) {
        placedBlocks.add(newBlock);
        missingPieces.remove(newBlock.getName());

        // add shape onto board
        char[][][] blockShape = newBlock.getShape();
        String blockCode = newBlock.getCode();
        for (int x = 0; x < blockShape.length; x++) {
            for (int y = 0; y < blockShape[x].length; y++) {
                for (int z = 0; z < blockShape[x][y].length; z++) {
                    if(Character.isLetter(blockShape[x][y][z])) {
                        board[x + Character.getNumericValue(blockCode.charAt(3))]
                             [y + Character.getNumericValue(blockCode.charAt(4))]
                             [z + Character.getNumericValue(blockCode.charAt(5))]
                                = blockShape[x][y][z];
                    }
                }
            }
        }
    }

    @Override
    public String toString() {
        String output = "";

        for (int y = 0; y < board[0].length; y++) {
            for (int z = 0; z < board[0][0].length; z++) {
                output = output.concat("[");
                for (int x = 0; x < board.length; x++) {
                    output = output.concat(String.valueOf(board[x][y][z]));
                }
                output = output.concat("]\t");
            }
            output = output.concat("\n");
        }
        output = output.concat("\n");
        return output;
    }
}
