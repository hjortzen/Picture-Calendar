dataSource {
    pooled = true
    driverClassName = "org.hsqldb.jdbcDriver"
    username = "sa"
    password = ""
}
hibernate {
    cache.use_second_level_cache = true
    cache.use_query_cache = true
    cache.provider_class = 'net.sf.ehcache.hibernate.EhCacheProvider'
}
// environment specific settings
environments {
    development {
        dataSource {
            dbCreate = "update" // one of 'create', 'create-drop','update'
            driverClassName = "com.mysql.jdbc.Driver"
            url = "jdbc:mysql://localhost/grails"
            username = "grails"
            password = "groovy"
        }
    }
    test {
        dataSource {
            dbCreate = "update"
            url = "jdbc:hsqldb:mem:testDb"
        }
    }
    production {
        dataSource {
            dbCreate = "update"
            driverClassName = "com.mysql.jdbc.Driver"
            //String host = System.getenv('OPENSHIFT_MYSQL_DB_HOST')
            //String port = System.getenv('OPENSHIFT_MYSQL_DB_PORT')
            //String dbName = ???
            //url = "jdbc:mysql://$OPENSHIFT_MYSQL_DB_HOST:$OPENSHIFT_MYSQL_DB_PORT/calendar"
            url = "jdbc:mysql://127.8.35.130/calendar"
            username = "adminB9fpyst"
            password = "qN4yaa3sFlWS"
            dialect = "org.hibernate.dialect.MySQLInnoDBDialect"
        }
    }
}
