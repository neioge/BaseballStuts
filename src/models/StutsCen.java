package models;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

@Entity
@NamedQueries({
    @NamedQuery(
        name = "getAllStuts",
        query = "SELECT s FROM StutsCen AS s ORDER BY s.ID DESC"
    )
})
@Table(name="stuts_cen")
public class StutsCen {

    @Id
    @Column(name = "ID")
    @GeneratedValue(strategy=GenerationType.SEQUENCE, generator = "id_Sequence")
    @SequenceGenerator(name = "id_Sequence", sequenceName = "seqBaseballStuts")
    private Integer ID;

    @Column(name = "YEAR", nullable = false)
    private Integer YEAR;

    @Column(name = "PLAYERNAME", nullable = false)
    private String PLAYERNAME;

    @Column(name = "AVG", nullable = false)
    private Double AVG;

    @Column(name = "HOMERUN", nullable = false)
    private Integer HOMERUN;

    @Column(name = "RBI", nullable = false)
    private Integer RBI;

    @Column(name = "OPS", nullable = false)
    private Double OPS;

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

    public Double getAVG() {
        return AVG;
    }

    public void setAVG(Double aVG) {
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

    public Double getOPS() {
        return OPS;
    }

    public void setOPS(Double oPS) {
        this.OPS = oPS;
    }

    public Integer getSTOLENBASE() {
        return STOLENBASE;
    }

    public void setSTOLENBASE(Integer sTOLENBASE) {
        this.STOLENBASE = sTOLENBASE;
    }
}
