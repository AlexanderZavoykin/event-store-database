package practice.event.store.config

data class DbConfig(
    val driverClassName: String,
    val jdbcUrl: String,
    val dataSourceUser: String,
    val dataSourcePassword: String,
    val maxLifetime: Int = 900000,
    val connectionTimeout: Int = 30000,
    val idleTimeout: Int = 300000,
    val maximumPoolSize: Int = 50,
    val minimumIdle: Int = 5,
    val poolName: String,
    val autoCommit: Boolean = true,
    val leakDetectionThreshold: Int = 20000,
)