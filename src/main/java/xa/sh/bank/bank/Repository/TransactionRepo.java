package xa.sh.bank.bank.Repository;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import xa.sh.bank.bank.Entity.Transaction;
import xa.sh.bank.bank.Entity.TransactionMethod;

@Repository
public interface TransactionRepo extends MongoRepository<Transaction, String> {

    @Query("{'$or': [ { 'senderAcc': ?0 }, { 'receiverAcc': ?0 } ] }")
    Page<Transaction> findAllByAccount(Long acc, Pageable pageable);

    @Query("{'receiverAcc':?0 }")
    Page<Transaction> findAllReceivedByAcc(Long receiverAcc,  Pageable pageable);

    @Query("{'senderAcc':?0 }")
    Page<Transaction> findAllSentByAcc(Long senderAcc ,  Pageable pageable);

    @Query("{'senderAcc':?0,'isSuccessful':true }")
    Page<Transaction> findAllSuccessTransaction(Long senderAcc,  Pageable pageable);

    @Query("{'senderAcc':?0,'isSuccessful':false }")
    Page<Transaction> findAllFailedTransactions(Long senderAcc,  Pageable pageable);

    Optional<Transaction> findById(String id);

    @Query("{'receiverAcc':?0, 'transactionDate':{ $gte:?1, $lte: ?2 } }")
    Page<Transaction> findReceivedBetweenDate(Long acc, LocalDateTime first, LocalDateTime last,  Pageable pageable);

    @Query("{'senderAcc':?0, 'transactionDate':{ $gte:?1, $lte: ?2 } }")
    Page<Transaction> findSentBetweenDate(Long acc, LocalDateTime first, LocalDateTime last,  Pageable pageable);

    @Query("{'$or': [ { 'senderAcc': ?0 }, { 'receiverAcc': ?0 } ], 'transactionDate': { $gte: ?1, $lte: ?2 } }")
    Page<Transaction> findAllByAccountAndDateRange(Long acc, LocalDateTime start, LocalDateTime end,  Pageable pageable);

    @Query("{'method': ?0, '$or': [ { 'senderAcc': ?1 }, { 'receiverAcc': ?1 } ] }")
    Page<Transaction> findByMethod(TransactionMethod method, Long acc,  Pageable pageable);

    Page<Transaction> findAllBySenderAccOrderByTransactionDateDesc(Long senderAcc,  Pageable pageable);

    Page<Transaction> findAllBySenderAccOrReceiverAcc(
    Long senderAcc, Long receiverAcc, Pageable pageable);

    /// Methods for the admin
    @Query("{ 'transactionDate': { $gte: ?0, $lte: ?1 } }")
    Page<Transaction> findAllBetweenDates(LocalDateTime start, LocalDateTime end, Pageable pageable);

    @Query("{'$or': [ { 'senderAcc': ?0 }, { 'receiverAcc': ?0 } ], 'amount': { $gte: ?1 } }")
    Page<Transaction> findHighValueTransactions(Long acc, BigDecimal threshold, Pageable pageable);

    @Query("{ 'isSuccessful': false }")
    Page<Transaction> findAllFailed( Pageable pageable);

    @Query("{'$or': [ { 'senderAcc': ?0 }, { 'receiverAcc': ?0 } ], 'isSuccessful': false }")
    Page<Transaction> findFailedByAccount(Long acc, Pageable pageable);

    @Query("{ 'senderAcc': ?0, 'receiverAcc': ?1, 'amount': ?2 }")
    List<Transaction> findSameAmountRepeatedTransactions(Long sender, Long receiver, BigDecimal amount);

    @Query("{ 'method': ?0, 'transactionDate': { $gte: ?1, $lte: ?2 } }")
    Page<Transaction> findByMethodAndDateRange(TransactionMethod method, LocalDateTime start, LocalDateTime end, Pageable pageable);

}














