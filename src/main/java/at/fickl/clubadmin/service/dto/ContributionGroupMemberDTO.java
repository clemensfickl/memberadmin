package at.fickl.clubadmin.service.dto;


import java.time.LocalDate;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import java.util.Objects;

/**
 * A DTO for the ContributionGroupMember entity.
 */
public class ContributionGroupMemberDTO implements Serializable {

    private Long id;

    private LocalDate startDate;

    private LocalDate endDate;

    private Long groupId;

    private String groupName;

    private Long memberId;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LocalDate getStartDate() {
        return startDate;
    }

    public void setStartDate(LocalDate startDate) {
        this.startDate = startDate;
    }

    public LocalDate getEndDate() {
        return endDate;
    }

    public void setEndDate(LocalDate endDate) {
        this.endDate = endDate;
    }

    public Long getGroupId() {
        return groupId;
    }

    public void setGroupId(Long contributionGroupId) {
        this.groupId = contributionGroupId;
    }

    public String getGroupName() {
        return groupName;
    }

    public void setGroupName(String contributionGroupName) {
        this.groupName = contributionGroupName;
    }

    public Long getMemberId() {
        return memberId;
    }

    public void setMemberId(Long memberId) {
        this.memberId = memberId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        ContributionGroupMemberDTO contributionGroupMemberDTO = (ContributionGroupMemberDTO) o;
        if(contributionGroupMemberDTO.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), contributionGroupMemberDTO.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "ContributionGroupMemberDTO{" +
            "id=" + getId() +
            ", startDate='" + getStartDate() + "'" +
            ", endDate='" + getEndDate() + "'" +
            "}";
    }
}
