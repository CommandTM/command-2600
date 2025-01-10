package command.net;

public enum Registers {
    C(0),
    Z(1),
    I(2),
    D(3),
    B(4),
    // 5 Is Always 1
    V(6),
    N(7);

    public final int bit;

    Registers(int bit) {
        this.bit = bit;
    }
}
