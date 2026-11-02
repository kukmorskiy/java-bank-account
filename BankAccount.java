public final class BankAccount {
    private final String owner;
    private final String accountNumber;
    // баланс, не файнал тк меняется
    private Balance balance;
    // лимит на снятие за операцию
    private final long withdrawLimitKopecks;
    // лимит на перевод за операцию
    private final long transferLimitKopecks;
    public BankAccount(String owner,
                       String accountNumber,
                       long withdrawLimitKopecks,
                       long transferLimitKopecks,
                       Currency currency) {
        this.owner = requireNonBlank(owner, "Владелец");
        this.accountNumber = requireNonBlank(accountNumber, "Номер счёта");
        if (withdrawLimitKopecks < 0) {
            throw new IllegalArgumentException("Лимит на сняите не может быть отрицательным");
        }
        if (transferLimitKopecks < 0) {
            throw new IllegalArgumentException("Лимит на перевод не может быть отрицательным");
        }
        if (currency == null) {
            throw new NullPointerException("Валюта не может быть null");
        }
        this.withdrawLimitKopecks = withdrawLimitKopecks;
        this.transferLimitKopecks = transferLimitKopecks;
        this.balance = Balance.zero(currency);
    }
    public String getOwner() {
        return owner;
    }
    public String getAccountNumber() {
        return accountNumber;
    }
    public Balance getBalance() {
        return balance;
    }
    public long getWithdrawLimitKopecks() {
        return withdrawLimitKopecks;
    }
    public long getTransferLimitKopecks() {
        return transferLimitKopecks;
    }
    // пополнить счет
    public void deposit(long amountKopecks) {
        balance = balance.add(amountKopecks);
    }
    // снять сумму
    public void withdraw(long amountKopecks) {
        if (amountKopecks > withdrawLimitKopecks) {
            throw new IllegalArgumentException("Сумма снятия " + amountKopecks
                    + " коп. превышает лимит " + withdrawLimitKopecks + " коп.");
        }
        balance = balance.subtract(amountKopecks);
    }
    // перевести сумму ( с учетом лимита)
    public void transferTo(BankAccount other, long amountKopecks) {
        if (other == null) {
            throw new NullPointerException("Счёт олучателя не может быть null");
        }
        if (other == this) {
            throw new IllegalArgumentException("Нельзя перевести средства на тот же счёт");
        }
        if (amountKopecks > transferLimitKopecks) {
            throw new IllegalArgumentException("Сумма перевода " + amountKopecks
                    + " коп. превышает лимит " + transferLimitKopecks + " коп.");
        }
        if (this.balance.getCurrency() != other.balance.getCurrency()) {
            throw new IllegalArgumentException("Валюты счетов не совпадают");
        }
        balance = balance.subtract(amountKopecks);
        other.balance = other.balance.add(amountKopecks);
    }
    private static String requireNonBlank(String value, String fieldName) {
        if (value == null) {
            throw new NullPointerException(fieldName + " не может быть null");
        }
        String trimmed = value.trim();
        if (trimmed.isEmpty()) {
            throw new IllegalArgumentException(fieldName + "не может быть пустым");
        }
        return trimmed;
    }
    public String toString() {
        return "BankAccount{owner='" + owner + "', number='" + accountNumber
                + "', balance=" + balance
                + ", withdrawLimit=" + withdrawLimitKopecks
                + ", transferLimit=" + transferLimitKopecks + "}";
    }
}