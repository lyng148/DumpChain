import com.google.gson.GsonBuilder;
import utils.StringUtil;

import java.security.Security;
import java.util.ArrayList;
import java.util.HashMap;

// Press Shift twice to open the Search Everywhere dialog and type `show whitespaces`,
// then press Enter. You can now see whitespace characters in your code.
public class Main {

    public static ArrayList<Block> blockchain = new ArrayList<>();
    public static HashMap<String, TransactionOutput> UTXOs = new HashMap<>();
    public static int difficulty = 5;
    public static float minimumTransaction = 0.001F;
    public static Wallet walletA;
    public static Wallet walletB;

    public static void main(String[] args) {
        Security.addProvider(new org.bouncycastle.jce.provider.BouncyCastleProvider());
        walletA = new Wallet();
        walletB = new Wallet();
        System.out.println("Public and Private key: ");
        System.out.println(StringUtil.getStringFromKey(walletA.publicKey));
        System.out.println(StringUtil.getStringFromKey(walletA.privateKey));

        Transaction transaction = new Transaction(walletA.publicKey, walletB.publicKey, 10.2F, null);
        transaction.generateSignature(walletA.privateKey);
        System.out.println("Is transaction verified: ");
        System.out.println(transaction.verifySignature());
    }

    public static boolean isValidChain()
    {
        Block currentBlock;
        Block previousBlock;
        String hashTarget = new String(new char[difficulty]).replace('\0', '0');

        // loop through blockchain to check hashes
        for (int i = 1; i < blockchain.size(); i++)
        {
            currentBlock = blockchain.get(i);
            previousBlock = blockchain.get(i - 1);
            // compare registered hash and calculated hash
            if (!currentBlock.hash.equals(currentBlock.calculateHash()))
            {
                System.out.println("Current hash not equal.");
                return false;
            }
            // compare previous hash and registered previous hash
            if (!currentBlock.previousHash.equals(previousBlock.hash)){
                System.out.println("Previous hash not equal.");
                return false;
            }
            // check if hash is solved
            if (!currentBlock.hash.substring(0, difficulty).equals(hashTarget))
            {
                System.out.println("The block hasn't been mined.");
                return false;
            }
        }
        return true;
    }
}