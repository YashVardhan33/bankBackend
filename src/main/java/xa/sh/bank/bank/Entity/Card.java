package xa.sh.bank.bank.Entity;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "card")
public class Card {

    @Id
    private String id;

    @Indexed
    private Long cardNumber;

    @Indexed
    private String cardType;

    @DBRef
    @Indexed
    private Account linkedAccount;

    private String holderName;
    private LocalDate expiryDate;

    private String cvv;

    private BigDecimal creditLimit;

    private BigDecimal outStandingAmount;

    private boolean isBlocked;

    private LocalDateTime issuedAt;

}
