import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) throws InterruptedException {
        Grille game = new Grille(new ArrayList<List<Integer>>(
            List.of(
                List.of(0,0,0,0,0),
                List.of(0,0,0,0,0),
                List.of(0,0,0,0,0),
                List.of(0,0,0,0,0),
                List.of(0,0,0,0,0)
            )
        ));
        VoltorbFlipSolver solver = new VoltorbFlipSolver(game);

        // List<List<Integer>> test = solver.generateLineWithNumber(new ArrayList<>(List.of(0,1,1,1,1)), 3); // 4 - 1
        // for(List<Integer> l1 : test){
        //     StringBuilder line = new StringBuilder();
        //     for(Integer i : l1){
        //         line.append(i).append(" ");
        //     }
        //     System.out.println(line.toString().trim());
        //     System.out.println("\n");
        // }

        // List<List<Integer>> l = Permuations.generatePermutations(new ArrayList<>(List.of(0, 0, 1, 1, 1)));
        // for(List<Integer> l1 : l){
        //     StringBuilder line = new StringBuilder();
        //     for(Integer i : l1){
        //         line.append(i).append(" ");
        //     }
        //     System.out.println(line.toString().trim());
        // }


        List<Grille> l = solver.getPossibleBoard();

        // for(Grille g : l){
        //     System.out.println(g.toString());
        // }

        // System.out.println(l.size());

        solver.compute();

        System.out.println(solver.getProbs().toString());
        System.out.println(solver.getBoard().toString());

        Scanner myObjt = new Scanner(System.in);

        while (!solver.isFinish()) {
            System.out.println("Quelle ligne ?:");

            String ligne = myObjt.nextLine();

            System.out.println("Quelle colonne ?:");

            String colonne = myObjt.nextLine();

            System.out.println("Quelle symbole ?:");

            String symbole = myObjt.nextLine();

            solver.getBoard().setCell(Integer.parseInt(ligne), Integer.parseInt(colonne), Integer.parseInt(symbole));


            solver.purify();
            solver.compute();

            System.out.println(solver.getPossibleGrille().size());

            for(Grille g : solver.getPossibleGrille()){
                System.out.println(g.toString());
            }


            System.out.println(solver.getProbs().toString());
            System.out.println(solver.getBoard().toString());

            Thread.sleep(1000);

            
        }

        System.out.println("FINISH");
        

        

        

        // List<List<Integer>> test = new ArrayList<>(
        //     List.of(
        //         List.of(1, 1, 1, 0, 1),
        //         List.of(0, 0, 1, 1, 1),
        //         List.of(1, 1, 0, 1, 1),
        //         List.of(0, 1, 1, 1, 1),
        //         List.of(1, 1, 1, 0, 1)
        //     )
        // );

        // Grille g = new Grille(test);
        // System.out.println(g.toString());

        // boolean t = solver.isNbVoltColCorrect(test) && solver.isNbVoltLigneCorrect(test);
        // boolean t1 = solver.isNbVoltColCorrect(test);
        // boolean t2 = solver.isNbVoltLigneCorrect(test);

        // System.out.println(t + " " + t1 + " " + t2);
    }
}
