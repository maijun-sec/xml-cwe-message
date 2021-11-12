package ping.cwe

/**
 * base class, used to simplify the usage of all data types
 *
 * @author zhang maijun
 * @since 2021-11-06
 */
abstract class Base {
    /**
     * transfer the java bean as array, so we can directly insert the java bean into the db using jdbc.
     */
    abstract fun asArray(): Array<Any?>

    /**
     * the sql how can we insert this java bean into the db.
     */
    abstract fun insertSql(): String
}

/**
 * weakness message
 */
data class Weakness(
    val id: Int,
    val name: String?,
    val abstraction: String?,
    val status: String?,
    val description: String?,
    val language: String?,
    val version: String
) : Base() {
    override fun asArray(): Array<Any?> = arrayOf(this.id, this.name, this.abstraction, this.status, this.description, this.language, this.version)
    override fun insertSql() = "insert into weakness(id, name, abstraction, status, description, language, version) values(?, ?, ?, ?, ?, ?, ?)"
}

data class RelatedWeakness(
    val base: Int,
    val targetCweId: Int,
    val nature: String,
    val viewId: Int,
    val ordinal: String?,
    val version: String
) : Base() {
    override fun asArray(): Array<Any?> = arrayOf(this.base, this.targetCweId, this.nature, this.viewId, this.ordinal, this.version)
    override fun insertSql() = "insert into related_weakness(base_weakness_id, target_cwe_id, nature, view_id, ordinal, version) values(?, ?, ?, ?, ?, ?)"
}

/**
 * category message
 */
data class Category(
    val id: Int,
    val name: String?,
    val status: String?,
    val summary: String?,
    val version: String
) : Base() {
    override fun asArray(): Array<Any?> = arrayOf(this.id, this.name, this.status, this.summary, this.version)
    override fun insertSql() = "insert into category(id, name, status, summary, version) values(?, ?, ?, ?, ?)"
}

data class CategoryRelationship(
    val categoryId: Int,
    val cweId: Int,
    val viewId: Int,
    val type: String,
    val version: String
) : Base() {
    override fun asArray(): Array<Any?> = arrayOf(this.categoryId, this.cweId, this.viewId, this.type, this.version)
    override fun insertSql() = "insert into category_relationship(category_id, target_cwe_id, view_id, type, version) values(?, ?, ?, ?, ?)"
}

/**
 * view message
 */
data class View(
    val id: Int,
    val name: String?,
    val type: String?,
    val status: String?,
    val objective: String?,
    val version: String
) : Base() {
    override fun asArray(): Array<Any?> = arrayOf(this.id, this.name, this.type, this.status, this.objective, this.version)
    override fun insertSql() = "insert into view(id, name, type, status, objective, version) values(?, ?, ?, ?, ?, ?)"
}

data class ViewMember(
    val base: Int,
    val cweId: Int,
    val viewId: Int,
    val type: String,
    val version: String
) : Base() {
    override fun asArray(): Array<Any?> = arrayOf(this.base, this.cweId, this.viewId, this.type, this.version)
    override fun insertSql() = "insert into view_member(base_view_id, target_cwe_id, view_id, type, version) values(?, ?, ?, ?, ?)"
}
