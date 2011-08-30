package com.bloatit.data;

import javax.persistence.Basic;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import org.hibernate.annotations.NamedQueries;
import org.hibernate.annotations.NamedQuery;
import org.hibernate.search.annotations.DocumentId;

//@formatter:off
@NamedQueries(value = { @NamedQuery(
                         name = "configuration.load",
                         query = "FROM DaoConfiguration"),
                      }
              )
//@formatter:on
@Entity
public class DaoConfiguration {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @DocumentId
    private Integer id;
    
    @Basic(optional = false)
    public int mercanetTransactionId;
    
    private DaoConfiguration(){
        // For hibernate mapping
    }

    private DaoConfiguration(final int mercanetTransactionId) {
        this.mercanetTransactionId = mercanetTransactionId;
    }

    public int getBankTransactionId() {
        return mercanetTransactionId;
    }

    public static DaoConfiguration getInstance() {
        return (DaoConfiguration) SessionManager.getNamedQuery("configuration.load").uniqueResult();
    }

    public int getMercanetTransactionId() {
        return mercanetTransactionId;
    }

    public void setMercanetTransactionId(int mercanetTransactionId) {
        this.mercanetTransactionId = mercanetTransactionId;
    }
    
    public Integer getId() {
        return this.id;
    }
}
