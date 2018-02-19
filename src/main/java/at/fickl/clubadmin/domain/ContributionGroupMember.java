package at.fickl.clubadmin.domain;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;

import org.springframework.data.elasticsearch.annotations.Document;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.Objects;

/**
 * A ContributionGroupMember.
 */
@Entity
@Table(name = "contribution_group_member")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Document(indexName = "contributiongroupmember")
public class ContributionGroupMember implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    private Long id;

    @Column(name = "start_date")
    private LocalDate startDate;

    @Column(name = "end_date")
    private LocalDate endDate;

    @ManyToOne
    private ContributionGroup group;

    @ManyToOne
    private Member member;

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LocalDate getStartDate() {
        return startDate;
    }

    public ContributionGroupMember startDate(LocalDate startDate) {
        this.startDate = startDate;
        return this;
    }

    public void setStartDate(LocalDate startDate) {
        this.startDate = startDate;
    }

    public LocalDate getEndDate() {
        return endDate;
    }

    public ContributionGroupMember endDate(LocalDate endDate) {
        this.endDate = endDate;
        return this;
    }

    public void setEndDate(LocalDate endDate) {
        this.endDate = endDate;
    }

    public ContributionGroup getGroup() {
        return group;
    }

    public ContributionGroupMember group(ContributionGroup contributionGroup) {
        this.group = contributionGroup;
        return this;
    }

    public void setGroup(ContributionGroup contributionGroup) {
        this.group = contributionGroup;
    }

    public Member getMember() {
        return member;
    }

    public ContributionGroupMember member(Member member) {
        this.member = member;
        return this;
    }

    public void setMember(Member member) {
        this.member = member;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here, do not remove

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        ContributionGroupMember contributionGroupMember = (ContributionGroupMember) o;
        if (contributionGroupMember.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), contributionGroupMember.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "ContributionGroupMember{" +
            "id=" + getId() +
            ", startDate='" + getStartDate() + "'" +
            ", endDate='" + getEndDate() + "'" +
            "}";
    }
}
