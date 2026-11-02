public class Main {
    public static void main(String[] args) {
        BankAccount zulya = new BankAccount("Зульфия", "ACC-001", 50_000, 100_000, Currency.RUB);
        BankAccount shamsik = new BankAccount("Шамиль", "ACC-002", 20_000, 30_000, Currency.RUB);
        System.out.println("Изначально:");
        System.out.println(zulya);
        System.out.println(shamsik);
        System.out.println();
        zulya.deposit(10_000); // сотка
        System.out.println("Зульфия пополнила на 100 ₽: " + zulya.getBalance());
        zulya.withdraw(5_000); // полтос
        System.out.println("Зульфия сняла 50 ₽: " + zulya.getBalance());
        zulya.transferTo(shamsik, 3_000); // тридцать
        System.out.println("Зульфия перевела Шамилю 30 ₽");
        System.out.println("  Зульфия: " + zulya.getBalance());
        System.out.println("  Шамиль:   " + shamsik.getBalance());
        System.out.println();
        System.out.println("Конвертация 100 ₽ в USD: "
                + new Balance(10_000, Currency.RUB).convertTo(Currency.USD));
        System.out.println("Конвертация 100 ₽ в EUR: "
                + new Balance(10_000, Currency.RUB).convertTo(Currency.EUR));
        System.out.println();
        System.out.println("Проверка корнер-кейсов:");
        tryAddNegative();
        tryWithdrawOverLimit(zulya);
        tryWithdrawMoreThanBalance(zulya);
        tryTransferOverLimit(zulya, shamsik);
        tryNullCurrency();
    }
    private static void tryAddNegative() {
        try {
            new Balance(1000, Currency.RUB).add(-100);
            System.out.println("  ОШИБКА: add(-100) прошёл без исключения");
        } catch (IllegalArgumentException e) {
            System.out.println("  add(-100) -> " + e.getMessage());
        }
    }
    private static void tryWithdrawOverLimit(BankAccount acc) {
        try {
            acc.withdraw(acc.getWithdrawLimitKopecks() + 1);
            System.out.println("  ОШИБКА: withdraw сверх лимита прошёл");
        } catch (IllegalArgumentException e) {
            System.out.println("  withdraw сверх лимита -> " + e.getMessage());
        }
    }
    private static void tryWithdrawMoreThanBalance(BankAccount acc) {
        try {
            // лимит достаточно большой, но баланс мал — проверяем именно недостаток средств
            long tooMuch = acc.getBalance().getAmountKopecks() + 1;
            if (tooMuch > acc.getWithdrawLimitKopecks()) {
                // уменьшим лимит искусственно не можем — просто пропустим демонстрацию
                System.out.println("  (пропуск: сумма больше лимита, показано выше)");
                return;
            }
            acc.withdraw(tooMuch);
            System.out.println("  ОШИБКА: withdraw больше баланса прошёл");
        } catch (IllegalArgumentException e) {
            System.out.println("  withdraw больше баланса -> " + e.getMessage());
        }
    }
    private static void tryTransferOverLimit(BankAccount from, BankAccount to) {
        try {
            from.transferTo(to, from.getTransferLimitKopecks() + 1);
            System.out.println("  ОШИБКА: перевод сверх лимита прошёл");
        } catch (IllegalArgumentException e) {
            System.out.println("  перевод сверх лимита -> " + e.getMessage());
        }
    }
    private static void tryNullCurrency() {
        try {
            new Balance(100, null);
            System.out.println("  ОШИБКА: null-валюта прошла");
        } catch (NullPointerException e) {
            System.out.println("  null-валюта -> " + e.getMessage());
        }
    }
}