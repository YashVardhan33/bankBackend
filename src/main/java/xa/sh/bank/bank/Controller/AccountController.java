package xa.sh.bank.bank.Controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import xa.sh.bank.bank.Entity.Account;
import xa.sh.bank.bank.Service.AccountService;
import xa.sh.bank.bank.dto.AccountDto;

@RestController
@RequestMapping("/api/acc")
public class AccountController {

    @Autowired
    private AccountService accSer;

    @PostMapping("/create")
    public ResponseEntity<Account> createAccount(@RequestBody AccountDto dto) throws Exception {

        return ResponseEntity.ok(accSer.createAccount(dto));
    }

    @GetMapping("/unfreeze")
    public String unFreezeAcc(@RequestParam Long accNo) throws Exception {
        accSer.unFreezeAccount(accNo);
        return "account unfreezed";
    }

    @GetMapping("/freeze")
    public String FreezeAcc(@RequestParam Long accNo, @RequestParam String freezeReason) throws Exception {
        accSer.freezeAccount(accNo, freezeReason);
        return "account freezed";
    }

    @GetMapping("/getAll")
    public ResponseEntity<List<Account>> getAllAccountsByCustomer(@RequestParam String customerId) throws Exception {
        return ResponseEntity.ok(accSer.getAllAccountsOfCustomer(customerId));
    }
}
