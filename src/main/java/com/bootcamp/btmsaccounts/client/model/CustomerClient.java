package com.bootcamp.btmsaccounts.client.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CustomerClient {

        private String id;
        private String name;
        private String lastName;
        private String email;
        private String documentNumber;
        private CustomerTypeClient customerType;
}
