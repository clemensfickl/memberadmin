package at.fickl.clubadmin.service.mapper;

import at.fickl.clubadmin.domain.*;
import at.fickl.clubadmin.service.dto.TrainingGroupMemberDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity TrainingGroupMember and its DTO TrainingGroupMemberDTO.
 */
@Mapper(componentModel = "spring", uses = {TrainingGroupMapper.class, MemberMapper.class})
public interface TrainingGroupMemberMapper extends EntityMapper<TrainingGroupMemberDTO, TrainingGroupMember> {

    @Mapping(source = "group.id", target = "groupId")
    @Mapping(source = "group.name", target = "groupName")
    @Mapping(source = "member.id", target = "memberId")
    TrainingGroupMemberDTO toDto(TrainingGroupMember trainingGroupMember);

    @Mapping(source = "groupId", target = "group")
    @Mapping(source = "memberId", target = "member")
    TrainingGroupMember toEntity(TrainingGroupMemberDTO trainingGroupMemberDTO);

    default TrainingGroupMember fromId(Long id) {
        if (id == null) {
            return null;
        }
        TrainingGroupMember trainingGroupMember = new TrainingGroupMember();
        trainingGroupMember.setId(id);
        return trainingGroupMember;
    }
}
