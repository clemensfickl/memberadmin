import { BaseEntity } from './../../shared';

export class TrainingGroupMember implements BaseEntity {
    constructor(
        public id?: number,
        public startDate?: any,
        public endDate?: any,
        public groupId?: number,
        public memberId?: number,
    ) {
    }
}
