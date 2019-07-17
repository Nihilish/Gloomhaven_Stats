import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.util.Objects;

public class Top extends BackgroundPanel {

    ClassLoader cl = GHC.class.getClassLoader();
    JButton addM, party, scenario;
    String teamName = "";

    public Top(String name){

        super(new BufferedImage(1,1, Image.SCALE_SMOOTH));

        teamName = name;

        BufferedImage image;

        try {
            image = ImageIO.read(Objects.requireNonNull(cl.getResourceAsStream("topbar.jpg")));
        } catch (Error | Exception e){
            image = null;
        }

        addM = new JButton();
        ImageIcon abg = new ImageIcon(((new ImageIcon(
                Objects.requireNonNull(cl.getResource("add.jpg"))).getImage())).getScaledInstance(60,60,java.awt.Image.SCALE_SMOOTH));
        addM.setIcon(abg);
        addM.setToolTipText("Add party member");
        addM.setPreferredSize(new Dimension(60,60));
        addM.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                GHC.createCharacter();
            }
        });

        party = new JButton();
        ImageIcon pbg = new ImageIcon(((new ImageIcon(
                Objects.requireNonNull(cl.getResource("party.jpg"))).getImage())).getScaledInstance(60,60,java.awt.Image.SCALE_SMOOTH));
        party.setIcon(pbg);
        party.setToolTipText("View party stats");
        party.setPreferredSize(new Dimension(60,60));
        party.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                GHC.partyStats();
            }
        });

        scenario = new JButton();
        ImageIcon sbg = new ImageIcon(((new ImageIcon(
                Objects.requireNonNull(cl.getResource("scenario.jpg"))).getImage())).getScaledInstance(60,60,java.awt.Image.SCALE_SMOOTH));
        scenario.setIcon(sbg);
        scenario.setToolTipText("Scenario menu");
        scenario.setPreferredSize(new Dimension(60,60));
        scenario.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                GHC.scenario();
            }
        });

        setLayout(new FlowLayout(FlowLayout.LEADING,10,0));


        add(addM);
        add(party);
        add(scenario);

        JLabel team = new JLabel(teamName);
        team.setBorder(new EmptyBorder(0,570,0,0));
        team.setForeground(Color.WHITE);
        team.setFont(new Font("SansSerif", Font.BOLD, 40));

        add(team);

        setBorder(new EmptyBorder(20,48,10,10));

        setImage(image.getScaledInstance(1920,100, Image.SCALE_SMOOTH));
    }

}
