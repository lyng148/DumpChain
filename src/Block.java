import utils.StringUtil;

import java.util.Date;

public class Block {
    public String hash;
    public String previousHash;
    private String data;    // for a dumb chain, data stored is a simple message
    private long timeStamp;
    private int nonce;

    public Block(String data, String previousHash) {
        this.data = data;
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
                        data
        );
        return calculatedHash;
    }

    public void mineBlock (int difficulty)
    {
        // Tao mot xau dich = ki tu '0' voi do dai bang difficulty
        String target = new String (new char[difficulty]).replace('\0', '0');
        while (!hash.substring(0, difficulty).equals(target))
        {
            nonce++;
            hash = calculateHash();
        }
        System.out.println("Block mined! " + hash);
    }
}
