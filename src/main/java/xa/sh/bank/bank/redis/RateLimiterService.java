package xa.sh.bank.bank.redis;

import java.time.Duration;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Service;

@Service
public class RateLimiterService {
    private static final int MAX_ATTEMPTS =4;
    private static final long LOCK_DURATION = 24*60;

    @Autowired
    // private StringRedisTemplate redisTemplate;
    private StringRedisTemplate redisTemplate;
    public void  recordFailedAttempt(String username){
        String key = "failed:"+username;

        ValueOperations<String, String> ops = redisTemplate.opsForValue();
        Long current = ops.increment(key);
        System.out.println("Acct " + username + " failure count = " + current);
        System.out.println("Locked? " + isAccountLocked(username));
        // redisTemplate.expire(key, Duration.ofSeconds(LOCK_DURATION));
        if(current==1){
            redisTemplate.expire(key, Duration.ofSeconds(LOCK_DURATION));

        }
        if(current>=MAX_ATTEMPTS){
            lockAccount(username);
        }
    }

    public boolean isAccountLocked(String username){
        String key = "locked:"+username;
        return Boolean.TRUE.equals(redisTemplate.hasKey(key));
    }

    public void lockAccount(String username){
        String lockKey ="locked:"+username;
        redisTemplate.opsForValue().set(lockKey, "true",Duration.ofSeconds(LOCK_DURATION));    
    }
    public void unlockAccount(String username) {
        redisTemplate.delete("locked:" + username);
        redisTemplate.delete("failed:" + username);
    }
}
