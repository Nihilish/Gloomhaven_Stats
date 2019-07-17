import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Character implements Serializable {

    String name, charclass;
    int dmg, damagetaken, kills, heals, turnsplayed, deaths, exhausts;
    int sDmg, sDt, sKills, sHeals, sTurns;
    Date timeCreated;
    boolean hasPlayed = false;
    int highestDamage, highestHeal;

    public Character(String name, String charclass){
        this.name = name;
        this.charclass = charclass;
        timeCreated = new Date();
        dmg = damagetaken = kills = heals = turnsplayed = deaths = exhausts = 0;
        sDmg = sDt = sKills = sHeals = sTurns = 0;
        highestDamage = highestHeal = 0;
    }

    public String getDate(){
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
        return dateFormat.format(timeCreated);
    }

    public void newScenario(){
        sDmg = sDt = sKills = sHeals = sTurns = 0;
        hasPlayed = false;
    }

    public String toString(){
        return name+" the "+charclass+", created on "+getDate();
    }

    public String values(){
        return dmg+" "+damagetaken+" "+kills+" "+heals;
    }

}
