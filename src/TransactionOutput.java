import utils.StringUtil;

import java.security.PublicKey;

public class TransactionOutput {
    public String id;
    public PublicKey recipient;
    public float value;
    public String parentTransactionId;

    //Constructor
    public TransactionOutput(PublicKey reciepient, float value, String parentTransactionId) {
        this.recipient = reciepient;
        this.value = value;
        this.parentTransactionId = parentTransactionId;
        this.id = StringUtil.applySHA256(StringUtil.getStringFromKey(reciepient) + Float.toString(value) + parentTransactionId);
    }

    // Kiem tra coin thuoc ve minh
    public boolean isMine(PublicKey publicKey) {
        return publicKey == recipient;
    }
}
