import java.util.ArrayList;
import java.util.List;

public class Grille {
    private List<List<Integer>> board;

    public Grille(List<List<Integer>> board){
        this.board = new ArrayList<>();
        //System.out.println(toString());

        for(List<Integer> row : board){
            this.board.add(new ArrayList<>(row));
        }
    }

    public int getSize(){
        return board.size();
    }

    public List<List<Integer>> getBoard(){
        return board;
    }

    public String toString(){
        String lines = "<br>GRID : <br>\n";
        for(List<Integer> l1 : board){
            StringBuilder line = new StringBuilder();
            for(Integer i : l1){
                if(Integer.toString(i).length() == 1){
                    line.append("  &nbsp;&nbsp;");
                    line.append(i).append(" ");
                }else if(Integer.toString(i).length() == 2){
                    line.append(" &nbsp;");
                    line.append(i).append(" ");
                }else{
                    line.append(i).append(" ");
                }
            }
            lines += line + "<br>\n";
        }
        return lines;
    }

    public void setCell(int row, int col, int value) {
        this.board.get(row).set(col, value);
    }

    public void resetProb(){
        for(int i = 0; i<board.size(); i++){
            for(int j = 0; j<board.size(); j++){
                board.get(i).set(j, 0);
            }
        }
    }

    public void resetBoard(){
        for(int i = 0; i<board.size(); i++){
            for(int j = 0; j<board.size(); j++){
                board.get(i).set(j, -1);
            }
        }
    }
}
