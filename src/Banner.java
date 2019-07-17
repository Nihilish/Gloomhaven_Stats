import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.util.Objects;

public class Banner extends BackgroundPanel {

    Character c;
    Counter d,k,h,dt; //damage, kills, heals, damage taken
    JButton dead, ex, info;
    JLabel name;
    ClassLoader cl = GHC.class.getClassLoader();

    public Banner(Character c){

        super(new BufferedImage(1,1,Image.SCALE_SMOOTH));

        this.c = c;

        setUp(c);


        this.setBorder(new EmptyBorder(0,10,10,0));

    }

    public void setUp(Character c){

        int i = GHC.banners.indexOf(this);

        this.removeAll();

        BufferedImage image;

        try {

            if(c!=null) image = ImageIO.read(Objects.requireNonNull(cl.getResource(c.charclass.toLowerCase()+".jpg")));
            else image = ImageIO.read(Objects.requireNonNull(cl.getResource("empty.jpg")));
        } catch (Error | Exception e){
            image = null;
        }

        setImage(image);

        setPreferredSize(new Dimension(1800,200));

        setLayout(new BorderLayout());

        JPanel top = new JPanel(new BorderLayout());

        if(c!=null) {

            name = new JLabel(c.name + " the " + c.charclass);
            name.setFont(new Font("SansSerif", Font.BOLD, 30));
            name.setHorizontalAlignment(JLabel.CENTER);
            name.setBorder(new EmptyBorder(10, 0, 0, 0));
            top.add(name, BorderLayout.CENTER);
        }



        JButton x = c!=null ? new JButton("x") : new JButton("+");
        x.setForeground(Color.YELLOW);
        x.setBackground(Color.RED);
        x.setFocusPainted(false);
        x.setPreferredSize(new Dimension(50,50));
        x.setCursor(new Cursor(Cursor.HAND_CURSOR));
        x.setFont(new Font("SansSerif", Font.BOLD, 19));

        x.setToolTipText((c==null) ? "Add a character" : "Remove from party");

        x.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if(c==null){
                    Character temp = GHC.loadCharacter();
                    GHC.party.add(temp);
                    setUp(temp);
                } else{
                    GHC.party.remove(c);
                    setUp(null);
                }
            }
        });

        top.add(x, BorderLayout.EAST);

        add(top, BorderLayout.NORTH);

        if(c!=null) {

            d = new Counter("dmg");
            k = new Counter("kills");
            h = new Counter("heals");
            dt = new Counter("shield");

            info = new JButton();
            ImageIcon ibg = new ImageIcon(((new ImageIcon(
                    Objects.requireNonNull(cl.getResource("info.jpg"))).getImage())).getScaledInstance(60, 60, java.awt.Image.SCALE_SMOOTH));
            info.setIcon(ibg);
            info.setToolTipText("Character info");

            dead = new JButton();
            ImageIcon dbg = new ImageIcon(((new ImageIcon(
                    Objects.requireNonNull(cl.getResource("dead.jpg"))).getImage())).getScaledInstance(60, 60, java.awt.Image.SCALE_SMOOTH));
            dead.setIcon(dbg);
            dead.setToolTipText("Character dead");
            dead.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    if(confirm("Characted dead?")){
                        c.deaths++;
                        GHC.party.remove(c);
                        setUp(null);
                    }
                }
            });

            ex = new JButton();
            ImageIcon ebg = new ImageIcon(((new ImageIcon(
                    Objects.requireNonNull(cl.getResource("exhaust.jpg"))).getImage())).getScaledInstance(60, 60, java.awt.Image.SCALE_SMOOTH));
            ex.setIcon(ebg);
            ex.setToolTipText("Character exhausted");
            ex.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    if(confirm("Character exhausted?")){
                        c.exhausts++;
                        GHC.party.remove(c);
                        setUp(null);
                    }
                }
            });

            JPanel buttonscontainer = new JPanel(new FlowLayout(FlowLayout.CENTER));
            buttonscontainer.setPreferredSize(new Dimension(60 * 4, 60 * 2));

            JPanel buttons = new JPanel(new GridLayout(2, 4));
            buttons.setPreferredSize(new Dimension(60 * 4, 60 * 2));
            buttonscontainer.setOpaque(false);
            buttons.setOpaque(false);

            buttons.add(d);
            buttons.add(k);
            buttons.add(new JLabel(""));
            buttons.add(dead);
            buttons.add(h);
            buttons.add(dt);
            buttons.add(info);
            buttons.add(ex);

            buttonscontainer.add(buttons);

            add(buttonscontainer);

            if(i >=0 ) GHC.banners.get(i).c = c;
        }

        revalidate();
        repaint();


    }

    public void updateValues(){
        c.dmg += d.getValue();
        c.sDmg = d.getValue();
        c.kills += k.getValue();
        c.sKills += k.getValue();
        c.heals += h.getValue();
        c.sHeals += h.getValue();
        c.damagetaken += dt.getValue();
        c.sDt += dt.getValue();
        c.turnsplayed++;
        c.sTurns++;
        if(d.getValue() > c.highestDamage){
            c.highestDamage = d.getValue();
        }

        if(h.getValue() > c.highestHeal){
            c.highestHeal = h.getValue();
        }

        c.hasPlayed = true;

        GHC.save();

        setUp(c);

    }

    public boolean confirm(String message){
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

}
