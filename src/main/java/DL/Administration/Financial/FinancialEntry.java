package DL.Administration.Financial;

import DL.Users.User;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Objects;

import static javax.persistence.InheritanceType.TABLE_PER_CLASS;

/**
 * Description:     This abstract class represents a financial entry.
 *                  * a user can only enter one entry per timestamp (id)
 *                  * negative amount represents an expense and positive an income.
 **/
@MappedSuperclass
@NamedQueries(value = {
        @NamedQuery(name = "FinancialEntries", query = "Select e From FinancialEntry e")
})
@IdClass(FinancialEntry.EntryPK.class)
public abstract class FinancialEntry implements Serializable
{
    /**
     * For Composite Primary Key
     */
    public class EntryPK implements Serializable
    {
        public FinancialUser source;
        public long timeStamp;
    }
    @Id
    @OneToOne(cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    private User source;
    @Id
    private long timeStamp;
    @Column
    private String name;
    @Column
    private double amount;

    /**
     * Constructor
     * @param source - the user that created the entry
     * @param name - the name (description) of the entry
     * @param amount - amount of entry, positive is income negative is expense
     * @param timeStamp - the date and time that the entry occurred
     */
    public FinancialEntry(User source, String name, double amount, long timeStamp)
    {
        this.source = source;
        this.name = name;
        this.amount = amount;
        this.timeStamp = timeStamp;
    }

    /**
     * Constructor, timeStamp will be the current date and time
     * @param source - the user that created the entry
     * @param name - the name (description) of the entry
     * @param amount - amount of entry, positive is income negative is expense
     */
    public FinancialEntry(User source, String name, double amount)
    {
        this(source,name,amount, System.currentTimeMillis());
    }

    /**
     * Default Constructor
     */
    public FinancialEntry()
    {
        this(null,null,0);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        FinancialEntry that = (FinancialEntry) o;
        return Double.compare(that.amount, amount) == 0 &&
                timeStamp == that.timeStamp &&
                Objects.equals(source, that.source) &&
                Objects.equals(name, that.name);
    }

    @Override
    public String toString() {
        return "FinancialEntry{" +
                "source=" + source +
                ", name='" + name + '\'' +
                ", amount=" + amount +
                ", timeStamp=" + timeStamp +
                '}';
    }
}
