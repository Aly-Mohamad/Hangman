import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

public class HangmanGUI extends JFrame {

    private final String[] WORDS = {
            "apple", "river", "mountain", "forest", "ocean",
            "sun", "sky", "tree", "cloud", "stone",
            "shadow", "winter", "summer", "autumn", "spring",
            "breeze", "thunder", "rain", "storm", "garden",
            "valley", "bridge", "castle", "village", "harbor",
            "island", "desert", "meadow", "canyon", "planet",
            "galaxy", "comet", "nebula", "meteor", "crystal",
            "emerald", "sapphire", "ruby", "diamond", "whisper",
            "echo", "rhythm", "melody", "harmony", "journey",
            "memory", "fortune", "destiny", "legend", "temple",
            "market", "harvest", "freedom", "victory", "compass",
            "lantern", "feather", "anchor", "voyage", "canvas",
            "mirror", "puzzle", "riddle", "cipher", "engine",
            "circuit", "voltage", "current", "signal", "network",
            "battery", "sensor", "module", "algorithm", "orbit",
            "gravity", "energy", "particle", "fusion", "copper",
            "silver", "carbon", "oxygen", "hydrogen", "harp",
            "trumpet", "violin", "guitar", "drum", "window",
            "tower", "street", "corner", "flame", "ember",
            "ash", "smoke", "fire", "path", "stone",
            "riverbank", "waterfall", "earth", "cliff", "stormy",
            "lightning", "blossom", "nature", "foresting", "wildlife",
            "terrain", "thicket", "prairie", "savanna", "rainforest",
            "glacier", "tundra", "volcano", "geyser", "horizon",
            "sunrise", "sunset", "dusk", "dawn", "midnight",
            "twilight", "daybreak", "seaside", "hurricane", "cyclone",
            "monsoon", "drought", "lake", "pond", "stream",
            "brook", "wave", "tidal", "islet", "cavern",
            "quartz", "granite", "marble", "basalt", "shale",
            "pebble", "boulder", "field", "grove", "orchard",
            "harvested", "crop", "farmland", "homestead", "cottage",
            "cabin", "campfire", "trail", "footpath", "gate",
            "fence", "laneway", "roadway", "highway", "pathway",
            "journeyman", "traveler", "wanderer", "explorer", "adventure",
            "discovery", "mystery", "enigma", "secret", "potion",
            "cauldron", "wizard", "sorcerer", "spellbook", "magic",
            "phantom", "spirit", "specter", "ghost", "portal",
            "dimension", "timeline", "universe", "cosmos", "starlight",
            "asteroid", "spaceship", "satellite", "robot", "android",
            "machine", "mechanism", "cable", "hardware", "software",
            "program", "compiler", "syntax", "function", "variable",
            "matrix", "vector", "element", "integer", "boolean",
            "decimal", "fraction", "formula", "equation", "theorem",
            "alchemy", "artifact", "relic", "chronicle", "scripture",
            "kingdom", "empire", "battle", "soldier", "weapon",
            "shield", "helmet", "armor", "chariot", "vessel",
            "merchant", "trader", "currency", "treasure", "goldmine",
            "minecart", "pickaxe", "forge", "smelter", "hammer",
            "workshop", "blueprint", "design", "structure", "foundation"
    };

    private String word;
    private char[] display;
    private Set<Character> guessed;
    private int mistakes = 0;
    private final int MAX_MISTAKES = 6;
    private ArrayList<Character> wrongs;
    private int MAX_HINTS;
    private int hints;

    private JLabel wordLabel;
    private JLabel statusLabel;
    private JTextField inputField;
    private JButton guessButton;
    private JButton hintButton;

    private JPanel rightPanel;
    private JPanel wrongListPanel;
    private HangmanPanel drawingPanel;

    public HangmanGUI() {
        setTitle("Hangman Game");
        setSize(800, 550);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        startNewGame();

        wordLabel = new JLabel(spacedDisplay(), SwingConstants.CENTER);
        wordLabel.setFont(new Font("Monospaced", Font.BOLD, 30));
        add(wordLabel, BorderLayout.NORTH);

        drawingPanel = new HangmanPanel();
        add(drawingPanel, BorderLayout.CENTER);

        JPanel bottom = new JPanel(new GridLayout(2, 1));

        statusLabel = new JLabel("Mistakes: 0 / " + MAX_MISTAKES, SwingConstants.CENTER);
        statusLabel.setFont(new Font("Arial", Font.BOLD, 18));

        JPanel inputPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 5));
        inputField = new JTextField(5);
        guessButton = new JButton("Guess");

        guessButton.addActionListener(e -> guessLetter());
        inputField.addActionListener(e -> guessLetter());

        hintButton = new JButton("💡 " + (MAX_HINTS-hints));
        hintButton.addActionListener(e -> hintLetter());

        inputPanel.add(new JLabel("Letter:"));
        inputPanel.add(inputField);
        inputPanel.add(guessButton);
        inputPanel.add(hintButton);

        bottom.add(statusLabel);
        bottom.add(inputPanel);
        bottom.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        rightPanel = new JPanel();
        rightPanel.setPreferredSize(new Dimension(140, 300));
        rightPanel.setLayout(new BorderLayout());

        JLabel wrongTitle = new JLabel("Wrong Letters", SwingConstants.CENTER);
        wrongTitle.setFont(new Font("Arial", Font.BOLD, 14));
        rightPanel.add(wrongTitle, BorderLayout.NORTH);

        wrongListPanel = new JPanel();
        wrongListPanel.setLayout(new BoxLayout(wrongListPanel, BoxLayout.Y_AXIS));
        wrongListPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JScrollPane scroll = new JScrollPane(wrongListPanel);
        scroll.setBorder(null);
        rightPanel.add(scroll, BorderLayout.CENTER);

        updateWrongList();

        add(rightPanel, BorderLayout.EAST);
        add(bottom, BorderLayout.SOUTH);
    }

    private void startNewGame() {
        word = WORDS[(int)(Math.random() * WORDS.length)];
        display = new char[word.length()];
        guessed = new HashSet<>();
        wrongs = new ArrayList<>();
        MAX_HINTS = word.length()/3;
        hints = 0;

        for (int i = 0; i < word.length(); i++)
            display[i] = '_';

        mistakes = 0;

        if (wrongListPanel != null) {
            updateWrongList();
        }
    }

    private void guessLetter() {
        String text = inputField.getText().trim();
        inputField.setText("");

        if (text.length() != 1) {
            JOptionPane.showMessageDialog(this, "Enter ONE letter.");
            return;
        }

        char c = Character.toLowerCase(text.charAt(0));

        if (guessed.contains(c)) {
            JOptionPane.showMessageDialog(this, "Already guessed!");
            return;
        }

        guessed.add(c);

        boolean found = false;
        for (int i = 0; i < word.length(); i++) {
            if (Character.toLowerCase(word.charAt(i)) == c) {
                display[i] = word.charAt(i);
                found = true;
            }
        }

        if (!found) {
            mistakes++;
            if(mistakes > MAX_MISTAKES/2){
                statusLabel.setForeground(Color.RED);
            }
            else{
                statusLabel.setForeground(Color.BLACK);
            }
            statusLabel.setText("Mistakes: " + mistakes + " / " + MAX_MISTAKES);
            drawingPanel.setMistakes(mistakes);
            wrongs.add(c);
            updateWrongList();
        } else {
            wordLabel.setText(spacedDisplay());
        }

        checkGameEnd();
    }

    private void checkGameEnd() {
        if (mistakes >= MAX_MISTAKES) {
            JOptionPane.showMessageDialog(this, "You lost! The word was: " + word);
            resetGame();
        } else if (String.valueOf(display).equals(word)) {
            JOptionPane.showMessageDialog(this, "You won!");
            resetGame();
        }
    }

    private void resetGame() {
        startNewGame();
        wordLabel.setText(spacedDisplay());
        statusLabel.setText("Mistakes: 0 / " + MAX_MISTAKES);
        drawingPanel.setMistakes(mistakes);
        hintButton.setEnabled(true);
        hintButton.setText("💡 " + MAX_HINTS);
        statusLabel.setForeground(Color.BLACK);
        updateWrongList();
    }

    private String spacedDisplay() {
        StringBuilder sb = new StringBuilder();
        for (char c : display) {
            sb.append(c).append(' ');
        }
        return sb.toString();
    }

    private void updateWrongList() {
        wrongListPanel.removeAll();

        if (wrongs.isEmpty()) {
            JLabel empty = new JLabel();
            empty.setAlignmentX(Component.CENTER_ALIGNMENT);
            wrongListPanel.add(empty);
        } else {
            for (char ch : wrongs) {
                JLabel lbl = new JLabel(String.valueOf(ch));
                lbl.setFont(new Font("Monospaced", Font.BOLD, 18));
                lbl.setAlignmentX(Component.CENTER_ALIGNMENT);
                wrongListPanel.add(lbl);
                wrongListPanel.add(Box.createRigidArea(new Dimension(0, 6)));
            }
        }

        wrongListPanel.revalidate();
        wrongListPanel.repaint();
    }

    public void hintLetter(){
        ArrayList<Integer> hidden = new ArrayList<>();
        for (int i = 0; i < word.length(); i++) {
            if (display[i] == '_') {
                hidden.add(i);
            }
        }

        if (hidden.isEmpty()) {
            return;
        }

        char x = word.charAt(hidden.get((int)(Math.random() * hidden.size())));
        for (int i = 0; i < word.length(); i++) {
            if (x == word.charAt(i)) {
                display[i] = x;
            }
        }
        guessed.add(x);
        wordLabel.setText(spacedDisplay());
        hints++;
        hintButton.setText("💡 " + (MAX_HINTS-hints));

        if ((MAX_HINTS-hints) <= 0) {
            hintButton.setEnabled(false);
        }

        checkGameEnd();
    }
}
