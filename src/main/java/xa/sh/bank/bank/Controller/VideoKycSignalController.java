package xa.sh.bank.bank.Controller;

import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.annotation.SubscribeMapping;
import org.springframework.stereotype.Controller;

@Controller
public class VideoKycSignalController {
    @MessageMapping("/signal/{roomId}")
    @SendTo("/topic/{roomId}")
    public SignalMessage relaySignal(@DestinationVariable String roomId, SignalMessage message){
            return message;
    }

    @SubscribeMapping("/topic/{roomId}")
    public void onSubscribe(@DestinationVariable String roomId){
        
    }
}
