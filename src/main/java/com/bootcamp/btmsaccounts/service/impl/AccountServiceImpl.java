package com.bootcamp.btmsaccounts.service.impl;

import com.bootcamp.btmsaccounts.client.model.CustomerClient;
import com.bootcamp.btmsaccounts.client.model.PassiveProductClient;
import com.bootcamp.btmsaccounts.dto.*;
import com.bootcamp.btmsaccounts.infrastructure.iwebapi.*;
import com.bootcamp.btmsaccounts.model.Account;
import com.bootcamp.btmsaccounts.repository.IAccountRepository;
import com.bootcamp.btmsaccounts.repository.IGenericRepository;
import com.bootcamp.btmsaccounts.service.IAccountHolderService;
import com.bootcamp.btmsaccounts.service.IAccountService;
import com.bootcamp.btmsaccounts.service.ICreditService;
import com.bootcamp.btmsaccounts.utils.IMemoryService;
import com.bootcamp.btmsaccounts.utils.ServiceServiceDiscoveryUtils;
import com.bootcamp.btmsaccounts.utils.constans.AccountConstans.AccountType;
import com.fasterxml.jackson.core.type.TypeReference;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class AccountServiceImpl extends GenericServiceImpl<Account,String> implements IAccountService {

    private final IAccountRepository accountRepository;
    private final IAccountHolderService accountHolderService;
    private final ServiceServiceDiscoveryUtils discoveryClient;
    private final ICustomerApi customerApi;
    private final IProductApi productApi;
    private final IMemoryService memoryService;
    private final ICreditService creditService;
    private final ITransferApi transferApi;

    @Override
    protected IGenericRepository<Account, String> getRepository() {
        return accountRepository;
    }

    @Override
    public Flux<Account> getAccountsByCustomer(String idCustomer) {
        return accountRepository.findByCustomerId(idCustomer);
    }

    private Mono<Boolean> loadClientInformation(String customerIdentifier,
                                                Account account,
                                                Mono<CustomerClient> customerDataMono,
                                                Map<String, PassiveProductClient> productMap) {

        Flux<Account> existingAccountsFlux = accountRepository.findByCustomerId(customerIdentifier);
        return customerDataMono.flatMap(customerData -> {
            if ("PERSONAL".equalsIgnoreCase(customerData.getCustomerType().getDescription())) {
                return isValidAccountForPersonalClient(existingAccountsFlux, account.getProductId(), productMap); // **PASAR productMap a isValidAccountForPersonalClient**
            } else {
                return Mono.just(isValidAccountForEmpresarialClient(account.getProductId(), productMap)); // Para Empresarial, validación sigue igual (o la adaptas)
            }
        });
    }

    @Override
    public Mono<Account> saveAccount(Account account) {
        return discoveryClient.getDiscoveryInstances("bt-ms-customers")
                .flatMap(customerServiceInstances -> {
                    if (customerServiceInstances.isEmpty()) {
                        return Mono.error(new RuntimeException("No se encontraron instancias para bt-ms-customers"));
                    }

                    String customerIdentifier = account.getCustomerId();
                    Mono<CustomerClient> customerDataMono =
                            customerApi.getCustomerInformation(customerServiceInstances, customerIdentifier);
                    log.info("Customer information: {}", customerDataMono.map(res -> {
                        System.out.println(res.toString());
                        return res;
                    }));


                    return memoryService.getValue("allProducts", new TypeReference<List<PassiveProductClient>>() {
                            })
                            .flatMapMany(Flux::fromIterable)
                            .collectMap(PassiveProductClient::getId, passiveProductClient -> passiveProductClient)
                            .flatMap(productMap -> {
                                Mono<Boolean> isValidPersonalAccountMono = loadClientInformation(customerIdentifier, account, customerDataMono, productMap);

                                return isValidPersonalAccountMono.flatMap(isValid -> {
                                    if (isValid) {
                                        return accountRepository.save(account);
                                    } else {
                                        log.error("Validación de cuenta falló (validación optimizada con productos en memoria).");
                                        return Mono.error(new RuntimeException("Fallo validación de cuenta"));
                                    }
                                });
                            });
                });
    }

    private Mono<Boolean> isValidAccountForPersonalClient(Flux<Account> existingAccountsFlux,
                                                          String productTypeId,
                                                          Map<String, PassiveProductClient> productMap) { // **RECIBIR productMap**
        // Contadores para tipos de cuenta existentes
        final int[] ahorroCount = {0};
        final int[] corrienteCount = {0};
        final int[] plazoFijoCount = {0};

        String newAccountType;

        PassiveProductClient productDataNewAccount = productMap.get(productTypeId); // **OBTENER ProductClient del Map en memoria (¡BÚSQUEDA RÁPIDA!)**

        if (productDataNewAccount != null) { // Verificar si se encontró el ProductClient en el Map
            String productDescriptionNewAccount = productDataNewAccount.getProductSubType();
            if (productDescriptionNewAccount.toLowerCase().contains(AccountType.AHORRO.getTipoCuenta())) {
                newAccountType = AccountType.AHORRO.getTipoCuenta();
            } else if (productDescriptionNewAccount.toLowerCase().contains(AccountType.CORRIENTE.getTipoCuenta())) {
                newAccountType = AccountType.CORRIENTE.getTipoCuenta();
            } else if (productDescriptionNewAccount.toLowerCase().contains(AccountType.PLAZO_FIJO.getTipoCuenta())) {
                newAccountType = AccountType.PLAZO_FIJO.getTipoCuenta();
            } else {
                newAccountType = "";
            }
        } else {
            newAccountType = "";
            log.info("productTypeId '{}' NO encontrado en el Map de ProductClient cargado en memoria. Validación FALLA.", productTypeId);
            return Mono.just(false);
        }

        return existingAccountsFlux.flatMap(existingAccount -> {
            String productTypeIdExisting = existingAccount.getProductId();
            PassiveProductClient productDataExistingAccount = productMap.get(productTypeIdExisting);

            if (productDataExistingAccount != null) {
                String productDescriptionExistingAccount = productDataExistingAccount.getProductType().getDescription();
                if (productDescriptionExistingAccount.toLowerCase().contains(AccountType.AHORRO.getTipoCuenta())) {
                    return Mono.just(AccountType.AHORRO.getTipoCuenta());
                } else if (productDescriptionExistingAccount.toLowerCase().contains(AccountType.CORRIENTE.getTipoCuenta())) {
                    return Mono.just(AccountType.CORRIENTE.getTipoCuenta());
                } else if (productDescriptionExistingAccount.toLowerCase().contains(AccountType.PLAZO_FIJO.getTipoCuenta())) {
                    return Mono.just(AccountType.PLAZO_FIJO.getTipoCuenta());
                } else {
                    return Mono.just("OTRO");
                }
            } else {
                log.error("AccountServiceImpl - isValidAccountForPersonalClient - Error al cargar los productos en el Map de ProductClient cargado en memoria.");
                return Mono.just("ERROR");
            }
        }).collectList().flatMap(existingAccountTypes -> {

            for (String existingAccountType : existingAccountTypes) {
                if (AccountType.AHORRO.getTipoCuenta().equalsIgnoreCase(existingAccountType)) {
                    ahorroCount[0]++;
                } else if (AccountType.CORRIENTE.getTipoCuenta().equalsIgnoreCase(existingAccountType)) {
                    corrienteCount[0]++;
                } else if (AccountType.PLAZO_FIJO.getTipoCuenta().equalsIgnoreCase(existingAccountType)) {
                    plazoFijoCount[0]++;
                }
            }

            log.info("Conteo de cuentas existentes por tipo (con productos en memoria) - Ahorro: {}, Corriente: {}, Plazo Fijo: {}", ahorroCount[0], corrienteCount[0], plazoFijoCount[0]);

            if (AccountType.AHORRO.getTipoCuenta().equalsIgnoreCase(newAccountType)) {
                if (ahorroCount[0] >= 1) {
                    log.info("Cliente ya tiene cuenta de ahorro");
                    return Mono.just(false);
                }
                ;// Ya tiene máximo de cuentas de ahorro
            } else if (AccountType.CORRIENTE.getTipoCuenta().equalsIgnoreCase(newAccountType)) {
                if (corrienteCount[0] >= 1) {
                    log.info("Cliente ya tiene cuenta corriente");
                    return Mono.just(false);
                }
            }
            return Mono.just(true);
        });
    }

    private boolean isValidAccountForEmpresarialClient(String productTypeId, Map<String, PassiveProductClient> productMap) {
        PassiveProductClient productData = productMap.get(productTypeId);

        if (productData == null) {
            log.info("productTypeId '{}' NO encontrado en el Map de ProductClient. Validación FALLA.", productTypeId);
            return false;
        }

        String productDescription = productData.getProductSubType();
        String productDescriptionLower = productDescription.toLowerCase();

        if (productDescriptionLower.contains(AccountType.AHORRO.getTipoCuenta()) || productDescriptionLower.contains(AccountType.PLAZO_FIJO.getTipoCuenta())) {
            log.info("Cliente Empresarial NO puede tener cuentas de Ahorro o Plazo Fijo (productTypeId: '{}', Descripción: '{}'). Validación FALLA.", productTypeId, productDescription);
            return false;
        }

        if (productDescriptionLower.contains("cuenta corriente")) {
            log.info("Validación para cliente Empresarial PASA para Cuenta Corriente (productTypeId: '{}', Descripción: '{}').", productTypeId, productDescription);
            return true;
        }

        log.info("Tipo de producto (productTypeId: '{}', Descripción: '{}') NO es 'Cuenta Corriente', 'Ahorro' o 'Plazo Fijo'. Para cliente Empresarial, solo se permiten Cuentas Corrientes. Validación FALLA.", productTypeId, productDescription);
        return false;
    }

    @Override
    public Mono<Void> updateAccountBalance(String accountId, String customerId, Double balance) {
        return accountRepository.findById(accountId)
                .filter(result -> result.getCustomerId().equals(customerId))
                .switchIfEmpty(Mono.error(new RuntimeException("Cuenta no encontrada o no pertenece al cliente.")))
                .flatMap(account -> {
                    account.setAccountBalance((account.getAccountBalance() + balance));
                    return accountRepository.save(account);
                }).thenEmpty(Mono.empty());
    }

    @Override
    public Mono<AccountTransferResponseDTO> savingTransferLocalPost(Mono<AccountTransferRequestDTO> requestDTO) {
        return requestDTO
                .flatMap(request -> accountRepository.findByCustomerId(request.getCustomerId()).collectList()
                        .flatMap(cuentas -> {
                            Account originAccount = cuentas
                                    .stream()
                                    .filter(e -> e.getAccountNumber().equalsIgnoreCase(request.getOriginAccountNumber())).findFirst().orElse(null);
                            Account targetAccount = cuentas
                                    .stream()
                                    .filter(e -> e.getAccountNumber().equalsIgnoreCase(request.getTargetAccountNumber())).findFirst().orElse(null);
                            if (originAccount == null || targetAccount == null) {
                                return Mono.error(new IllegalArgumentException("Una o ambas cuentas no existen"));
                            }
                            if (originAccount.getAccountBalance() < request.getTransferAmount()) {
                                return Mono.error(new IllegalArgumentException("Saldo insuficiente en la cuenta de origen"));
                            }
                            // Realizar la transferencia
                            targetAccount.setAccountBalance(targetAccount.getAccountBalance() + request.getTransferAmount());
                            originAccount.setAccountBalance(originAccount.getAccountBalance() + (request.getTransferAmount() * -1));
                            return accountRepository.save(originAccount)
                                    .then(accountRepository.save(targetAccount))
                                    .map(e -> {
                                        AccountTransferResponseDTO responseDTO = new AccountTransferResponseDTO();
                                        responseDTO.setValidationStatus("Solicitud Aprobada");
                                        responseDTO.setValidationDate(LocalDate.now().toString());
                                        return responseDTO;
                                    });
                        })
                );
    }

    @Override
    public Mono<AccountsConsolidateResponseDTO> allProductsByCustomerId(String customerId) {

        Flux<PassiveProductDTO> passiveFlux = accountRepository.findByCustomerId(customerId)
                .map(account -> {
                    PassiveProductDTO passive = new PassiveProductDTO();
                    passive.setAccountBalance(account.getAccountBalance());
                    passive.setCustomerId(account.getCustomerId());
                    passive.setProductId(account.getProductId());
                    passive.setAccountNumber(account.getAccountNumber());
                    passive.setAccountStatus(account.getAccountStatus());
                    passive.setAccountCreationDate(account.getAccountCreationDate());
                    return passive;
                });

        Flux<ActiveProductDTO> activeFlux = creditService.findByCustomerId(customerId)
                .map(credit -> {
                    ActiveProductDTO active = new ActiveProductDTO();
                    active.setAccountNumber(credit.getAccountNumber());
                    active.setProductId(credit.getProductId());
                    active.setAvailableCredit(credit.getAvailableCredit().doubleValue());
                    active.setCreationDate(credit.getCreationDate().toString());
                    active.setCreditSubType(credit.getCreditSubType());
                    return active;
                });

        return Mono.zip(passiveFlux.collect(Collectors.toList()), activeFlux.collect(Collectors.toList()))
                .map(tuple -> {
                    AccountsConsolidateResponseDTO responseDTO = new AccountsConsolidateResponseDTO();
                    responseDTO.setPassiveProducts(tuple.getT1());
                    responseDTO.setActiveProducts(tuple.getT2());
                    return responseDTO;
                });
    }

    @Override
    public Mono<AccountCommissionsDTO> accountCommissions(String accountId, String startDate, String endDate) {
        return discoveryClient.getDiscoveryInstances("bt-ms-transfers")
                .flatMap(transfersApi -> {
                    if (transfersApi.isEmpty()) {
                        return Mono.error(new RuntimeException("No se encontraron instancias para bt-ms-transfers"));
                    }
                    Flux<CommissionDTO> transfer = transferApi.getAllTransfersByAccountId(transfersApi, accountId, startDate,endDate)
                            .map(transferClient -> {
                                CommissionDTO commissionDTO = new CommissionDTO();
                                commissionDTO.setAmount(transferClient.getCommission());
                                commissionDTO.setDescription(transferClient.getDescription());
                                commissionDTO.setCollectionDate(transferClient.getMovementDate());
                                return commissionDTO;
                            }).filter(e -> !Objects.isNull(e.getAmount()));

                    return transfer.collect(Collectors.toList())
                            .map(transfers -> {
                                AccountCommissionsDTO responseDTO = new AccountCommissionsDTO();
                                responseDTO.setCommissions(transfers);
                                return responseDTO;
                            });
                });
    }
}
