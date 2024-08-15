package javanotepad;

import java.io.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.awt.event.KeyEvent;
import javax.swing.KeyStroke;
import javax.swing.filechooser.*;
import java.awt.print.PrinterException;

public class JavaNotepad extends JFrame implements ActionListener {

    JMenuBar menubar = new JMenuBar();
    JMenu file = new JMenu("File");
    JMenu edit = new JMenu("Edit");
    JMenu help = new JMenu("Help");

    JMenuItem newFile = new JMenuItem("New");
    JMenuItem openFile = new JMenuItem("Open");
    JMenuItem saveFile = new JMenuItem("Save");
    JMenuItem print = new JMenuItem("Print");
    JMenuItem exit = new JMenuItem("Exit");

    JMenuItem cut = new JMenuItem("Cut");
    JMenuItem copy = new JMenuItem("Copy");
    JMenuItem paste = new JMenuItem("Paste");
    JMenuItem find = new JMenuItem("Find");
    JMenuItem replace = new JMenuItem("Replace");
    JMenuItem selectAll = new JMenuItem("Select All");

    JMenuItem about = new JMenuItem("About");

    JTextArea textArea = new JTextArea();

    JFileChooser fileChooser = new JFileChooser();
    
     private String lastSearchTerm = "";
    private int lastIndex = 0;

    JavaNotepad() {
        setTitle("Notepad Application");
        setBounds(100, 100, 800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        ImageIcon icon = new ImageIcon(getClass().getResource("edge-logo.png"));
        setIconImage(icon.getImage());

        setJMenuBar(menubar);
        menubar.add(file);
        menubar.add(edit);
        menubar.add(help);

        file.add(newFile);
        file.add(openFile);
        file.add(saveFile);
        file.add(print);
        file.add(exit);

        edit.add(cut);
        edit.add(copy);
        edit.add(paste);
        edit.add(selectAll);
        edit.add(find);
        edit.add(replace);

        help.add(about);

        JScrollPane scrollPane = new JScrollPane(textArea);
        add(scrollPane);
        scrollPane.setHorizontalScrollBarPolicy(scrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        scrollPane.setVerticalScrollBarPolicy(scrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);

        textArea.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, 20));
        textArea.setLineWrap(true);
        textArea.setWrapStyleWord(true);

        // Set action listeners
        newFile.addActionListener(this);
        openFile.addActionListener(this);
        saveFile.addActionListener(this);
        print.addActionListener(this);
        exit.addActionListener(this);
        cut.addActionListener(this);
        copy.addActionListener(this);
        paste.addActionListener(this);
        selectAll.addActionListener(this);
        about.addActionListener(this);
        find.addActionListener(this);
        replace.addActionListener(this);

        // Add keyboard shortcuts
        newFile.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_N, ActionEvent.CTRL_MASK));
        openFile.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_O, ActionEvent.CTRL_MASK));
        saveFile.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_S, ActionEvent.CTRL_MASK));
        print.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_P, ActionEvent.CTRL_MASK));
        cut.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_X, ActionEvent.CTRL_MASK));
        copy.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_C, ActionEvent.CTRL_MASK));
        paste.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_V, ActionEvent.CTRL_MASK));
        selectAll.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_A, ActionEvent.CTRL_MASK));
        find.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_F, ActionEvent.CTRL_MASK));
        replace.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_R, ActionEvent.CTRL_MASK));
    }
    
    
    public static void main(String[] args) {
        new JavaNotepad().setVisible(true);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getActionCommand().equalsIgnoreCase("New")) {
            textArea.setText(null);
        } else if (e.getActionCommand().equalsIgnoreCase("Open")) {
            openFile();
        } else if (e.getActionCommand().equalsIgnoreCase("Save")) {
            saveFile();
        } else if (e.getActionCommand().equalsIgnoreCase("Print")) {
            printFile();
        } else if (e.getActionCommand().equalsIgnoreCase("Exit")) {
            System.exit(0);
        } else if (e.getActionCommand().equalsIgnoreCase("Cut")) {
            textArea.cut();
        } else if (e.getActionCommand().equalsIgnoreCase("Copy")) {
            textArea.copy();
        } else if (e.getActionCommand().equalsIgnoreCase("Paste")) {
            textArea.paste();
        } else if (e.getActionCommand().equalsIgnoreCase("Select All")) {
            textArea.selectAll();
        } else if (e.getActionCommand().equalsIgnoreCase("Find")) {
            findText();
        } else if (e.getActionCommand().equalsIgnoreCase("Replace")) {
            replaceText();
        } else if (e.getActionCommand().equalsIgnoreCase("About")) {
            showAboutDialog();
        }
    }

    private void openFile() {
        int returnValue = fileChooser.showOpenDialog(this);
        if (returnValue == JFileChooser.APPROVE_OPTION) {
            File file = fileChooser.getSelectedFile();
            try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
                textArea.read(reader, null);
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
    }

    private void printFile() {
        try {
            boolean printed = textArea.print();
            if (!printed) {
                JOptionPane.showMessageDialog(this, "Printing Cancelled", "Printer", JOptionPane.INFORMATION_MESSAGE);
            }
        } catch (PrinterException ex) {
            ex.printStackTrace();
        }
    }

    private void saveFile() {
    int returnValue = fileChooser.showSaveDialog(this);
    if (returnValue == JFileChooser.APPROVE_OPTION) {
        File file = fileChooser.getSelectedFile();
        
        // Check if the file has the .txt extension; if not, add it
        if (!file.getName().toLowerCase().endsWith(".txt")) {
            file = new File(file.getParentFile(), file.getName() + ".txt");
        }
        
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(file))) {
            textArea.write(writer);
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }
}

    private void showAboutDialog() {
        JOptionPane.showMessageDialog(this, "Java Notepad\nVersion 1.0", "About", JOptionPane.INFORMATION_MESSAGE);
    }

    private void findText() {
        String searchText = JOptionPane.showInputDialog(this, "Find:", "Find", JOptionPane.PLAIN_MESSAGE);
        if (searchText != null && !searchText.isEmpty()) {
            String textContent = textArea.getText();
            if (!searchText.equals(lastSearchTerm)) {
                lastSearchTerm = searchText;
                lastIndex = 0;
            }
            lastIndex = textContent.indexOf(searchText, lastIndex);
            if (lastIndex != -1) {
                textArea.select(lastIndex, lastIndex + searchText.length());
                lastIndex += searchText.length();
            } else {
                JOptionPane.showMessageDialog(this, "No more occurrences found", "Find", JOptionPane.INFORMATION_MESSAGE);
                lastIndex = 0;
            }
        }
    }

    private void replaceText() {
        JPanel panel = new JPanel(new GridLayout(2, 2));
        JTextField findField = new JTextField(10);
        JTextField replaceField = new JTextField(10);
        panel.add(new JLabel("Find:"));
        panel.add(findField);
        panel.add(new JLabel("Replace with:"));
        panel.add(replaceField);

        int option = JOptionPane.showConfirmDialog(this, panel, "Replace", JOptionPane.OK_CANCEL_OPTION);
        if (option == JOptionPane.OK_OPTION) {
            String findText = findField.getText();
            String replaceText = replaceField.getText();
            if (!findText.isEmpty()) {
                textArea.setText(textArea.getText().replace(findText, replaceText));
            }
        }
    }
}