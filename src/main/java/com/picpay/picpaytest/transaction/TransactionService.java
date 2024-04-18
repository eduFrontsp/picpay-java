package com.picpay.picpaytest.transaction;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.picpay.picpaytest.authorization.AuthorizerService;
import com.picpay.picpaytest.notification.NotificationService;
import com.picpay.picpaytest.wallet.Wallet;
import com.picpay.picpaytest.wallet.WalletRepository;
import com.picpay.picpaytest.wallet.WalletType;

@SuppressWarnings("unused")
@Service
public class TransactionService {
    private final TransactionRepository transactionRepository;
    private final WalletRepository walletRepository;
    private final AuthorizerService authorizerService;
    private final NotificationService notificationService;

    public TransactionService(TransactionRepository transactionRepository,
            WalletRepository walletRepository, AuthorizerService authorizerService,
            NotificationService notificationService) {
        this.transactionRepository = transactionRepository;
        this.walletRepository = walletRepository;
        this.authorizerService = authorizerService;
        this.notificationService = notificationService;
    }

    @Transactional
    public Transaction create(Transaction transaction) {
        // validar a transação
        validate(transaction);

        // criar a transação
        var newTransaction = transactionRepository.save(transaction);

        // debitar da carteira
        var walletPayer = walletRepository.findById(transaction.payer()).get();
        var walletPayee = walletRepository.findById(transaction.payee()).get();
        walletRepository.save(walletPayer.debit(transaction.value()));
        walletRepository.save(walletPayee.credit(transaction.value()));

        // chamar serviços externos
        // autorização de transações
        authorizerService.authorize(transaction);

        // notificação
        notificationService.notify(transaction);

        return newTransaction;

    }

    // pagador tem carteira comum
    // pagador tem saldo suficiente
    // pagador nao pode ser recebedor
    private void validate(Transaction transaction) {
        walletRepository.findById(transaction.payee())
                .map(payee -> walletRepository.findById(transaction.payer())
                        .map(payer -> isTransactionValid(transaction, payer) ? transaction : null)
                        .orElseThrow(
                                () -> new InvalidTransactionException(
                                        "transação inválida - %s".formatted(transaction))))
                .orElseThrow(() -> new InvalidTransactionException("transação inválida - %s".formatted(transaction)));

    }

    private boolean isTransactionValid(Transaction transaction, Wallet payer) {
        return payer.type() == WalletType.COMUM.getValue() &&
                payer.balance().compareTo(transaction.value()) >= 0 &&
                !payer.id().equals(transaction.payee());
    }

    public List<Transaction> list() {
        return transactionRepository.findAll();
    }
}
