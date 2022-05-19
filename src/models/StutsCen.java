package models;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name="stuts_cen")
public class StutsCen {

    @Id
    @Column(name = "ID", nullable = false)
    private Integer ID;

    @Column(name = "YEAR", nullable = false)
    private Integer YEAR;

    @Column(name = "PLAYERNAME", nullable = false)
    private String PLAYERNAME;

    @Column(name = "AVG", nullable = false)
    private Integer AVG;

    @Column(name = "HOMERUN", nullable = false)
    private Integer HOMERUN;

    @Column(name = "RBI", nullable = false)
    private Integer RBI;

    @Column(name = "OPS", nullable = false)
    private Integer OPS;

    @Column(name = "STOLENBASE", nullable = false)
    private Integer STOLENBASE;

    public Integer getID() {
        return ID;
    }

    public void setID(Integer iD) {
        this.ID = iD;
    }

    public Integer getYEAR() {
        return YEAR;
    }

    public void setYEAR(Integer yEAR) {
        this.YEAR = yEAR;
    }

    public String getPLAYERNAME() {
        return PLAYERNAME;
    }

    public void setPLAYERNAME(String pLAYERNAME) {
        this.PLAYERNAME = pLAYERNAME;
    }

    public Integer getAVG() {
        return AVG;
    }

    public void setAVG(Integer aVG) {
        this.AVG = aVG;
    }

    public Integer getHOMERUN() {
        return HOMERUN;
    }

    public void setHOMERUN(Integer hOMERUN) {
        this.HOMERUN = hOMERUN;
    }

    public Integer getRBI() {
        return RBI;
    }

    public void setRBI(Integer rBI) {
        this.RBI = rBI;
    }

    public Integer getOPS() {
        return OPS;
    }

    public void setOPS(Integer oPS) {
        this.OPS = oPS;
    }

    public Integer getSTOLENBASE() {
        return STOLENBASE;
    }

    public void setSTOLENBASE(Integer sTOLENBASE) {
        this.STOLENBASE = sTOLENBASE;
    }
}
