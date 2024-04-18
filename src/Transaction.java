//-------------------------------------------
// Moi giao dich se chua nhung thong tin sau:
// + Public key cua nguoi gui
// + Public key cua nguoi nhan
// + Khoi luong giao dich
// + Dau vao tham chieu den cac giao dich truoc cua nguoi dung
//   de chung minh co du so du
// + Dau ra tham chieu den cac dia chi nhan duoc trong giao dich
//   (su dung lam dau vao tham chieu cho giao dich moi)
// + Chu ki de chung minh chu cua dia chi la nguoi thuc hien giao dich

import utils.StringUtil;

import java.security.*;
import java.util.ArrayList;

public class Transaction {
    public String transactionId;    // hash cua giao dich
    public PublicKey sender;        // public key cua nguoi gui
    public PublicKey reciepient;    // public key cua nguoi nhan
    public float value;             // khoi luong giao dich
    public byte[] signature;

    public ArrayList<TransactionInput> inputs = new ArrayList<>();
    public ArrayList<TransactionOutput> outputs = new ArrayList<>();

    private static int sequence = 0; // Dem tong so luong giao dich da duoc thuc hien

    public Transaction(PublicKey sender, PublicKey recipient, float value, ArrayList<TransactionInput> inputs) {
        this.sender = sender;
        this.reciepient = recipient;
        this.value = value;
        this.inputs = inputs;
    }

    // Tinh hash cua giao dich (duoc su dung nhu Id cua giao dich)
    private String calculateHash() {
        sequence++; // Dam bao khong co 2 giao dich co cung ma hash
        return StringUtil.applySHA256(
                StringUtil.getStringFromKey(sender) +
                        StringUtil.getStringFromKey(reciepient) +
                        Float.toString(value) +
                        sequence
        );
    }

    public void generateSignature(PrivateKey privateKey)
    {
        String data = StringUtil.getStringFromKey(sender) + StringUtil.getStringFromKey(reciepient) + Float.toString(value);
        signature =  StringUtil.applyECDSASig(privateKey, data);
    }

    // Xac thuc xem private key khi gen ra co trung voi public key nguoi gui
    public boolean verifySignature()
    {
        String data = StringUtil.getStringFromKey(sender) + StringUtil.getStringFromKey(reciepient) + Float.toString(value);
        return StringUtil.verifyECDSASig(sender, data, signature);
    }

    public boolean processTransaction()
    {
        if (verifySignature() == false)
        {
            System.out.println("Transaction Signature");
            return false;
        }

        for (TransactionInput i : inputs)
        {
            i.UTXO = Main.UTXOs.get(i.transactionOutputId);
        }

        if (getInputsValue() < Main.minimumTransaction)
        {
            System.out.println("Transaction input too small: " + getInputsValue());
            return false;
        }

        float leftOver = getInputsValue() - value;
        transactionId = calculateHash();

        outputs.add(new TransactionOutput(this.reciepient, value, transactionId));
        outputs.add(new TransactionOutput(this.sender, leftOver, transactionId));

        for (TransactionOutput o : outputs)
        {
            Main.UTXOs.put(o.id, o);
        }

        for (TransactionInput i : inputs)
        {
            if (i.UTXO == null) continue;
            Main.UTXOs.remove(i.UTXO.id);
        }

        return true;
    }

    // tra ve tong gia tri input(UTXOs)
    public float getInputsValue()
    {
        float total = 0;
        for (TransactionInput i : inputs)
        {
            if (i.UTXO == null) continue;
            total += i.UTXO.value;
        }
        return total;
    }

    // tra ve tong gia tri outputs
    public float getOutputsValue()
    {
        float total = 0;
        for (TransactionOutput o : outputs)
        {
            total += o.value;
        }
        return total;
    }

}
