package org.jboss.as.quickstarts.hibernate_search.model.data;


import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * Created by IntelliJ IDEA.
 * User: tharinduj
 * Date: Jan 13, 2012
 * Time: 10:33:06 PM
 */
@Entity
@Table(name = "SEQUENCE_TABLE")
public class SequenceBean {


    private String sequenceName;
    private int sequenceCount;

    public SequenceBean() {
    }

    public SequenceBean(String sequenceName, int sequenceCount) {
        this.sequenceName = sequenceName;
        this.sequenceCount = sequenceCount;
    }

    @Id
    @Column(name = "SEQ_NAME")
    public String getSequenceName() {
        return sequenceName;
    }

    public void setSequenceName(String sequenceName) {
        this.sequenceName = sequenceName;
    }


    @Column(name = "SEQ_COUNT")
    public int getSequenceCount() {
        return sequenceCount;
    }

    public void setSequenceCount(int sequenceCount) {
        this.sequenceCount = sequenceCount;
    }
}
