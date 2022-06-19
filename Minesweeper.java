import java.util.Arrays;
import java.util.Random;
import java.util.Scanner;

/**
 * マインスイーパTyomoEdition
 * @author Tyomo
 * @version 1.0
 */
class Minesweeper{
    
    private int[][] bombPosition; // 爆弾の位置を0と1で記録
    private int[][] numberOfBombs; // 周囲にある爆弾の数を記録(これをユーザーに見せる)
    private int[][] openedBlocks; // 開かれたブロックを0と1で記録
    private final int EDGE_LENGTH; // 正方形の辺長(将来変更可能にする予定)

    /** */
    Minesweeper(){
        this.EDGE_LENGTH = 9; // 9x9のフィールド
        this.bombPosition = new int[EDGE_LENGTH][EDGE_LENGTH];
        this.numberOfBombs = new int[bombPosition.length][bombPosition.length];
        this.openedBlocks = new int[bombPosition.length][bombPosition.length];
    }

    public int[] convertFromInputToNum(String keyboardInput){
        int[] output = {0, 0};
        if(keyboardInput.length() > 2){
            System.out.println("error");
        }else{
            char inputChar1 = keyboardInput.charAt(0);
            char inputChar2 = keyboardInput.charAt(1);
            //cahr型のaはintにすると10，bは11，以降も同じつまり9引けば1以上の連続する整数になる
            int xOutput = Character.getNumericValue(inputChar1) - 9 - 1;
            int yOutput = Character.getNumericValue(inputChar2) - 1;
            
            output[0] = xOutput;
            output[1] = yOutput;
        }
        return output;
    }

    public void makeBombs(int firstX, int firstY){
        Random rand = new Random();
        int count = 0;
        while(count < 10){
            
            int bombX = rand.nextInt(bombPosition.length);
            int bombY = rand.nextInt(bombPosition.length);
            if((Math.abs(bombX - firstX) <= 1) && (Math.abs(bombY - firstY) <= 1)){
                continue;
            }

            if(this.bombPosition[bombY][bombX] == 0){
                this.bombPosition[bombY][bombX] = 1;
                count++;
            }else{
                continue;
            }
        }
    }

    public boolean openBlocks(int x, int y){
        if(x < 0 || x >= this.openedBlocks.length || y < 0 || y >= this.openedBlocks.length || openedBlocks[y][x]==1){
            //System.out.println("error");
            return true; //範囲外のため何もしない
        }else{
            //範囲内のためブロックを開ける
            openedBlocks[y][x] = 1;
        }

        if(numberOfBombs[y][x] == 0){
            for(int i = -1; i <= 1; i++){
                for(int j = -1; j <= 1; j++){
                    if(!(i == 0 && j == 0)){
                        openBlocks(x + j, y + i);
                    }
                }
            }
            return true;
        }else if(numberOfBombs[y][x] == 9){
            return false; //Return false if there is a bomb in the block that player is opening.
        }else{
            return true;
        }
        
    }

    public void makeGameField(){

    }
    
    public void showGameField(){
        String[][] output = new String[9][9];
        for(int i = 0; i < output.length; i++){
            Arrays.fill(output[i], "■");
        }

        for(int i = 0; i < openedBlocks.length; i++){
            for(int j = 0; j < openedBlocks.length; j++){
                if(openedBlocks[i][j] == 1){
                    if(numberOfBombs[i][j] == 9){
                        output[i][j] = "@";
                    }else{
                        output[i][j] = String.valueOf(numberOfBombs[i][j]);
                    }
                }
            }
        }
        this.showFieldData(output);
    }

    //for debug
    public void showFieldData(int[][] field){
        for(int i = 0; i < field.length + 2; i++){
            if(i == 0){
                System.out.println(" |a b c d e f g h i");
                continue;
            }else if (i == 1){
                System.out.println("-+-----------------");
                continue;
            }else{
                System.out.print((i - 1) + "|");
            }
            for(int j = 0; j < field.length; j++){
                System.out.print(field[i - 2][j] + " ");
            }
            System.out.print("\n");
        }
    }

    public void showFieldData(String[][] field){
        for(int i = 0; i < field.length + 2; i++){
            System.out.print("\u001b[00;47m");
            if(i == 0){
                System.out.print(" |a b c d e f g h i ");
            }else if (i == 1){
                System.out.print("-+----------------- ");
            }else{
                System.out.print((i - 1) + "|");
                for(int j = 0; j < field.length; j++){
                    System.out.print(field[i - 2][j] + " ");
                } 
            }
            System.out.print("\u001b[00m");
            System.out.print("\n");
        }
    }

    public void setNumbers(){
        for(int i = 0; i < bombPosition.length; i++){
            for(int j = 0; j < bombPosition.length; j++){
                int bombCount = 0;
                if(bombPosition[i][j] == 1){
                    bombCount = 9;
                    numberOfBombs[i][j] = bombCount;
                    continue;
                }
                for(int y = 0; y < 3; y++){
                    for(int x = 0; x < 3; x++){
                        try{
                            if(bombPosition[i + (y - 1)][j + (x - 1)] == 1) {
                                bombCount++;
                            }
                        }catch(ArrayIndexOutOfBoundsException e){
                            continue;
                        }
                    }
                }
                numberOfBombs[i][j] = bombCount;
            }
                
        }
    }

    public boolean isGameClear(){
        for (int i = 0; i < bombPosition.length; i++) {
            for (int j = 0; j < bombPosition.length; j++) {
                if(bombPosition[i][j] == 1 || openedBlocks[i][j] == 1){
                    continue;
                }else{
                    return false;
                }
            }
        }
        return true;
    }
   
    //getter(for debug)
    public int[][] getBombPosition(){
        return this.bombPosition;
    }

    public int[][] getNumberOfBombs(){
        return this.numberOfBombs;
    }

    public int[][] getOpenedBlocks(){
        return this.openedBlocks;
    }

    public int[] aiueo(){
        int[] output = {1, 2};
        return output;
    }

    public static void main(String[] args) {
        Minesweeper game = new Minesweeper();
        Scanner scanner = new Scanner(System.in);
        game.showGameField();
        System.out.println("どこを開ける?");
        int[] firstMove = game.convertFromInputToNum(scanner.nextLine());
        game.makeBombs(firstMove[0], firstMove[1]); //初手の周りを避けて爆弾を生成
        game.setNumbers();
        game.openBlocks(firstMove[0], firstMove[1]);
        boolean gameClear = false;

        mainLoop: while(!gameClear){
            game.showGameField();
            System.out.println("どこを開ける?");
            int[] input = game.convertFromInputToNum(scanner.nextLine());
            if(game.openBlocks(input[0], input[1]) == false){
                // What to do when there is a bomb.
                System.out.println("GameOver!!!"); 
                game.showGameField();
                break mainLoop;
            }else if(game.isGameClear() == true){
                //what to do when the game is cleared.
                for(int i = 0; i < game.getBombPosition().length * 2 + 2; i++){
                    if(i % 2 == 0)System.out.print("★");
                    else System.out.print("☆");
                }
                System.out.println();
                game.showGameField();
                System.out.println("Game Clear!!! Congratulations!!!");
                System.out.println();
                break mainLoop;
            }
        }
        scanner.close();
    }
}