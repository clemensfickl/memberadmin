package at.fickl.clubadmin.service.dto;

import java.io.Serializable;
import io.github.jhipster.service.filter.BooleanFilter;
import io.github.jhipster.service.filter.DoubleFilter;
import io.github.jhipster.service.filter.Filter;
import io.github.jhipster.service.filter.FloatFilter;
import io.github.jhipster.service.filter.IntegerFilter;
import io.github.jhipster.service.filter.LongFilter;
import io.github.jhipster.service.filter.StringFilter;
import io.github.jhipster.service.filter.BigDecimalFilter;





/**
 * Criteria class for the ContributionGroupEntry entity. This class is used in ContributionGroupEntryResource to
 * receive all the possible filtering options from the Http GET request parameters.
 * For example the following could be a valid requests:
 * <code> /contribution-group-entries?id.greaterThan=5&amp;attr1.contains=something&amp;attr2.specified=false</code>
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class ContributionGroupEntryCriteria implements Serializable {
    private static final long serialVersionUID = 1L;


    private LongFilter id;

    private IntegerFilter year;

    private BigDecimalFilter amount;

    private LongFilter groupId;

    public ContributionGroupEntryCriteria() {
    }

    public LongFilter getId() {
        return id;
    }

    public void setId(LongFilter id) {
        this.id = id;
    }

    public IntegerFilter getYear() {
        return year;
    }

    public void setYear(IntegerFilter year) {
        this.year = year;
    }

    public BigDecimalFilter getAmount() {
        return amount;
    }

    public void setAmount(BigDecimalFilter amount) {
        this.amount = amount;
    }

    public LongFilter getGroupId() {
        return groupId;
    }

    public void setGroupId(LongFilter groupId) {
        this.groupId = groupId;
    }

    @Override
    public String toString() {
        return "ContributionGroupEntryCriteria{" +
                (id != null ? "id=" + id + ", " : "") +
                (year != null ? "year=" + year + ", " : "") +
                (amount != null ? "amount=" + amount + ", " : "") +
                (groupId != null ? "groupId=" + groupId + ", " : "") +
            "}";
    }

}
