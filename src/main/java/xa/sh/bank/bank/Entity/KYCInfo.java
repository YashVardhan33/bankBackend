package xa.sh.bank.bank.Entity;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "kycInfo")
public class KYCInfo {
    @Id
    private String id;
    @Indexed
    private String idCardName;
    private String idCardNumber;
    private String photoUrl;

    public KYCInfo(String idCardName, String idCardNumber, String photoUrl) {
        this.idCardName = idCardName;
        this.idCardNumber = idCardNumber;
        this.photoUrl = photoUrl;
    }

    public KYCInfo() {
    }

    public String getIdCardName() {
        return idCardName;
    }

    public void setIdCardName(String idCardName) {
        this.idCardName = idCardName;
    }

    public String getIdCardNumber() {
        return idCardNumber;
    }

    public void setIdCardNumber(String idCardNumber) {
        this.idCardNumber = idCardNumber;
    }

    public String getPhotoUrl() {
        return photoUrl;
    }

    public void setPhotoUrl(String photoUrl) {
        this.photoUrl = photoUrl;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

}
