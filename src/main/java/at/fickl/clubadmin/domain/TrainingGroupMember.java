package at.fickl.clubadmin.domain;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;

import org.springframework.data.elasticsearch.annotations.Document;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.Objects;

/**
 * A TrainingGroupMember.
 */
@Entity
@Table(name = "training_group_member")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Document(indexName = "traininggroupmember")
public class TrainingGroupMember implements Serializable {

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
    private TrainingGroup group;

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

    public TrainingGroupMember startDate(LocalDate startDate) {
        this.startDate = startDate;
        return this;
    }

    public void setStartDate(LocalDate startDate) {
        this.startDate = startDate;
    }

    public LocalDate getEndDate() {
        return endDate;
    }

    public TrainingGroupMember endDate(LocalDate endDate) {
        this.endDate = endDate;
        return this;
    }

    public void setEndDate(LocalDate endDate) {
        this.endDate = endDate;
    }

    public TrainingGroup getGroup() {
        return group;
    }

    public TrainingGroupMember group(TrainingGroup trainingGroup) {
        this.group = trainingGroup;
        return this;
    }

    public void setGroup(TrainingGroup trainingGroup) {
        this.group = trainingGroup;
    }

    public Member getMember() {
        return member;
    }

    public TrainingGroupMember member(Member member) {
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
        TrainingGroupMember trainingGroupMember = (TrainingGroupMember) o;
        if (trainingGroupMember.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), trainingGroupMember.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "TrainingGroupMember{" +
            "id=" + getId() +
            ", startDate='" + getStartDate() + "'" +
            ", endDate='" + getEndDate() + "'" +
            "}";
    }
}
