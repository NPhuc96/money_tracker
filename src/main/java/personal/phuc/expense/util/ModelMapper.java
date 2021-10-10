package personal.phuc.expense.util;

import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;
import personal.phuc.expense.dto.GroupsDTO;
import personal.phuc.expense.dto.TransactionDTO;
import personal.phuc.expense.entity.Groups;
import personal.phuc.expense.entity.Transaction;

@Mapper(componentModel = "spring",
        unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface ModelMapper {

    TransactionDTO transactionToDTO(Transaction transaction);

    Transaction dtoToTransaction(TransactionDTO transactionDTO);

    GroupsDTO groupsToDto(Groups groups);

    Groups dtoToGroups(GroupsDTO groupsDTO);


}
