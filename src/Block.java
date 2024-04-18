import java.util.ArrayList;
import java.util.Date;

public class Block {
    public String hash;
    public String previousHash;
    public String merkleRoot;
    private String data;    // for a dumb chain, data stored is a simple message
    private long timeStamp;
    public ArrayList<Transaction> transactions = new ArrayList<Transaction>(); //our data will be a simple message.
    private int nonce;

    public Block(String previousHash) {
        this.previousHash = previousHash;
        this.timeStamp = new Date().getTime();
        this.hash = calculateHash();
    }

    // Calculate new hash based on block content
    public String calculateHash() {
        String calculatedHash = StringUtil.applySHA256(
                previousHash +
                        Long.toString(timeStamp) +
                        Integer.toString(nonce) +
                        merkleRoot
        );
        return calculatedHash;
    }

    public void mineBlock (int difficulty)
    {
        merkleRoot = StringUtil.getMerkleRoot(transactions);
        // Tao mot xau dich = ki tu '0' voi do dai bang difficulty
        String target = new String (new char[difficulty]).replace('\0', '0');
        while (!hash.substring(0, difficulty).equals(target))
        {
            nonce++;
            hash = calculateHash();
        }
        System.out.println("Block mined! " + hash);
    }

    public boolean addTransaction(Transaction transaction)
    {
        if (transaction == null) return false;
        if (previousHash != "0")
        {
            if (transaction.processTransaction() != true)
            {
                System.out.println("Transaction failed to process");
                return false;
            }
        }
        transactions.add(transaction);
        System.out.println("Transaction successfully added to Block.");
        return true;
    }
}
