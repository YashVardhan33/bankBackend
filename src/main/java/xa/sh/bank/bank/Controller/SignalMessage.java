package xa.sh.bank.bank.Controller;

public class SignalMessage {
    private String type;
    private String sdp;
    private Object candidate;

    public SignalMessage(){}

    /**
     * @return String return the type
     */
    public String getType() {
        return type;
    }

    /**
     * @param type the type to set
     */
    public void setType(String type) {
        this.type = type;
    }

    /**
     * @return String return the sdp
     */
    public String getSdp() {
        return sdp;
    }

    /**
     * @param sdp the sdp to set
     */
    public void setSdp(String sdp) {
        this.sdp = sdp;
    }

    /**
     * @return Object return the candidate
     */
    public Object getCandidate() {
        return candidate;
    }

    /**
     * @param candidate the candidate to set
     */
    public void setCandidate(Object candidate) {
        this.candidate = candidate;
    }

}
