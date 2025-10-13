package ch.fge;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;

@SpringBootApplication
@ConfigurationPropertiesScan
public class DataCopyApplication implements CommandLineRunner {

    private static final Logger logger = LoggerFactory.getLogger(DataCopyApplication.class);

    public static void main(String[] args) {
        SpringApplication.run(DataCopyApplication.class, args);
    }

    private final CopyConfig copyConfig;
    private final DbInfoService infoService;
    private final DbCopyService copyService;

    public DataCopyApplication(CopyConfig copyConfig, DbInfoService infoService, DbCopyService copyService) {
        this.copyConfig = copyConfig;
        this.infoService = infoService;
        this.copyService = copyService;
    }

    @Override
    public void run(String... args) {
        infoService.connect();
        logger.info("Starting");
        copyConfig.tables().forEach(tableName -> {
            logger.info("Copying {} ({} rows)", tableName, infoService.getRowCount(tableName));
            var tableInfo = infoService.getTableInfo(tableName);
            copyService.clear(tableInfo);
            copyService.copy(tableInfo);
        });
        logger.info("Completed");
    }
}
