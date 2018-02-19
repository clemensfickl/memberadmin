package at.fickl.clubadmin.service.dto;

import java.io.Serializable;
import io.github.jhipster.service.filter.BooleanFilter;
import io.github.jhipster.service.filter.DoubleFilter;
import io.github.jhipster.service.filter.Filter;
import io.github.jhipster.service.filter.FloatFilter;
import io.github.jhipster.service.filter.IntegerFilter;
import io.github.jhipster.service.filter.LongFilter;
import io.github.jhipster.service.filter.StringFilter;


import io.github.jhipster.service.filter.LocalDateFilter;



/**
 * Criteria class for the ContributionGroupMember entity. This class is used in ContributionGroupMemberResource to
 * receive all the possible filtering options from the Http GET request parameters.
 * For example the following could be a valid requests:
 * <code> /contribution-group-members?id.greaterThan=5&amp;attr1.contains=something&amp;attr2.specified=false</code>
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class ContributionGroupMemberCriteria implements Serializable {
    private static final long serialVersionUID = 1L;


    private LongFilter id;

    private LocalDateFilter startDate;

    private LocalDateFilter endDate;

    private LongFilter groupId;

    private LongFilter memberId;

    public ContributionGroupMemberCriteria() {
    }

    public LongFilter getId() {
        return id;
    }

    public void setId(LongFilter id) {
        this.id = id;
    }

    public LocalDateFilter getStartDate() {
        return startDate;
    }

    public void setStartDate(LocalDateFilter startDate) {
        this.startDate = startDate;
    }

    public LocalDateFilter getEndDate() {
        return endDate;
    }

    public void setEndDate(LocalDateFilter endDate) {
        this.endDate = endDate;
    }

    public LongFilter getGroupId() {
        return groupId;
    }

    public void setGroupId(LongFilter groupId) {
        this.groupId = groupId;
    }

    public LongFilter getMemberId() {
        return memberId;
    }

    public void setMemberId(LongFilter memberId) {
        this.memberId = memberId;
    }

    @Override
    public String toString() {
        return "ContributionGroupMemberCriteria{" +
                (id != null ? "id=" + id + ", " : "") +
                (startDate != null ? "startDate=" + startDate + ", " : "") +
                (endDate != null ? "endDate=" + endDate + ", " : "") +
                (groupId != null ? "groupId=" + groupId + ", " : "") +
                (memberId != null ? "memberId=" + memberId + ", " : "") +
            "}";
    }

}
