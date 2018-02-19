package at.fickl.clubadmin.service.mapper;

import at.fickl.clubadmin.domain.*;
import at.fickl.clubadmin.service.dto.ContributionGroupMemberDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity ContributionGroupMember and its DTO ContributionGroupMemberDTO.
 */
@Mapper(componentModel = "spring", uses = {ContributionGroupMapper.class, MemberMapper.class})
public interface ContributionGroupMemberMapper extends EntityMapper<ContributionGroupMemberDTO, ContributionGroupMember> {

    @Mapping(source = "group.id", target = "groupId")
    @Mapping(source = "group.name", target = "groupName")
    @Mapping(source = "member.id", target = "memberId")
    ContributionGroupMemberDTO toDto(ContributionGroupMember contributionGroupMember);

    @Mapping(source = "groupId", target = "group")
    @Mapping(source = "memberId", target = "member")
    ContributionGroupMember toEntity(ContributionGroupMemberDTO contributionGroupMemberDTO);

    default ContributionGroupMember fromId(Long id) {
        if (id == null) {
            return null;
        }
        ContributionGroupMember contributionGroupMember = new ContributionGroupMember();
        contributionGroupMember.setId(id);
        return contributionGroupMember;
    }
}
