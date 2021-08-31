package personal.nphuc96.money_tracker.util;

import org.mapstruct.Mapper;
import personal.nphuc96.money_tracker.dto.TransactionDTO;
import personal.nphuc96.money_tracker.dto.TransactionGroupDTO;
import personal.nphuc96.money_tracker.entity.Transaction;
import personal.nphuc96.money_tracker.entity.TransactionGroup;

@Mapper(componentModel = "spring")
public interface ModelMapper {

    TransactionDTO transactionToDTO(Transaction transaction);

    Transaction dtoToTransaction(TransactionDTO transactionDTO);

    TransactionGroupDTO transactionGroupToDto(TransactionGroup transactionGroup);

    TransactionGroup dtoToTransactionGroup(TransactionGroupDTO transactionGroupDTO);


}
