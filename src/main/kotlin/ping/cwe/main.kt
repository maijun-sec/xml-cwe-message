package ping.cwe

import java.io.File
import java.sql.DriverManager

fun main() {
    val base = File("")
    println(base.canonicalPath)
    val cweExtractor = CweExtractor(base.canonicalPath + File.separator + "doc\\cwec_v4.6.xml")

    // TODO if use this program, need to modify the host, port, account and password
    Class.forName("com.mysql.cj.jdbc.Driver")
    val conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/cwe", "root", "maijun")

    insert(conn, cweExtractor.weaknesses.toMutableList())
    insert(conn, cweExtractor.relatedWeaknesses.toMutableList())
    insert(conn, cweExtractor.categories.toMutableList())
    insert(conn, cweExtractor.categoryRelationships.toMutableList())
    insert(conn, cweExtractor.views.toMutableList())
    insert(conn, cweExtractor.viewMembers.toMutableList())
}