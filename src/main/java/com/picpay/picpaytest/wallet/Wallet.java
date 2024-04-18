package com.picpay.picpaytest.wallet;

import java.math.BigDecimal;

import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

@Table("wallets")
public record Wallet(

                @Id Long id,
                String fullname,
                Long cpf,
                String password,
                int type,
                BigDecimal balance) {

        public Wallet debit(BigDecimal value) {
                return new Wallet(id, fullname, cpf, password, type, balance.subtract(value));
        }

        public Wallet credit(BigDecimal value) {
                return new Wallet(id, fullname, cpf, password, type, balance.add(value));
        }

}
