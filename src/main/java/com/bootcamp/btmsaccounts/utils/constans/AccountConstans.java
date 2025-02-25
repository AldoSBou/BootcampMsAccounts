package com.bootcamp.btmsaccounts.utils.constans;

public class AccountConstans {
    public enum AccountType {
        AHORRO("ahorro"),
        CORRIENTE("corriente"),
        PLAZO_FIJO("plazo fijo");

        private final String tipoCuenta;

        AccountType(String tipoCuenta) {
            this.tipoCuenta = tipoCuenta;
        }

        public String getTipoCuenta() {
            return tipoCuenta;
        }

        @Override
        public String toString() {
            return tipoCuenta;
        }
    }
}
