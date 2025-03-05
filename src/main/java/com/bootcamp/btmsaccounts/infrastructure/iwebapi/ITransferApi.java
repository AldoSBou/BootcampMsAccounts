package com.bootcamp.btmsaccounts.infrastructure.iwebapi;

import com.bootcamp.btmsaccounts.client.model.TransferClient;
import reactor.core.publisher.Flux;

public interface ITransferApi {
    Flux<TransferClient> getAllTransfersByAccountId (String transferUrlApi, String accountId, String startDate, String endDate);
}
