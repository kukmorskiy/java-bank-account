// неизменяемый баланс, сумма хранитсья в копейках
public final class Balance {
    private final long amountKopecks;
    private final Currency currency;
    public Balance(long amountKopecks, Currency currency) {
        if (amountKopecks < 0) {
            throw new IllegalArgumentException("Сумма баланса не может быть отрицательной: " + amountKopecks);
        }
        if (currency == null) {
            throw new NullPointerException("Валюта не может быть null");
        }
        this.currency = currency;
        this.amountKopecks = amountKopecks;
    }
    public static Balance zero(Currency currency) {
        return new Balance(0, currency);
    }
    public long getAmountKopecks() {
        return amountKopecks;
    }
    public Currency getCurrency() {
        return currency;
    }
    public Balance add(long amountKopecks) {
        if (amountKopecks < 0) {
            throw new IllegalArgumentException("Нельзя прибавить отрицательную сумму: " + amountKopecks);
        }
        return new Balance(this.amountKopecks + amountKopecks, currency);
    }
    public Balance subtract(long amountKopecks) {
        if (amountKopecks < 0) {
            throw new IllegalArgumentException("Нельзя вычесть отрицательную сумму: " + amountKopecks);
        }
        if (amountKopecks > this.amountKopecks) {
            throw new IllegalArgumentException("Недостаточно средств: на балансе "
                    + this.amountKopecks + " коп., требуется " + amountKopecks);
        }
        return new Balance(this.amountKopecks - amountKopecks, currency);
    }
    // конвертация баланса по курсц enum
    public Balance convertTo(Currency target) {
        if (target == null) {
            throw new NullPointerException("Целевая валюта не может быть null");
        }
        if (target == this.currency) {
            return this;
        }
        // переводим в рубли (в копейках), затем в целевую валюту
        long rublesKopecks = this.amountKopecks * this.currency.getPriceInRublesKopecks();
        long targetKopecks = rublesKopecks / target.getPriceInRublesKopecks();
        return new Balance(targetKopecks, target);
    }
    public String toString() {
        long rubles = amountKopecks / 100;
        long kopecks = amountKopecks % 100;
        return String.format("%d,%02d %s", rubles, kopecks, currency.getSymbol());
    }
}