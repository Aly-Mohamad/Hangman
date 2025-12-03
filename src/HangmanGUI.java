import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.HashSet;
import java.util.Set;

public class HangmanGUI extends JFrame {

    private final String[] WORDS = {
            "apple", "river", "mountain", "forest", "ocean",
            "sunlight", "shadow", "winter", "summer", "autumn",
            "spring", "breeze", "thunder", "rainfall", "storm",
            "garden", "valley", "bridge", "castle", "village",
            "harbor", "island", "desert", "meadow", "canyon",
            "planet", "galaxy", "comet", "nebula", "meteor",
            "crystal", "emerald", "sapphire", "ruby", "diamond",
            "whisper", "echo", "rhythm", "melody", "harmony",
            "journey", "memory", "fortune", "destiny", "legend",
            "temple", "market", "harvest", "freedom", "victory",
            "compass", "lantern", "feather", "anchor", "voyage",
            "canvas", "mirror", "puzzle", "riddle", "cipher",
            "engine", "circuit", "voltage", "current", "signal",
            "network", "battery", "sensor", "module", "algorithm",
            "orbit", "gravity", "energy", "particle", "fusion",
            "copper", "silver", "carbon", "oxygen", "hydrogen",
            "harp", "trumpet", "violin", "guitar", "drum",
            "window", "tower", "street", "market", "corner",
            "flame", "ember", "ash", "smoke", "fire",
            "bridge", "path", "stone", "cloud", "sky"
    };

    private String word;
    private char[] display;
    private Set<Character> guessed;
    private int mistakes = 0;
    private final int MAX_MISTAKES = 6;

    private JLabel wordLabel;
    private JLabel statusLabel;
    private JTextField inputField;
    private JButton guessButton;

    private HangmanPanel drawingPanel;

    public HangmanGUI() {
        setTitle("Hangman Game");
        setSize(550, 550);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());;

        startNewGame();

        wordLabel = new JLabel(String.valueOf(display), SwingConstants.CENTER);
        wordLabel.setFont(new Font("Monospaced", Font.BOLD, 30));
        add(wordLabel, BorderLayout.NORTH);

        drawingPanel = new HangmanPanel();
        add(drawingPanel, BorderLayout.CENTER);

        JPanel bottom = new JPanel(new GridLayout(2, 1));

        statusLabel = new JLabel("Mistakes: 0 / " + MAX_MISTAKES, SwingConstants.CENTER);
        statusLabel.setFont(new Font("Arial", Font.BOLD, 18));

        JPanel inputPanel = new JPanel();
        inputField = new JTextField(5);
        guessButton = new JButton("Guess");

        guessButton.addActionListener(e -> guessLetter());
        inputField.addActionListener(e -> guessLetter());

        inputPanel.add(new JLabel("Letter:"));
        inputPanel.add(inputField);
        inputPanel.add(guessButton);

        bottom.add(statusLabel);
        bottom.add(inputPanel);

        add(bottom, BorderLayout.SOUTH);
    }

    private void startNewGame() {
        word = WORDS[(int)(Math.random() * WORDS.length)];
        display = new char[word.length()];
        guessed = new HashSet<>();

        for (int i = 0; i < word.length(); i++)
            display[i] = '_';

        mistakes = 0;
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
            statusLabel.setText("Mistakes: " + mistakes + " / " + MAX_MISTAKES);
            drawingPanel.setMistakes(mistakes);
        } else {
            wordLabel.setText(String.valueOf(display));
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
        wordLabel.setText(String.valueOf(display));
        statusLabel.setText("Mistakes: 0 / " + MAX_MISTAKES);
        drawingPanel.setMistakes(mistakes);
    }
}
