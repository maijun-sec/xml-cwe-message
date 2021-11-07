package ping.cwe

import org.apache.commons.dbutils.QueryRunner
import java.sql.Connection
import java.util.stream.Collectors

/**
 * insert all data into the db
 *
 * @param conn Connection
 * @param data all cwe elements
 */
fun insert(conn: Connection, data: List<Base>) {
    val runner = QueryRunner()

    if (data == null || data.isEmpty()) {
        println("there is no data, will return directly")
        return
    }

    val param = data.stream().map { it.asArray() }.collect(Collectors.toList()).toTypedArray()
    runner.batch(conn, data[0].insertSql(), param)
}
