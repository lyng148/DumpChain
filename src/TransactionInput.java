public class TransactionInput {
    public String transactionOutputId;
    public TransactionOutput UTXO; // Chua giao dich chua su dung
    public TransactionInput(String transactionOutputId)
    {
        this.transactionOutputId = transactionOutputId;
    }
}
