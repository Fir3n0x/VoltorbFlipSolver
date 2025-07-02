import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class VoltorbFlipSolver{
    private Grille probs;
    private Grille board;

    private List<Grille> possible_comb = new ArrayList<Grille>();

    private Pair<Integer,Integer>[] colVolt; // colonne située à droite du jeu (correspond aux lignes)
    private Pair<Integer,Integer>[] ligneVolt; // ligne située en bas du jeu (correspond aux colonnes)

    /*onevolt,twovolt,threevolt sont les listes contenant toutes les possibilités de positionnement des volts sur une ligne */
    // private List<List<Integer>> onevolt;
    // private List<List<Integer>> twovolt;
    // private List<List<Integer>> threevolt;

    // private Map<Integer,List<List<Integer>>> map = new HashMap<>();

    private Map<Integer,List<Integer>> map = new HashMap<>();

    
    public VoltorbFlipSolver(Grille probs){
        this.probs = probs;

        board = new Grille(new ArrayList<>(
            List.of(
                new ArrayList<>(List.of(-1,-1,-1,-1,-1)),
                new ArrayList<>(List.of(-1,-1,-1,-1,-1)),
                new ArrayList<>(List.of(-1,-1,-1,-1,-1)),
                new ArrayList<>(List.of(-1,-1,-1,-1,-1)),
                new ArrayList<>(List.of(-1,-1,-1,-1,-1))
            )
        ));


        // colVolt = (Pair<Integer,Integer>[]) new Pair[5];
        // ligneVolt = (Pair<Integer,Integer>[]) new Pair[5];
        // initColVolt();
        // initLigneVolt();


        // for(int i = 0; i<5; i++){
        //     /*Opti pour ne pas avoir a initialisé cette structure si ça n'est pas nécessaire */
        //     /*0 correspond à un voltorbe et 1 au gain */
        //     if(colVolt[i].getVolt() > 2 || ligneVolt[i].getVolt() > 2){
        //         threevolt = Permuations.generatePermutations(new ArrayList<>(List.of(0,0, 0, 1, 1)));
        //         map.put(3, threevolt);
        //         break;
        //     }
        // }
        // onevolt = Permuations.generatePermutations(new ArrayList<>(List.of(0, 1, 1, 1, 1)));
        // twovolt = Permuations.generatePermutations(new ArrayList<>(List.of(0, 0, 1, 1, 1)));

        // map.put(0, new ArrayList<>(List.of(List.of(1,1,1,1,1))));
        // map.put(1, onevolt);
        // map.put(2, twovolt);

        map.put(0, new ArrayList<>(List.of(1,1,1,1,1)));
        map.put(1, new ArrayList<>(List.of(0,1,1,1,1)));
        map.put(2, new ArrayList<>(List.of(0,0,1,1,1)));
        map.put(3, new ArrayList<>(List.of(0,0,0,1,1)));
        map.put(4, new ArrayList<>(List.of(0,0,0,0,1)));
        map.put(5, new ArrayList<>(List.of(0,0,0,0,0)));

        

        
    }

    public Grille getProbs(){
        return probs;
    }

    public Grille getBoard(){
        return board;
    }

    public List<Grille> getPossibleGrille(){
        return possible_comb;
    }

    public void setColVolt(Pair<Integer,Integer>[] colVolt){
        this.colVolt = colVolt;
    }

    public void setLigneVolt(Pair<Integer,Integer>[] ligneVolt){
        this.ligneVolt = ligneVolt;
    }

    public void initColVolt(){
        /*A DEFINIR */
        colVolt[0] = new Pair<Integer,Integer>(3,2);
        colVolt[1] = new Pair<Integer,Integer>(8,1);
        colVolt[2] = new Pair<Integer,Integer>(4,1);
        colVolt[3] = new Pair<Integer,Integer>(7,1);
        colVolt[4] = new Pair<Integer,Integer>(3,2);
    }

    public void initLigneVolt(){
        /*A DEFINIR */
        ligneVolt[0] = new Pair<Integer,Integer>(3,2);
        ligneVolt[1] = new Pair<Integer,Integer>(5,2);
        ligneVolt[2] = new Pair<Integer,Integer>(8,0);
        ligneVolt[3] = new Pair<Integer,Integer>(5,2);
        ligneVolt[4] = new Pair<Integer,Integer>(4,1);
    }







     /**
     * Fonction récursive pour générer toutes les combinaisons.
     * 
     * @param colVolt   Tableau de Pair contenant les informations.
     * @param map       Map associant les clés à des listes de listes.
     * @param index     Index actuel dans `colVolt`.
     * @param current   Liste courante pour construire une combinaison.
     * @param results   Liste finale contenant toutes les combinaisons générées.
     */
    
    /*Map(Integer,List<List<Integer>>) */
    public List<Grille> getPossibleBoard(){

        possible_comb.clear();

        generateBoard(colVolt, map, 0, new ArrayList<List<Integer>>(), possible_comb);

        // System.out.println("Nombre de grilles initiales : " + possible_comb.size());

        return possible_comb;

    }

    public void generateBoard(
        Pair<Integer,Integer>[] colVolt,
        Map<Integer, List<Integer>> map,
        int index,
        List<List<Integer>> current,
        List<Grille> results
        ){

            if (index == colVolt.length) {

                /*Clonage profond : En clonant chaque sous-liste de current, tu t'assures que chaque 
                instance de Grille est indépendante et conserve son état au moment où elle est ajoutée à results. */

                List<List<Integer>> currentCopy = new ArrayList<>();
                for (List<Integer> row : current) {
                    currentCopy.add(new ArrayList<>(row));
                }
        
                Grille g = new Grille(currentCopy);

                
                if(isNbVoltLigneCorrect(currentCopy) && isNbVoltColCorrect(currentCopy) && !results.contains(g)){
                    // System.out.println(current.toString());
                    // System.out.println(g.toString());
                    //System.out.println("true");
                    // List<Grille> res = generateBoardWithNumber(currentCopy,1);
                    // for(Grille num_grille : res){
                    //     results.add(num_grille);
                    // }
                    results.add(g);
                    
                }
                else{//System.out.println("false");
                }
                return;
            }

        List<Integer> pattern = map.get(colVolt[index].getVolt());
        List<List<Integer>> possibilities = generateLineWithNumber(pattern, index);
        //System.out.println(possibilities.size());

        if (possibilities == null) return;

        for (List<Integer> possibility : possibilities) {
            current.add(possibility); // Ajouter l'élément courant
            generateBoard(colVolt, map, index + 1, current, results); // Récursion pour l'élément suivant
            current.remove(current.size() - 1); // Backtracking : supprimer l'élément ajouté
        }



    }

    /*Vérifier que sur chaque ligne, il y ait le bon nombre de voltorbe. Ici on parle d'une ligne de jeu*/
    public boolean isNbVoltLigneCorrect(List<List<Integer>> current){
        
        for(int i = 0; i<colVolt.length; i++){
            //Test sur chaque ligne
            if(Collections.frequency(current.get(i), 0) != colVolt[i].getVolt()) return false;
        }
        
        return true;
    }

    /*Vérifier que sur chaque colonne, il y ait le bon nombre de voltorbe. Ici on parle d'une colonne de jeu*/
    /*Vérifier également que la somme de point sur chaque colonne soit la bonne */
    public boolean isNbVoltColCorrect(List<List<Integer>> current){
        for(int i = 0; i<ligneVolt.length; i++){
            int compt = 0;
            int point = 0;
            for(int j = 0; j<colVolt.length; j++){
                // System.out.println(Integer.toString(current.get(j).get(i)));
                point += current.get(j).get(i);
                if(current.get(j).get(i).equals(0)){
                    compt++;
                }
            }
            // System.out.println("compt : " + compt);
            // System.out.println("Volt : " +  ligneVolt[i].getVolt());
            if(ligneVolt[i].getVolt() != compt || ligneVolt[i].getPoint() != point) return false;
        }
        
        return true;
    }


    public List<List<Integer>> generateLineWithNumber(List<Integer> pattern, int index){
        /*max = 3 car peut aller de 0 à 3 (0 voltorbe), size = 5 car 5 cases par ligne, target est le nombre de point */
        List<List<Integer>> l = findCombinations(pattern, colVolt[index].getPoint(), 3, 5);
        // System.out.println(index +  ":Nombre de combinaisons de chiffre pour atteindre " + colVolt[index].getPoint() + " avec " + colVolt[index].getVolt() + " voltorbe(s) : " + l.size());
        List<List<Integer>> results = new ArrayList<>();
        for(List<Integer> l1 : l){
            results.addAll(Permuations.generatePermutations(l1));
        }
        return results;

    }

    public List<List<Integer>> findCombinations(List<Integer> pattern, int target, int max, int size){
        List<List<Integer>> results = new ArrayList<>();
        generateCombinations(results, new ArrayList<>(), pattern, target, max, size, 0);
        return results;
    }

    public boolean areListsEqualIgnoringOrderWithStreams(List<Integer> list1, List<Integer> list2) {
    return list1.size() == list2.size() && 
           list1.stream().sorted().collect(Collectors.toList())
               .equals(list2.stream().sorted().collect(Collectors.toList()));
    }

    public void generateCombinations(List<List<Integer>> results, List<Integer> current, List<Integer> pattern, int target, int max, int size, int index){
        if(index == size){
            /*On enlève la redondance */
            if(target == 0 && !results.contains(current)){
                for(List<Integer> l : results){
                    if(areListsEqualIgnoringOrderWithStreams(l, current)) return;
                }
                results.add(new ArrayList<>(current));
            }
            return;
        }

        for(int i = 1; i<= max; i++){
            if(pattern.get(index).equals(0)){
                current.add(0);
                generateCombinations(results, current, pattern, target - 0, max, size, index + 1);
            }else{
                current.add(i);
                generateCombinations(results, current, pattern, target - i, max, size, index + 1);
            }
            current.remove(current.size() - 1);
        }

    }

    /*Permet de définir les nouvelles probas dans le board */
    public void compute(){

        for(int i = 0; i<probs.getBoard().size(); i++){
            List<Integer> l1 = new ArrayList<>(probs.getBoard().get(i)); // Convertir en liste modifiable
            int comptCol = 0; // compte le nombre de -1 pour la ligne
            
            for(int j = 0; j<l1.size(); j++){
                if(board.getBoard().get(i).get(j).equals(-1)){
                    comptCol++;
                    int newValue = 0;
                    for(int k = 0; k<possible_comb.size(); k++){
                        if(possible_comb.get(k).getBoard().get(i).get(j) >= 1)
                            newValue += 1;
                    }
                    // System.out.println("newValue : " + newValue);
                    l1.set(j, (int)(((double)newValue / possible_comb.size()) * 100));
                }
            }
            int comptLigne = 0; // compte le nombre de -1 pour la colonne
            // System.out.println("comptCol : " + comptCol);
            // System.out.println("comptLigne : " + comptLigne);
            if(comptCol != 0 && comptLigne != 0){
                for(int j = 0; j<l1.size(); j++){
                    for(int k = 0; k<l1.size(); k++){
                        if(board.getBoard().get(k).get(j).equals(-1)){
                            comptLigne++;
                        }
                    }
                    double probCol = (1 - ((double)colVolt[i].getVolt() / comptCol));
                    // System.out.println("ProbCol " + probCol);
                    double probLigne = (1 - ((double)ligneVolt[j].getVolt() / comptLigne));
                    // System.out.println("ProbLigne " + probLigne);
                    double probSide = probCol * probLigne;
                    // System.out.println("ProbSide : " + probSide);
                    // System.out.println("Probs board : " + probs.getBoard().get(i).get(j));
                    l1.set(j, (int)((double)l1.get(j) * probSide));
                }
            }
            
            
            probs.getBoard().set(i, l1); // Remplacer la liste d'origine par la liste modifiée
        }

    }


    /*Permet de converger vers la bonne grille parmi la liste de grille, on enlève les "mauvaises grilles" au fur et à mesure */
    public void purify(){
        for(int i = 0; i<board.getSize(); i++){
            List<Integer> l1 = board.getBoard().get(i);
            for(int j = 0; j<l1.size(); j++){
                if(!l1.get(j).equals(-1)){
                    for(int k = possible_comb.size() - 1; k>=0; k--){
                        if(!possible_comb.get(k).getBoard().get(i).get(j).equals(l1.get(j))){
                            possible_comb.remove(k);
                        }
                    }
                }
            }
        }
        // System.out.println("Grilles restantes : " + possible_comb.size());
    }


    public boolean isFinish(){
        boolean finish = false;
        for(int i = 0; i<board.getSize(); i++){
            int point = colVolt[i].getPoint();
            int num = colVolt[i].getPoint() - colVolt[i].getVolt();
            List<Integer> l1 = board.getBoard().get(i);
            if(num > 0){
                int compt = 0;
                for(Integer nb : l1){
                    if(nb > 1){
                        point = point - nb;
                    }else{
                        compt++;
                    }
                }
                if(point - (compt - colVolt[i].getVolt()) == 0){
                    finish = true;
                }else{
                    return false;
                }
            }else if(colVolt[i].getPoint().equals(2) && colVolt[i].getVolt().equals(3)){
                finish = true;
            }else{
                if(colVolt[i].getPoint() == 1) finish = true;
                else if(Math.abs(num) > 1){
                    int compt = 0;
                    for(Integer nb : l1){
                        if(nb > 1){
                            point = point - nb;
                        }else{
                            compt++;
                        }
                    }
                    if(point - (compt - colVolt[i].getVolt()) == 0){
                        finish = true;
                    }else{
                        return false;
                    }
                }
            }
        }
        return finish;
    }
}