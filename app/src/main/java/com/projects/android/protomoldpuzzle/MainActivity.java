package com.projects.android.protomoldpuzzle;

import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Hashtable;


public class MainActivity extends ActionBarActivity {

    private TextView mTextOut;
    private ArrayList<String> colors = getMissingColors();
    private Hashtable<String, Hashtable> shapeBank = new Hashtable();
    private final String TAG = "MainActivity";
    String textOut = "Let's begin!";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mTextOut = (TextView)findViewById(R.id.text_out);
        mTextOut.setMovementMethod(new ScrollingMovementMethod());
        mTextOut.setText(textOut);

        // fill shapeBank with orientation permutations, organized by color
        for (int i = 0; i < colors.size(); i++) {
            String color = colors.get(i);
            Hashtable orientations = generateOrientations(color);
            shapeBank.put(color, orientations);
        }

        final long start = System.nanoTime();
//        int boardCount = 0;
        ArrayList<Board> boardList = new ArrayList<>();
        boardList.add(new Board(getMissingColors()));
        ArrayList<Board> solutionList = new ArrayList<>();
        for (int boardIndex = 0; boardIndex < boardList.size();
             boardIndex++) {
            Board board = boardList.get(boardIndex);

            ArrayList<String> missingPieces = board.getMissingPieces();
            if(missingPieces.size() == 0) {
                solutionList.add(board);
            } else {
                // picks the piece with the largest volume
                String bestColor = findBestColor(missingPieces);

                Hashtable orientations;
                // don't rotate for first piece
                if (missingPieces.size() == 9) {
                    Block block = new Block(bestColor);  // GT0
                    orientations = new Hashtable<String, Block>();
                    orientations.put(block.getCode(), block);
                } else {
                    orientations = shapeBank.get(bestColor);
                }

/*                If there are 0 candidates, then this partial solution is a
                dead end. It means that at least one piece can't be placed
                anywhere, and so something else is in the wrong place.
                returns all non-colliding locations of each orientation*/
                ArrayList<Block> candidates = searchCollisions(board,
                        orientations);
//                boardCount += candidates.size();
                createChildBoards(boardList, candidates, boardIndex, board);
            }
            boardList.remove(boardIndex);
            boardIndex--;
        }

        // print out solution boards
        textOut = "There are "+solutionList.size()+" solution(s):\n";
        for (int i = 0; i < solutionList.size(); i++) {
            Board solution = solutionList.get(i);
            ArrayList<Block> pieces = solution.getPlacedBlocks();
            textOut = textOut.concat("[");
            for (int j = 0; j < pieces.size(); j++) {
                String code = pieces.get(j).getCode();
                textOut = textOut.concat(code +", ");
            }
            textOut = textOut.concat("]\n\n"+solution.toString()+"\n\n");
        }
        mTextOut.setText(textOut);
    }

    private ArrayList<String> getMissingColors() {
        ArrayList<String> colors = new ArrayList<>();
        colors.add("green");
        colors.add("purple");
        colors.add("orange");
        colors.add("red");
        colors.add("yellow");
        colors.add("black");
        colors.add("clear");
        colors.add("white");
        colors.add("blue");
        return colors;
    }

    private String findBestColor(ArrayList<String> missingPieces) {

        // chooses the next missing piece with the largest volume
        if(missingPieces.size() > 0) {
            String bestColor = "";
            int maxVolume = 0;
            for (int i = 0; i < missingPieces.size(); i++) {
                String colorKey = missingPieces.get(i);
                Block piece = new Block(colorKey);
                int volume = piece.getVolume();
                if (volume > maxVolume) {
                    maxVolume = volume;
                    bestColor = colorKey;
                }
            }
            return bestColor;
        } else return null;
    }

    private Hashtable generateOrientations(String color) {

        // I'm rotating and adding blocks without a loop because the direction
        // of rotation changes in a too-complex-to-care way.
        Hashtable orientationsHash = new Hashtable();
        Block block = new Block(color);  // GT0
        Block newBlock = new Block(block);
        orientationsHash.put(newBlock.getCode(), newBlock);
        block.yawBlock();       // GT3
        newBlock = new Block(block);
        orientationsHash.put(newBlock.getCode(), newBlock);
        block.yawBlock();       // GT6
        newBlock = new Block(block);
        orientationsHash.put(newBlock.getCode(), newBlock);
        block.yawBlock();       // GT9
        newBlock = new Block(block);
        orientationsHash.put(newBlock.getCode(), newBlock);
        block.pitchBlock();     // GF9
        newBlock = new Block(block);
        orientationsHash.put(newBlock.getCode(), newBlock);
        block.rollBlock();      // GF0
        newBlock = new Block(block);
        orientationsHash.put(newBlock.getCode(), newBlock);
        block.rollBlock();      // GF3
        newBlock = new Block(block);
        orientationsHash.put(newBlock.getCode(), newBlock);
        block.rollBlock();      // GF6
        newBlock = new Block(block);
        orientationsHash.put(newBlock.getCode(), newBlock);
        block.yawBlock();       // GL6
        newBlock = new Block(block);
        orientationsHash.put(newBlock.getCode(), newBlock);
        block.pitchBlock();     // GL9
        newBlock = new Block(block);
        orientationsHash.put(newBlock.getCode(), newBlock);
        block.pitchBlock();     // GL0
        newBlock = new Block(block);
        orientationsHash.put(newBlock.getCode(), newBlock);
        block.pitchBlock();     // GL3
        newBlock = new Block(block);
        orientationsHash.put(newBlock.getCode(), newBlock);
        block.yawBlock();       // GK3
        newBlock = new Block(block);
        orientationsHash.put(newBlock.getCode(), newBlock);
        block.rollBlock();      // GK0
        newBlock = new Block(block);
        orientationsHash.put(newBlock.getCode(), newBlock);
        block.rollBlock();      // GK9
        newBlock = new Block(block);
        orientationsHash.put(newBlock.getCode(), newBlock);
        block.rollBlock();      // GK6
        newBlock = new Block(block);
        orientationsHash.put(newBlock.getCode(), newBlock);
        block.yawBlock();       // GR6
        newBlock = new Block(block);
        orientationsHash.put(newBlock.getCode(), newBlock);
        block.pitchBlock();     // GR3
        newBlock = new Block(block);
        orientationsHash.put(newBlock.getCode(), newBlock);
        block.pitchBlock();     // GR0
        newBlock = new Block(block);
        orientationsHash.put(newBlock.getCode(), newBlock);
        block.pitchBlock();     // GR9
        newBlock = new Block(block);
        orientationsHash.put(newBlock.getCode(), newBlock);
        block.rollBlock();      // GB0
        newBlock = new Block(block);
        orientationsHash.put(newBlock.getCode(), newBlock);
        block.yawBlock();       // GB9
        newBlock = new Block(block);
        orientationsHash.put(newBlock.getCode(), newBlock);
        block.yawBlock();       // GB6
        newBlock = new Block(block);
        orientationsHash.put(newBlock.getCode(), newBlock);
        block.yawBlock();       // GB3
        newBlock = new Block(block);
        orientationsHash.put(newBlock.getCode(), newBlock);

        return orientationsHash;
    }

    private ArrayList<Block> searchCollisions(
            Board board, Hashtable<String, Block> orientations) {
        ArrayList<Block> candidates = new ArrayList<>();
        Enumeration orientationKeys = orientations.keys();
        while (orientationKeys.hasMoreElements()) {
            String code = (String)orientationKeys.nextElement();
            Block block = orientations.get(code);

            // iterate through each possible location of this orientation.
            char[][][] shape = block.getShape();
            for (int x = 0; x <= (4 - shape.length); x++) {
                for (int y = 0; y <= (4 - shape[0].length); y++) {
                    for (int z = 0; z <= (4 - shape[0][0].length); z++) {
                        int[] anchor = {x,y,z};
                        if (!collide(board, block, anchor)) {
                            // bake location into block code
                            char[] codeArray = block.getCode().toCharArray();
                            for (int a = 0; a < anchor.length; a++) {
                                codeArray[a + 3] =
                                        Integer.toString(anchor[a]).charAt(0);
                            }
                            block.setCode(String.valueOf(codeArray));
                            candidates.add(new Block(block));
                        }
                    }
                }
            }
        }
        return candidates;
    }

    private boolean collide(Board board, Block block, int[] anchor) {
        char[][][] blockShape = block.getShape();
        char[][][] boardShape = board.getBoard();
        for (int x = 0; x < blockShape.length; x++) {
            for (int y = 0; y < blockShape[x].length; y++) {
                for (int z = 0; z < blockShape[x][y].length; z++) {
                    char p = blockShape[x][y][z];
                    char b = boardShape[(x + anchor[0])]
                            [(y + anchor[1])][(z + anchor[2])];
                    if(Character.isLetter(p) &&
                            Character.isLetter(b)) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    private void createChildBoards(
            ArrayList<Board> boardList, ArrayList<Block> candidates,
             int boardIndex, Board board) {

        if (candidates.size() > 0) {
            // create clone boards with new candidates placed
            ArrayList<Board> newBoards = new ArrayList<>();
            for (int i = 0; i < candidates.size(); i++) {
                Board newBoard = new Board(board);
                newBoard.addPiece(candidates.get(i));
                newBoards.add(newBoard);
            }
            // inserting in this way makes a depth-first search
            boardList.addAll(boardIndex + 1, newBoards);

//            Log.d(TAG, "#" + boardCount + ", " + boardList.size() + " open boards, " +
//                    missingPieces.size() + " missing pieces.");
            //                    textOut = "#" + boardCount + ", " + boardList.size() + " open boards, " +
            //                            missingPieces.size() + " missing pieces.";
        }
        //                mTextOut.setText(textOut);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
