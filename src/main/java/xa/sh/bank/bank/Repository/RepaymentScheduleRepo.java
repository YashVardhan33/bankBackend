package xa.sh.bank.bank.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import xa.sh.bank.bank.Entity.RepaymentSchedule;

@Repository
public interface RepaymentScheduleRepo extends MongoRepository<RepaymentSchedule, String> {
    Optional<RepaymentSchedule> findFirstByAccountNoAndPaidFalseOrderByDueDateAsc(Long accountNo);
    boolean existsByAccountNoAndDueDate(Long accountNo, LocalDate date);
    List<RepaymentSchedule> findByDueDateBetweenAndPaidFalse(LocalDate today, LocalDate dueSoon);
    
    List<RepaymentSchedule> findByAccountNoAndPaidFalse(Long accountNo);
    List<RepaymentSchedule> findByDueDateAndPaidFalse(LocalDate dueDate);
}
