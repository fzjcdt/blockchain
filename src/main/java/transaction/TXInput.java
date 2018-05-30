package transaction;

public class TXInput {
    private String outputId;
    private TXOutput UTXO;

    public TXInput(String outputId, TXOutput UTXO) {
        setOutputId(outputId);
        setUTXO(UTXO);
    }

    public String getOutputId() {
        return outputId;
    }

    public void setOutputId(String outputId) {
        this.outputId = outputId;
    }

    public TXOutput getUTXO() {
        return UTXO;
    }

    public void setUTXO(TXOutput UTXO) {
        this.UTXO = UTXO;
    }
}