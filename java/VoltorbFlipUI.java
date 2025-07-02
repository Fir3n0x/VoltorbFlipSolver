import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

public class VoltorbFlipUI {
    private JFrame frame;
    private JButton[][] boardButtons;
    private JLabel infoLabel;
    private JLabel infoProba;
    private VoltorbFlipSolver solver; // Votre classe de logique
    private JTextField[][] rowFields;
    private JTextField[][] columnFields;
    private Pair<Integer,Integer>[] ligneVolt;
    private Pair<Integer,Integer>[] colVolt;


    public VoltorbFlipUI(VoltorbFlipSolver solver) {
        this.solver = solver;
        initialize();
    }

    private void initialize() {
        // Créer la fenêtre principale
        frame = new JFrame("Voltorb Flip Solver");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(800, 800); // Augmenter la hauteur pour inclure les champs et boutons
        frame.setLayout(new BorderLayout());

        ligneVolt = (Pair<Integer, Integer>[]) new Pair[5]; // 5 pour le nombre de lignes
        colVolt = (Pair<Integer, Integer>[]) new Pair[5];  
 
        // Créer le plateau de jeu
        JPanel boardPanel = new JPanel();
        boardPanel.setLayout(new GridLayout(5, 5)); // Grille de 5x5
        boardButtons = new JButton[5][5];

        for (int i = 0; i < 5; i++) {
            for (int j = 0; j < 5; j++) {
                JButton button = new JButton("-1");
                button.setBackground(Color.LIGHT_GRAY);
                button.setFont(new Font("Arial", Font.PLAIN, 20));
                int row = i, col = j;

                // Ajouter un événement de clic pour chaque bouton
                button.addActionListener(e -> onCellClicked(row, col, button));
                boardButtons[i][j] = button;
                boardPanel.add(button);
            }
        }

        // Ajouter les champs d'entrée pour les lignes
        JPanel rowInputsPanel = new JPanel();
        rowInputsPanel.setLayout(new GridLayout(5, 1)); // Une ligne par paire de champs

        rowFields = new JTextField[5][2]; // Deux champs par ligne
        for (int i = 0; i < 5; i++) {
            JPanel rowInputPair = new JPanel(); // Panneau horizontal pour chaque ligne
            rowInputPair.setLayout(new FlowLayout(FlowLayout.CENTER));

            JTextField field1 = new JTextField(5); // Premier champ
            JTextField field2 = new JTextField(5); // Deuxième champ

            rowFields[i][0] = field1;
            rowFields[i][1] = field2;

            rowInputPair.add(field1);
            rowInputPair.add(field2);

            rowInputsPanel.add(rowInputPair); // Ajouter la paire au panneau des lignes
        }

        // Ajouter les champs d'entrée pour les colonnes
        JPanel columnInputsPanel = new JPanel();
        columnInputsPanel.setLayout(new GridLayout(1, 5)); // Une colonne par champ d'entrée

        columnFields = new JTextField[5][2]; // Deux champs par colonne
        for (int j = 0; j < 5; j++) {
            JPanel columnInputPair = new JPanel(); // Panneau vertical pour chaque colonne
            columnInputPair.setLayout(new BoxLayout(columnInputPair, BoxLayout.Y_AXIS)); // Champs empilés

            JTextField field1 = new JTextField(5); // Premier champ
            JTextField field2 = new JTextField(5); // Deuxième champ

            columnFields[j][0] = field1;
            columnFields[j][1] = field2;

            columnInputPair.add(field1);
            columnInputPair.add(field2);

            columnInputsPanel.add(columnInputPair); // Ajouter la paire au panneau des colonnes
        }

        // Panneau pour les informations
        // JPanel infoPanel = new JPanel();
        // infoPanel.setLayout(new BorderLayout());

        // infoLabel = new JLabel("Bienvenue dans Voltorb Flip!");
        // infoLabel.setHorizontalAlignment(SwingConstants.CENTER);
        // infoPanel.add(infoLabel, BorderLayout.CENTER);

        // Boutons pour résoudre ou réinitialiser
        JPanel TopPanel = new JPanel(); // Panneau pour placer les boutons en bas à gauche
        TopPanel.setLayout(new FlowLayout(FlowLayout.CENTER));

        JButton solveButton = new JButton("Solve");
        solveButton.addActionListener(e -> solveBoard());
        JButton resetButton = new JButton("Reset");
        resetButton.addActionListener(e -> resetBoard());
        JButton setValues = new JButton("Set Values");
        setValues.addActionListener(e -> setValues());


        TopPanel.add(solveButton);
        TopPanel.add(resetButton);
        TopPanel.add(setValues);

        // Panneau pour afficher le tableau des probabilités (gauche)
        JPanel probabilityPanel = new JPanel();
        probabilityPanel.setLayout(new BorderLayout());
        probabilityPanel.setBorder(BorderFactory.createTitledBorder("Probs"));

        infoProba = new JLabel("Set values");
        infoProba.setHorizontalAlignment(SwingConstants.CENTER);
        probabilityPanel.add(infoProba, BorderLayout.CENTER);

        // JEditorPane probabilityTable = new JEditorPane();
        // probabilityTable.setContentType("text/html");
        // probabilityTable.setText("<html><body><h3>Probabilités</h3><table border='1'><tr><th>Case</th><th>Probabilité</th></tr></table></body></html>");
        // probabilityTable.setEditable(false);

        // JScrollPane scrollPane = new JScrollPane(probabilityTable);
        // probabilityPanel.add(scrollPane, BorderLayout.CENTER);

        // Ajouter les panneaux à la fenêtre
        frame.add(boardPanel, BorderLayout.CENTER); // Grille centrale
        frame.add(rowInputsPanel, BorderLayout.EAST); // Champs pour les lignes à droite
        frame.add(columnInputsPanel, BorderLayout.SOUTH); // Champs pour les colonnes en haut
        frame.add(TopPanel, BorderLayout.NORTH); // Boutons "Résoudre" et "Réinitialiser" en haut
        frame.add(probabilityPanel, BorderLayout.WEST); // Tableau des probabilités à gauche

        frame.setVisible(true);
    }

    private void onCellClicked(int row, int col, JButton button) {
        String[] options = {"0", "1", "2", "3"}; // Valeurs possibles
        String value = (String) JOptionPane.showInputDialog(
                frame,
                "Sélectionnez une valeur pour la cellule [" + row + ", " + col + "]:",
                "Modifier cellule",
                JOptionPane.QUESTION_MESSAGE,
                null,
                options,
                options[0]
        );

        if (value != null) {
            button.setBackground(Color.GREEN);
            button.setText(value);
            solver.getBoard().setCell(row, col, Integer.parseInt(value));
            solver.purify();
            solver.compute();
            updateInfo();
        }
    }

    private void setValues() {
        for (int i = 0; i < 5; i++) {
            // Récupérer les valeurs pour les lignes
            String colValue1 = columnFields[i][0].getText(); // Probabilité
            String colValue2 = columnFields[i][1].getText(); // Voltorb
    
            // Assurez-vous que les valeurs ne sont pas vides et les convertir en entier
            try {
                int prob = Integer.parseInt(colValue1.trim());
                int volt = Integer.parseInt(colValue2.trim());
    
                // Mettre à jour ligneVolt[i] avec les valeurs
                ligneVolt[i] = new Pair<>(prob, volt);
            } catch (NumberFormatException e) {
                // Gérer les erreurs si les champs ne contiennent pas des entiers valides
                JOptionPane.showMessageDialog(frame, "Valeurs invalides pour la ligne " + (i+1));
                return; // Sortir de la méthode si une valeur est invalide
            }
            
            // Récupérer les valeurs pour les colonnes
            String rowValue1 = rowFields[i][0].getText(); // Probabilité
            String rowValue2 = rowFields[i][1].getText(); // Voltorb
    
            // Assurez-vous que les valeurs ne sont pas vides et les convertir en entier
            try {
                int prob = Integer.parseInt(rowValue1.trim());
                int volt = Integer.parseInt(rowValue2.trim());
    
                // Mettre à jour colVolt[i] avec les valeurs
                colVolt[i] = new Pair<>(prob, volt);
            } catch (NumberFormatException e) {
                // Gérer les erreurs si les champs ne contiennent pas des entiers valides
                JOptionPane.showMessageDialog(frame, "Valeurs invalides pour la colonne " + (i+1));
                return; // Sortir de la méthode si une valeur est invalide
            }
        }

        solver.setColVolt(colVolt);
        solver.setLigneVolt(ligneVolt);
        solver.getPossibleBoard();
    }

    private void solveBoard() {

        solver.compute();
        updateInfo();

    }

    private void resetBoard() {
        solver.getBoard().resetBoard();
        for (int i = 0; i < 5; i++) {
            for (int j = 0; j < 5; j++) {
                boardButtons[i][j].setText("-1");
                boardButtons[i][j].setBackground(Color.LIGHT_GRAY);
            }
        }

        for (int i = 0; i < rowFields.length; i++) {
            for (int j = 0; j < rowFields[i].length; j++) {
                rowFields[i][j].setText("");
            }
        }

        for (int i = 0; i < columnFields.length; i++) {
            for (int j = 0; j < columnFields[i].length; j++) {
                columnFields[i][j].setText("");
            }
        }


        infoProba.setText("Board reset");
    }

    private void updateInfo() {
        infoProba.setText("<html>" +
                "Grid remaining : " + solver.getPossibleGrille().size() + "<br>" +
                solver.getProbs().toString() +
                "</html>");
    }

    public static void main(String[] args) {
        // Exemple d'utilisation avec votre logique
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
        new VoltorbFlipUI(solver);
    }
}
