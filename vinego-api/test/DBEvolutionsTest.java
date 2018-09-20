import java.sql.Connection;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import play.db.Database;
import play.db.Databases;
import play.db.evolutions.Evolution;
import play.db.evolutions.Evolutions;

import static org.junit.Assert.assertTrue;

/**
 * Created by gibson.luo on 2018-09-17.
 */
public class DBEvolutionsTest {

    Database database;

    @Before
    public void setupDatabase() {
        database = Databases.inMemory();
        Evolutions.applyEvolutions(database, Evolutions.forDefault(new Evolution(
            1,
            "create table test (id bigint not null, name varchar(255));",
            "drop table test;"
        )));
    }

    @After
    public void shutdownDatabase() {
        Evolutions.cleanupEvolutions(database);
        database.shutdown();
    }

    @Test
    public void testDatabase() throws Exception {
        Connection connection = database.getConnection();
        connection.prepareStatement("insert into test values (10, 'testing')").execute();

        assertTrue(
            connection.prepareStatement("select * from test where id = 10")
                .executeQuery().next()
        );
    }

}
