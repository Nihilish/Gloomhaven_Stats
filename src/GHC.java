import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.ChartUtilities;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PiePlot;
import org.jfree.data.general.DefaultPieDataset;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import javax.swing.text.Style;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.*;
import java.sql.Array;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Objects;

public class GHC {

    static JFrame f;
    static ClassLoader cl = GHC.class.getClassLoader();
    static ArrayList<Character> characters = new ArrayList<>();
    static ArrayList<Banner> banners = new ArrayList<>();
    static String name = "";
    static BackgroundPanel container;
    static Top topBar;
    static JPanel container2, charContainer, scenarioButtons;
    static ArrayList<Character> party = new ArrayList<>();

    public static void main(String[] args) {

        f = new JFrame("Gloomhaven Companion");
        f.setSize(1920,1080);
        f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        try {
            FileInputStream fis = new FileInputStream("char.tmp");
            ObjectInputStream ois = new ObjectInputStream(fis);
            characters = (ArrayList<Character>) ois.readObject();
            ois.close();

            fis = new FileInputStream("party.tmp");
            ois = new ObjectInputStream(fis);
            name = (String) ois.readObject();
            ois.close();
        } catch (Error | Exception e){
            JOptionPane.showMessageDialog(null, "Welcome to Gloomhaven Companion 1.0!");
            JFrame frame = new JFrame();
            Object result = JOptionPane.showInputDialog(frame, "Enter your party's name: ");
            name = ""+result;

            try {
                FileOutputStream fos = new FileOutputStream("party.tmp");
                ObjectOutputStream oos = new ObjectOutputStream(fos);
                oos.writeObject(name);
                oos.close();
            } catch (Error | Exception a){

            }

        }

        //Top level container. Nests all the components within the frame
        container = new BackgroundPanel(new BufferedImage(1,1,Image.SCALE_SMOOTH));
        container.setLayout(new BorderLayout());

        //The Top bar is a nav-bar, therefore is the only components that will never get updated
        topBar = new Top(name);

        container.add(topBar, BorderLayout.NORTH);

        //Container2 basically nests any kind of dynamic content
        container2 = new JPanel(new BorderLayout());

        //Simply the participating characters
        charContainer = new JPanel(new FlowLayout(FlowLayout.CENTER,0,8));

        //Allows for a background eventually, won't be visible for a full party
        charContainer.setPreferredSize(new Dimension(1800, 800));

        for(int i = 0; i < 4; i++){
            if(i >= characters.size()) {
                Banner temp = new Banner(null);
                charContainer.add(temp);
                banners.add(temp);
            }
            else {
                Banner temp = new Banner(characters.get(i));
                party.add(characters.get(i));
                charContainer.add(temp);
                banners.add(temp);
            }
        }


        container2.add(charContainer, BorderLayout.CENTER);

        scenarioButtons = new JPanel(new FlowLayout(FlowLayout.TRAILING));

        JButton newScenario = new JButton("New scenario");
        StretchIcon bg = new StretchIcon(((new StretchIcon(
                Objects.requireNonNull(cl.getResource("button.png"))).getImage().getScaledInstance(250, 40, Image.SCALE_SMOOTH))));
        formatButton(bg, newScenario);
        newScenario.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                if(confirm("New Scenario?")) {

                    for (int i = 0; i < characters.size(); i++) {
                        characters.get(i).newScenario();
                    }

                }
            }
        });


        JButton stats = new JButton("Scenario Stats");
        formatButton(bg, stats);
        stats.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                scenarioStats();
            }
        });

        JButton newTurn = new JButton("Next round");
        formatButton(bg, newTurn);
        newTurn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                if(confirm("Did everyone play their turn?")) {

                    for (int i = 0; i < party.size(); i++) {
                        if (banners.get(i).c != null) {
                            banners.get(i).updateValues();
                        }
                    }
                }
            }
        });


        scenarioButtons.add(newScenario);
        scenarioButtons.add(stats);
        scenarioButtons.add(newTurn);

        container2.add(scenarioButtons, BorderLayout.NORTH);

        scenarioButtons.setBorder(new EmptyBorder(10,0,0,50));

        scenarioButtons.setOpaque(false);
        charContainer.setOpaque(false);
        container2.setOpaque(false);

        BufferedImage image;

        try {

            image = ImageIO.read(Objects.requireNonNull(cl.getResource("wpbg.jpg")));
        } catch (Error | Exception e){
            image = null;
        }

        container.setImage(image);

        container.add(container2, BorderLayout.CENTER);

        f.add(container);

        f.setVisible(true);

    }

    public static boolean confirm(String message){
        int response = JOptionPane.showConfirmDialog(null, message, "Confirm",
                JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
        if (response == JOptionPane.NO_OPTION) {
            return false;
        } else if (response == JOptionPane.YES_OPTION) {
            return true;
        } else if (response == JOptionPane.CLOSED_OPTION) {
            return false;
        }

        return false;
    }

    private static void formatButton(StretchIcon bg, JButton stats) {
        stats.setIcon(bg);
        stats.setPreferredSize(new Dimension(180,30));
        stats.setOpaque(false);
        stats.setFocusPainted(false);
        stats.setBorderPainted(false);
        stats.setContentAreaFilled(false);
        stats.setBorder(BorderFactory.createEmptyBorder());
        stats.setHorizontalTextPosition(JButton.CENTER);
        stats.setVerticalTextPosition(JButton.CENTER);
        stats.setForeground(Color.WHITE);
        stats.setFont(new Font("SansSerif", Font.BOLD, 15));
        stats.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                stats.setForeground(Color.YELLOW);
            }

            public void mouseExited(java.awt.event.MouseEvent evt) {
                stats.setForeground(Color.WHITE);
            }
        });

        stats.setCursor(new Cursor(Cursor.HAND_CURSOR));
    }

    public static void createCharacter(){
        String[] choices = { "Cragheart", "Scoundrel", "Mindthief", "Brute", "Tinkerer", "Spellweaver" };
        String input = (String) JOptionPane.showInputDialog(null, "Choose the character's class...",
                "Class", JOptionPane.QUESTION_MESSAGE, null, // Use
                // default
                // icon
                choices, // Array of choices
                choices[1]); // Initial choice

        JFrame frame = new JFrame();
        Object result = JOptionPane.showInputDialog(frame, "Enter the character's name:");

        if(result != null && result.toString().length() > 0) {

            Character newchar = new Character("" + result, input);

            JOptionPane.showMessageDialog(null, "Character created!");

            characters.add(newchar);

            try {
                FileOutputStream fos = new FileOutputStream("char.tmp");
                ObjectOutputStream oos = new ObjectOutputStream(fos);
                oos.writeObject(characters);
                oos.close();
            } catch (Error | Exception e) {

            }

        }
    }

    public static void save(){
        try {
            FileOutputStream fos = new FileOutputStream("char.tmp");
            ObjectOutputStream oos = new ObjectOutputStream(fos);
            oos.writeObject(characters);
            oos.close();
        } catch (Error | Exception e){

        }
    }

    public static Character loadCharacter(){


        String[] choices = new String[characters.size()];
        for(int i = 0; i < choices.length; i++){
            choices[i] = characters.get(i).toString();
        }

        String input="";

        if(choices.length > 0) {

            input = (String) JOptionPane.showInputDialog(null, "Choose the character to add...",
                    "Add member", JOptionPane.QUESTION_MESSAGE, null, // Use
                    // default
                    // icon
                    choices, // Array of choices
                    choices[0]); // Initial choice

        } else{
            JOptionPane.showMessageDialog(null, "You must create a character first!");
        }

        for(int i = 0; i < choices.length; i++){
            if(choices[i].equals(input)) return characters.get(i);
        }

        return null;

    }

    public static void partyStats(){
        container2.removeAll();
        JPanel bg = new JPanel(new GridLayout(1,2,20,0));
        bg.setBorder(new EmptyBorder(20,20,20,20));
        bg.setBackground(new Color(1.0f, 1.0f, 1.0f, 0.5f));

        JTextArea stats = new JTextArea();
        stats.setOpaque(false);
        stats.setFont(new Font("SansSerif", Font.BOLD, 40));

        int totalDamage, totalHeals, totalKills, totalDamageTaken, totalTurns;
        totalDamage = totalHeals = totalKills = totalDamageTaken =  totalTurns = 0;
        double averageDamagePerTurn, averageHealsPerTurn, averageDamageTakenPerTurn, averageKillsperTurn;

        for(int i = 0; i < characters.size(); i++){

            totalDamage+=characters.get(i).dmg;
            totalHeals+=characters.get(i).heals;
            totalKills+=characters.get(i).kills;
            totalDamageTaken+=characters.get(i).damagetaken;

            if(characters.get(i).turnsplayed > totalTurns){
                totalTurns = characters.get(i).turnsplayed;
            }

        }

        averageDamagePerTurn = (double) totalDamage/totalTurns;
        averageHealsPerTurn = (double) totalHeals/totalTurns;
        averageKillsperTurn = (double) totalKills/totalTurns;
        averageDamageTakenPerTurn = (double) totalDamageTaken/totalTurns;

        DecimalFormat df = new DecimalFormat("#.##");

        String textStats = "";
        textStats+="Total damage: "+totalDamage+"\n";
        textStats+="Total kills: "+totalKills+"\n";
        textStats+="Total Heals: "+totalHeals+"\n";
        textStats+="Total Damage Taken: "+totalDamageTaken+"\n";
        textStats+="Total Turns Played: "+totalTurns+"\n";
        textStats+="Average damage per round: "+df.format(averageDamagePerTurn)+"\n";
        textStats+="Average kills per turn: "+df.format(averageKillsperTurn)+"\n";
        textStats+="Average heals per turn: "+df.format(averageHealsPerTurn)+"\n";
        textStats+="Average damage taken per turn: "+df.format(averageDamageTakenPerTurn)+"\n";

        stats.setText(textStats);

        JPanel stats2 = new JPanel(new GridLayout(4,1));
        JScrollPane stats2cont = new JScrollPane(stats2);

        //DAMAGE/////////////

        DefaultPieDataset dataset = new DefaultPieDataset( );

        for(int i = 0; i < characters.size(); i++){
            dataset.setValue(characters.get(i).name, characters.get(i).dmg);
        }

        JFreeChart chart = ChartFactory.createPieChart(
                "Total Damage",   // chart title
                dataset,          // data
                true,             // include legend
                true,
                false);

        PiePlot plot1 = (PiePlot) chart.getPlot();

        painting(plot1);

        ChartPanel damageChart = new ChartPanel(chart);

        //KILLS/////////////

        DefaultPieDataset dataset2 = new DefaultPieDataset( );

        for(int i = 0; i < characters.size(); i++){
            dataset2.setValue(characters.get(i).name, characters.get(i).kills);
        }

        JFreeChart chart2 = ChartFactory.createPieChart(
                "Total Kills",   // chart title
                dataset2,          // data
                true,             // include legend
                true,
                false);

        PiePlot plot2 = (PiePlot) chart2.getPlot();

        painting(plot2);

        ChartPanel killsChart = new ChartPanel(chart2);

        //HEALS/////////////

        DefaultPieDataset dataset3 = new DefaultPieDataset( );

        for(int i = 0; i < characters.size(); i++){
            dataset3.setValue(characters.get(i).name, characters.get(i).heals);
        }

        JFreeChart chart3 = ChartFactory.createPieChart(
                "Total Heals",   // chart title
                dataset3,          // data
                true,             // include legend
                true,
                false);


        PiePlot plot3 = (PiePlot) chart3.getPlot();

        painting(plot3);

        ChartPanel healsChart = new ChartPanel(chart3);

        //DAMAGE TAKEN/////////////

        DefaultPieDataset dataset4 = new DefaultPieDataset( );

        for(int i = 0; i < characters.size(); i++){
            dataset4.setValue(characters.get(i).name, characters.get(i).damagetaken);
        }

        JFreeChart chart4 = ChartFactory.createPieChart(
                "Total Damage Taken",   // chart title
                dataset4,          // data
                true,             // include legend
                true,
                false);

        PiePlot plot4 = (PiePlot) chart4.getPlot();

        painting(plot4);

        ChartPanel dtChart = new ChartPanel(chart4);

        stats2.add(damageChart);
        stats2.add(killsChart);
        stats2.add(healsChart);
        stats2.add(dtChart);





        ///////////////////////

        bg.add(stats);
        bg.add(stats2cont);



        container2.setBorder(new EmptyBorder(58,56,5,56));




        container2.add(bg, BorderLayout.CENTER);






        container2.revalidate();
        container2.repaint();


    }

    public static void scenarioStats(){
        container2.removeAll();
        JPanel bg = new JPanel(new GridLayout(1,2,20,0));
        bg.setBorder(new EmptyBorder(20,20,20,20));
        bg.setBackground(new Color(1.0f, 1.0f, 1.0f, 0.5f));

        JTextArea stats = new JTextArea();
        stats.setOpaque(false);
        stats.setFont(new Font("SansSerif", Font.BOLD, 40));

        int totalDamage, totalHeals, totalKills, totalDamageTaken, totalTurns;
        totalDamage = totalHeals = totalKills = totalDamageTaken =  totalTurns = 0;
        double averageDamagePerTurn, averageHealsPerTurn, averageDamageTakenPerTurn, averageKillsperTurn;

        for(int i = 0; i < characters.size(); i++){

            if(characters.get(i).hasPlayed) {

                totalDamage += characters.get(i).sDmg;
                totalHeals += characters.get(i).sHeals;
                totalKills += characters.get(i).sKills;
                totalDamageTaken += characters.get(i).sDt;

                if (characters.get(i).sTurns > totalTurns) {
                    totalTurns = characters.get(i).sTurns;
                }
            }

        }

        averageDamagePerTurn = (double) totalDamage/totalTurns;
        averageHealsPerTurn = (double) totalHeals/totalTurns;
        averageKillsperTurn = (double) totalKills/totalTurns;
        averageDamageTakenPerTurn = (double) totalDamageTaken/totalTurns;

        DecimalFormat df = new DecimalFormat("#.##");

        String textStats = "";
        textStats+="Total damage: "+totalDamage+"\n";
        textStats+="Total kills: "+totalKills+"\n";
        textStats+="Total Heals: "+totalHeals+"\n";
        textStats+="Total Damage Taken: "+totalDamageTaken+"\n";
        textStats+="Total Turns Played: "+totalTurns+"\n";
        textStats+="Average damage per round: "+df.format(averageDamagePerTurn)+"\n";
        textStats+="Average kills per turn: "+df.format(averageKillsperTurn)+"\n";
        textStats+="Average heals per turn: "+df.format(averageHealsPerTurn)+"\n";
        textStats+="Average damage taken per turn: "+df.format(averageDamageTakenPerTurn)+"\n";

        stats.setText(textStats);

        JPanel stats2 = new JPanel(new GridLayout(4,1));
        JScrollPane stats2cont = new JScrollPane(stats2);

        //DAMAGE/////////////

        DefaultPieDataset dataset = new DefaultPieDataset( );

        for(int i = 0; i < characters.size(); i++){
            if(characters.get(i).hasPlayed) {
                dataset.setValue(characters.get(i).name, characters.get(i).sDmg);
            }
        }

        JFreeChart chart = ChartFactory.createPieChart(
                "Total Damage",   // chart title
                dataset,          // data
                true,             // include legend
                true,
                false);

        PiePlot plot1 = (PiePlot) chart.getPlot();

        painting(plot1);

        ChartPanel damageChart = new ChartPanel(chart);

        //KILLS/////////////

        DefaultPieDataset dataset2 = new DefaultPieDataset( );

        for(int i = 0; i < characters.size(); i++){
            if(characters.get(i).hasPlayed) {
                dataset2.setValue(characters.get(i).name, characters.get(i).sKills);
            }
        }

        JFreeChart chart2 = ChartFactory.createPieChart(
                "Total Kills",   // chart title
                dataset2,          // data
                true,             // include legend
                true,
                false);

        PiePlot plot2 = (PiePlot) chart2.getPlot();

        painting(plot2);

        ChartPanel killsChart = new ChartPanel(chart2);

        //HEALS/////////////

        DefaultPieDataset dataset3 = new DefaultPieDataset( );

        for(int i = 0; i < characters.size(); i++){
            if(characters.get(i).hasPlayed) {
                dataset3.setValue(characters.get(i).name, characters.get(i).sHeals);
            }
        }

        JFreeChart chart3 = ChartFactory.createPieChart(
                "Total Heals",   // chart title
                dataset3,          // data
                true,             // include legend
                true,
                false);


        PiePlot plot3 = (PiePlot) chart3.getPlot();

        painting(plot3);

        ChartPanel healsChart = new ChartPanel(chart3);

        //DAMAGE TAKEN/////////////

        DefaultPieDataset dataset4 = new DefaultPieDataset( );

        for(int i = 0; i < characters.size(); i++){
            if(characters.get(i).hasPlayed) {
                dataset4.setValue(characters.get(i).name, characters.get(i).sDt);
            }
        }

        JFreeChart chart4 = ChartFactory.createPieChart(
                "Total Damage Taken",   // chart title
                dataset4,          // data
                true,             // include legend
                true,
                false);

        PiePlot plot4 = (PiePlot) chart4.getPlot();

        painting(plot4);

        ChartPanel dtChart = new ChartPanel(chart4);

        stats2.add(damageChart);
        stats2.add(killsChart);
        stats2.add(healsChart);
        stats2.add(dtChart);





        ///////////////////////

        bg.add(stats);
        bg.add(stats2cont);



        container2.setBorder(new EmptyBorder(58,56,5,56));




        container2.add(bg, BorderLayout.CENTER);






        container2.revalidate();
        container2.repaint();


    }

    public static void scenario(){
        container2.removeAll();
        container2.setBorder(new EmptyBorder(0,0,0,0));
        container2.add(scenarioButtons, BorderLayout.NORTH);
        container2.add(charContainer, BorderLayout.CENTER);
        container2.revalidate();
        container2.repaint();
    }

    public static void painting(PiePlot plot){

        GradientPaint scoundrel, brute, cragheart, tinkerer, spellweaver, mindthief;

        scoundrel = new GradientPaint(100,100,new Color(123,244,76), 600, 600, new Color(1,100,7));
        brute = new GradientPaint(200,200,new Color(58,40,61), 400, 400, new Color(89, 86, 142));
        cragheart = new GradientPaint(100,100,new Color(22,176,60), 600, 600, new Color(253, 255, 2));
        tinkerer = new GradientPaint(100,100,new Color(194,201,119), 600, 600, new Color(100,64,37));
        spellweaver = new GradientPaint(150,150,new Color(67,56,104), 250, 250,new Color(183,77,115));
        mindthief = new GradientPaint(100,100,new Color(28,183,60), 500, 500,new Color(7,0,137) );


        for(int i = 0; i < characters.size(); i++){

            GradientPaint paint = null;

            switch (characters.get(i).charclass.toLowerCase()){
                case "brute": paint = brute; break;
                case "scoundrel": paint = scoundrel; break;
                case "cragheart": paint = cragheart; break;
                case "tinkerer": paint = tinkerer; break;
                case "spellweaver": paint = spellweaver; break;
                case "mindthief": paint = mindthief; break;
            }

            plot.setSectionPaint(characters.get(i).name, paint);
        }
    }

}
