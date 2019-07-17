import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.Objects;

public class Counter extends JButton {

    int value = 0;
    String type;
    ClassLoader cl = GHC.class.getClassLoader();

    public Counter(String type){
        this.type = type;
        setText(""+value);

        switch(type){
            case "dmg" : {
                setToolTipText("Damage inflicted");
                break;
            }

            case "heals" : {
                setToolTipText("HP restored");
                break;
            }

            case "shield" : {
                setToolTipText("Damage taken");
                break;
            }

            case "kills" : {
                setToolTipText("Enemies killed");
                break;
            }
        }

        addMouseListener(new MouseListener() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if(SwingUtilities.isLeftMouseButton(e)){
                    value++;
                    if(value >= 10) {
                        setFont(new Font("SansSerif", Font.BOLD, 15));
                    }
                    setText(""+value);
                } else{
                    value--;
                    value = value < 0 ? 0 : value;
                    if(value < 10) {
                        setFont(new Font("SansSerif", Font.BOLD, 30));
                    }
                    setText(""+value);
                }
            }

            @Override
            public void mousePressed(MouseEvent e) {

            }

            @Override
            public void mouseReleased(MouseEvent e) {

            }

            @Override
            public void mouseEntered(MouseEvent e) {

            }

            @Override
            public void mouseExited(MouseEvent e) {

            }
        });

        setPreferredSize(new Dimension(60,60));

        ImageIcon bg = new ImageIcon(((new ImageIcon(
                Objects.requireNonNull(cl.getResource(type+".jpg"))).getImage())).getScaledInstance(60,60,java.awt.Image.SCALE_SMOOTH));

        setIcon(bg);

        setHorizontalTextPosition(JButton.CENTER);
        setVerticalTextPosition(JButton.CENTER);

        setForeground(Color.WHITE);
        setFocusPainted(false);
        setFont(new Font("SansSerif", Font.BOLD, 30));

        setCursor(new Cursor(Cursor.HAND_CURSOR));
    }

    public int getValue() {
        return value;
    }


}
