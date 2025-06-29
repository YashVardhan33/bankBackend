package xa.sh.bank.bank.Service;

import java.math.BigDecimal;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import xa.sh.bank.bank.Entity.AccountType;
import xa.sh.bank.bank.Entity.DepositInterestRateConfig;
import xa.sh.bank.bank.Entity.LoanInterestRateConfig;
import xa.sh.bank.bank.Entity.LoanType;
import xa.sh.bank.bank.Repository.DepositRateRepo;
import xa.sh.bank.bank.Repository.LoanRateRepo;

@Service
public class InterestRateService {

    @Autowired private DepositRateRepo dRepo ;
    @Autowired private LoanRateRepo loanRepo;  

    public LoanInterestRateConfig updateLoanInterest(LoanType type, BigDecimal newRate){
        Optional<LoanInterestRateConfig> opt = loanRepo.findByType(type);
        LoanInterestRateConfig config ;
        if(opt.isEmpty()){
            config = new LoanInterestRateConfig();
        }
        else config= opt.get();
        config.setType(type);
        config.setAnnualRate(newRate);
        return loanRepo.save(config);
    }

    public BigDecimal getAnnualRate(LoanType type){
        return loanRepo.findByType(type).map(LoanInterestRateConfig::getAnnualRate).orElse(BigDecimal.valueOf(6.5));
        
    }

    public BigDecimal getAnnualRate(AccountType type){
        return dRepo.findByType(type).map(DepositInterestRateConfig::getAnnualRate).orElse(BigDecimal.valueOf(2.5));

    }


    public DepositInterestRateConfig updateDepositInterest(AccountType type, BigDecimal newRate){
        Optional<DepositInterestRateConfig> opt = dRepo.findByType(type);
        DepositInterestRateConfig config ;
        if(opt.isEmpty()){
            config = new DepositInterestRateConfig();
        }
        else config= opt.get();
        config.setType(type);
        config.setAnnualRate(newRate);
        return dRepo.save(config);
    }
}
