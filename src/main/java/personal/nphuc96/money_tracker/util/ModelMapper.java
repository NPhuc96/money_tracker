package personal.nphuc96.money_tracker.util;

import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;
import personal.nphuc96.money_tracker.dto.GroupsDTO;
import personal.nphuc96.money_tracker.dto.TransactionDTO;
import personal.nphuc96.money_tracker.entity.Groups;
import personal.nphuc96.money_tracker.entity.Transaction;

@Mapper(componentModel = "spring",
        unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface ModelMapper {

    TransactionDTO transactionToDTO(Transaction transaction);

    Transaction dtoToTransaction(TransactionDTO transactionDTO);

    GroupsDTO groupsToDto(Groups groups);

    Groups dtoToGroups(GroupsDTO groupsDTO);


}
