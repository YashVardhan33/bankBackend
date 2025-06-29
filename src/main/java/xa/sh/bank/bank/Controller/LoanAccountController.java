package xa.sh.bank.bank.Controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import xa.sh.bank.bank.Entity.LoanAccount;
import xa.sh.bank.bank.Entity.Repayment;
import xa.sh.bank.bank.Service.LoanAccountService;
import xa.sh.bank.bank.Service.RepaymentService;
import xa.sh.bank.bank.dto.LoanAccountDto;
import xa.sh.bank.bank.dto.RepaymentDto;



@RestController
@RequestMapping("/api/loans")
public class LoanAccountController {
    @Autowired private LoanAccountService loanService;
    @Autowired private RepaymentService repaymentService;

    @PostMapping("/apply")
    public ResponseEntity<LoanAccount> applyLoan(@RequestBody LoanAccountDto dto) throws Exception{
        LoanAccount loan = loanService.createLoanAccount(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(loan);
    }

    @GetMapping("/{accNo}")
    public ResponseEntity<LoanAccount> getLoan(@PathVariable Long accountNo) throws Exception{
        return ResponseEntity.ok(loanService.getLoanByAccountNo(accountNo));
    }
    
    @PostMapping("/repay")
    public ResponseEntity<Repayment> repay(@RequestBody RepaymentDto dto) throws Exception{
        return ResponseEntity.ok(repaymentService.repayLoan(dto));
    }
    
}
