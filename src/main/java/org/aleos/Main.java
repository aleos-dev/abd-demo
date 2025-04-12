package org.aleos;

import lombok.extern.slf4j.Slf4j;
import org.aleos.dao.StandardUserDao;
import org.aleos.dao.UserDao;
import org.aleos.db.ConnectionFactory;
import org.aleos.db.DriverManagerConnectionFactory;
import org.aleos.db.HikariConnectionFactory;
import org.aleos.service.StandardUserService;
import org.aleos.service.UserService;
import org.aleos.transaction.TransactionManager;
import org.aleos.transaction.TransactionProxy;
import org.flywaydb.core.Flyway;

import java.lang.reflect.Proxy;

@Slf4j
public class Main {

    private static final String JDBC_URL_SQLITE = "jdbc:sqlite:demo.db";
    private static final String JDBC_URL = "jdbc:postgresql://localhost:5432/demo.db?username=aleos&password=123";

    private static final ConnectionFactory dmConnectionFactory = new DriverManagerConnectionFactory(JDBC_URL_SQLITE);
    public static final ConnectionFactory dsConnectionFactory = new HikariConnectionFactory("db_sqlite.config");

    private static final UserDao userDao = new StandardUserDao();
    private static final UserService userService = new StandardUserService(userDao);

    static {
        Flyway.configure()
                .dataSource(JDBC_URL_SQLITE, null, null)
                .load()
                .migrate();
    }

    public static void main(String[] args) {
        var dmServiceProxy = createUserServiceProxy(new TransactionManager(dmConnectionFactory));
        var dsServiceProxy = createUserServiceProxy(new TransactionManager(dsConnectionFactory));
        dmServiceProxy.transferMerits(1L, 2L, 100L);
        dmServiceProxy.findById(1L).ifPresent(System.out::println);

        measureTransactionPerformance(dmServiceProxy);
        measureTransactionPerformance(dsServiceProxy);
    }

    private static UserService createUserServiceProxy(TransactionManager transactionManager) {
        return (UserService) Proxy.newProxyInstance(
                userService.getClass().getClassLoader(),
                new Class[]{UserService.class},
                new TransactionProxy(userService, transactionManager)

        );
    }

    private static void measureTransactionPerformance(UserService userService) {
        long start = System.currentTimeMillis();
        loadUserDataInCycles(userService);
        long end = System.currentTimeMillis();
        log.info("Time taken: " + (end - start) + " ms");
    }

    private static void loadUserDataInCycles(UserService userService) {
        int cycleCount = 10_000;
        for (int i = 0; i < cycleCount; i++) {
            userService.findById(1L);
        }
    }
}